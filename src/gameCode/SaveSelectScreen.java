package gameCode;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

public class SaveSelectScreen extends Window {

	public SaveSelectScreen(GameState g) {
		super(g);
		setUpScreen();
		loadComponents();
	}
	
	@Override
	protected void setUpScreen() {
		//List all of the saves in boxes
		
		String[] arr = LevelReader.getSaveNames();
		
		JLabel title = new JLabel("Saves");
		title.setFont(UIHandler.getMainFont(100));
		title.setSize(title.getPreferredSize());
		Point titlePosition = UIHandler.centerComponentsChildren(this, title);
		titlePosition.y = 20;
		title.setLocation(titlePosition);
		
		JPanel savePanelHolder = new JPanel(null);
		
		for(int i = 0;i<arr.length;i++) {
			int row = i / 3;
			int column = i % 3;
			
			Point position = new Point(200 + 400 * column, 100 + 400 * row);
			
			JPanel savePanel = new JPanel(null);
			savePanel.setSize(300, 300);
			savePanel.setPreferredSize(new Dimension(300, 300));
			savePanel.setBackground(Color.LIGHT_GRAY);
			savePanel.setLocation(position);
			
			JLabel saveName = new JLabel(arr[i]);
			saveName.setFont(UIHandler.getMainFont(17));
			saveName.setBackground(Color.black);
			saveName.setSize(saveName.getPreferredSize());
			saveName.setLocation(UIHandler.centerComponentsChildren(savePanel, saveName));
			
			JButton clickButton = new JButton("Load");
			clickButton.setFont(UIHandler.getMainFont());
			clickButton.setBackground(Color.white);
			clickButton.setSize(clickButton.getPreferredSize());
			clickButton.setLocation(UIHandler.centerComponentsChildren(savePanel, clickButton).x, 50);
			
			savePanel.add(clickButton);
			savePanel.add(saveName);
			
			initButtonConnection(clickButton, arr[i]);
			
			savePanelHolder.add(savePanel);
		}
		
		savePanelHolder.setPreferredSize(new Dimension(1500, 850 * (arr.length/4)));
		savePanelHolder.setSize(new Dimension(1500, 500));
		savePanelHolder.setBackground(Color.gray);
		savePanelHolder.setOpaque(true);
		
		JScrollPane scrollBox = new JScrollPane(savePanelHolder);
		scrollBox.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollBox.setSize(1500, 500);
		
		JPanel finalHolder = new JPanel(new FlowLayout());
		finalHolder.add(scrollBox);
		finalHolder.setSize(1500, 500);
		finalHolder.setLocation(UIHandler.centerComponentsChildren(this, finalHolder));
		
		if(arr.length!=0) {
			this.windowComponents.add(finalHolder);
			this.windowComponents.add(title);
			return;
		}
		
		JLabel noSavesTxt = new JLabel("No Saves Found");
		noSavesTxt.setFont(UIHandler.getMainFont(20));
		noSavesTxt.setSize(noSavesTxt.getPreferredSize());
		Point noSavesPosition = UIHandler.centerComponentsChildren(this, noSavesTxt);
		noSavesTxt.setLocation(noSavesPosition);
		
		this.windowComponents.add(noSavesTxt);
	}
	
	private void initButtonConnection(JButton button, String file) {
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				SaveHandler.loadSave(file);
			}
			
		});
	}
}