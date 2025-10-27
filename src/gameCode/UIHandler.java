package gameCode;

import java.awt.Font;
import java.awt.Point;

import javax.swing.*;

public class UIHandler {
	private static Font textFont = new Font("Britannic Bold", 1, 50);
	
	public static Point centerComponentsChildren(JComponent stationary, JComponent movable) {
		Point returnPoint = new Point();
		returnPoint.x = stationary.getBounds().width - movable.getBounds().width;
		returnPoint.x /= 2;
		
		returnPoint.y = stationary.getBounds().height - movable.getBounds().height;
		returnPoint.y /= 2;
		return returnPoint;
	}
	
	public static Font getMainFont() {
		return textFont;
	}
	
	public static Font getMainFont(int fontSize) {
		Font font = new Font(textFont.getName(), 1, fontSize);
		return font;
	}
}