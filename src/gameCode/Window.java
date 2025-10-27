package gameCode;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class Window extends JPanel {
	protected ArrayList<JComponent> windowComponents;
	private boolean scrolling = false;
	
	public Window(GameState state) {
		GameState.setWindow(this);
		setWindowSettings();
		windowComponents = new ArrayList<JComponent>();
	}
	
	protected void loadComponents() {
		for(JComponent i : windowComponents) {
			this.add(i);
		}
	}
	
	protected void setUpScreen() {
		//Add default Components
		//Maybe a screen not found or something similar
		System.out.println("Screen set up not loaded");
	}
	
	public void added() {
		
	}
	
	protected void setWindowSettings() {
		this.setSize(1600, 900);
		this.setLayout(null);
	}
}