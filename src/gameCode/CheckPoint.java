package gameCode;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;

public class CheckPoint extends Block {
	public boolean visible;
	public int link;
	public static ArrayList<CheckPoint> checkPoints;
	private Image imageTextureBackground;
	private Image imageTextureForeground;
	public Block collisionBlock;
	
	public CheckPoint(boolean isVisible, Block checkPointBlock, int checkPointLink) {
		super(checkPointBlock);
		visible = isVisible;
		link = checkPointLink;
		setImageTextures();
		initCollisionBlock();
	}
	
	private void initCollisionBlock() {
		collisionBlock = new Block(x, y+40, width, 24, Color.blue);
		collisionBlock.isVisible = false;
	}
	
	public static ArrayList<CheckPoint> getVisibleCheckPoints() {
		ArrayList<CheckPoint> returnList = new ArrayList<CheckPoint>();
		
		for(CheckPoint i : checkPoints) {
			if(!i.visible) continue;
			
			returnList.add(i);
		}
		
		return returnList;
	}
	
	public static void initCheckPoints(int level) {
		checkPoints = new ArrayList<CheckPoint>();
		
		String fileName = LevelHandler.getLevelName(level);
		fileName = "src/gameCode/levels/" + fileName;
		
		int length = Integer.parseInt(LevelReader.fileReader(fileName, "Check_Point_Blocks.Length"));
		
		for(int i = 0;i<length;i++) {
			int x = Integer.parseInt(LevelReader.fileReader(fileName, "Check_Point_Blocks.CheckPoint"+ i +".Location.X"));
			int y = Integer.parseInt(LevelReader.fileReader(fileName, "Check_Point_Blocks.CheckPoint"+ i +".Location.Y"));
			boolean isVisible = LevelReader.fileReader(fileName, "Check_Point_Blocks.CheckPoint"+ i +".Visible").equals("true");
			int checkPointLink = Integer.parseInt(LevelReader.fileReader(fileName, "Check_Point_Blocks.CheckPoint" + i + ".Link"));
			
			Block currentBlock = new Block(x, y, 128, 128, Color.green);
			CheckPoint checkPoint = new CheckPoint(isVisible, currentBlock, checkPointLink);
			
			checkPoints.add(checkPoint);
		}
	}
	
	public void setImageTextures() {
		imageTextureBackground = SpriteSheet.spriteSheets.get("base").getTexture("checkPointBackground", 0, 0);
		imageTextureForeground = SpriteSheet.spriteSheets.get("base").getTexture("checkPointForeground", 0, 0);
	}
	
	@Override
	public void draw(Graphics2D paint, Vector camera) {
		System.out.println("BAD FUNCTION CALL");
	}
	
	public void drawBackground(Graphics2D paint, Vector camera) {
		paint.drawImage(imageTextureBackground, (int) (x - camera.x) + 32, (int) (y - camera.y) - 28, null);
	}
	
	public void drawForeground(Graphics2D paint, Vector camera) {
		paint.drawImage(imageTextureForeground, (int) (x - camera.x), (int) (y - camera.y) - 64, null);
	}
}