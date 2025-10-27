package gameCode;

import java.awt.Image;
import java.awt.image.BufferedImage;

public class Sprite {
	public String filePath;
	public int x;
	public int y;
	public int size;
	public BufferedImage image;
	
	public Sprite(SpriteSheet spriteSheet, int x, int y, int size) {
		this.x = x;
		this.y = y;
		this.size = size;
		
		image = spriteSheet.image.getSubimage(x * size,  y * size, size, size);
	}
}
