package entities;

import java.awt.image.BufferedImage;

import gameplay.GameManager;
import utilities.ImageLoader;

/**
 * The 3 items available in the game. <p>
 * The heart adds 20 HP.<p>
 * The sword add 5 attack power.<p>
 * The boots add 1 movement speed.
 */
public class Item{

	private Position position; 
	private BufferedImage image; 
	private String name;
	private Monster monster;

	/**
	 * @param name the name of the item, to select the correct file.
	 * @param position where it will appear
	 * @param monster the monster which dropped it.
	 */
	public Item(String name, Position position, Monster monster){
		this.monster = monster;
		this.name = name; 
		this.position = position; 
		image = new ImageLoader().load("/item/"+name+".png");
	}

	public void updateGraphics(){
		GameManager.updateGraphics(image, position,0);
	}

	/**
	 * In the case of the chest, load the openned chest image.
	 */
	public void actionItem(){
		if(name=="chest"){
			image=new ImageLoader().load("/item/"+name+"Open.png");
		}
	}

	public Position getPosition(){
		return position;
	}

	/**
	 * Hides the monster which dropped the item.
	 * @return the name of the item.
	 */
	public String lootItem(){
		monster.hideMonster();
		return name;
	}

}
