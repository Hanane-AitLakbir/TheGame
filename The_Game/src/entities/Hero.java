package entities;

import graphics.AnimatedSprite;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicInteger;

import main.Game;

public class Hero {

	// ajouter le mode protected
	// mettre les interactions Monter/Hero
	// Mettre en static les zones 
	// faire une interface (actor)
	

	private int x,y;
	public StatePlayer state = StatePlayer.NONE;
	public HeroPosition position = HeroPosition.ROOM;
	
	
	public boolean attack =false;
	public boolean protect = false;
	private final int SPEED = 2;
	
	private final int ANIMATIONSPEED = 2;
	private AnimatedSprite front, back, left, right, attack_left, attack_right, attack_front, attack_back;
	private BufferedImage currentSprite;

	private int deltaX, deltaY;
	private int lastAction;

	public static AtomicInteger life;

	public Hero(int x, int y, String name){
		this.x=x;
		this.y=y;

		life = new AtomicInteger(100);

		front = new AnimatedSprite(("/"+name+"_front.png"), 7, 40, 35,ANIMATIONSPEED);
		back = new AnimatedSprite(("/"+name+"_back.png"), 7, 40,35, ANIMATIONSPEED); //20 30
		left = new AnimatedSprite(("/"+name+"_left.png"), 7, 40, 35, ANIMATIONSPEED);
		right = new AnimatedSprite(("/"+name+"_right.png"), 7, 40, 35, ANIMATIONSPEED);

		attack_front = new AnimatedSprite(("/"+name+"_front_attack.png"), 6, 40, 35, ANIMATIONSPEED);
		attack_back = new AnimatedSprite(("/"+name+"_back_attack.png"), 6, 40, 35, ANIMATIONSPEED);
		attack_left = new AnimatedSprite(("/"+name+"_left_attack.png"), 6, 40, 35, ANIMATIONSPEED);
		attack_right = new AnimatedSprite(("/"+name+"_right_attack.png"), 6, 40, 35, ANIMATIONSPEED);

	}

	public void setXY(int x,int y){
		this.x = x;
		this.y = y;
	}
	public void move(){
		if(!attack){
			attack_front.reset();
			attack_left.reset();
			attack_right.reset();
			attack_back.reset();

			switch(state){
			case UP :
				lastAction=8;
				front.reset();
				left.reset();
				right.reset();

				currentSprite = back.next();
				if((y-1>31*Game.SCALE*2 || (x>80*Game.SCALE*2 && x<95*2*Game.SCALE))){
					y-=SPEED;
				}

				break;
			case DOWN :
				lastAction=2;
				back.reset();
				right.reset();
				left.reset();

				currentSprite = front.next();
				if(y+1<122*2*Game.SCALE || (x>64*Game.SCALE*2 && x<79*2*Game.SCALE))
					y+=SPEED;
				break;
			case LEFT :
				lastAction=4;
				front.reset();
				back.reset();
				right.reset();

				currentSprite = left.next();
				if( x-1>32*2*Game.SCALE || (y>60*Game.SCALE*2 && y<75*2*Game.SCALE) )
					x-=SPEED;
				break;

			case RIGHT :
				lastAction = 6;
				front.reset();
				back.reset();
				left.reset();

				currentSprite = right.next();
				if(x+1<128*2*Game.SCALE || (y>80*Game.SCALE*2 && y<95*2*Game.SCALE) )
					x+=SPEED;
				break;
			default:
				break;
			}
			deltaX=40;
			deltaY=40;

		}
		if(attack){
			front.reset();
			left.reset();
			right.reset();
			back.reset();

			if(lastAction==8){
				attack_front.reset();
				attack_left.reset();
				attack_right.reset();

				currentSprite = attack_back.next();
			}
			if(lastAction==2){
				attack_back.reset();
				attack_left.reset();
				attack_right.reset();

				currentSprite = attack_front.next();
			}
			if(lastAction==4){
				attack_back.reset();
				attack_front.reset();
				attack_right.reset();

				currentSprite = attack_left.next();
			}
			if(lastAction==6){
				attack_back.reset();
				attack_left.reset();
				attack_front.reset();

				currentSprite = attack_right.next();
			}
			//attack();
			deltaX=40;
			deltaY=40;
		}

		if(protect){

		}


	}

	public void attack() {
		Monster.getDamaged(); //probl�me si plusieurs monstres

	}

	public void updateGraphic(Graphics g){
		// x,y -> coordinates of the player
		// 20/30*Game.SCALE -> makes the img larger
		if(position == HeroPosition.ROOM) g.drawImage(currentSprite, x,y,deltaX*Game.SCALE, deltaY*Game.SCALE,null);
		//System.out.println(isOut + " " + x);
	}

	public static synchronized void getDamaged(){
		life.getAndAdd(-10);
	}

	public void GrabItem(){
		/*
		 * Attape un item (dans un coffre ou issu de la mort d'un monstre)
		 */
	}

	public void isOutOfRoom(){
		if (x>80*Game.SCALE*2 && x<95*2*Game.SCALE && y<16*2*Game.SCALE) position = HeroPosition.UPEXIT; //porte nord
		if (x>64*Game.SCALE*2 && x<79*2*Game.SCALE && y>138*2*Game.SCALE) position = HeroPosition.DOWNEXIT; //porte sud
		if (y>60*Game.SCALE*2 && y<75*2*Game.SCALE && x<16*2*Game.SCALE) position = HeroPosition.LEFTEXIT; //porte ouest
		if (y>80*Game.SCALE*2 && y<95*2*Game.SCALE && x>140*2*Game.SCALE){
			position = HeroPosition.RIGHTEXIT ; // porte est
		}
		else{
			position = HeroPosition.ROOM;
		}
	}
	
	public HeroPosition getPosition(){
		if (x>80*Game.SCALE*2 && x<95*2*Game.SCALE && y<16*2*Game.SCALE){ position = HeroPosition.UPEXIT;} //porte nord
		if (x>64*Game.SCALE*2 && x<79*2*Game.SCALE && y>138*2*Game.SCALE){ position = HeroPosition.DOWNEXIT;} //porte sud
		if (y>60*Game.SCALE*2 && y<75*2*Game.SCALE && x<16*2*Game.SCALE){ position = HeroPosition.LEFTEXIT;} //porte ouest
		if (y>80*Game.SCALE*2 && y<95*2*Game.SCALE && x>140*2*Game.SCALE){
			position = HeroPosition.RIGHTEXIT ; // porte est
		}
		else{
			position = HeroPosition.ROOM;
		}
		return position;	
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}

}