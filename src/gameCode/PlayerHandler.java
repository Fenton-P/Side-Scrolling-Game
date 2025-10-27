package gameCode;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlayerHandler extends Block {
	public boolean onGround = false;
	public Map<Character, Boolean> keyMap = new HashMap<>();
	private char[] keyList = {' ', 'a', 'd', 'w', 's'};
	public double jumpHeight = 15;
	public double speed = 1;
	public double maxSpeed = 4;
	public double crouchSpeed = 2;
	public double walkSpeed = 4;
	public boolean isColliding = false;
	public int id = 0;
	public int energy = 100;
	public int maxEnergy = 100;
	private String direction = "player-right";
	private String mode = "wheel";
	public double wheelRotation = 0;
	public double stepFrame = 0;
	public double widthOffset = 12;
	public ArrayList<Ability> abilities = new ArrayList<Ability>();

	public PlayerHandler(double x, double y, double width, double height, Color color) {
		super(x, y, width, height, color);
		abilities.add(new Ability("ResetNormal", "true"));
		updateImages();
	}
	
	public PlayerHandler(double x, double y, double width, double height, String image) {
		super(x, y, width, height, image);
		abilities.add(new Ability("ResetNormal", "true"));
		updateImages();
	}
	
	@Override
	public void draw(Graphics2D paint, Vector camera) {
		if(image == null) {
			paint.setColor(color);
			paint.fillRect((int) (x - widthOffset - camera.x),  (int) (y - camera.y), (int) width, (int) height);
			return;
		}
		
		paint.drawImage(imageTexture, (int) (x - widthOffset - camera.x), (int) (y - 4 - camera.y), null);
	}
	
	public void updateImages() {
		BufferedImage bottom;
		BufferedImage player;
		BufferedImage texture;
		Graphics2D paint;
		int offset;
		switch(mode) {
		case "legs":
			if(!direction.equals("player-front")) {
				player = (BufferedImage) SpriteSheet.spriteSheets.get("base").getTexture(direction, 0, 0);
			} else {
				player = (BufferedImage) SpriteSheet.spriteSheets.get("base").getTexture("player-front-legs", 0, 0);
			}
			
			texture = new BufferedImage(64, 68, player.getType());
			paint = texture.createGraphics();
			
			Vector leftLegPosition = getLeftLegPosition();
			Vector rightLegPosition = getRightLegPosition();
			
			BufferedImage leftLeg = (BufferedImage) SpriteSheet.spriteSheets.get("base").getTexture(direction + "-leg1", 0, 0);
			BufferedImage rightLeg = (BufferedImage) SpriteSheet.spriteSheets.get("base").getTexture(direction + "-leg2", 0, 0);
			
			paint.drawImage(leftLeg, (int)leftLegPosition.x + 4, (int)leftLegPosition.y + 4, null);
			paint.drawImage(rightLeg, (int)rightLegPosition.x, (int)rightLegPosition.y + 4, null);
			
			offset = (int)(4*Math.sin(4*(wheelRotation - 3 * Math.PI / 8)));
	        if(direction.equals("player-front")) offset = 0;
			
			paint.drawImage(player, 0, offset+4, null);
			
			paint.dispose();
			
			break;
		default:
			bottom = (BufferedImage) SpriteSheet.spriteSheets.get("base").getTexture("player-wheel", 0, 0);
			AffineTransform tx = AffineTransform.getRotateInstance(wheelRotation, 16, 16);
	        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
	        bottom = op.filter(bottom, null);
	        player = (BufferedImage) SpriteSheet.spriteSheets.get("base").getTexture(direction, 0, 0);
	        texture = new BufferedImage(64, 68, bottom.getType());
	        paint = texture.createGraphics();
			
	        offset = (int)(3*Math.sin(2*(wheelRotation - 3 * Math.PI / 4)));
	        if(direction.equals("player-front")) offset = 0;
	        
			paint.drawImage(bottom, 16, 36, null);
			paint.drawImage(player, 0, offset+4, null);
			
			paint.dispose();
	        
			break;
		}
		
		this.imageTexture = texture;
	}
	
	private Vector getRightLegPosition() {
		Vector returnVector = new Vector(0, 0);
		
		double step = Math.abs(stepFrame);
		
		if(vx > 0) step = 80 - step;
		
		if(step <= 10) {
			returnVector.y = - step * (4.0 / 10);
		} else if(step <= 30) {
			returnVector.y = -4;
			returnVector.x = - (step - 10) * (8.0 / 20);
		} else if(step <= 40) {
			returnVector.x = -8;
			returnVector.y = (step - 30) * (4.0 / 10) - 4;
		} else if(step <= 80) {
			returnVector.y = 0;
			returnVector.x = (step - 40) * (8.0 / 40) - 8;
		}
		
		return returnVector;
	}

	private Vector getLeftLegPosition() {
		Vector returnVector = new Vector(0, 0);
		
		double step = (stepFrame + 40) % 80;
		
		if(vx > 0) step = 80 - step;
		
		if(step <= 10) {
			returnVector.y = - step * (4.0 / 10);
		} else if(step <= 30) {
			returnVector.y = -4;
			returnVector.x = - (step - 10) * (8.0 / 20);
		} else if(step <= 40) {
			returnVector.x = -8;
			returnVector.y = (step - 30) * (4.0 / 10) - 4;
		} else if(step <= 80) {
			returnVector.y = 0;
			returnVector.x = (step - 40) * (8.0 / 40) - 8;
		}
		
		return returnVector;
	}
	
	public Block getBlock() {
		return new Block((int) (x + widthOffset), (int) (y), (int) (width), (int) (height), Color.black);
	}

	public void updateDirection(String direction, double rotation) {
		this.direction = direction;
		this.wheelRotation += rotation / 50;
		this.wheelRotation = wheelRotation % (2 * Math.PI);
		
		this.stepFrame += Math.abs(rotation * 1.5);
		stepFrame %= 80;
		
		updateImages();
	}
	
	public void instantiateKeys() {
		for(char i : keyList) {
			keyMap.put(i, false);
		}
	}

	public void setAbilities(ArrayList<Ability> abilities2) {
		int index = contains(abilities2, "ResetNormal.true");
		if(index != -1) {
			jumpHeight = 15;
			speed = 1;
			maxSpeed = 4;
			crouchSpeed = 2;
			walkSpeed = 4;
			maxEnergy = 100;
			mode = "wheel";
			wheelRotation = 0;
			stepFrame = 0;
			widthOffset = 12;
			width = 40;
			if(energy > maxEnergy) energy = maxEnergy;
			this.abilities = new ArrayList<>();
		}
		
		if(abilities2 == null) return;
		
		for(Ability i : abilities2) {
			switch(i.abilityName) {
				case "ResetNormal":
					this.abilities.add(i);
					break;
				case "JumpHeight":
					this.abilities.add(i);
					jumpHeight = Integer.parseInt(i.abilityValue);
					break;
//				case "ResetNormal":
//					if(!i[1].equals("true")) continue;
//					jumpHeight = 15;
//					speed = 1;
//					maxSpeed = 4;
//					crouchSpeed = 2;
//					walkSpeed = 4;
//					maxEnergy = 100;
//					if(energy > maxEnergy) energy = maxEnergy;
//					break;
				case "Speed":
					this.abilities.add(i);
					walkSpeed = Integer.parseInt(i.abilityValue);
					break;
				case "Mode":
					this.abilities.add(i);
					mode = i.abilityValue;
					width = 48;
					widthOffset = 8;
					break;
				case "MaxEnergy":
					this.abilities.add(i);
					maxEnergy = Integer.parseInt(i.abilityValue);
					if(energy > maxEnergy) energy = maxEnergy;
					break;
			}
		}
	}
	
	public String[][] exclude(String[][] arr, int index) {
		String[][] temp = new String[arr.length-1][arr[0].length];
		
		for(int i = 0;i<index;i++) {
			for(int j = 0;j<arr[0].length;j++) {
				temp[i][j] = arr[i][j];
			}
		}
		for(int i = index;i<arr.length-1;i++) {
			for(int j = 0;j<arr[0].length;j++) {
				temp[i][j] = arr[i+1][j];
			}
		}
		
		return temp;
	}
	
	public int contains(ArrayList<Ability> abilities2, String str) {
		if(abilities2 == null) return -1;
		String[] strList = LevelReader.dataSplit(str);
		
		for(int i = 0;i<abilities2.size();i++) {
			Ability item = abilities2.get(i);
			if(item.abilityName.equals(strList[0]) && item.abilityValue.equals(strList[1])) return i;
		}
		
		return -1;
	}
}
