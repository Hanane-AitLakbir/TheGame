package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import main.Game;
import gamePlay.Room;
import graphics.AnimatedSprite;


public class Monster extends Thread {
	int x,y;
	AnimatedSprite back,front,right,left;
	private BufferedImage currentSprite;

	Room currentRoom;
	Hero target; //necessaire pour declencher l'attaque-> cf Pacman
	int upStep,downStep,leftStep,rightStep; // pour la methode patrol

	public static AtomicInteger life;
	private int speed = 1; //a modifier selon difficulte
	private final int ANIMATIONSPEED = 2;

	public Monster(int x,int y, String name,int difficulty, Room room){
		this.x=x;
		this.y=y;
		this.target=Game.player;
		currentRoom = room;

		life = new AtomicInteger(difficulty*10);

		front = new AnimatedSprite(("/"+name+"_front.png"), 7, 40,40, ANIMATIONSPEED);
		back = new AnimatedSprite(("/"+name+"_back.png"), 7, 40,40, ANIMATIONSPEED);
		left = new AnimatedSprite(("/"+name+"_left.png"), 7, 40,40, ANIMATIONSPEED);
		right = new AnimatedSprite(("/"+name+"_right.png"), 7, 40,40, ANIMATIONSPEED);
	}

	public void run(){
		/*
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
				// TODO Auto-generated method stub
				//step();
				move();
			}


		};
		timer.scheduleAtFixedRate(task,startTime,delay);
	}
	private void move(){
		/*
		 * Serait pas mal d'avoir mouvement aleatoire
		 */
		double random = Math.floor(4*Math.random());

		if(random==0){
			for(int i=0;i<20;i++) up();
		}

		if(random==1){
			for(int i=0;i<20;i++) down();
		}

		if(random==2){
			for(int i=0;i<20;i++) left();
		}

		if(random==3){
			for(int i=0;i<20;i++) right();
		}
	}

	private void up() {
		front.reset();
		left.reset();
		right.reset();

		currentSprite = back.next();
		if((y-1>31*Game.SCALE*2)){
			y-=speed;
		}
	}

	private void right() {
		front.reset();
		back.reset();
		left.reset();

		currentSprite = right.next();
		if(x+1<128*2*Game.SCALE )
			x+=speed;
	}

	private void down() {
		back.reset();
		right.reset();
		left.reset();

		currentSprite = front.next();
		if(y+1<122*2*Game.SCALE )
			y+=speed;
	}

	private void left() {
		front.reset();
		back.reset();
		right.reset();

		currentSprite = left.next();
		if( x-1>32*2*Game.SCALE )
			x-=speed;
	}

	public void updateGraphics(Graphics g){
		/*
		 * Changer les entiers avant Game.SCALE
		 */
		 g.drawImage(currentSprite, x,y,40*Game.SCALE, 40*Game.SCALE,null);
	}

	public void attack(){
		
		Hero.getDamaged();
	}

	public static synchronized void getDamaged(){
		life.getAndAdd(-10); // a modifier selon xp du Hero
	}

	/*
	 * Tells whether the Hero is Dead or Alive
	 */
	public boolean isDead(){
		if(life.get()==0){
		return true;
		// TODO Faire apparaitre un item
		}
		else return false;
	}

	private void patrol() {
		// TODO Auto-generated method stub
		if(rightStep<200){
			right();
			rightStep++;
		}
		if(rightStep==200 && downStep<200){
			down();
			downStep++;
		}

		if(downStep==200 && leftStep<200){
			left();
			leftStep++;
		}
		if(leftStep==200 && upStep<200){
			up();
			upStep++;
		}
		if(upStep==200){
			rightStep=0;
			leftStep=0;
			downStep=0;
			upStep=0;
		}
	}
	
	private boolean isInHitbox(){
		
		//TODO faire cette methode qui dit si le héros est dans la zone d'attaque du monstre.
		return false;
		
	}
}