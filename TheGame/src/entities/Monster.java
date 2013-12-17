package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import display.Position;
import main.Game;
import gameplay.GameManager;
import graphics.AnimatedSprite;


public class Monster extends Thread {

	private Position position;
	private final int deltaX = 40, deltaY = 40;
	private AnimatedSprite sprite;
	private BufferedImage currentSprite;

	Hero target; //necessaire pour declencher l'attaque-> cf Pacman
	String name;
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
				move();
			}


		};
		timer.scheduleAtFixedRate(task,startTime,delay);
	}

	public void action(){

		move();
		attack();

	}

	private void move(){
		/*TODO
		 * Serait pas mal d'avoir mouvement aleatoire
		 */
		double random = Math.floor(4*Math.random());
		int x = position.getX();
		int y = position.getY();

		if(random==0){
			for(int i=0;i<20;i++){
				currentSprite = sprite.next();
				if((y-1>31*GameManager.SCALE*2)){
					position.setXY(x, y-speed);
				}
			}
		}

		if(random==1){
			for(int i=0;i<20;i++){
				currentSprite = sprite.next();
				if(y+1<122*2*GameManager.SCALE )
					position.setXY(x, y+speed);
			}
		}

		if(random==2){
			for(int i=0;i<20;i++){
				currentSprite = sprite.next();
				if( x-1>32*2*GameManager.SCALE )
					position.setXY(x-speed, y);
			}
		}

		if(random==3){
			for(int i=0;i<20;i++){
				currentSprite = sprite.next();
				if(x+1<128*2*GameManager.SCALE )
					position.setXY(x+speed, y);
			}
		}
	}




	public void updateGraphics(Graphics g){
		/*
		 * Changer les entiers avant Game.SCALE
		 */
		g.drawImage(currentSprite, position.getX(), position.getY(), deltaX*GameManager.SCALE, deltaY*GameManager.SCALE,null);
	}
	

	public void attack(){
		//TODO
	}
	

	public boolean isDead(){
		if(life.get()==0){
			 // TODO Faire apparaitre un item 
			return true;
		}
		return false;
	}
	

//	private void patrol() {
//		if(rightStep<200){
//			right();
//			rightStep++;
//		}
//		if(rightStep==200 && downStep<200){
//			down();
//			downStep++;
//		}
//
//		if(downStep==200 && leftStep<200){
//			left();
//			leftStep++;
//		}
//		if(leftStep==200 && upStep<200){
//			up();
//			upStep++;
//		}
//		if(upStep==200){
//			rightStep=0;
//			leftStep=0;
//			downStep=0;
//			upStep=0;
//		}
//	}
	
}