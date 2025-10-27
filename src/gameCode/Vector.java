package gameCode;

import java.awt.Point;

public class Vector {
	public double x;
	public double y;
	
	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector(Point point) {
		this.x = point.x;
		this.y = point.y;
	}
	
	public double getLength() {
		return Math.sqrt(x * x + y * y);
	}
	
	public static double distance(Vector v1, Vector v2) {
		double dx = v1.x - v2.x;
		double dy = v1.y - v2.y;
		
		return (new Vector(dx, dy)).getLength();
	}
}
