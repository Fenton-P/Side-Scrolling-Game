package gameCode;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class GameMode extends Window {
	public GameMode(GameState g) {
		super(g);
		setUpScreen();
		this.loadComponents();
	}
	
	@Override
	protected void setUpScreen() {
		//Two options: single player & multiplayer
		
		JPanel optionsBox = new JPanel();
		optionsBox.setSize(1300, 600);
		optionsBox.setOpaque(false);
		optionsBox.setLocation(UIHandler.centerComponentsChildren(this, optionsBox));
		optionsBox.setBackground(Color.gray);
		
		JPanel singlePlayerBox = new JPanel();
		singlePlayerBox.setSize(1300/2, 600);
		singlePlayerBox.setOpaque(false);
		singlePlayerBox.setBackground(Color.blue);
		
		JPanel multiPlayerBox = new JPanel();
		multiPlayerBox.setSize(1300/2, 600);
		multiPlayerBox.setOpaque(false);
		multiPlayerBox.setBackground(Color.green);
		multiPlayerBox.setLocation(1300/2, 0);
		
		JPanel singlePlayerOutline = new JPanel();
		singlePlayerOutline.setSize(400, 600);
		singlePlayerOutline.setOpaque(true);
		singlePlayerOutline.setBackground(Color.gray);
		singlePlayerOutline.setLocation(UIHandler.centerComponentsChildren(singlePlayerBox, singlePlayerOutline));
		
		JPanel multiPlayerOutline = new JPanel();
		multiPlayerOutline.setSize(400, 600);
		multiPlayerOutline.setOpaque(true);
		multiPlayerOutline.setBackground(Color.gray);
		multiPlayerOutline.setLocation(UIHandler.centerComponentsChildren(multiPlayerBox, multiPlayerOutline));
		
		JButton singlePlayer = new JButton("Singleplayer");
		singlePlayer.setSize(250, 70);
		singlePlayer.setBackground(Color.white);
		singlePlayer.setFont(UIHandler.getMainFont(30));
		singlePlayer.setLocation(UIHandler.centerComponentsChildren(singlePlayerOutline, singlePlayer));
		
		JButton multiPlayer = new JButton("Multiplayer");
		multiPlayer.setSize(250, 70);
		multiPlayer.setBackground(Color.white);
		multiPlayer.setFont(UIHandler.getMainFont(30));
		multiPlayer.setLocation(UIHandler.centerComponentsChildren(multiPlayerOutline, multiPlayer));
		
		singlePlayerOutline.add(singlePlayer);
		multiPlayerOutline.add(multiPlayer);
		
		singlePlayerBox.add(singlePlayerOutline);
		multiPlayerBox.add(multiPlayerOutline);
		
		optionsBox.add(singlePlayerBox);
		optionsBox.add(multiPlayerBox);
		
		//Temporary functions for what to do when the buttons are clicked
		singlePlayer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.switchScreens(new LoadSaveScreen(GameState.getState().addMessage("gamemode", "singlePlayer")));
			}
		});
		
		multiPlayer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.switchScreens(new MenuScreen(GameState.getState().addMessage("gamemode", "multiPlayer")));
			}
		});
		
		this.windowComponents.add(optionsBox);
	}
}