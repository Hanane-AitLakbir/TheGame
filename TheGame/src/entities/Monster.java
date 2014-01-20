package entities;

import java.awt.Graphics;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import networking.TurnManager;
import gameplay.GameManager;

/**
 * The monster of the game, which will try to kill the hero every change he gets.<p>
 * He has a random movement for a moment, where he can't even attack (he is vulnerable), 
 * then a chase pattern where he will try to chase down and kill the Hero.<p>
 * <p>
 * He has a maximum of (50/75/100), a starting attack of (5/10/15) and a starting speed of (1/2/3) (here, the higher the faster).
 */
public class Monster extends Thread {

	protected Position position;
	private StateActor state = StateActor.RIGHT, previousState = StateActor.NONE;
	private AtomicInteger speed; //modify according to difficulty
	private final double LIFE_MAX; // double to have a "real" division (not euclidian)
	private AtomicInteger life;
	private AtomicInteger power; //Power of the monster (damage dealt when attacking a hero)
	private Item loot = null;
	
	private int ANIMATIONSPEED = 4;
	private AnimatedSprite sprite;
	
	protected Hero target;
	
	private final int STEP = 20; //Step for the moveCounter
	private double random = 0, randomTime = 0;
	private boolean display = false;
	private final long delay = 30; // en ms
	private AtomicInteger moveCounter, pauseCounter; 
	private int[] message = new int[2]; // 0 = id + 1 = action when multiplayer && turn

	public Monster(int x, int y, String name,int difficulty){

		position = new Position(x, y);
		this.target = GameManager.getPlayer().get(0);

		//Novice
		if(difficulty==1){
			power = new AtomicInteger(5);
			speed = new AtomicInteger(1);
			LIFE_MAX = 50;
			life = new AtomicInteger(50);
		}
		//Normal
		else if(difficulty==2){
			power = new AtomicInteger(5);
			speed = new AtomicInteger(2);
			LIFE_MAX = 75;
			life = new AtomicInteger(75);
		}
		//Expert
		else if(difficulty==3){
			power = new AtomicInteger(15);
			speed = new AtomicInteger(3);
			LIFE_MAX = 100;
			life = new AtomicInteger(100);
		}
		else{
			power = new AtomicInteger(25);
			speed = new AtomicInteger(3);
			LIFE_MAX = 200;
			life = new AtomicInteger(200);
		}

		moveCounter = new AtomicInteger(0);
		pauseCounter = new AtomicInteger(0);

		sprite = new AnimatedSprite(name, ANIMATIONSPEED);
	}

	public void setId(int id){
		message[0] = id;
	}

	/**
	 * Being a thread, the Monster must read the same code again and again, writen in this method.
	 */
	public void run(){

		long startTime = 0;
		final Monster monster = this;

		final Timer timer = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				if ((TurnManager.turn && GameManager.multiplayer)
						|| !GameManager.multiplayer) {
					target = GameManager.getPlayer().get(0);
				}else{
					target=GameManager.getPlayer().get(1);
				}

				if(display && !isDead()){

					action();

					sprite.next();
					GameManager.updateGraphics(sprite.getCurrentSprite(), position, life.get() / LIFE_MAX); // if the player is in its room.
					if (GameManager.multiplayer) {
						message[1] = StateActor.convertToInt(state)+1000*position.getX()+1000*1000*position.getY();
						try {
							GameManager.buffer.produce(message);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				if(display && isDead() && loot==null){ //If he is dead, he drops an item
					String itemName = randomItemName();
					loot = new Item(itemName,position,monster);
				}
				if(display && isDead() && loot!=null){// while the item hasn't been picked up, update its graphics.
					loot.updateGraphics();
				}
				if(!display && isDead()){//if the items is picked up, null it and interrupt the thread.
					loot = null;
					timer.cancel();
					timer.purge();
					interrupt();
				}

			}

		};
		timer.scheduleAtFixedRate(task,startTime,delay);
	}

	/**
	 * true if the player is in the monster's room, false otherwise. -> used by MonsterRoom
	 * @param display
	 */
	public void setDisplay(boolean display){
		this.display = display;
	}

	/**
	 * The actions performed by the monster.<p>
	 * Move and attack.
	 */
	private void action(){

		if(isMoving()){ //If the monster can move (UP DOWN LEFT or RIGHT)
			if(moveCounter.get()>=4000){moveCounter.set(0);}
			if(moveCounter.get()<2000){ 
				if(ANIMATIONSPEED!=4){speed.getAndDecrement(); ANIMATIONSPEED = 4; sprite.setSpeed(ANIMATIONSPEED);} //He goes slower
				random(); //He evades the target by walking randomly
			}
			if(moveCounter.get()>=2000 && moveCounter.get()<4000){ 
				if(ANIMATIONSPEED!=3){speed.getAndIncrement(); ANIMATIONSPEED = 3; sprite.setSpeed(ANIMATIONSPEED);} //He goes faster !
				chase(); //He chases the target
			}
		}

		if(canAttack()){ //If he can attack (the hero is within range)
			switch (state){
			case UP : setState(StateActor.ATTACKINGUP); break;
			case DOWN : setState(StateActor.ATTACKINGDOWN); break;
			case LEFT : setState(StateActor.ATTACKINGLEFT); break;
			case RIGHT : setState(StateActor.ATTACKINGRIGHT); break;
			default: setState(StateActor.ATTACKINGRIGHT); break;
			}
		}
		else if(isAttacking()){ //if he is attacking (can't prepare to attack and attack in the same turn)

			target.getAttacked(power,state); //Deals damage
			setState(StateActor.NONE); //Wait a bit.

		}
		else if(state == StateActor.NONE){

			pauseCounter.getAndIncrement(); //Adjust !!!
			if(pauseCounter.get() >= ANIMATIONSPEED*7+1) {setState(StateActor.RIGHT); pauseCounter.set(0);} 

		}
		moveCounter.getAndAdd(STEP);
	}

	/**
	 * Tells if the monster is moving or not.
	 * @return true if he is moving, false otherwise.
	 */
	private boolean isMoving(){

		if(state == StateActor.UP || state == StateActor.DOWN || state == StateActor.LEFT || state == StateActor.RIGHT){
			return true;
		}
		else return false;
	}

	/**
	 * Creates the random movement pattern.<p>
	 * A random integer between 0 and 4 is drawn, and the direction is chosen accordingly.
	 */
	private void random(){

		int x = position.getX();
		int y = position.getY();

		if(randomTime<=STEP){
			random = Math.floor(4*Math.random()); //draw the random integer
		}

		if(randomTime<=STEP){randomTime = Math.floor(1000*Math.random()) + STEP+1;} /* Follow the same direction for a random amount of time 
		 																			   to prevent the monster from changing direction at
		 																			   every single call of the random() method.*/

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

	/**
	 * Creates the chasing movement pattern, and allows the monster to attack.
	 */
	private void chase(){
		int x = position.getX();
		int y = position.getY();

		int dx = x - target.getPosition().getX(); //Computes the horizontal distance
		int dy = y - target.getPosition().getY(); //Computes the vertical distance

		//Do the Math : try all the different cases (8) and summarize them into this. Draw a little scheme to understand.
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

	/**
	 * Tells if the Monster is attacking or not.
	 * @return true if the Monster is attacking, false otherwise.
	 */
	private boolean isAttacking(){

		if(state == StateActor.ATTACKINGUP || state == StateActor.ATTACKINGDOWN || state == StateActor.ATTACKINGLEFT || state == StateActor.ATTACKINGRIGHT){
			return true;
		}
		else return false;
	}

	/**
	 * Tells if the monster can attack the target or not. You can change the range of the attack in this method.
	 * @return true if he can attack the hero, false otherwise
	 */
	private boolean canAttack(){

		int dx = position.getX() - target.getPosition().getX(); //Computes the horizontal distance
		int dy = position.getY() - target.getPosition().getY(); //Computes the vertical distance

		switch (state) //Change the range of the attack here !
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

	/**
	 * Deals the damage FROM the Hero.
	 * 
	 * @param power the power of the attack
	 */
	public void getAttacked(AtomicInteger power){
		life.getAndAdd(-power.get());
	}

	/**
	 * Tells if the monster is dead.
	 * @return true if he is dead, false otherwise.
	 */
	public boolean isDead(){
		if(life.get()<=0){
			return true;
		}

		return false;
	}

	/**
	 * Changes the state of the Monster.
	 * 
	 * @param state the state.
	 */
	public void setState(StateActor state){
		if(previousState != state){sprite.changeAnimation(state);} //to prevent the animation from resetting everytime !
		previousState = this.state;
		this.state = state;
	}
	
	public Position getPosition(){
		return position;
	}

	/**
	 * Creates a randomItemName to be able to drop an item.
	 * @return the name of the dropped item.
	 */
	private String randomItemName(){
		int random = (int) Math.floor(Math.random()*3); 

		switch (random){
		case 0 : return "heart";
		case 1 : return "sword";
		case 2 : {random = (int) Math.floor(Math.random()*6); //To lower the odds of having boots, 
		if(random==0){return "boots";} 	//and increasing the odds to get a heart.
		else return "heart";}
		case 3 : return "heart";
		default : return null;
		}
	}

	/**
	 * In multiplayer, the monsters do the actions differently, because they must be synchronized with the monsters of the server.
	 */
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

	/**
	 * Hides the monster, and therefore prevents him from doing anything (dies).
	 */
	public void hideMonster(){
		display = false;
	}
	
	/**
	 * update its graphics.
	 * @param g
	 */
	public void updateGraphic(Graphics g) {
		g.drawImage(sprite.getCurrentSprite(), position.getX(), position.getY(), 40 * GameManager.SCALE, 40 * GameManager.SCALE, null);
	}

}
