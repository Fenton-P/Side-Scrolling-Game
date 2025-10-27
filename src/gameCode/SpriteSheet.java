package gameCode;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

public class SpriteSheet {
	public String path;
	public int width;
	public int height;
	public BufferedImage image;
	public static Map<String, SpriteSheet> spriteSheets;
	
	public static void defineSpriteSheet(String name, String path) {
		if(spriteSheets == null) spriteSheets = new HashMap<>();
		
		spriteSheets.put(name, new SpriteSheet(path));
	}
	
	public SpriteSheet(String path) {
		image = null;
		
		try {
			image = ImageIO.read(new File(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(image == null) return;
		
		this.path = path;
		this.width = image.getWidth();
		this.height = image.getHeight();
	}

	public Image getTexture(String string, int width2, int height2) {
		switch(string) {
		case "brick-base":
			return (new Sprite(this, 0, 0, 64)).image;
		case "brick-top":
			return (new Sprite(this, 11, 0, 64)).image;
		case "brick-bottom":
			return (new Sprite(this, 10, 0, 64)).image;
		case "brick-left":
			return (new Sprite(this, 9, 0, 64)).image;
		case "brick-right":
			return (new Sprite(this, 8, 0, 64)).image;
		case "building1":
			return (new Sprite(this, 0, 0, 512)).image;
		case "building2":
			return (new Sprite(this, 1, 0, 512)).image;
		case "building3":
			return (new Sprite(this, 0, 1, 512)).image;
		case "checkPointBackground":
			return (new Sprite(this, 15, 1, 64)).image;
		case "checkPointForeground":
			List<List<BufferedImage>> images = new ArrayList<>();
			images.add(new ArrayList<>());
			images.add(new ArrayList<>());
			images.get(0).add((new Sprite(this, 13, 1, 64)).image);
			images.get(0).add((new Sprite(this, 13, 2, 64)).image);
			images.get(1).add((new Sprite(this, 14, 1, 64)).image);
			images.get(1).add((new Sprite(this, 14, 2, 64)).image);
			return combineImages(images);
		case "brick":
			BufferedImage texture = (BufferedImage) this.getTexture("brick-base", 0, 0);
			BufferedImage finalImage = new BufferedImage(width2, height2, texture.getType());
			
			BufferedImage top = (BufferedImage) this.getTexture("brick-top", 0, 0);
			BufferedImage bottom = (BufferedImage) this.getTexture("brick-bottom", 0, 0);
			BufferedImage left = (BufferedImage) this.getTexture("brick-left", 0, 0);
			BufferedImage right = (BufferedImage) this.getTexture("brick-right", 0, 0);
			
			Graphics2D paint = finalImage.createGraphics();
			
			int x = 0;
			
			while(x < width2) {
				int y = 0;
				
				while(y < height2) {
					paint.drawImage(texture, x, y, null);
					
					if(y == 0) {
						paint.drawImage(top, x, y, null);
					}
					
					if(y + 64 >= height2) {
						if(height2 % 64 == 0) paint.drawImage(bottom, x, y, null);
						else paint.drawImage(bottom, x, y + height2 % 64 - 64, null);
					}
					
					if(x == 0) {
						paint.drawImage(left, x, y, null);
					}
					
					if(x + 64 >= width2) {
						if(width2 % 64 == 0) paint.drawImage(right, x, y, null);
						else paint.drawImage(right, x + width2 % 64 - 64, y, null);
					}
					
					y += 64;
				}
				
				x+= 64;
			}
			
			paint.dispose();
			
			return finalImage;
		case "brick-old":
			BufferedImage[][] rtn = new BufferedImage[width2 / 64][height2 / 64];
			
			for(int i = 0;i<rtn.length;i++) {
				for(int j = 0;j<rtn[i].length;j++) {
					Set<Integer> walls = new HashSet<>();

					for(int v = 1;v<16;v++) walls.add(v);
					
					rtn[i][j] = new Sprite(this, 0, 0, 64).image;
					
					if(i == 0) {
						walls.remove(3);
						walls.remove(4);
						walls.remove(8);
						walls.remove(10);
						walls.remove(11);
						walls.remove(15);
						walls.remove(13);
					} else {
						walls.remove(1);
						walls.remove(2);
						walls.remove(5);
						walls.remove(6);
						walls.remove(7);
						walls.remove(9);
						walls.remove(12);
						walls.remove(14);
					}
					
					if(i == rtn.length - 1) {
						walls.remove(2);
						walls.remove(3);
						walls.remove(9);
						walls.remove(10);
						walls.remove(11);
						walls.remove(12);
						walls.remove(14);
					} else {
						walls.remove(1);
						walls.remove(4);
						walls.remove(5);
						walls.remove(6);
						walls.remove(7);
						walls.remove(8);
						walls.remove(13);
						walls.remove(15);
					}
					
					if(j == 0) {
						walls.remove(6);
						walls.remove(7);
						walls.remove(8);
						walls.remove(9);
						walls.remove(10);
						walls.remove(14);
						walls.remove(15);
					} else {
						walls.remove(1);
						walls.remove(2);
						walls.remove(3);
						walls.remove(4);
						walls.remove(5);
						walls.remove(11);
						walls.remove(12);
						walls.remove(13);
					}
					
					if(j == rtn[i].length - 1) {
						walls.remove(5);
						walls.remove(6);
						walls.remove(8);
						walls.remove(9);
						walls.remove(11);
						walls.remove(12);
						walls.remove(13);
					} else {
						walls.remove(1);
						walls.remove(2);
						walls.remove(3);
						walls.remove(4);
						walls.remove(7);
						walls.remove(10);
						walls.remove(14);
						walls.remove(15);
					}
					
					if(walls.size() != 0) {
						for(Integer index : walls) {
							rtn[i][j] = overlayImages(rtn[i][j], new Sprite(this, index, 0, 64).image);
						}
					}
				}
			}
			
			List<List<BufferedImage>> imagesArr = new ArrayList<>();
			
			for(int i = 0;i<rtn.length;i++) {
				imagesArr.add(Arrays.asList(rtn[i]));
			}
			
			BufferedImage gridBlocks = combineImages(imagesArr);
			
			return gridBlocks;
		case "player-right":
			return (new Sprite(this, 0, 1, 64)).image;
		case "player-left":
			return (new Sprite(this, 1, 1, 64)).image;
		case "player-wheel":
			return (new Sprite(this, 2, 1, 64)).image;
		case "player-front":
			return (new Sprite(this, 3, 1, 64)).image;
		case "player-front-legs":
			return (new Sprite(this, 4, 1, 64)).image;
		case "player-left-leg1":
			return (new Sprite(this, 5, 1, 64)).image;
		case "player-left-leg2":
			return (new Sprite(this, 6, 1, 64)).image;
		case "player-right-leg1":
			return (new Sprite(this, 8, 1, 64)).image;
		case "player-right-leg2":
			return (new Sprite(this, 7, 1, 64)).image;
		case "swapbutton":
			return addImages((new Sprite(this, 9, 1, 64)).image, (new Sprite(this, 10, 1, 64)).image);
		}
		return null;
	}
	
	public BufferedImage[] splitImageHorizontal(BufferedImage image) {
		BufferedImage[] images = new BufferedImage[2];
		
		images[0] = new BufferedImage(image.getWidth(), image.getHeight() / 2, image.getType());
		images[1] = new BufferedImage(image.getWidth(), image.getHeight() - image.getHeight() / 2, image.getType());
		
		Graphics2D paint = images[0].createGraphics();
		paint.drawImage(image, 0, 0, null);
		paint.dispose();
		
		paint = images[1].createGraphics();
		paint.drawImage(image, 0, -image.getHeight()/2, null);
		
		return images;
	}
	
	public static BufferedImage overlayImages(BufferedImage baseImage, BufferedImage overlayImage) {
        // Create a new BufferedImage with the same size as the base image
        BufferedImage combinedImage = new BufferedImage(baseImage.getWidth(), baseImage.getHeight(), baseImage.getType());

        // Create a Graphics2D object to draw on the combined image
        Graphics2D g2d = combinedImage.createGraphics();

        // Draw the base image
        g2d.drawImage(baseImage, 0, 0, null);

        // Draw the overlay image on top of the base image
        g2d.drawImage(overlayImage, 0, 0, null);

        // Dispose of the Graphics2D object
        g2d.dispose();

        return combinedImage;
    }
	
	public static BufferedImage addImages(BufferedImage image1, BufferedImage image2) {
		BufferedImage returnImage = new BufferedImage(image1.getWidth() + image2.getWidth(), Math.max(image1.getHeight(), image2.getHeight()), image1.getType());
		
		Graphics2D paint = returnImage.createGraphics();
		
		paint.drawImage(image1, 0, 0, null);
		paint.drawImage(image2, image1.getWidth(), 0, null);
		
		paint.dispose();
		
		return returnImage;
	}
	
	public static BufferedImage combineImages(List<List<BufferedImage>> images) {
		int width = images.get(0).get(0).getWidth();
		int height = images.get(0).get(0).getHeight();
		
		BufferedImage returnImage = new BufferedImage(images.size() * width,images.get(0).size() * height, images.get(0).get(0).getType());
		
		Graphics2D paint = returnImage.createGraphics();
		
		for(int i = 0;i<images.size();i++) {
			for(int j = 0;j<images.get(0).size();j++) {
				paint.drawImage(images.get(i).get(j), i * width, j * height, null);
			}
		}
		
		paint.dispose();
		
		return returnImage;
	}
}
