package gameCode;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class MenuScreen extends Window {
	public MenuScreen(GameState g) {
		super(g);
		checkMessages(g);
		setUpScreen();
		this.loadComponents();
	}
	
	private void checkMessages(GameState g) {
		if(g.getMessages().containsKey("gamemode")) {
			System.out.println(g.getMessages().get("gamemode"));
		}
	}
	
	@Override
	protected void setUpScreen() {	
		JPanel buttonHolder = new JPanel();
		buttonHolder.setSize(300, 400);
		buttonHolder.setBackground(Color.gray);
		buttonHolder.setOpaque(true);
		buttonHolder.setLayout(null);
		buttonHolder.setLocation(UIHandler.centerComponentsChildren(this, buttonHolder));
		
		JButton playButton = new JButton("PLAY!");
		playButton.setSize(200, 70);
		playButton.setFont(UIHandler.getMainFont());
		playButton.setBackground(Color.white);
		
		playButton.setLocation(UIHandler.centerComponentsChildren(buttonHolder, playButton));
		
		buttonHolder.add(playButton);
		
		playButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.switchScreens(new GameMode(GameState.getState()));
			}
			
		});
		
		this.windowComponents.add(buttonHolder);
	}
}