package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import gameplay.GameManager;
import networking.TurnManager;

/**
 * The Hero character of the game, represented by Link from "The Legend of Zelda".<p>
 * The main actor, which the player controls, it is also a Thread to have a life of its own.<p>
 * When he dies, the whole game stops and restarts from the game menu.<p>
 * <p>
 * He has a maximum of {@value LIFE_MAX}, a starting attack of 10 and a starting speed of 3 (here, the higher the faster).
 */
public class Hero extends Thread{

	private Position position; //The position of the Hero
	private StateActor state = StateActor.NONE, previousState = StateActor.NONE; //The state and previous state of the Hero
	private AtomicInteger speed;
	private final double LIFE_MAX = 100; // double to have a "real" division (not euclidian)
	private static AtomicInteger life;
	private AtomicInteger power;

	private int ANIMATIONSPEED = 2; //The higher the slower.
	private AnimatedSprite sprite;
	private BufferedImage currentSprite;

	private Monster[] monsters; //The monsters in the same room.

	private int pauseCounter; //To prevent the Hero from attacking too fast.
	private final long delay = 30; // in ms
	private int[] message = new int[2];


	public Hero(int x, int y, String name){

		position = new Position(x, y);
		sprite = new AnimatedSprite(name, ANIMATIONSPEED);
		currentSprite = sprite.next();
		life = new AtomicInteger(100);
		power = new AtomicInteger(10);
		speed = new AtomicInteger(3);
		message[0] = -1;
	}

	/**
	 * Being a thread, the Hero must read the same code again and again, writen in this method.
	 */
	public void run(){

		long startTime = 0;

		//Creates a timer to periodically do the same actions for the Hero.
		final Timer timer = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {

				if(!isDead()){ //While he is not dead.

					if((TurnManager.turn && GameManager.multiplayer) || !GameManager.multiplayer){
						action(); //Move + Attack + grab items
					}

					if(isMoving() || isAttacking()){
						currentSprite = sprite.next(); //Changes the current sprite of the Hero.
					}

					if(GameManager.multiplayer && GameManager.gameIsRunning){ //for the multiplayer sends the messages.
						message[1] = StateActor.convertToInt(state)+1000*position.getX()+1000*1000*position.getY();
						try {
							GameManager.buffer.produce(message);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

					//System.out.println("\t\t\t\t Hero updates graphics");
					GameManager.updateGraphics(currentSprite, position, life.get()/LIFE_MAX);
				}
				else if(isDead()){
					timer.cancel();
					timer.purge();
					
				}
			}

		};
		timer.scheduleAtFixedRate(task,startTime,delay);

	}

	/**
	 * Defines the thres actions the hero will at all time.<p>
	 * First : Move<p>
	 * Second : Attack<p>
	 * Third : Grab the items at your feet.
	 */
	public void action(){

		move(); 
		attack();
		grabItem();

	}

	/**
	 * Tells if the Hero is moving or not.
	 * @return true if the Hero is moving, false otherwise.
	 */
	private boolean isMoving(){

		if(state == StateActor.UP || state == StateActor.DOWN || state == StateActor.LEFT || state == StateActor.RIGHT){
			return true;
		}
		else return false;
	}

	/**
	 * Changes the position of the Hero, according to the direction he is facing and to his speed.<p>
	 * The Hero will also stops if he encounters a wall.
	 */
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

	/**
	 * Tells if the Hero is attacking or not.
	 * @return true if the Hero is attacking, false otherwise.
	 */
	private boolean isAttacking(){

		if(state == StateActor.ATTACKINGUP || state == StateActor.ATTACKINGDOWN || state == StateActor.ATTACKINGLEFT || state == StateActor.ATTACKINGRIGHT){
			return true;
		}
		else return false;
	}

	/**
	 * Returns the list of all the monsters a Hero can attack from its position.<p>
	 * You can change the range of attack in this method.
	 * 
	 * @return the list of the monsters he can attack, null if he cannot attack any.
	 */
	private ArrayList<Monster> canAttack(){

		ArrayList<Monster> monsterList = new ArrayList<Monster>();

		if(monsters!=null){

			for(Monster m : monsters){

				int dx = position.getX() - m.getPosition().getX(); //computes the horizontal distance.
				int dy = position.getY() - m.getPosition().getY(); //computes the vertical distance.

				switch (state) //Make a drawing, you will see it ! You can change the range here.
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

	/**
	 * Creates the attack, and deals the damage.
	 */
	private void attack(){

		if(isAttacking()){
			if(GameManager.multiplayer){ //Message for multiplayer mode.
				message[1] = StateActor.convertToInt(state) +1000*position.getX()+1000*1000*position.getY();
				try {
					GameManager.buffer.produce(message);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			ArrayList<Monster> monstersList = canAttack(); //Creates the list of monsters he can attack.

			if(monstersList!=null){
				for(Monster m : canAttack()) {m.setState(StateActor.NONE); m.getAttacked(power);} //Stops the monster and attacks it. (to make it easier to win the game)
			}

			while(pauseCounter<ANIMATIONSPEED*7+1){ //Can't launch another attack right away ! But displays the animation completely.
				currentSprite = sprite.next();
				GameManager.updateGraphics(currentSprite, position,life.get()/LIFE_MAX);
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
	 * Grabs an item from the floor, dropped by a monster.
	 */
	private void grabItem(){

		if(monsters!=null){

			String action = "";
			Item[] items = new Item[monsters.length];

			for(int i=0; i<monsters.length; i++){
				items[i] = monsters[i].getItem(); //List the items dropped from the monsters in the room.

				if(items[i]!=null){
					int dx = position.getX() - items[i].getPosition().getX(); //Horizontal distance
					int dy = position.getY()+10 - items[i].getPosition().getY(); //Vertical distance

					if(Math.abs(dx)<15 && Math.abs(dy)<15){
						action = items[i].lootItem(); //If the hero is close enough, take the item. (hide the sprite of the monster)
					}

					switch (action){
					case "heart" : //if it is a heart, add 20HP
						if(life.get()>=LIFE_MAX-10){life.set((int) LIFE_MAX);} 
						else {life.getAndAdd(10);}
						break;
					case "sword" : //if it is a sword, add 5 attack power.
						power.getAndAdd(5);
						break;
					case "boots" : //if it is boots, add 1 speed, caps at 7.
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

	/**
	 * Deals the damage FROM the monsters, and creates the knockback of the hero.
	 * 
	 * @param power the power of the attack
	 * @param state the current state of the monster, to push the hero in that direction
	 */
	public synchronized void getAttacked(AtomicInteger power, StateActor state){

		life.getAndAdd(-power.get()); //Deals the damage.

		switch(state){ //Knockback effect.
		case ATTACKINGUP :
			if(getPosition().getY()-15>24*2*GameManager.SCALE){
				position.setXY(position.getX(), position.getY()-15);
			}
			break;
		case ATTACKINGDOWN :
			if(getPosition().getY()+15<122*2*GameManager.SCALE){
				position.setXY(position.getX(), position.getY()+15);
			}
			break;
		case ATTACKINGLEFT :
			if(getPosition().getX()-15>22*2*GameManager.SCALE){
				position.setXY(position.getX()-15, position.getY());
			}
			break;
		case ATTACKINGRIGHT :
			if(getPosition().getX()+15<122*2*GameManager.SCALE){
				position.setXY(position.getX()+15, position.getY());
			}
			break;
		default :
			break;
		}
	}

	/**
	 * Tells if the hero is dead.
	 * @return true if he is dead, false otherwise.
	 */
	public boolean isDead(){
		if(life.get()<=0){ 
			return true;
		}
		else return false;
	}

	/**
	 * Updates the Hero's graphics.
	 */
	public void updateGraphic(Graphics g){
		g.drawImage(currentSprite, position.getX(), position.getY(), 40*GameManager.SCALE, 40*GameManager.SCALE,null);
	}

	/**
	 * Changes the state of the Hero.
	 * 
	 * @param state the state.
	 */
	public void setState(StateActor state){
		if(previousState != state){sprite.changeAnimation(state);} //to prevent the animation from resetting everytime !
		previousState = this.state;
		this.state = state;
	}

	/**
	 * @return the current state of the Hero.
	 */
	public StateActor getHeroState(){
		return state;
	}

	/**
	 * @return the object Position which represents the position of the Hero in the pixel grid.
	 */
	public Position getPosition(){
		return position;
	}

	/**
	 * Sets the monsters alive in the room.
	 * @param monsters the monsters in the room.
	 */
	public void setMonsters(Monster[] monsters){
		this.monsters = monsters;
	}

}
