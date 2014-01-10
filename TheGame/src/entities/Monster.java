package entities;

import java.awt.Graphics;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import display.Position;
import gameplay.GameManager;
import graphics.AnimatedSprite;


public class Monster extends Thread {

	private Position position;
	private final int deltaX = 40, deltaY = 40;
	private AnimatedSprite sprite;
	private Hero target;
	private boolean display = false;
	
	//USELESS
	//private String name; 
	
	private int power = 10; //Power of the monster (damage dealt when attacking a hero)
	private StateActor state = StateActor.RIGHT, previousState = StateActor.NONE;

	private AtomicInteger life, moveCounter, pauseCounter;
	private int speed = 1; //a modifier selon difficulty => NECESSAIRE ?? 
	private final int ANIMATIONSPEED = 2;

	public Monster(AtomicInteger x, AtomicInteger y, String name,int difficulty){

		position = new Position(x, y);
		//this.name = name;
		this.target=GameManager.getPlayer();

		life = new AtomicInteger(50);
		
		//
		if(difficulty==1){
			power = 5;
		}
		else{
			power = 10;
		}
		
		moveCounter = new AtomicInteger(0);
		pauseCounter = new AtomicInteger(0);

		sprite = new AnimatedSprite(name, ANIMATIONSPEED);
	}

	public void run(){
		/* TODO
		 * -> a mettre dans le run du timer ??
		 * Tant qu'on est dans sa salle
		 *                 si distance entre target et this < rayon (peut-etre dimension d'un sprite)
		 *                         attack()
		 *                         updateGraphics()
		 *                 sinon
		 *                         move() -> le monstre erre en fait
		 *                         updateGraphics();
		 */
		long delay = 50; // en ms
		long startTime = 0;

		Timer timer = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				action();
				if(display) GameManager.updateGraphics(sprite.getCurrentSprite(), position); //if the player is in its room.
			}

		};
		timer.scheduleAtFixedRate(task,startTime,delay);
	}

	// true if the player is in its rooms, false otherwise. => used by MonsterRoom
	public void setDisplay(boolean display){
		this.display = display;
	}
	
	//TODO change random by evade ? 
	private void action(){

		//target = closerHero();  //IF MORE THAN ONE PLAYER !

		if(isMoving()){ //If the monster must move (UP DOWN LEFT or RIGHT)
			if(moveCounter.get()>=4000){moveCounter.set(0);}
			while(moveCounter.get()<2000){ //for about 2s
				random(); //He evades the target by walking randomly
				moveCounter.getAndAdd(20);
			}
			while(moveCounter.get()>=2000 && moveCounter.get()<4000){ //for about 2s
				chase(); //He chases the target
				moveCounter.getAndAdd(20);
			}

			if(previousState!=state){sprite.changeAnimation(state);}
			sprite.next();

			if(canAttack()){
				switch (state){
				case UP : setState(StateActor.ATTACKINGUP); break;
				case DOWN : setState(StateActor.ATTACKINGDOWN); break;
				case LEFT : setState(StateActor.ATTACKINGLEFT); break;
				case RIGHT : setState(StateActor.ATTACKINGRIGHT); break;
				default: setState(StateActor.ATTACKINGRIGHT); break;
				}
			}

		}
		else if(isAttacking()){

			target.getAttacked(power); //Deals damage
			if(previousState!=state){sprite.changeAnimation(state);}
			sprite.next();
			setState(StateActor.NONE); //Wait a bit.

		}
		else if(state == StateActor.NONE){

			sprite.next();
			pauseCounter.getAndIncrement(); //Adjust !!!
			if(pauseCounter.get() >= 7) {setState(StateActor.RIGHT); pauseCounter.set(0);} 

		}

		//TODO ADD else if attacking blabla (I have it on a paper)
		// + SPRITE + verify else if or if !!! (can attack on the same call of action() or not)

	}

	private boolean isMoving(){

		if(state == StateActor.UP || state == StateActor.DOWN || state == StateActor.LEFT || state == StateActor.RIGHT){
			return true;
		}
		else return false;
	}

	private void random(){
		double random = Math.floor(4*Math.random());
		int x = position.getX();
		int y = position.getY();


		if(random==0){
			setState(StateActor.UP);
			if( y-1>31*GameManager.SCALE*2 ){
				position.setXY(x, y-speed);
			}
		}

		if(random==1){
			setState(StateActor.DOWN);
			if( y+1<122*2*GameManager.SCALE ){
				position.setXY(x, y+speed);
			}
		}

		if(random==2){
			setState(StateActor.LEFT);
			if( x-1>32*2*GameManager.SCALE ){
				position.setXY(x-speed, y);
			}
		}

		if(random==3){
			setState(StateActor.RIGHT);
			if( x+1<128*2*GameManager.SCALE ){
				position.setXY(x+speed, y);
			}
		}

	}

	//TODO condense the moving parts into moveUp(), ...
	private void chase(){
		int x = target.getPosition().getX();
		int y = target.getPosition().getY();

		int dx = position.getX() - x;
		int dy = position.getY() - y;

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

		int dx = Math.abs(position.getX() - target.getPosition().getX());
		int dy = Math.abs(position.getY() - target.getPosition().getY());

		//TODO Create a parameter RANGE
		//TODO Make a more accurate box ! (more height than width ?) [ ]<- and not []<-
		if(dx<20 && dy<20) return true; //change to set the attacking range !
		else return false;
	}

//	public void updateGraphics(Graphics g){
//		/*
//		 * Changer les entiers avant Game.SCALE
//		 */
//		g.drawImage(sprite.getCurrentSprite(), position.getX(), position.getY(), deltaX*GameManager.SCALE, deltaY*GameManager.SCALE,null);
//	}	

	private void setState(StateActor state){
		this.state = state;
	}

	public void getAttacked(int power){
		life.getAndAdd(-power);
	}

	public boolean isDead(){
		if(life.get()==0){
			// TODO Faire apparaitre un item 
			return true;
		}
		return false;
	}

	public Position getPosition(){
		return position;
	}

}