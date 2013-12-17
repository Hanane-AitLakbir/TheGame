package entities;

import gameplay.GameManager;
import graphics.AnimatedSprite;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicInteger;

import display.Position;

public class Hero extends Thread{

	private Position position;
	private final int deltaX = 40, deltaY = 40;
	private final int ANIMATIONSPEED = 2;
	private int speed = 2;

	private StatePlayer state = StatePlayer.NONE;
	private AnimatedSprite sprite;
	//private BufferedImage currentSprite;

	public static AtomicInteger life;
	public String name;

	public Hero(AtomicInteger x, AtomicInteger y, String name){

		position = new Position(x, y);
		sprite = new AnimatedSprite(name, ANIMATIONSPEED);
		//currentSprite = sprite.next();
		life = new AtomicInteger(100);

	}

	public void run(){
		while(!isDead()){
			action();
			grabItem();
			//updateGraphic(g);
		}
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

	private boolean isMoving(){

		if(state == StatePlayer.UP || state == StatePlayer.DOWN || state == StatePlayer.LEFT || state == StatePlayer.RIGHT){
			return true;
		}
		else return false;
	}

	private void move(){

		if(isMoving()){
			int x = position.getX();
			int y = position.getY();
			
			//METTRE LE RESET DANS LE CONTROLLER ?
			sprite.changeAnimation(state);
			
			switch(state){
			case UP :
				sprite.next();
				if(y-1>31*2*GameManager.SCALE || (x>80*GameManager.SCALE*2 && x<95*2*GameManager.SCALE))
					position.setXY(x, y-speed);
				break;
			case DOWN :
				sprite.next();
				if(y+1<122*2*GameManager.SCALE || (x>64*GameManager.SCALE*2 && x<79*2*GameManager.SCALE))
					position.setXY(x, y+speed);
				break;
			case LEFT :
				sprite.next();
				if(x-1>32*2*GameManager.SCALE || (y>60*GameManager.SCALE*2 && y<75*2*GameManager.SCALE) )
					position.setXY(x-speed, y);
				break;

			case RIGHT :
				sprite.next();
				if(x+1<128*2*GameManager.SCALE || (y>80*GameManager.SCALE*2 && y<95*2*GameManager.SCALE) )
					position.setXY(x+speed, y);
				break;
			default:
				break;
			}
			
		}

	}

	private boolean isAttacking(){

		if(state == StatePlayer.ATTACKINGUP || state == StatePlayer.ATTACKINGDOWN || state == StatePlayer.ATTACKINGLEFT || state == StatePlayer.ATTACKINGRIGHT){
			return true;
		}
		else return false;
	}

	private void attack(){

		if(isAttacking()){
			int x = position.getX();
			int y = position.getY();
			
			switch(state){
			case ATTACKINGUP :
				sprite.next();
				break;
			case ATTACKINGDOWN :
				sprite.next();
				break;
			case ATTACKINGLEFT :
				sprite.next();
				break;
			case ATTACKINGRIGHT :
				sprite.next();
				break;
			default:
				break;
			}
		}
		
	}

	/*
	 * Grabs an item from the floor (chest or monster)
	 */
	private void grabItem(){

	}

	private boolean isDead(){
		if(life.get()==0){ 
			return true;
		}
		else return false;
	}

	/*
	 * Update the Hero's graphics.
	 * Use sprite.next() here ? or in move ?
	 */
	public void updateGraphic(Graphics g){
		g.drawImage(sprite.getCurrentSprite(), position.getX(), position.getY(), deltaX*GameManager.SCALE, deltaY*GameManager.SCALE,null);
	}

	public Position getPosition(){
		return position;
	}

}
