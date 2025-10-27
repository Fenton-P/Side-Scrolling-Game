package gameCode;

import java.awt.Color;
import javax.swing.text.*;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SaveCreaterScreen extends Window {

	public SaveCreaterScreen(GameState state) {
		super(state);
		setUpScreen();
		this.loadComponents();
	}

	@Override
	protected void setUpScreen() {
		//A Title at top
		//Place to put name of the file
		//Place to select difficulty
		
		JLabel title = new JLabel("Create New Save");
		title.setFont(UIHandler.getMainFont(80));
		title.setSize(title.getPreferredSize());
		Point titleLocation = UIHandler.centerComponentsChildren(this, title);
		titleLocation.y = 0;
		title.setLocation(titleLocation);
		 
		JPanel nameInputPanel = new JPanel();
		
		JLabel descriptionText = new JLabel("Save File Name:  ");
		descriptionText.setFont(UIHandler.getMainFont(30));
		descriptionText.setSize(descriptionText.getPreferredSize());
		
		JTextField inputField = new JTextField(20);
		inputField.setFont(UIHandler.getMainFont(30));
		inputField.setSize(inputField.getPreferredSize());
		inputField.setLocation(descriptionText.getBounds().width, 0);
		inputField.setDocument(new LimitDocument(30));
		
		nameInputPanel.add(descriptionText);
		nameInputPanel.add(inputField);
		
		nameInputPanel.setSize(nameInputPanel.getPreferredSize());
		Point panelLocation = UIHandler.centerComponentsChildren(this, nameInputPanel);
		panelLocation.y = 150;
		nameInputPanel.setLocation(panelLocation);
		
		JSlider difficultyLevel = new JSlider(JSlider.HORIZONTAL, 1, 3, 2);
		difficultyLevel.setMajorTickSpacing(1);
		
		Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        labelTable.put(1, new JLabel("Easy"));
        labelTable.put(2, new JLabel("Normal"));
        labelTable.put(3, new JLabel("Hard"));
		
		difficultyLevel.setLabelTable(labelTable);
		difficultyLevel.setPaintTicks(true);
		difficultyLevel.setPaintLabels(true);
		difficultyLevel.setSize(400, 50);
		difficultyLevel.setOpaque(false);
		Point centerPoint = UIHandler.centerComponentsChildren(this, difficultyLevel);
		centerPoint.y = 300;
		difficultyLevel.setLocation(centerPoint);
		
		JButton submitButton = new JButton("Submit");
		submitButton.setFont(UIHandler.getMainFont(30));
		submitButton.setSize(200, 50);
		submitButton.setBackground(Color.white);
		submitButton.setLocation(UIHandler.centerComponentsChildren(this, submitButton));
		
		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {				
				//Create a new file in the saves folder
				//Load the game/file and start the game
				createNewSave(inputField.getText(), difficultyLevel.getValue());
				SaveHandler.loadSave(inputField.getText());
			}
			
		});
		
		this.windowComponents.add(title);
		this.windowComponents.add(nameInputPanel);
		this.windowComponents.add(difficultyLevel);
		this.windowComponents.add(submitButton);
	}
	
	private void createNewSave(String fileName, int difficultyLevel) {
		try {
			String tempName = new String(fileName);
			String finalName = fileName.replaceAll("\\s", "");
			finalName += ".txt";
			finalName = "src/gameCode/saves/" + finalName;
			File newFile = new File(finalName);
			
	        if (!newFile.createNewFile()) {
	            //TODO: Create some pop up saying that the name is invalid and to pick another one
	        }
	        
	        File fileInfo = new File("src/gameCode/saves/savesList.txt");
	        Scanner infoReader = new Scanner(fileInfo);
	        
	        String finalWrite = "";
	        
	        while(infoReader.hasNextLine()) {
	        	String line = infoReader.nextLine();
	        	finalWrite += line + "\n";
	        }
	        
	        FileWriter infoWriter = new FileWriter(fileInfo);
	        
	        
	        infoWriter.write(finalWrite + "Save_Info: " + tempName + "\n");
	        infoWriter.close();
	        
	        FileWriter writer = new FileWriter(newFile);
	        writer.write("Save_Name: " + fileName + "\n");
            writer.write("Difficulty_Level: " + difficultyLevel + "\n");
            writer.write("Checkpoint: " + 0);
            writer.close();
		} catch(Exception e) {
			System.out.println(e);
		}
	}
}