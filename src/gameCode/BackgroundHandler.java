package gameCode;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;

public class BackgroundHandler {
	public static Image building = SpriteSheet.spriteSheets.get("buildings").getTexture("building1", 0, 0);
	public static Image building2 = SpriteSheet.spriteSheets.get("buildings").getTexture("building2", 0, 0);
	public static Image building3 = SpriteSheet.spriteSheets.get("buildings").getTexture("building3", 0, 0);
	public static ArrayList<Vector> buildingPositions = new ArrayList<>();
	public static ArrayList<Image> buildingImages = new ArrayList<>();
	public static ArrayList<Vector> buildingPositions2 = new ArrayList<>();
	public static ArrayList<Image> buildingImages2 = new ArrayList<>();
	public static ArrayList<Vector> buildingPositions3 = new ArrayList<>();
	public static ArrayList<Image> buildingImages3 = new ArrayList<>();
	
	public static void drawBackground(Graphics2D paint, String background, Vector camera) {
		switch(background) {
		case "ruinedCity":
			paint.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .6f));
			
			paint.setColor(new Color(20, 20, 80));
			paint.fillRect(0,  0,  1600,  900);
			
			//Background
			
			paint.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
			
			for(int i = 0;i<buildingPositions3.size();i++) {
				Image image = buildingImages3.get(i);
				Vector point = buildingPositions3.get(i);
				paint.drawImage(image, (int) (point.x - camera.x / 20.0), (int) (point.y - camera.y / 20.0), null);
			}
			
			paint.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .1f));
			
			paint.setColor(Color.blue);
			paint.fillRect(0, 0, 1600, 900);
			
			//Midground
			
			paint.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
			
			for(int i = 0;i<buildingPositions.size();i++) {
				Image image = buildingImages.get(i);
				Vector point = buildingPositions.get(i);
				paint.drawImage(image, (int) (point.x - camera.x / 10.0), (int) (point.y - camera.y / 10.0), null);
			}
			
			paint.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .1f));
			
			paint.setColor(Color.blue);
			paint.fillRect(0, 0, 1600, 900);
			
			paint.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
			
			//This is for the fore-ground
			
			for(int i = 0;i<buildingPositions2.size();i++) {
				Image image = buildingImages2.get(i);
				Vector point = buildingPositions2.get(i);
				paint.drawImage(image, (int) (point.x - camera.x / 5.0), (int) (point.y - camera.y / 5.0), null);
			}
			
			paint.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .05f));
			
			paint.setColor(Color.blue);
			paint.fillRect(0,  0, 1600, 900);
			
			paint.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
			
			break;
		}
	}
	
	public static void addBuildingsRandom(int buildingCount) {
		for(int i = 0;i<buildingCount;i++) {
			addBuilding(new Vector(Math.random() * 1600, Math.random() * 600 + 100), building);
		}
	}
	
	public static void addBuildings() {
		addBuilding(new Vector(100, 200), building);
		addBuilding(new Vector(500, 150), building2);
		addBuilding(new Vector(850, 300), building);
		addBuilding(new Vector(-300, 250), building2);
		addBuilding(new Vector(1200, 150), building3);
		addBuilding(new Vector(1500, 300), building);
		
		addForegroundBuilding(new Vector(-550, 420), building2);
		addForegroundBuilding(new Vector(-200, 410), building);
		addForegroundBuilding(new Vector(150, 400), building2);
		addForegroundBuilding(new Vector(475, 390), building);
		addForegroundBuilding(new Vector(800, 390), building3);
		addForegroundBuilding(new Vector(1200, 390), building2);
		addForegroundBuilding(new Vector(1550, 400), building);
		addForegroundBuilding(new Vector(1850, 390), building3);
		
		addBackgroundBuilding(new Vector(-100, 100), building3);
		addBackgroundBuilding(new Vector(200, 100), building);
		addBackgroundBuilding(new Vector(500, 100), building3);
		addBackgroundBuilding(new Vector(900, 100), building2);
		addBackgroundBuilding(new Vector(1300, 100), building);
		addBackgroundBuilding(new Vector(1600, 100), building2);
		addBackgroundBuilding(new Vector(1900, 100), building3);
	}
	
	public static void addBuilding(Vector v, Image building) {
		buildingPositions.add(v);
		buildingImages.add(building);
	}
	
	public static void addForegroundBuilding(Vector v, Image building) {
		buildingPositions2.add(v);
		buildingImages2.add(building);
	}
	
	public static void addBackgroundBuilding(Vector v, Image building) {
		buildingPositions3.add(v);
		buildingImages3.add(building);
	}
}
