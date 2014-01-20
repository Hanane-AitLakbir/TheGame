package entities;

import java.awt.image.BufferedImage;

import utilities.ImageLoader;

/**
 * A class that manages the different animations in the game. For example the attacks of the player, how the monsters move, ...<p>
 * 
 * The movement animations must include a 281pixels*40pixels file, with 7 sprites.<p>
 * The attack animations must include a 281pixels*40pixels file, with 6 sprites. (it won't read the others) <p>
 * 
 * Each sprite in the animation must be drawn within 40 pixels. So 0->39 : first sprite, 40->79 : second sprite, ...<p>
 * 
 * You can also chose the speed of the animation, the higher the slower.
 */
public class AnimatedSprite {

	private BufferedImage[] up,down,left,right,attackingUp,attackingDown,attackingLeft,attackingRight;
	private BufferedImage[] currentSprite;
	private final int WIDTH = 40, HEIGHT = 35;
	private int current = 0; //The current sprite in the corresponding animation.
	private int counter = 0; //To count the speed.
	private int speed = 1;
	private int nbSprites;

	/**
	 * Creates the animated sprite from the files in the "res" folder.
	 * @param name the name of the animation, refering to who it represents : "Monster1", "Monster2", "Link" or "Link2"
	 * @param speed the "speed" of the animation, the higher the slower. It represents the number of times a sprite is repeated.
	 */
	public AnimatedSprite(String name, int speed){

		this.speed = speed;
		counter = speed;

		//up
		nbSprites = 7;
		up = new BufferedImage[nbSprites];
		for(int i = 0; i<nbSprites;i++){
			up[i] = new ImageLoader().load("/"+name+"_back.png").getSubimage(i*WIDTH,0,WIDTH,HEIGHT);
		}
		//down
		down = new BufferedImage[nbSprites];
		for(int i = 0; i<nbSprites;i++){
			down[i] = new ImageLoader().load("/"+name+"_front.png").getSubimage(i*WIDTH,0,WIDTH,HEIGHT);
		}
		//left
		left = new BufferedImage[nbSprites];
		for(int i = 0; i<nbSprites;i++){
			left[i] = new ImageLoader().load("/"+name+"_left.png").getSubimage(i*WIDTH,0,WIDTH,HEIGHT);
		}
		//right
		right = new BufferedImage[nbSprites];
		for(int i = 0; i<nbSprites;i++){
			right[i] = new ImageLoader().load("/"+name+"_right.png").getSubimage(i*WIDTH,0,WIDTH,HEIGHT);
		}
		//attackingUp
		nbSprites = 6;
		attackingUp = new BufferedImage[nbSprites];
		for(int i = 0; i<nbSprites;i++){
			attackingUp[i] = new ImageLoader().load("/"+name+"_back_attack.png").getSubimage(i*WIDTH,0,WIDTH,HEIGHT);
		}
		//attackingDown
		attackingDown = new BufferedImage[nbSprites];
		for(int i = 0; i<nbSprites;i++){
			attackingDown[i] = new ImageLoader().load("/"+name+"_front_attack.png").getSubimage(i*WIDTH,0,WIDTH,HEIGHT);
		}
		//attackingLeft
		attackingLeft = new BufferedImage[nbSprites];
		for(int i = 0; i<nbSprites;i++){
			attackingLeft[i] = new ImageLoader().load("/"+name+"_left_attack.png").getSubimage(i*WIDTH,0,WIDTH,HEIGHT);
		}
		//attackingRight
		attackingRight = new BufferedImage[nbSprites];
		for(int i = 0; i<nbSprites;i++){
			attackingRight[i] = new ImageLoader().load("/"+name+"_right_attack.png").getSubimage(i*WIDTH,0,WIDTH,HEIGHT);
		}

		currentSprite = right; //Starting state.
	}

	/**
	 * Returns the next animation of the current BufferedImage[].
	 * @return the next sprite.
	 */
	public BufferedImage next(){
		if(counter==0){
			counter=speed; //resets the counter
			if(current<nbSprites-1){ //If it is not the end of the animation, it keeps changing.
				current++;
				return currentSprite[current];
			}
			current = 0; //Or it goes back to the beginning.
			return currentSprite[0];
		}
		counter--; //Counts down the speed.
		return currentSprite[current];
	}

	/**
	 * Change the animation and resets the counters.
	 * @param state the state of the actor, to chose the correct animation.
	 */
	public void changeAnimation(StateActor state){

		this.reset();
		switch (state){

		case UP : 
			nbSprites=7;
			currentSprite = up;
			break;
		case DOWN : 
			nbSprites=7;
			currentSprite = down;
			break;
		case LEFT : 
			nbSprites=7;
			currentSprite = left;
			break;
		case RIGHT : 
			nbSprites=7;
			currentSprite = right;
			break;
		case ATTACKINGUP :
			nbSprites=6;
			currentSprite = attackingUp;
			break;
		case ATTACKINGDOWN :
			nbSprites=6;
			currentSprite = attackingDown;
			break;
		case ATTACKINGLEFT :
			nbSprites=6;
			currentSprite = attackingLeft;
			break;
		case ATTACKINGRIGHT :
			nbSprites=6;
			currentSprite = attackingRight;
			break;
		default:
			break;
		}
	}

	/**
	 * Resets the current sprite to the beginning of the animation.
	 */
	public void reset(){
		current = 0;
	}

	/**
	 * @return the current Sprite of the animation.
	 */
	public BufferedImage getCurrentSprite(){
		return currentSprite[current];
	}

	/**
	 * Sets the speed of the animation.
	 * @param speed the chose "speed" (the higher, the slower).
	 */
	public void setSpeed(int speed){
		this.reset();
		this.speed = speed;
	}


}