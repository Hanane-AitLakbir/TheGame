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
	private int speed = 4; 

	private StateActor state = StateActor.PROTECTED, previousState = StateActor.NONE;
	private AnimatedSprite sprite;
	private BufferedImage currentSprite;

	private final double lifeMax;
	private static AtomicInteger life;
	private int power = 10;

	private int pauseCounter;
	private long delay = 30; // en ms


	public Hero(int x, int y, String name){

		position = new Position(x, y);
		sprite = new AnimatedSprite(name, ANIMATIONSPEED);
		currentSprite = sprite.next();
		life = new AtomicInteger(100);
		lifeMax = 100;

	}

	public void run(){

		long startTime = 0;

		Timer timer = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {

				if(GameManager.turn){
					action();
					grabItem();
					if(isMoving() || isAttacking()){
						currentSprite = sprite.next();
					}
				}
				GameManager.updateGraphics(currentSprite, position,life.get()/lifeMax);
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

	public StateActor getPreviousState(){
		return previousState;
	}
	private boolean isMoving(){

		if(state == StateActor.UP || state == StateActor.DOWN || state == StateActor.LEFT || state == StateActor.RIGHT){
			return true;
		}
		else return false;
	}

	//TODO Gerer collisions monstres ! ! !
	private void move(){

		if(isMoving()){
			int x = position.getX();
			int y = position.getY();

			switch(state){
			case UP :
				if(y-speed>24*2*GameManager.SCALE || (x>80*GameManager.SCALE*2 && x<90*2*GameManager.SCALE))
					position.setXY(x, y-speed);
				break;
			case DOWN :
				if(y+speed<122*2*GameManager.SCALE || (x>60*GameManager.SCALE*2 && x<73*2*GameManager.SCALE))
					position.setXY(x, y+speed);
				break;
			case LEFT :
				if(x-speed>26*2*GameManager.SCALE || (y>56*GameManager.SCALE*2 && y<71*2*GameManager.SCALE) )
					position.setXY(x-speed, y);
				break;
			case RIGHT :
				if(x+speed<122*2*GameManager.SCALE || (y>73*GameManager.SCALE*2 && y<87*2*GameManager.SCALE) )
					position.setXY(x+speed, y);
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

	/*
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

	/*
	 * Grabs an item from the floor (chest or monster)
	 */
	private void grabItem(){

	}

	public void getAttacked(int power){
		life.getAndAdd(-power);
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
