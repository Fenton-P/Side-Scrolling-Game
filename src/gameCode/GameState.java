package gameCode;

import java.util.HashMap;
import java.util.Map;

public class GameState {
	private static Window currentWindow;
	private static Window prevWindow;
	private static GameAction action;
	private static String currentSave;
	private static Map<String, String> map = new HashMap<>();
	
	public static GameState getState() {
		return new GameState();
	}
	
	public static void setWindow(Window window) {
		prevWindow = currentWindow;
		currentWindow = window;
	}
	
	public static Window getWindow() {
		return currentWindow;
	}
	
	public static Window getWindowPrevious() {
		return prevWindow;
	}

	public GameState addMessage(String string, String string2) {
		map.put(string, string2);
		return this;
	}
	
	public Map<String, String> getMessages() {
		return map;
	}
	
	public static void setSave(String saveName) {
		currentSave = saveName;
	}
}