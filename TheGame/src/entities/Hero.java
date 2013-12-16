package entities;

import gameplay.GameManager;
import graphics.AnimatedSprite;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicInteger;

import main.Game;

import display.Position;

public class Hero extends Thread{

	private Position position;
	private final int deltaX = 40, deltaY = 40;
	private final int ANIMATIONSPEED = 2;
	private final int SPEED = 2;

	private StatePlayer state = StatePlayer.NONE;
	private AnimatedSprite sprite;
	private BufferedImage currentSprite;

	public static AtomicInteger life;
	public String name;

	public Hero(AtomicInteger x, AtomicInteger y, String name){

		position = new Position(x, y);
		sprite = new AnimatedSprite(name, ANIMATIONSPEED);
		currentSprite = sprite.next();
		life = new AtomicInteger(100);

	}

	public StatePlayer action(){

		move();
		attack();
		//defend();

		return state;

	}

	public void setState(StatePlayer state){
		this.state = state;
	}
	
	public StatePlayer getHeroState(){
		return state;
	}

	public boolean isMoving(){

		if(state == StatePlayer.UP || state == StatePlayer.DOWN || state == StatePlayer.LEFT || state == StatePlayer.RIGHT){
			return true;
		}
		else return false;
	}

	public void move(){

		if(isMoving()){
			int x = position.getX();
			int y = position.getY();
			
			switch(state){
			case UP :
				currentSprite = sprite.next();
				if(y-1>31*2*GameManager.SCALE || (x>80*GameManager.SCALE*2 && x<95*2*GameManager.SCALE))
					position.setXY(x, y-SPEED);
				break;
			case DOWN :
				currentSprite = sprite.next();
				if(y+1<122*2*GameManager.SCALE || (x>64*GameManager.SCALE*2 && x<79*2*GameManager.SCALE))
					position.setXY(x, y+SPEED);
				break;
			case LEFT :
				currentSprite = sprite.next();
				if(x-1>32*2*GameManager.SCALE || (y>60*GameManager.SCALE*2 && y<75*2*GameManager.SCALE) )
					position.setXY(x-SPEED, y);
				break;

			case RIGHT :
				currentSprite = sprite.next();
				if(x+1<128*2*GameManager.SCALE || (y>80*GameManager.SCALE*2 && y<95*2*GameManager.SCALE) )
					position.setXY(x+SPEED, y);
				break;
			default:
				break;
			}
			
		}

	}

	public boolean isAttacking(){

		if(state == StatePlayer.ATTACKINGUP || state == StatePlayer.ATTACKINGDOWN || state == StatePlayer.ATTACKINGLEFT || state == StatePlayer.ATTACKINGRIGHT){
			return true;
		}
		else return false;
	}

	public void attack(){

		if(isAttacking()){
			
		}
		
	}

	/*
	 * Grabs an item from the floor (chest or monster)
	 */
	public void GrabItem(){

	}

	public boolean isDead(){
		if(life.get()==0){ 
			return true;
		}
		else return false;
	}

	public void updateGraphic(Graphics g){
		g.drawImage(currentSprite, position.getX(), position.getY(), deltaX*GameManager.SCALE, deltaY*GameManager.SCALE,null);
	}

	public Position getPosition(){
		return position;
	}

}
