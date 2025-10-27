package gameCode;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class LoadSaveScreen extends Window {
	public LoadSaveScreen(GameState g) {
		super(g);
		setUpScreen();
		this.loadComponents();
	}
	
	@Override
	protected void setUpScreen() {
		//Load Save or Create a new save
		
		JPanel optionsBox = new JPanel();
		optionsBox.setSize(1300, 600);
		optionsBox.setOpaque(false);
		optionsBox.setLocation(UIHandler.centerComponentsChildren(this, optionsBox));
		optionsBox.setBackground(Color.gray);
		
		JPanel newSaveBox = new JPanel();
		newSaveBox.setSize(1300/2, 600);
		newSaveBox.setOpaque(false);
		newSaveBox.setBackground(Color.blue);
		
		JPanel oldSaveBox = new JPanel();
		oldSaveBox.setSize(1300/2, 600);
		oldSaveBox.setOpaque(false);
		oldSaveBox.setBackground(Color.green);
		oldSaveBox.setLocation(1300/2, 0);
		
		JPanel newSaveOutline = new JPanel();
		newSaveOutline.setSize(400, 600);
		newSaveOutline.setOpaque(true);
		newSaveOutline.setBackground(Color.gray);
		newSaveOutline.setLocation(UIHandler.centerComponentsChildren(newSaveBox, newSaveOutline));
		
		JPanel oldSaveOutline = new JPanel();
		oldSaveOutline.setSize(400, 600);
		oldSaveOutline.setOpaque(true);
		oldSaveOutline.setBackground(Color.gray);
		oldSaveOutline.setLocation(UIHandler.centerComponentsChildren(oldSaveBox, oldSaveOutline));
		
		JButton newSave = new JButton("New Save");
		newSave.setSize(250, 70);
		newSave.setBackground(Color.white);
		newSave.setFont(UIHandler.getMainFont(30));
		newSave.setLocation(UIHandler.centerComponentsChildren(newSaveOutline, newSave));
		
		JButton oldSave = new JButton("Old Save");
		oldSave.setSize(250, 70);
		oldSave.setBackground(Color.white);
		oldSave.setFont(UIHandler.getMainFont(30));
		oldSave.setLocation(UIHandler.centerComponentsChildren(oldSaveOutline, oldSave));
		
		newSaveOutline.add(newSave);
		oldSaveOutline.add(oldSave);
		
		newSaveBox.add(newSaveOutline);
		oldSaveBox.add(oldSaveOutline);
		
		optionsBox.add(newSaveBox);
		optionsBox.add(oldSaveBox);
		
		//Temporary functions for what to do when the buttons are clicked
		newSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.switchScreens(new SaveCreaterScreen(GameState.getState()));
			}
		});
		
		oldSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.switchScreens(new SaveSelectScreen(GameState.getState()));
			}
		});
		
		this.windowComponents.add(optionsBox);
	}
}