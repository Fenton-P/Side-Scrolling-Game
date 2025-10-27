package gameCode;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.*;

public class SwapButton extends Block {
	public int link;
	public boolean hasPlayerSwap = false;
	public int id;
	public int level;
	public ArrayList<Ability> abilities = new ArrayList<>();
	
	public SwapButton(double x, double y, double width, double height, Color color, int link, boolean hasSwap, int id, int level) {
		super(x, y, width, height, color);
		
		this.link = link;
		hasPlayerSwap = hasSwap;
		this.id = id;
		this.level = level;
		setAbilities();
	}                                                                            
	                                                                             
	public SwapButton(double x, double y, double width, double height, String image, int link, boolean hasSwap, int id, int level) {
		super(x, y, width, height, image);
		
		this.link = link;
		hasPlayerSwap = hasSwap;
		this.id = id;
		this.level = level;
		setAbilities();
	}
	
	@Override
	public void draw(Graphics2D paint, Vector camera) {
		if(image == null) {
			paint.setColor(color);
			paint.fillRect((int) (x - camera.x),  (int) (y - camera.y), (int) width, (int) height);
			return;
		}
		
		paint.drawImage(imageTexture, (int) (x - camera.x), (int) (y - 48 - camera.y), null);
	}
	
	public static ArrayList<SwapButton> getSwapButtons(int level) {
		ArrayList<SwapButton> swapButtons = new ArrayList<SwapButton>();
		
		String levelName = LevelHandler.getLevelName(level);
		String path = "src/gameCode/levels/"+levelName;     
		int length = Integer.parseInt(LevelReader.fileReader(path, "Swap_Buttons.Length"));
		
		for(int i = 0;i<length;i++) {
			String attrName = "Swap_Buttons.Button"+i;
			
			int positionX = Integer.parseInt(LevelReader.fileReader(path, attrName + ".Location.X"));
			int positionY = Integer.parseInt(LevelReader.fileReader(path, attrName + ".Location.Y"));
			int link = Integer.parseInt(LevelReader.fileReader(path, attrName+".Link"));
			boolean hasPlayerSwap = LevelReader.fileReader(path, attrName+".HasSwap").equals("true");
			
			swapButtons.add(new SwapButton(positionX, positionY + 4, 128, 16, "base.swapbutton", link, hasPlayerSwap, i, level));
		}
		
		return swapButtons;
	}
	
	public void setAbilities() {
		abilities = new ArrayList<>();
		ArrayList<String[]> returnList = new ArrayList<String[]>(); 
		
		String levelName = LevelHandler.getLevelName(level);
		String path = "src/gameCode/levels/"+levelName;
		
		String[] fileInfo = LevelReader.readFile(path);
		Map<String, DataInfo> file = LevelReader.decode(fileInfo);
		
		String attr = "Swap_Buttons.Button" + id + ".Abilities";
		System.out.println(id);		
		Map<String, DataInfo> abilities = LevelReader.getValueFromString(attr, file).getChildren();
		
		for(int i = 0;i<abilities.size();i++) {
			String key = (String) abilities.keySet().toArray()[i];
			String value = abilities.get(key).getVal();
			this.abilities.add(new Ability(key, value));
		}
	}
	
	public String[][] toArr(ArrayList<String[]> list) {
		String[][] returnVal = new String[list.size()][list.get(0).length];
		
		for(int i = 0;i<list.size();i++) {
			for(int j = 0;j<list.get(i).length;j++) {
				returnVal[i][j] = list.get(i)[j];
			}
		}
		
		return returnVal;
	}

	public void setAbilities(ArrayList<Ability> abilities2) {
		abilities = new ArrayList<>();
		for(Ability ability : abilities2) {
			this.abilities.add(new Ability(ability.abilityName, ability.abilityValue));
		}
	}
}
