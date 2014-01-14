package entities;

import gameplay.GameManager;
import graphics.AnimatedSprite;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import display.Position;

public class Hero extends Thread{

	private Position position;
	private Monster[] monsters;
	ArrayList<Monster> deadMonsters = new ArrayList<>();
	
	private final int deltaX = 40, deltaY = 40;
	private final int ANIMATIONSPEED = 2;
	private int speed = 2; 

	private StateActor state = StateActor.PROTECTED, previousState = StateActor.NONE;
	private AnimatedSprite sprite;
	private BufferedImage currentSprite;
	
	private final double lifeMax;
	private static AtomicInteger life;
	private int power = 10;
	//private String name;
	private int pauseCounter;
	

	public Hero(int x, int y, String name){

		position = new Position(x, y);
		sprite = new AnimatedSprite(name, ANIMATIONSPEED);
		//currentSprite = sprite.next();
		life = new AtomicInteger(100);
		lifeMax = 100;

	}

	public void run(){

		long delay = 30; // en ms
		long startTime = 0;

		Timer timer = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				//step();
				if(GameManager.turn){
					action();
					grabItem();
				}
				GameManager.updateGraphics(currentSprite, position,life.get()/lifeMax);
			}

		};
		timer.scheduleAtFixedRate(task,startTime,delay);


	}

	public void action(){

		move();
		attack();

		//return state;

	}

	public void setState(StateActor state){
		if(previousState != state){sprite.changeAnimation(state);}
		previousState = this.state;
		this.state = state;
	}

	public StateActor getHeroState(){
		return state;
	}
	
	public StateActor getPreviousState(){
		return previousState;
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

			switch(state){
			case UP :
				if(y-speed>31*2*GameManager.SCALE || (x>80*GameManager.SCALE*2 && x<95*2*GameManager.SCALE))
					position.setXY(x, y-speed);
				break;
			case DOWN :
				if(y+speed<122*2*GameManager.SCALE || (x>64*GameManager.SCALE*2 && x<79*2*GameManager.SCALE))
					position.setXY(x, y+speed);
				break;
			case LEFT :
				if(x-speed>32*2*GameManager.SCALE || (y>60*GameManager.SCALE*2 && y<75*2*GameManager.SCALE) )
					position.setXY(x-speed, y);
				break;
			case RIGHT :
				if(x+speed<128*2*GameManager.SCALE || (y>80*GameManager.SCALE*2 && y<95*2*GameManager.SCALE) )
					position.setXY(x+speed, y);
				break;
			default:
				break;
			}
			currentSprite = sprite.next();

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

		if(monsters!=null){
			for(Monster m : monsters){
				int dx = Math.abs(position.getX() - m.getPosition().getX());
				int dy = Math.abs(position.getY() - m.getPosition().getY());

				if(dx<30 && dy<30) monsterList.add(m);
			}
		}
		//TODO Create a parameter RANGE
		//TODO Make a more accurate box ! (more height than width ?) [ ]<- and not []<-
		if(monsterList.size()==0) return null;
		else return monsterList;
	}

	private void attack(){
		
		if(isAttacking()){
			
			currentSprite = sprite.next();
			setState(StateActor.NONE);
			ArrayList<Monster> monstersList = canAttack();
			if(monstersList!=null){
				for(Monster m : canAttack()) {m.getAttacked(power);}
			}

		}

		else if(state == StateActor.NONE){
			currentSprite = sprite.next();
			pauseCounter++; //Adjust !!!
			if(pauseCounter >= 7) {
				setState(StateActor.PROTECTED);
				pauseCounter=0;
			}			
		}
	}
	/*
	 * Grabs an item from the floor (chest or monster)
	 */
	private void grabItem(){
		
	}

	public void getAttacked(int power){
		life.getAndAdd(-power);
		//rajouter le recul du coup ???
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

	public void setMonsters(Monster[] monsters){
		this.monsters = monsters;
	}
	
}
