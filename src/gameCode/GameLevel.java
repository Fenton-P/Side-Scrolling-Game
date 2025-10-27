package gameCode;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Timer;

import javax.swing.JPanel;

public class GameLevel extends Window implements MouseListener, ActionListener, Runnable, KeyListener {
	private ArrayList<Block> midBlocks;
	private PlayerHandler playerBlock = new PlayerHandler(0, 0, 40, 64, "base.player");
	private int FPS = 60;
	private final int delay = 1000/FPS;
	private Thread gameThread;
	private double gravity = .5;
	private double airFriction = .97;
	private ArrayList<SwapButton> swapButtons;
	private int checkPoint;
	private Thread tempThread;
	private final int COYOTETIMEFRAMES = 4;
	private int coyoteTimeLeft = 0;
	private boolean displayPauseMenu = false;
	private ArrayList<Block> pauseMenuBlocks = new ArrayList<Block>();
	private Map<String, Integer> pauseMenuBlocksMap = new HashMap<>();
	private String saveFileName = "";
	private Block topLeftBars = new Block(0, 0, 300, 100, Color.white);
	private Vector camera = new Vector(0, 0);
	private Vector cameraVelocity = new Vector(0, 0);
	private int maxCameraSpeed = 20;
	private double currentSpeed = 0;
	private double acceleration = 0.1;
	private String background;
	private int finalCheckPoint = 0;
	
	public GameLevel(int level, GameState g, String saveFile) {
		super(g);
		//TODO: with the level get the info for that level
		//this includes how the level looks and the entities
		
		saveFileName = saveFile;
		background = LevelReader.fileReader("src/gameCode/levels/" + LevelHandler.getLevelName(level), "Background");
		//BackgroundHandler.addBuildingsRandom(20);
		BackgroundHandler.addBuildings();
		//topLeftBars.shouldDraw = false;
		addBars();
		playerBlock.instantiateKeys();
		CheckPoint.initCheckPoints(level);
		swapButtons = SwapButton.getSwapButtons(level);
		this.setFocusable(true);
		this.addKeyListener(this);
		this.addMouseListener(this);
		midBlocks = Block.getBlocks(level);
		pauseMenuBlocks = getPauseMenuBlocks();
		
		addCheckPointBlocks();
		midBlocks.addAll(swapButtons);
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	private void addCheckPointBlocks() {
		for(CheckPoint i : CheckPoint.checkPoints) {
			midBlocks.add(i.collisionBlock);
		}
	}
	
	public void addBars() {
		Block energyBar = new Block(50, 25, 200, 20, Color.yellow);
		Block borderBlock = getBlockOutline(energyBar, 2, Color.black);
		energyBar = new Block(2, 2, 200, 20, Color.yellow);
		borderBlock.childBlocks.add(energyBar);
		topLeftBars.childBlocks.add(borderBlock);
	}
	
	public ArrayList<Block> getPauseMenuBlocks() {
		ArrayList<Block> pauseMenuBlocks = new ArrayList<Block>();
		
		Block resumeButton = new Block(50, 50, 200, 75, Color.white);
		resumeButton.setText("Resume");
		
		Block outlineResumeButton = getBlockOutline(resumeButton, 2, Color.black);
		
		pauseMenuBlocks.add(outlineResumeButton);
		pauseMenuBlocks.add(resumeButton);
		
		Block exitButton = new Block(50, 150, 200, 75, Color.white);
		exitButton.setText("Exit");
		
		Block outlineExitButton = getBlockOutline(exitButton, 2, Color.black);
		
		pauseMenuBlocks.add(outlineExitButton);
		pauseMenuBlocks.add(exitButton);
		
		for(Block i : pauseMenuBlocks) {
			i.x += 1300/2;
			i.y += 200;
		}
		
		pauseMenuBlocksMap.put("resume", 1);
		pauseMenuBlocksMap.put("exit", 3);
		
		return pauseMenuBlocks;
	}
	
	public Block getBlockOutline(Block block, int thickness, Color color) {
		Block returnBlock = new Block(block.x - thickness, block.y - thickness, block.width + thickness*2, block.height + thickness*2, color);
		return returnBlock;
	}
	
	@Override
	public void added() {
		this.requestFocusInWindow();
	}
	
	public void setProgress(int checkPoint, String context) {
		//TODO: sets the spawn and other things that may change the player progresses through the level and then loads it again
		this.checkPoint = checkPoint;
		finalCheckPoint = Integer.parseInt(LevelReader.fileReader("src/gameCode/levels/" + LevelHandler.getLevelName(LevelHandler.getLevelFromCheckpoint(checkPoint)), "Check_Point.Length"));
		int x = Integer.parseInt(LevelReader.fileReader("src/gameCode/levels/" + LevelHandler.getLevelName(LevelHandler.getLevelFromCheckpoint(checkPoint)), "Check_Point.Point"+checkPoint+".X"));
		int y = Integer.parseInt(LevelReader.fileReader("src/gameCode/levels/" + LevelHandler.getLevelName(LevelHandler.getLevelFromCheckpoint(checkPoint)), "Check_Point.Point"+checkPoint+".Y"));
		spawnPlayer(new Point(x, y)); 
	}
	
	public void paint(Graphics g) {
		super.paintComponent(g);
		Graphics2D paint = (Graphics2D) g;
		
		drawBackground(paint);
		drawMidground(paint);
		drawEntities(paint);
		drawPlayer(paint);
		drawForeground(paint);
		
		if(displayPauseMenu) drawPauseMenu(paint);
	}
	
	public void drawPauseMenu(Graphics2D paint) {
		paint.setColor(Color.black);
		paint.fillRect(1296/2, 198, 304, 504);
		paint.setColor(Color.white);
		paint.fillRect(1300/2, 200, 300, 500);
		
		for(Block i : pauseMenuBlocks) {
			paint.setColor(i.color);
			paint.fillRect((int)i.x, (int)i.y, (int)i.width, (int)i.height);
			
			if(!i.getText().equals("")) {
				paint.setColor(Color.black);
				paint.setFont(UIHandler.getMainFont());
				FontMetrics metrics = paint.getFontMetrics(paint.getFont());
				
				paint.drawString(i.getText(), (int)((i.width - metrics.stringWidth(i.getText()))/2 + i.x), (int)((i.height - metrics.getHeight())/2 + i.y + 50));
			}
		}
	}
	
	public void drawBackground(Graphics2D paint) {
		BackgroundHandler.drawBackground(paint, background, camera);
	}
	
	public void drawMidground(Graphics2D paint) {
		for(SwapButton i : swapButtons) {
			if(!i.hasPlayerSwap) continue;
			//paint.setColor(Color.black);
			//paint.fillRect((int)(i.x + i.width / 2 - 25), (int)i.y - 25, 50, 25);
			BufferedImage inGroundPlayer = (BufferedImage) SpriteSheet.spriteSheets.get("base").getTexture("player-front", 0, 0);
			
			paint.drawImage(inGroundPlayer, (int)(i.x + i.width / 2 - 32 - camera.x), (int) (i.y - 32 - camera.y), null);
		}
		
		for(Block i : midBlocks) {
//			paint.setColor(i.color);
//			paint.fillRect((int)i.x, (int)i.y, (int)i.width, (int)i.height);
			if(!i.isVisible) continue;
			i.draw(paint, camera);
		}
		
		for(CheckPoint i : CheckPoint.getVisibleCheckPoints()) {
			//paint.setColor(i.checkPointBlock.color);
			//paint.fillRect((int)i.checkPointBlock.x, (int)i.checkPointBlock.y, (int)i.checkPointBlock.width, (int)i.checkPointBlock.height);
			i.drawBackground(paint, camera);
		}
		
		playerBlock.draw(paint, camera);
	}
	
	public void drawEntities(Graphics2D paint) {
	}
	
	public void drawPlayer(Graphics2D paint) {
	}
	
	public void drawForeground(Graphics2D paint) {
		for(CheckPoint i : CheckPoint.getVisibleCheckPoints()) {
			i.drawForeground(paint, camera);
		}
		
		drawBlockChain(getBlockOutline(topLeftBars, 2, Color.black), paint);
		drawBlockChain(topLeftBars, paint);
	}
	
	public void drawBlockChain(Block block, Graphics2D paint) {	
		if(block.shouldDraw) {
			paint.setColor(block.color);
			paint.fillRect((int)block.x, (int)block.y, (int)block.width, (int)block.height);
		}
		
		for(int i = 0;i<block.childBlocks.size();i++) {
			Block tempBlock = block.childBlocks.get(i);
			tempBlock.x += block.x;
			tempBlock.y += block.y;
			drawBlockChain(tempBlock, paint);
			tempBlock.x -= block.x;
			tempBlock.y -= block.y;
		}
	}
	
	private void spawnPlayer(Point location) {
		playerBlock.x = location.x;
		playerBlock.y = location.y;
	}
	
	private void loop() {
		Point mousePosition = MouseInfo.getPointerInfo().getLocation();
		
		Point frameLocation = Main.getFrame().getLocationOnScreen();
		mousePosition = new Point(mousePosition.x - frameLocation.x - Main.getFrame().getInsets().left, mousePosition.y - frameLocation.y - Main.getFrame().getInsets().top);
		
		playerBlock.vy+=gravity;
		
		addKeyMovement();
		
		handleCollisions();
		
		updatePlayerPosition();
		
		updateBars();
		
		if(checkPoint == finalCheckPoint - 1) loadNextLevel();
		
		repaint();
	}
	
	public void loadNextLevel() {
		
	}
	
	public void updateBars() {
		playerBlock.energy = playerBlock.energy < 0 ? 0 : playerBlock.energy;
		topLeftBars.childBlocks.get(0).childBlocks.get(0).width = (int) (playerBlock.energy / (double) playerBlock.maxEnergy * (topLeftBars.childBlocks.get(0).width-4));
	}
	
	private void addKeyMovement() {
		if((playerBlock.keyMap.get(' ') || playerBlock.keyMap.get('w')) && playerBlock.onGround && playerBlock.energy >= playerBlock.jumpHeight) {
			playerBlock.vy = -playerBlock.jumpHeight;
			playerBlock.energy -= playerBlock.jumpHeight;
			coyoteTimeLeft = 0;
		}
		if(playerBlock.keyMap.get('a') && playerBlock.vx > -playerBlock.maxSpeed) {
			playerBlock.vx -= playerBlock.speed;
			if(playerBlock.vx < -playerBlock.maxSpeed) playerBlock.vx = -playerBlock.maxSpeed;
		}
		if(playerBlock.keyMap.get('d') && playerBlock.vx < playerBlock.maxSpeed) {
			playerBlock.vx += playerBlock.speed;
			if(playerBlock.vx > playerBlock.maxSpeed) playerBlock.vx = playerBlock.maxSpeed;
		}
		if(playerBlock.keyMap.get('s')) {
			playerBlock.vy += playerBlock.speed;
			playerBlock.maxSpeed = playerBlock.crouchSpeed;
		} else playerBlock.maxSpeed = playerBlock.walkSpeed;
	}
	
	private void handleCollisions() {
		coyoteTimeLeft--;
		playerBlock.onGround = coyoteTimeLeft>0;
		playerBlock.isColliding = false;
		
//		while(true) {
//			ArrayList<Vector> entryTimes = new ArrayList<Vector>();
//			Block broadPhaseBox = CollisionHandler.getBroadPhaseBlock(playerBlock);
//			int index = 0;
//			for(Block i : midBlocks) {
//				if(!CollisionHandler.aabbCheckExclusive(broadPhaseBox, i)) { index++; continue; }
//				entryTimes.add(new Vector(CollisionHandler.staticCollision(playerBlock, i).entryTime, index));
//				index++;
//			}
//			
//			System.out.println(entryTimes.size());
//			if(entryTimes.size() == 0) break;
//			
//			ArrayList<Vector> sortedEntryTimes = sortVectorsX(entryTimes);
//			responseHandler(playerBlock, midBlocks.get((int) sortedEntryTimes.get(0).y), true, ((int) sortedEntryTimes.get(0).y)>=midBlocks.size()-swapButtons.size(), ((int) sortedEntryTimes.get(0).y));
//		}
		
		while(true) {
			int earliestCollisionIndex = getEarliestCollisionIndex(playerBlock, midBlocks);

			if(earliestCollisionIndex == -1) break;
			
			responseHandler(playerBlock, midBlocks.get(earliestCollisionIndex), true, earliestCollisionIndex>=midBlocks.size()-swapButtons.size(), earliestCollisionIndex);
		}
		
		for(CheckPoint i : CheckPoint.checkPoints) {
			if(!(CollisionHandler.aabbCheck(i, playerBlock))) continue;
			playerBlock.energy += 1;
			if(playerBlock.energy > playerBlock.maxEnergy) playerBlock.energy = playerBlock.maxEnergy;
			checkPoint = i.link + 1; // 0 is for the spawn point
		}
		
		if(!playerBlock.isColliding) {
			playerBlock.vx *= airFriction;
			playerBlock.vy *= airFriction;
		}
	}
	
	public int getEarliestCollisionIndex(Block block1, ArrayList<Block> blocks) {
		int returnIndex = -1;
		double lowestTime = Double.MAX_VALUE;
		
		Block broadPhaseBox = CollisionHandler.getBroadPhaseBlock(block1);
		
		for(int i = 0;i<blocks.size();i++) {
			if(!CollisionHandler.aabbCheckExclusive(broadPhaseBox, blocks.get(i))) continue;
			double time = CollisionHandler.staticCollision(block1, blocks.get(i)).entryTime;
			if(lowestTime > time) {
				lowestTime = time;
				returnIndex = i;
			}
		}
		
		if(lowestTime == 1) returnIndex = -1;
		
		return returnIndex;
	}
	
	public ArrayList<Vector> sortVectorsY(ArrayList<Vector> vectors) {
		ArrayList<Vector> sortedVectors = new ArrayList<Vector>(vectors);
		
		boolean change = true;
		while(change) {
			change = false;
			for(int i = 0;i>sortedVectors.size();i++) {
				if(sortedVectors.get(i).y <= sortedVectors.get(i+1).y) continue;
				Vector tempVector = sortedVectors.get(i);
				sortedVectors.set(i, sortedVectors.get(i+1));
				sortedVectors.set(i+1, tempVector);
				change = true;
			}
		}
		
		return sortedVectors;
	}
	
	public ArrayList<Vector> sortVectorsX(ArrayList<Vector> vectors) {
		ArrayList<Vector> sortedVectors = new ArrayList<Vector>(vectors);
		
		boolean change = true;
		while(change) {
			change = false;
			for(int i = 0;i>sortedVectors.size();i++) {
				if(sortedVectors.get(i).x <= sortedVectors.get(i+1).x) continue;
				Vector tempVector = sortedVectors.get(i);
				sortedVectors.set(i, sortedVectors.get(i+1));
				sortedVectors.set(i+1, tempVector);
				change = true;
			}
		}
		
		return sortedVectors;
	}
	
	private void updatePlayerPosition() {
		playerBlock.x += playerBlock.vx;
		playerBlock.y += playerBlock.vy;
		
		if(playerBlock.vx > 0 || (playerBlock.vx == 0 && playerBlock.keyMap.get('d'))) {
			playerBlock.updateDirection("player-right", playerBlock.vx);
		} else if(playerBlock.vx == 0 && !playerBlock.keyMap.get('a')) {
			playerBlock.updateDirection("player-front", playerBlock.vx);
		} else {
			playerBlock.updateDirection("player-left", playerBlock.vx);
		}
		
		cameraVelocity.x = (playerBlock.x - camera.x - 768) / 50;
		cameraVelocity.y = (playerBlock.y - camera.y - 418) / 50;
		
		double length = cameraVelocity.getLength();
		
		if(length > maxCameraSpeed) {
			currentSpeed += acceleration;
			
			if(currentSpeed > maxCameraSpeed) currentSpeed = maxCameraSpeed;
			
			cameraVelocity.x *= currentSpeed / length;
			cameraVelocity.y *= currentSpeed / length;
		} else {
			currentSpeed = length;
		}
		
		double distance = Vector.distance(camera, new Vector(playerBlock.x - 768, playerBlock.y - 418));
		
		if(distance > 10) {
			camera.x += cameraVelocity.x;
			camera.y += cameraVelocity.y;
		} else if(distance > 1) {
			camera.x += cameraVelocity.x * 4;
			camera.y += cameraVelocity.y * 4;
		} else {
			camera.x = playerBlock.x - 768;
			camera.y = playerBlock.y - 418;
		}
	}
	
	public void swapPlayer(int index) {
		int link = swapButtons.get(index - (midBlocks.size()-swapButtons.size())).link;
		SwapButton nextButton = swapButtons.get(link);
		
		if(!nextButton.hasPlayerSwap || playerBlock.keyMap.get('s') == false || playerBlock.hasSwapped || playerBlock.energy < 10) return;
		
		playerBlock.energy -= 20;
		nextButton.hasPlayerSwap = false;
		SwapButton currentButton = swapButtons.get(nextButton.link);
		currentButton.hasPlayerSwap = true;
		currentButton.setAbilities(playerBlock.abilities);
		
		playerBlock.setAbilities(nextButton.abilities);
		playerBlock.x = nextButton.x + (nextButton.width - playerBlock.width - playerBlock.widthOffset) / 2;
		playerBlock.y = nextButton.y - playerBlock.height;
		playerBlock.vx = 0;
		playerBlock.vy = 0;
		playerBlock.hasSwapped = true;
	}
	
	public void responseHandler(Block block1, Block block2, boolean isPlayer, boolean isTPBlock, int index) {
		CollisionHandler response = CollisionHandler.staticCollision(block1, block2);
		
		double vyInitial = block1.vy;
		double vxInitial = block1.vx;
		
		double changeVX = block1.vx * response.entryTime; 
		double changeVY = block1.vy * response.entryTime;
		
		double remainingtime = 1.0 - response.entryTime;
		
		double dotprod = (block1.vx * response.normal.y + block1.vy * response.normal.x) * remainingtime;
		block1.vx = dotprod * response.normal.y + changeVX;
		block1.vy = dotprod * response.normal.x + changeVY;
		
		if(response.normal.y == -1 && isTPBlock && isPlayer) swapPlayer(index);
		
		if(response.normal.y != 0 && remainingtime > 0 && block1.vx != 0) {
			double forceNormal = block1.vy - vyInitial;
			forceNormal *= -FPS * block1.getMass() / remainingtime / Math.pow(10, 0);
			double friction = block2.getForceFriction(block1, forceNormal);
			friction = friction / block1.getMass() / FPS * remainingtime / FPS * remainingtime;
			
			if(block1.vx > 0) {
				block1.vx += friction * response.normal.y;
				if(block1.vx < 0) block1.vx = 0;
			} else {
				block1.vx -= friction * response.normal.y;
				if(block1.vx > 0) block1.vx = 0;
			}
		}
		
		if(response.normal.x != 0 && remainingtime > 0 && block1.vy != 0) {
			double forceNormal = block1.vx - vxInitial;
			forceNormal *= FPS * block1.getMass() / remainingtime / Math.pow(10, 0);
			double friction = block2.getForceFriction(block1, forceNormal);
			friction = friction / block1.getMass() / FPS * remainingtime / FPS * remainingtime;
			
			if(block1.vy > 0) {
				block1.vy -= friction * response.normal.x;
				if(block1.vy < 0) block1.vy = 0;
			} else {
				block1.vy += friction * response.normal.x;
				if(block1.vy > 0) block1.vy = 0;
			}
		}
		
		if(!isPlayer) return;
		
		if(response.normal.x != 0 || response.normal.y != 0) {
			playerBlock.isColliding = true;
		}
		
		if(response.normal.y<0) {
			coyoteTimeLeft = COYOTETIMEFRAMES;
			playerBlock.onGround = true;
		}
	}
	
	public void updateSaveFile() {
		ArrayList<String[]> valuesList = new ArrayList<String[]>();
		String[] newCheckPoint = {"Checkpoint", "" + checkPoint};
		valuesList.add(newCheckPoint);
		SaveHandler.writeToSave(saveFileName, valuesList);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
		Point location = e.getLocationOnScreen();
		if(!displayPauseMenu) return;
		
		Point locationOnScreen = this.getLocationOnScreen();
		
		location.x -= locationOnScreen.x;
		location.y -= locationOnScreen.y;
		if(CollisionHandler.pointInBox(new Vector(location), pauseMenuBlocks.get(pauseMenuBlocksMap.get("resume")))) {
			displayPauseMenu = false;
			gameThread = new Thread(this);
			gameThread.start();
		}
		if(CollisionHandler.pointInBox(new Vector(location), pauseMenuBlocks.get(pauseMenuBlocksMap.get("exit")))) {
			updateSaveFile();
			//Switch back to the main/menu screen
			Main.switchScreens(new MenuScreen(GameState.getState()));
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() {
		
		while(gameThread != null) {
			long pastTime = System.nanoTime();

			loop();
			
			long currentTime = System.nanoTime();
			long finalDelay = delay*1000000 - (currentTime - pastTime);
			if(finalDelay<0) finalDelay = 0;
			try {
				gameThread.sleep(finalDelay/1000000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public void pauseGame() {
		tempThread = gameThread;
		gameThread = null;
		
		displayPauseMenu = true;
		repaint();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
		if(e.getKeyCode() == 27) pauseGame();
		
		playerBlock.keyMap.put(e.getKeyChar(), true);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
		playerBlock.keyMap.put(e.getKeyChar(), false);
		if(!playerBlock.keyMap.get('s')) playerBlock.hasSwapped = false;
	}
}