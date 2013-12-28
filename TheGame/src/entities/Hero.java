package entities;

import gameplay.GameManager;
import gameplay.MonsterRoom;
import graphics.AnimatedSprite;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import display.Position;

public class Hero extends Thread{

	private Position position;
	private MonsterRoom currentRoom;
	private final int deltaX = 40, deltaY = 40;
	private final int ANIMATIONSPEED = 2;
	private int speed = 2; 

	private StateActor state = StateActor.RIGHT, previousState = StateActor.NONE;
	private AnimatedSprite sprite;
	//private BufferedImage currentSprite;

	private static AtomicInteger life;
	private int power = 10;
	private String name;

	public Hero(AtomicInteger x, AtomicInteger y, String name){

		position = new Position(x, y);
		sprite = new AnimatedSprite(name, ANIMATIONSPEED);
		//currentSprite = sprite.next();
		life = new AtomicInteger(100);

	}

	public void run(){
		
		long delay = 50; // en ms
		long startTime = 0;

		Timer timer = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				//step();
				action();
				grabItem();
			}

		};
		timer.scheduleAtFixedRate(task,startTime,delay);
		
	}
	
	public StateActor action(){

		move();
		attack();

		return state;

	}

	public void setState(StateActor state){
		previousState = this.state;
		this.state = state;
	}
	
	public StateActor getHeroState(){
		return state;
	}

	private boolean isMoving(){

		if(state == StateActor.UP || state == StateActor.DOWN || state == StateActor.LEFT || state == StateActor.RIGHT){
			return true;
		}
		else return false;
	}

	private void move(){

		if(isMoving()){
			int x = position.getX();
			int y = position.getY();
			
			//METTRE LE RESET DANS LE CONTROLLER ?
			if(previousState != state){sprite.changeAnimation(state);}
			
			switch(state){
			case UP :
				if(y-1>31*2*GameManager.SCALE || (x>80*GameManager.SCALE*2 && x<95*2*GameManager.SCALE))
					position.setXY(x, y-speed);
				break;
			case DOWN :
				if(y+1<122*2*GameManager.SCALE || (x>64*GameManager.SCALE*2 && x<79*2*GameManager.SCALE))
					position.setXY(x, y+speed);
				break;
			case LEFT :
				if(x-1>32*2*GameManager.SCALE || (y>60*GameManager.SCALE*2 && y<75*2*GameManager.SCALE) )
					position.setXY(x-speed, y);
				break;
			case RIGHT :
				if(x+1<128*2*GameManager.SCALE || (y>80*GameManager.SCALE*2 && y<95*2*GameManager.SCALE) )
					position.setXY(x+speed, y);
				break;
			default:
				break;
			}
			sprite.next();
			
		}

	}

	private boolean isAttacking(){

		if(state == StateActor.ATTACKINGUP || state == StateActor.ATTACKINGDOWN || state == StateActor.ATTACKINGLEFT || state == StateActor.ATTACKINGRIGHT){
			return true;
		}
		else return false;
	}
	
	/*
	 * Returns the list of all the monsters a Hero can attack from its position.
	 * Null if none,
	 * a list otherwise.
	 */
	private ArrayList<Monster> canAttack(){

		ArrayList<Monster> monsterList = new ArrayList<Monster>();
		
		for(Monster m : currentRoom.getMonsters()){
			int dx = Math.abs(position.getX() - m.getPosition().getX());
			int dy = Math.abs(position.getY() - m.getPosition().getY());
			
			if(dx<20 && dy<20) monsterList.add(m);
		}
		//TODO Create a parameter RANGE
		//TODO Make a more accurate box ! (more height than width ?) [ ]<- and not []<-
		if(monsterList.size()==0) return null;
		else return monsterList;
	}

	private void attack(){

		if(isAttacking()){
			
			if(previousState!=state){sprite.changeAnimation(state);}
			for(Monster m : canAttack()) {m.getAttacked(power);}
			sprite.next();
			
		}
		
	}

	/*
	 * Grabs an item from the floor (chest or monster)
	 */
	private void grabItem(){

	}
	
	public void getAttacked(int power){
		life.getAndAdd(-power);
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
