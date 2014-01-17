package entities;

import gameplay.GameManager;
import graphics.ImageLoader;

import java.awt.image.BufferedImage;

import display.Position;

public class Item{

	private Position position; 
	private BufferedImage image; 
	private String name;
	private Monster monster;

	public Item(String name, Position position, Monster monster){
		this.monster = monster;
		this.name = name; 
		this.position = position; 
		image = new ImageLoader().load("/item/"+name+".png");
	}

	public void updateGraphics(){
		GameManager.updateGraphics(image, position,0);
	}

	public void actionItem(){
		if(name=="chest"){
			image=new ImageLoader().load("/item/"+name+"Open.png");
		}
	}

	public Position getPosition(){
		return position;
	}

	public String lootItem(){
		monster.hideMonster();
		return name;
	}

}
