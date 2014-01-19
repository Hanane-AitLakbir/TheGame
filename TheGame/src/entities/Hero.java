package entities;

import gameplay.GameManager;
import graphics.AnimatedSprite;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

import networking.TurnManager;
import display.Position;

public class Hero extends Thread{

	private Position position;
	private Monster[] monsters;
	ArrayList<Monster> deadMonsters = new ArrayList<>();

	private final int deltaX = 40, deltaY = 40;
	private int ANIMATIONSPEED = 2;
	private AtomicInteger speed = new AtomicInteger(3); 

	private StateActor state = StateActor.PROTECTED, previousState = StateActor.NONE;
	private AnimatedSprite sprite;
	private BufferedImage currentSprite;

	private final double lifeMax;
	private static AtomicInteger life;
	private AtomicInteger power = new AtomicInteger(10);

	private int pauseCounter;
	private long delay = 30; // en ms
	private int[] message = new int[2];


	public Hero(int x, int y, String name){

		position = new Position(x, y);
		sprite = new AnimatedSprite(name, ANIMATIONSPEED);
		currentSprite = sprite.next();
		life = new AtomicInteger(100);
		lifeMax = 100;
		message[0] = -1;
	}

	public void run(){

		long startTime = 0;

		Timer timer = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {

				if(!isDead()){
					if((TurnManager.turn && GameManager.multiplayer) || !GameManager.multiplayer){
						action();
						grabItem();
					
					if(isMoving() || isAttacking()){
						currentSprite = sprite.next();
					}
					//}
					if(GameManager.multiplayer){ //&& TurnManager.turn){
						message[1] = StateActor.convertToInt(state)+1000*position.getX()+1000*1000*position.getY();
						try {
							GameManager.buffer.produce(message);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

					}
				//System.out.println("\t\t\t\t Hero updates graphics");
					GameManager.updateGraphics(currentSprite, position,life.get()/lifeMax);
				}
				else if(isDead()){
					interrupt();  //Comment for testing purpose...
				}
			}

		};
		timer.scheduleAtFixedRate(task,startTime,delay);


	}

	public void action(){

		move();
		attack();

	}

	public void setState(StateActor state){
		if(previousState != state){sprite.changeAnimation(state);}
		previousState = this.state;
		this.state = state;
	}

	public StateActor getHeroState(){
		return state;
	}

	//USELESS
	//	public StateActor getPreviousState(){
	//		return previousState;
	//	}
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
				if(y-speed.get()>24*2*GameManager.SCALE || (x>80*GameManager.SCALE*2 && x<90*2*GameManager.SCALE))
					position.setXY(x, y-speed.get());
				break;
			case DOWN :
				if(y+speed.get()<122*2*GameManager.SCALE || (x>60*GameManager.SCALE*2 && x<73*2*GameManager.SCALE))
					position.setXY(x, y+speed.get());
				break;
			case LEFT :
				if(x-speed.get()>26*2*GameManager.SCALE || (y>56*GameManager.SCALE*2 && y<71*2*GameManager.SCALE) )
					position.setXY(x-speed.get(), y);
				break;
			case RIGHT :
				if(x+speed.get()<122*2*GameManager.SCALE || (y>73*GameManager.SCALE*2 && y<87*2*GameManager.SCALE) )
					position.setXY(x+speed.get(), y);
				break;
			default:
				break;
			}

		}

	}

	private boolean isAttacking(){

		if(state == StateActor.ATTACKINGUP || state == StateActor.ATTACKINGDOWN || state == StateActor.ATTACKINGLEFT || state == StateActor.ATTACKINGRIGHT){
			return true;
		}
		else return false;
	}

	/**
	 * Returns the list of all the monsters a Hero can attack from its position.
	 * Null if none,
	 * a list otherwise.
	 */
	private ArrayList<Monster> canAttack(){

		ArrayList<Monster> monsterList = new ArrayList<Monster>();

		if(monsters!=null){

			for(Monster m : monsters){

				int dx = position.getX() - m.getPosition().getX();
				int dy = position.getY() - m.getPosition().getY();

				switch (state)
				{
				case ATTACKINGUP : 
					if(Math.abs(dx)<40 && dy>0 && dy<60) monsterList.add(m);
					break;
				case ATTACKINGDOWN :
					if(Math.abs(dx)<40 && dy<0 && dy>-60) monsterList.add(m);
					break;
				case ATTACKINGLEFT :
					if(dx>0 && dx<60 && Math.abs(dy)<40) monsterList.add(m);
					break;
				case ATTACKINGRIGHT :
					if(dx<0 && dx>-60 && Math.abs(dy)<40) monsterList.add(m);
					break;
				default : 
					break;
				}
			}

			if(monsterList.size()==0) return null;
		}

		return monsterList;

	}

	private void attack(){

		if(isAttacking()){

			ArrayList<Monster> monstersList = canAttack();
			if(monstersList!=null){
				for(Monster m : canAttack()) {m.setState(StateActor.NONE); m.getAttacked(power);} //Stops the monster and attacks it.
			}

			while(pauseCounter<ANIMATIONSPEED*7+1){ //Can't launch another attack right away ! But displays the animation.
				currentSprite = sprite.next();
				GameManager.updateGraphics(currentSprite, position,life.get()/lifeMax);
				try {
					sleep(delay);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				pauseCounter++;
			}
			setState(StateActor.NONE);
			pauseCounter=0;
			sprite.reset();

		}

	}

	/**
	 * Grabs an item from the floor
	 */
	private void grabItem(){

		if(monsters!=null){

			String action = "";
			Item[] items = new Item[monsters.length];
			for(int i=0; i<monsters.length; i++){
				items[i] = monsters[i].getItem();

				if(items[i]!=null){
					int dx = position.getX() - items[i].getPosition().getX();
					int dy = position.getY()+10 - items[i].getPosition().getY();

					if(Math.abs(dx)<15 && Math.abs(dy)<15){
						action = items[i].lootItem();
					}


					switch (action){
					case "heart" : 
						if(life.get()>=lifeMax-10){life.set((int) lifeMax);} 
						else {life.getAndAdd(10);}
						break;
					case "sword" : 
						power.getAndAdd(5);
						break;
					case "boots" :
						if(speed.get()<=6){speed.getAndIncrement();}
						if(speed.get()>=5){ANIMATIONSPEED = 1; sprite.setSpeed(ANIMATIONSPEED);}
						break;
					default :
						break;
					}

				}

			}
		}
	}

	public synchronized void getAttacked(AtomicInteger power, StateActor state){
		life.getAndAdd(-power.get());
		switch(state){
		case ATTACKINGUP :
			position.setXY(position.getX(), position.getY()-10);
			break;
		case ATTACKINGDOWN :
			position.setXY(position.getX(), position.getY()+10);
			break;
		case ATTACKINGLEFT :
			position.setXY(position.getX()-10, position.getY());
			break;
		case ATTACKINGRIGHT :
			position.setXY(position.getX()+10, position.getY());
			break;
		default :
			break;
		}
	}

	private boolean isDead(){
		if(life.get()<=0){ 
			return true;
		}
		else return false;
	}

	/*
	 * Update the Hero's graphics.
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
