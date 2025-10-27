package gameCode;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.Map;

public class Block {
	public double x;
	public double y;
	public double width;
	public double height;
	public double vx;
	public double vy;
	public Color color;
	public String image;
	private double mass;
	private double density = 1;
	public double coefficientFriction = 0.01;
	public boolean hasSwapped = false;
	private String text = "";
	public boolean shouldDraw = true;
	public ArrayList<Block> childBlocks = new ArrayList<Block>();
	public Image imageTexture;
	public boolean isVisible = true;
	
	public Block(double x, double y, double width, double height, Color color) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.color = color;
		vx = 0;
		vy = 0;
		
		mass = width * height * density;
	}
	
	public Block(double x, double y, double width, double height, String image) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		vx = 0;
		vy = 0;
		this.image=image;
		
		mass = width * height * density;
		
		String[] texture = image.split("\\.");
		
		if(texture[1].equals("player")) return;
		
		imageTexture = SpriteSheet.spriteSheets.get(texture[0]).getTexture(texture[1], (int) width, (int) height);
	}
	
	public Block(Block block) {
		this.x = block.x;
		this.y = block.y;
		this.width = block.width;
		this.height = block.height;
		this.vx = block.vx;
		this.vy = block.vy;
		this.image = block.image;
		this.mass = block.mass;
		this.color = block.color;
		this.density = block.density;
		this.hasSwapped = block.hasSwapped;
		this.text = block.text;
		this.shouldDraw = block.shouldDraw;
		this.childBlocks = new ArrayList<Block>(block.childBlocks);
		this.isVisible = block.isVisible;
	}

	public static ArrayList<Block> getBlocks(int level) {
		ArrayList<Block> list = new ArrayList<Block>();
		
		String levelName = LevelHandler.getLevelName(level);
		String path = "src/gameCode/levels/"+levelName;
		int length = Integer.parseInt(LevelReader.fileReader(path, "Map_Blocks.Length"));
		
		for(int i = 0;i<length;i++) {
			String[] fileInfo = LevelReader.readFile(path);
			String[] str = {("Block" + i)};
			DataInfo block = DataInfo.getChildNode(str, LevelReader.decode(fileInfo).get("Map_Blocks"));
			
			int positionX = Integer.parseInt(DataInfo.getChildNode(("Location.X").split("\\."), block).getVal());
			int positionY = Integer.parseInt(DataInfo.getChildNode(("Location.Y").split("\\."), block).getVal());
			int width = Integer.parseInt(DataInfo.getChildNode(("Size.Width").split("\\."), block).getVal());
			int height = Integer.parseInt(DataInfo.getChildNode(("Size.Height").split("\\."), block).getVal());
			String color = DataInfo.getChildNode(("Color").split("\\."), block).getVal();
			String image = DataInfo.getChildNode(("Texture").split("\\."), block).getVal();
			
			if(image.equals("null")) {
				list.add(new Block(positionX, positionY, width, height, Color.decode(color)));
				continue;
			}
			list.add(new Block(positionX, positionY, width, height, image));
		}
		
		for(int i = 0;i<length;i++) {
			for(int j = 0;j<length;j++) {
				Block b1 = list.get(i);
				Block b2 = list.get(j);
				
				if(!CollisionHandler.aabbCheck(b1,  b2)) continue;
				
				//Only 4 cases - handle them individually
				
				if(b1.x == b2.x + b2.width) {
					double heightDif = Math.abs(b1.y - b2.y);
					double startY = Math.min(b1.y, b2.y) + heightDif;
					double endY = Math.min(b2.y + b2.height, b1.y + b1.height);
					
					//paint.fillRect(0, (int) (startY - b1.y + 8), 20, (int) (endY - startY - 16));
					
					BufferedImage brickTexture = (BufferedImage) SpriteSheet.spriteSheets.get("base").getTexture("brick-base", 64, 64);
					if((endY - startY - 16) == 0) continue;
					BufferedImage brick = new BufferedImage(24, (int) Math.abs(endY - startY - 16), brickTexture.getType());
					Graphics2D paint2 = brick.createGraphics();
					
					int brickOn = 0;
					
					while(brickOn * 64 < (endY - startY - 16)) {
						paint2.drawImage(brickTexture, 0, brickOn * 64 - 8, null);
						brickOn++;
					}
					
					paint2.dispose();
					
					Graphics2D paint = (Graphics2D) b1.imageTexture.getGraphics();
					
					paint.drawImage(brick, 0, (int) (startY + 8 - b1.y), null);
					
					paint.dispose();
				}
				
				if(b1.x == b2.x - b1.width) {
					double heightDif = Math.abs(b1.y - b2.y);
					double startY = Math.min(b1.y, b2.y) + heightDif;
					double endY = Math.min(b2.y + b2.height, b1.y + b1.height);
					
					//paint.fillRect(0, (int) (startY - b1.y + 8), 20, (int) (endY - startY - 16));
					
					BufferedImage brickTexture = (BufferedImage) SpriteSheet.spriteSheets.get("base").getTexture("brick-base", 64, 64);
					if((endY - startY - 16) == 0) continue;
					BufferedImage brick = new BufferedImage(24, (int) Math.abs((endY - startY - 16)), brickTexture.getType());
					Graphics2D paint2 = brick.createGraphics();
					
					int brickOn = 0;
					
					while(brickOn * 64 < (endY - startY - 16)) {
						paint2.drawImage(brickTexture, 0, brickOn * 64 - 8, null);
						brickOn++;
					}
					
					paint2.dispose();
					
					Graphics2D paint = (Graphics2D) b1.imageTexture.getGraphics();
					
					paint.drawImage(brick, (int) b1.width - 24, (int) (startY + 8 - b1.y), null);
					
					paint.dispose();
				}
				
				if(b1.y == b2.y + b2.height) {
					double widthDif = Math.abs(b1.x - b2.x);
					double startX = Math.min(b1.x, b2.x) + widthDif;
					double endX = Math.min(b2.x + b2.width, b1.x + b1.width);
					
					//paint.fillRect(0, (int) (startY - b1.y + 8), 20, (int) (endY - startY - 16));
					
					BufferedImage brickTexture = (BufferedImage) SpriteSheet.spriteSheets.get("base").getTexture("brick-base", 64, 64);
					if((int) Math.abs((endX - startX - 16)) ==0) continue;
					BufferedImage brick = new BufferedImage((int) Math.abs((endX - startX - 16)), 24, brickTexture.getType());
					Graphics2D paint2 = brick.createGraphics();
					
					int brickOn = 0;
					
					while(brickOn * 64 < (endX - startX - 16)) {
						paint2.drawImage(brickTexture, brickOn * 64 - 8, 0, null);
						brickOn++;
					}
					
					paint2.dispose();
					
					Graphics2D paint = (Graphics2D) b1.imageTexture.getGraphics();
					
					paint.drawImage(brick, (int) (startX + 8 - b1.x), 0, null);
					
					paint.dispose();
				}
				
				if(b1.y == b2.y - b1.height) {
					double widthDif = Math.abs(b1.x - b2.x);
					double startX = Math.min(b1.x, b2.x) + widthDif;
					double endX = Math.min(b2.x + b2.width, b1.x + b1.width);
					
					//paint.fillRect(0, (int) (startY - b1.y + 8), 20, (int) (endY - startY - 16));
					
					BufferedImage brickTexture = (BufferedImage) SpriteSheet.spriteSheets.get("base").getTexture("brick-base", 64, 64);
					if(endX - startX - 16 == 0) continue;
					BufferedImage brick = new BufferedImage((int) Math.abs(endX - startX - 16), 24, brickTexture.getType());
					Graphics2D paint2 = brick.createGraphics();
					
					int brickOn = 0;
					
					while(brickOn * 64 < (endX - startX - 16)) {
						paint2.drawImage(brickTexture, brickOn * 64 - 8, -40, null);
						brickOn++;
					}
					
					paint2.dispose();
					
					Graphics2D paint = (Graphics2D) b1.imageTexture.getGraphics();
					
					paint.drawImage(brick, (int) (startX + 8 - b1.x), (int) (b1.height - 24), null);
					
					paint.dispose();
				}
			}
		}
		
		return list;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}
	
	public double getMass() {
		return mass;
	}
	
	public double getForceFriction(Block other, double normalForce) {
		return other.getMass() * normalForce * (other.coefficientFriction + coefficientFriction) / 2.0;
	}

	public void draw(Graphics2D paint, Vector camera) {
		if(image == null) {
			paint.setColor(color);
			paint.fillRect((int) (x - camera.x),  (int) (y - camera.y), (int) width, (int) height);
			return;
		}
		
		paint.drawImage(imageTexture, (int) (x - camera.x), (int) (y - camera.y), null);
	}
}
