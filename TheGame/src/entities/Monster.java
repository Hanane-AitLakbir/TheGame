package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
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
	private BufferedImage currentSprite;

	Hero target;
	String name;
	private StateActor state = StateActor.RIGHT;
	private int moveCounter=0;
	int upStep,downStep,leftStep,rightStep; // pour la methode patrol

	public static AtomicInteger life;
	private int speed = 1; //a modifier selon difficulte
	private final int ANIMATIONSPEED = 2;

	public Monster(AtomicInteger x, AtomicInteger y, String name,int difficulty){

		position = new Position(x, y);
		this.name = name;
		this.target=GameManager.getPlayer();

		life = new AtomicInteger(difficulty*10);

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
		long delay = 150; // en ms
		long startTime = 0;

		Timer timer = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				action();
			}


		};
		timer.scheduleAtFixedRate(task,startTime,delay);
	}


	//TODO change random by evade ? 
	public void action(){

		//target = closerHero();  //IF MORE THAN ONE PLAYER !

		if(isMoving()){ //If the monster must move (UP DOWN LEFT or RIGHT)
			if(moveCounter>=4000){moveCounter = 0;}
			while(moveCounter<2000){ //for about 2s
				random(); //He evades the target by walking randomly
				moveCounter += 20;
			}
			while(moveCounter>=2000 && moveCounter<4000){ //for about 2s
				chase(); //He chases the target
				moveCounter += 20;
			}
		}
		if(canAttack()){
			switch (state){
			case UP : setState(StateActor.ATTACKINGUP); break;
			case DOWN : setState(StateActor.ATTACKINGDOWN); break;
			case LEFT : setState(StateActor.ATTACKINGLEFT); break;
			case RIGHT : setState(StateActor.ATTACKINGRIGHT); break;
			}
		}
		
		//ADD else if attacking blabla (I have it on a paper)
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
			currentSprite = sprite.next();
			if( y-1>31*GameManager.SCALE*2 ){
				position.setXY(x, y-speed);
			}
		}

		if(random==1){
			currentSprite = sprite.next();
			if( y+1<122*2*GameManager.SCALE ){
				position.setXY(x, y+speed);
			}
		}

		if(random==2){
			currentSprite = sprite.next();
			if( x-1>32*2*GameManager.SCALE ){
				position.setXY(x-speed, y);
			}
		}

		if(random==3){
			currentSprite = sprite.next();
			if( x+1<128*2*GameManager.SCALE ){
				position.setXY(x+speed, y);
			}
		}
	}

	//TODO condense the moving parts into moveUp(), ...
	public void chase(){
		int x = target.getPosition().getX();
		int y = target.getPosition().getY();

		int dx = position.getX() - x;
		int dy = position.getY() - y;

		//Do the Math : try all the different cases (8) and summarize into this.
		//The monster will try to chase the hero by covering the biggest horizontal/vertical distance first.
		if(Math.abs(dx)<Math.abs(dy)){ 
			if(dy>0){
				currentSprite = sprite.next();
				if( y-1>31*GameManager.SCALE*2 ){
					position.setXY(x, y-speed);
				}
			}
			else if(dy<0){
				currentSprite = sprite.next();
				if( y+1<122*2*GameManager.SCALE ){
					position.setXY(x, y+speed);
				}				
			}
		}
		else if(Math.abs(dx)>=Math.abs(dy)){
			if(dx>0){
				currentSprite = sprite.next();
				if( x-1>32*2*GameManager.SCALE ){
					position.setXY(x-speed, y);
				}
			}
			else if(dx<0){
				currentSprite = sprite.next();
				if( x+1<128*2*GameManager.SCALE ){
					position.setXY(x+speed, y);
				}
			}
		}
	}

	private boolean canAttack(){

		int dx = Math.abs(position.getX() - target.getPosition().getX());
		int dy = Math.abs(position.getY() - target.getPosition().getY());

		//TODO Create a parameter RANGE
		//TODO Make a more accurate box ! (more height than width ?) [ ]<- and not []<-
		if(dx<20 && dy<20) return true; //change to set the attacking range !
		else return false;
	}

	public void updateGraphics(Graphics g){
		/*
		 * Changer les entiers avant Game.SCALE
		 */
		g.drawImage(currentSprite, position.getX(), position.getY(), deltaX*GameManager.SCALE, deltaY*GameManager.SCALE,null);
	}	

	public void setState(StateActor state){
		this.state = state;
	}

	public boolean isDead(){
		if(life.get()==0){
			// TODO Faire apparaitre un item 
			return true;
		}
		return false;
	}

}