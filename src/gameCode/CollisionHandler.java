package gameCode;

import java.awt.Color;
import java.awt.Point;

public class CollisionHandler {
	public double entryTime;
	public Point normal;
	public boolean isColliding;
	
	public CollisionHandler(double entryTime, Point normal) {
		this.entryTime = entryTime;
		this.normal = normal;
		isColliding = true;
	}
	
	public CollisionHandler(boolean colliding) {
		isColliding = colliding;
	}
	
	public static boolean aabbCheck(Block a, Block b) {
		boolean exp1 = a.x + a.width >= b.x && b.x + b.width >= a.x;
		boolean exp2 = a.y + a.height >= b.y && b.y + b.height >= a.y;
		
		return exp1 && exp2;
	}
	
	public static boolean aabbCheckExclusive(Block a, Block b) {
		boolean exp1 = a.x + a.width > b.x && b.x + b.width > a.x;
		boolean exp2 = a.y + a.height > b.y && b.y + b.height > a.y;
		
		return exp1 && exp2;
	}
	
	public static CollisionHandler staticCollision(Block b1, Block b2) {
		double entryX;
		double entryY;
		
		if(b1.vx > 0) entryX = b2.x - (b1.x + b1.width);
		else entryX = b1.x - (b2.x + b2.width);
		
		if(b1.vx != 0) entryX /= Math.abs(b1.vx);
		else entryX = -Double.MAX_VALUE;
		
		if(b1.vy > 0) entryY = b2.y - (b1.y + b1.height);
		else entryY = b1.y - (b2.y + b2.height);
		
		if(b1.vy != 0) entryY /= Math.abs(b1.vy);
		else entryY = -Double.MAX_VALUE;
		
		double entryTime = Math.max(entryX, entryY);
		Point normal = new Point(0, 0);
		
		if(!advancedCollisionCheck(b1, b2, entryTime)) return new CollisionHandler(1, normal);
		
		if(entryTime == entryY) normal = new Point(0, -(int)(Math.abs(b1.vy)/b1.vy));
		else normal = new Point(-(int)(Math.abs(b1.vx)/b1.vx), 0);
		
		if(Math.abs(entryTime) > 1) entryTime /= Math.abs(entryTime);
		
		return new CollisionHandler(entryTime, normal);
	}
	
	public static boolean advancedCollisionCheck(Block b1, Block b2, double entryTime) {
		Block b3 = new Block(b1.x + b1.vx * entryTime, b1.y + b1.vy * entryTime, b1.width, b1.height, Color.black);
		
		return aabbCheck(b3, b2);
	}
	
	public static Block getBroadPhaseBlock(Block c) {
		Block a = new Block(0, 0, c.width + Math.abs(c.vx), c.height + Math.abs(c.vy), Color.black);
		
		if(c.vx >= 0) {
			a.x = c.x;
		} else {
			a.x = c.x - Math.abs(c.vx);
		}
		
		if(c.vy >= 0) {
			a.y = c.y;
		} else {
			a.y = c.y - Math.abs(c.vy);
		}
		
		return a;
	}
	
	public static boolean pointInBox(Vector point, Block block) {
		boolean bool1 = point.x > block.x && point.x < block.x + block.width;
		boolean bool2 = point.y > block.y && point.y < block.y + block.height;
		
		return bool1 && bool2;
	}
}
