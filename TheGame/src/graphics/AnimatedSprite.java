package graphics;

import java.awt.image.BufferedImage;

import entities.StateActor;

public class AnimatedSprite {

	private BufferedImage[] up,down,left,right,attackingUp,attackingDown,attackingLeft,attackingRight;
	private BufferedImage[] currentSprite;
	private final int WIDTH = 40, HEIGHT = 35;
	private int current = 0;
	private int counter = 0;
	private int speed = 1;
	private int nbSprites;

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

		currentSprite = right;
	}

	public BufferedImage next(){
		if(counter==0){
			counter=speed;
			if(current<nbSprites-1){
				current++;
				return currentSprite[current];
			}
			current = 0;
			return currentSprite[0];
		}
		counter--;
		return currentSprite[current];
	}

	public void changeAnimation(StateActor state){

		this.reset(); //METTRE LE RESET DANS LE CONTROLLER ?
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

	public void reset(){
		current = 0;
	}
	
	public BufferedImage getCurrentSprite(){
		return currentSprite[current];
	}

	public void setSpeed(int speed){
		this.reset();
		this.speed = speed;
	}


}