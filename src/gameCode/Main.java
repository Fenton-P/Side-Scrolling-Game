package gameCode;

import javax.swing.JFrame;

public class Main {
	private static JFrame mainFrame;

	public static void main(String[] args) {
		setUp();
		Window startScreen = new MenuScreen(GameState.getState());
		mainFrame.add(startScreen);
		mainFrame.setVisible(true);
	}

	private static void setUp() {
		mainFrame = new JFrame("Game");
		mainFrame.setSize(1600, 900);
		mainFrame.setResizable(false);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setLayout(null);
		mainFrame.setLocationRelativeTo(null);
	}
	
	public static void switchScreens(Window window) {
		mainFrame.remove(GameState.getWindowPrevious()); 
		mainFrame.add(window);
		window.added();
		mainFrame.repaint();
	}
	
	public static JFrame getFrame() {
		return mainFrame;
	}
}