package entities;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

import networking.TurnManager;
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
	private Item loot = null;

	private AtomicInteger power = new AtomicInteger(10); //Power of the monster (damage dealt when attacking a hero)
	private StateActor state = StateActor.RIGHT, previousState = StateActor.NONE;

	private AtomicInteger life, moveCounter, pauseCounter;
	private AtomicInteger speed = new AtomicInteger(1); //modify according to difficulty
	private int ANIMATIONSPEED = 4;
	private int[] message = new int[2]; // 0 = id + 1 = action when multiplayer&& turn

	public Monster(int x, int y, String name,int difficulty){

		position = new Position(x, y);
		this.target=GameManager.getPlayer();

		//
		if(difficulty==1){
			power.set(5);
			lifeMax = 50;
			life = new AtomicInteger(50);
		}
		else if(difficulty==2){
			power.set(10);
			lifeMax = 75;
			life = new AtomicInteger(75);
		}
		else{
			power.set(15);
			speed.set(2);
			lifeMax = 100;
			life = new AtomicInteger(100);
		}

		moveCounter = new AtomicInteger(0);
		pauseCounter = new AtomicInteger(0);

		sprite = new AnimatedSprite(name, ANIMATIONSPEED);
	}

	public void setId(int id){
		message[0] = id;
	}

	public void run(){

		long delay = 30; // en ms
		long startTime = 0;
		final Monster monster = this;

		final Timer timer = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				if(display && !isDead()){

					if((TurnManager.turn && GameManager.multiplayer) || !GameManager.multiplayer){
						//						actionMultiplayer();
						//					}else{
						action();
					}

					sprite.next();
					GameManager.updateGraphics(sprite.getCurrentSprite(), position, life.get()/lifeMax); //if the player is in its room.
					if(GameManager.multiplayer && TurnManager.turn){
						message[1] = StateActor.convertToInt(state);
						try {
							GameManager.buffer.produce(message);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				if(display && isDead() && loot==null){
					String itemName = randomItemName();
					loot = new Item(itemName,position,monster);
				}
				if(display && isDead() && loot!=null){
					loot.updateGraphics();
				}
				if(!display && isDead()){
					//timer.cancel();
					//timer.purge();
					interrupt();
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
				if(ANIMATIONSPEED!=4){speed.getAndDecrement(); ANIMATIONSPEED = 4; sprite.setSpeed(ANIMATIONSPEED);}
				random(); //He evades the target by walking randomly
				moveCounter.getAndAdd(STEP);
			}
			if(moveCounter.get()>=1500 && moveCounter.get()<4000){ //for about 2s
				if(ANIMATIONSPEED!=3){speed.getAndIncrement(); ANIMATIONSPEED = 3; sprite.setSpeed(ANIMATIONSPEED);}
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

			target.getAttacked(power,state); //Deals damage
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
			if( y-speed.get()>31*GameManager.SCALE*2 ){
				position.setXY(x, y-speed.get());
			}
		}

		if(random==1){
			setState(StateActor.DOWN);
			if( y+speed.get()<122*2*GameManager.SCALE ){
				position.setXY(x, y+speed.get());
			}
		}

		if(random==2){
			setState(StateActor.LEFT);
			if( x-speed.get()>32*2*GameManager.SCALE ){
				position.setXY(x-speed.get(), y);
			}
		}

		if(random==3){
			setState(StateActor.RIGHT);
			if( x+speed.get()<128*2*GameManager.SCALE ){
				position.setXY(x+speed.get(), y);
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
					position.setXY(x, y-speed.get());
				}
			}
			else if(dy<0){
				setState(StateActor.DOWN);
				if( y+1<122*2*GameManager.SCALE ){
					position.setXY(x, y+speed.get());
				}				
			}
		}
		else if(Math.abs(dx)>=Math.abs(dy)){
			if(dx>0){
				setState(StateActor.LEFT);
				if( x-1>32*2*GameManager.SCALE ){
					position.setXY(x-speed.get(), y);
				}
			}
			else if(dx<0){
				setState(StateActor.RIGHT);
				if( x+1<128*2*GameManager.SCALE ){
					position.setXY(x+speed.get(), y);
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

	public void getAttacked(AtomicInteger power){
		life.getAndAdd(-power.get());
	}

	public boolean isDead(){
		if(life.get()<=0){
			return true;
		}

		return false;
	}

	public Position getPosition(){
		return position;
	}

	private String randomItemName(){
		int random = (int) Math.floor(Math.random()*3); 

		switch (random){
		case 0 : return "heart";
		case 1 : return "sword";
		case 2 : {random = (int) Math.floor(Math.random()*2); //To lower the chance of having boots, 
		if(random==0){return "boots";} 	//and increasing the chance to get a heart.
		else return "heart";}
		case 3 : return "heart";
		default : return null;
		}
	}

	public void actionMultiplayer() {
		if(isMoving()){
			int x = position.getX();
			int y = position.getY();

			switch(state){
			case UP :
				if(y-speed.get()>31*2*GameManager.SCALE || (x>80*GameManager.SCALE*2 && x<95*2*GameManager.SCALE))
					position.setXY(x, y-speed.get());
				break;
			case DOWN :
				if(y+speed.get()<122*2*GameManager.SCALE || (x>64*GameManager.SCALE*2 && x<79*2*GameManager.SCALE))
					position.setXY(x, y+speed.get());
				break;
			case LEFT :
				if(x-speed.get()>32*2*GameManager.SCALE || (y>60*GameManager.SCALE*2 && y<75*2*GameManager.SCALE) )
					position.setXY(x-speed.get(), y);
				break;
			case RIGHT :
				if(x+speed.get()<128*2*GameManager.SCALE || (y>80*GameManager.SCALE*2 && y<95*2*GameManager.SCALE) )
					position.setXY(x+speed.get(), y);
				break;
			default:
				break;
			}
		}
	}

	public Item getItem(){
		return loot;
	}

	public void hideMonster(){
		display = false;
	}
	
}