package gameCode;

import java.util.*;

public class SaveHandler {
	public static void loadSave(String saveName) {
		String fileName = saveName.replaceAll("\\s", "");
		fileName += ".txt";
		fileName = "src/gameCode/saves/" + fileName;
		
		//Get the game context and the most recent checkPoint
		String context = "";
		int checkPoint = 0;
		
		checkPoint = Integer.parseInt(LevelReader.fileReader(fileName, "Checkpoint"));
		
		//Start up the game at that checkPoint with the context given
		startGame(checkPoint, context, fileName);
	}
	
	public static void writeToSave(String saveName, ArrayList<String[]> values) {
		String[] fileInfo = LevelReader.readFile(saveName);
		Map<String, DataInfo> file = LevelReader.decode(fileInfo);
		
		for(String[] i : values) {
			String[] valuePath = i[0].split(".");
			if(valuePath.length <= 1) {
				file.get(i[0]).setVal(i[1]);
				continue;
			}
			DataInfo.getChildNode(LevelReader.exclude(valuePath, 0), file.get(valuePath[0])).setVal(i[1]);
		}
		
		LevelWriter.writeToFile(saveName, LevelWriter.encode(file, true));
	}
	
	public static void startGame(int checkPoint, String context, String fileName) {
		SpriteSheet.defineSpriteSheet("base", "src/gameCode/Sprites/textures1.png");
		SpriteSheet.defineSpriteSheet("buildings", "src/gameCode/Sprites/buildings1.png");
		
		int gameLevel = LevelHandler.getLevelFromCheckpoint(checkPoint);
		GameLevel currentLevel = LevelHandler.getLevel(gameLevel, fileName);
		currentLevel.setProgress(checkPoint, context);
		GameHandler.loadGame(currentLevel);
	}
}