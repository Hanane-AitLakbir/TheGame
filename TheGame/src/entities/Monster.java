package entities;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import display.Position;
import gameplay.GameManager;
import graphics.AnimatedSprite;


public class Monster extends Thread {

	private Position position;
	private final int STEP = 20;
	private final double lifeMax; // to have a "real" division (not euclidian)
	private double random = 0, randomTime = 0;
	private AnimatedSprite sprite;
	private Hero target;
	private boolean display = false;
	private int itemNumber = generateRandomItem();

	private int power = 10; //Power of the monster (damage dealt when attacking a hero)
	private StateActor state = StateActor.RIGHT, previousState = StateActor.NONE;

	private AtomicInteger life, moveCounter, pauseCounter;
	private int speed = 1; //modify according to difficulty
	private int ANIMATIONSPEED = 4;

	public Monster(int x, int y, String name,int difficulty){

		position = new Position(x, y);
		this.target=GameManager.getPlayer();

		life = new AtomicInteger(50);
		lifeMax = 50;

		//
		if(difficulty==1){
			power = 5;
		}
		else if(difficulty==2){
			power = 10;
		}
		else{
			power = 15;
		}

		moveCounter = new AtomicInteger(0);
		pauseCounter = new AtomicInteger(0);

		sprite = new AnimatedSprite(name, ANIMATIONSPEED);
	}

	public void run(){

		long delay = 30; // en ms
		long startTime = 0;

		final Timer timer = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				if(display && !isDead()){
					action();
					sprite.next();
					GameManager.updateGraphics(sprite.getCurrentSprite(), position, life.get()/lifeMax); //if the player is in its room.
				}
				if(display && isDead()){
					//					timer.cancel();
					//					timer.purge();
				}

			}

		};
		timer.scheduleAtFixedRate(task,startTime,delay);
	}

	// true if the player is in its rooms, false otherwise. => used by MonsterRoom
	public void setDisplay(boolean display){
		this.display = display;
	}

	private void action(){

		//target = closerHero();  //IF MORE THAN ONE PLAYER !

		if(isMoving()){ //If the monster must move (UP DOWN LEFT or RIGHT)
			if(moveCounter.get()>=4000){moveCounter.set(0);}
			if(moveCounter.get()<1500){ //for about 2s
				if(ANIMATIONSPEED!=4){speed = 1; ANIMATIONSPEED = 4; sprite.setSpeed(ANIMATIONSPEED);}
				random(); //He evades the target by walking randomly
				moveCounter.getAndAdd(STEP);
			}
			if(moveCounter.get()>=1500 && moveCounter.get()<4000){ //for about 2s
				if(ANIMATIONSPEED!=3){speed = 2; ANIMATIONSPEED = 3; sprite.setSpeed(ANIMATIONSPEED);}
				chase(); //He chases the target
				moveCounter.getAndAdd(STEP);
			}
		}

		if(canAttack()){
			switch (state){
			case UP : setState(StateActor.ATTACKINGUP); break;
			case DOWN : setState(StateActor.ATTACKINGDOWN); break;
			case LEFT : setState(StateActor.ATTACKINGLEFT); break;
			case RIGHT : setState(StateActor.ATTACKINGRIGHT); break;
			default: setState(StateActor.ATTACKINGRIGHT); break;
			}
		}
		else if(isAttacking()){

			target.getAttacked(power); //Deals damage
			setState(StateActor.NONE); //Wait a bit.

		}
		else if(state == StateActor.NONE){

			pauseCounter.getAndIncrement(); //Adjust !!!
			if(pauseCounter.get() >= ANIMATIONSPEED*7+1) {setState(StateActor.RIGHT); pauseCounter.set(0);} 

		}
	}

	private boolean isMoving(){

		if(state == StateActor.UP || state == StateActor.DOWN || state == StateActor.LEFT || state == StateActor.RIGHT){
			return true;
		}
		else return false;
	}

	private void random(){

		int x = position.getX();
		int y = position.getY();

		if(randomTime<=STEP){
			random = Math.floor(4*Math.random());
		}

		if(randomTime<=STEP){randomTime = Math.floor(1000*Math.random()) + STEP+1;}

		if(random==0){
			setState(StateActor.UP);
			if( y-speed>31*GameManager.SCALE*2 ){
				position.setXY(x, y-speed);
			}
		}

		if(random==1){
			setState(StateActor.DOWN);
			if( y+speed<122*2*GameManager.SCALE ){
				position.setXY(x, y+speed);
			}
		}

		if(random==2){
			setState(StateActor.LEFT);
			if( x-speed>32*2*GameManager.SCALE ){
				position.setXY(x-speed, y);
			}
		}

		if(random==3){
			setState(StateActor.RIGHT);
			if( x+speed<128*2*GameManager.SCALE ){
				position.setXY(x+speed, y);
			}
		}

		randomTime-=STEP;

	}

	private void chase(){
		int x = position.getX();
		int y = position.getY();

		int dx = x - target.getPosition().getX();
		int dy = y - target.getPosition().getY();

		//Do the Math : try all the different cases (8) and summarize into this.
		//The monster will try to chase the hero by covering the biggest horizontal/vertical distance first.
		if(Math.abs(dx)<Math.abs(dy)){ 
			if(dy>0){
				setState(StateActor.UP);
				if( y-1>31*GameManager.SCALE*2 ){
					position.setXY(x, y-speed);
				}
			}
			else if(dy<0){
				setState(StateActor.DOWN);
				if( y+1<122*2*GameManager.SCALE ){
					position.setXY(x, y+speed);
				}				
			}
		}
		else if(Math.abs(dx)>=Math.abs(dy)){
			if(dx>0){
				setState(StateActor.LEFT);
				if( x-1>32*2*GameManager.SCALE ){
					position.setXY(x-speed, y);
				}
			}
			else if(dx<0){
				setState(StateActor.RIGHT);
				if( x+1<128*2*GameManager.SCALE ){
					position.setXY(x+speed, y);
				}
			}
		}
	}

	private boolean isAttacking(){

		if(state == StateActor.ATTACKINGUP || state == StateActor.ATTACKINGDOWN || state == StateActor.ATTACKINGLEFT || state == StateActor.ATTACKINGRIGHT){
			return true;
		}
		else return false;
	}

	private boolean canAttack(){

		int dx = position.getX() - target.getPosition().getX();
		int dy = position.getY() - target.getPosition().getY();

		switch (state)
		{
		case UP : 
			if(Math.abs(dx)<20 && dy>0 && dy<35) return true;
			break;
		case DOWN :
			if(Math.abs(dx)<20 && dy<0 && dy>-35) return true;
			break;
		case LEFT :
			if(dx>0 && dx<35 && Math.abs(dy)<20) return true;
			break;
		case RIGHT :
			if(dx<0 && dx>-35 && Math.abs(dy)<20) return true;
			break;
		default : 
			return false;
		}
		return false;
	}

	public void setState(StateActor state){
		if(previousState != state){sprite.changeAnimation(state);}
		previousState = this.state;
		this.state = state;
	}

	public void getAttacked(int power){
		life.getAndAdd(-power);
	}

	public boolean isDead(){
		if(life.get()<=0){

			switch(itemNumber){
			case 0 : 
				new Item("heart",position).updateGraphics();
				break;
			case 1 : 
				new Item("sword",position).updateGraphics();
				break;
			}
			return true;
		}

		return false;
	}

	public Position getPosition(){
		return position;
	}

	private int generateRandomItem(){
		return (int) Math.floor(Math.random()*2);
	}
}