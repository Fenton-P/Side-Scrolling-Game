package gameCode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class LevelHandler {
	private static int levelOn;
	private static Map<Integer, Integer> levelMap = new HashMap<>();
	private static Map<Integer, String> fileMap = new HashMap<>();
	private static final String levelInfoPath = "src/gameCode/levels/levelInfo.txt";
	
	public static int getLevelFromCheckpoint(int checkPoint) {
		if(levelMap.isEmpty()&&!initLevelMap()) {
			System.out.println("level map couldn't be initialized");
			return 0;
		}
		return levelMap.get(checkPoint);
	}
	
	public static GameLevel getLevel(int level, String saveFile) {
		return new GameLevel(level, GameState.getState(), saveFile);
	}
	
	public static String getLevelName(int level) {
		if(fileMap.isEmpty()&&!initLevelMap()) {
			System.out.println("file map couldn't be initialized");
			return "";
		}
		return fileMap.get(level);
	}
	
	private static boolean initLevelMap() {
		File levelInfo = new File(levelInfoPath);
		try {
			Scanner fileReader = new Scanner(levelInfo);
			
			while(fileReader.hasNextLine()) {
				String nextLine = fileReader.nextLine();
				int checkPoint = Integer.parseInt(nextLine.split(":")[0]);
				int level = Integer.parseInt(nextLine.split(":")[1]);
				String levelName = nextLine.split(":")[2];
				
				levelMap.put(checkPoint, level);
				fileMap.put(level, levelName);
			}
			
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}
}