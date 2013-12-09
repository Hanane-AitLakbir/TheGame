package main;

import entities.Hero;
import entities.Monster;
import gamePlay.MonsterRoom;
import gamePlay.Room;
import graphics.Background;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

import javax.sound.midi.SoundbankResource;
import javax.swing.JFrame;

import sound.SoundPlayer;


/**
 * Game est la classe qui gere l'ensemble du jeu. Elle cree un fenetre et un canvas qui sera mis � 
 * jour par les differents acteurs du jeu (le(s) joueur(s), les monstres , et le d�cor).
 * Elle est construite comme un Thread (pourra �tre modifi�)
 * 
 * @author hanane
 *
 */
public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 340, HEIGHT = 340, SCALE = 2;
	public static boolean running = false; // the game is running ?
	public static boolean endIntro = false;
	public Thread gameThread; // 
	public static Hero player;
	private Monster monster;
	private Background bg;
	private SoundPlayer sound;
	private Room currentRoom; 
	private int numRoom = 0;

	/**
	 * Initialise tous les acteurs (joueurs, monstres, et d�cor) du jeu
	 */
	public void init(){
		player = new Hero(50*4, 50*4,"Link");
		monster = new Monster(80*4,80*4,"Monster1",1);
		sound = new SoundPlayer("Adventure_Title");
		monster.start();
		sound.playSound();
		bg = new Background(0);
		this.addKeyListener(new Controller());
		bg.updateGraphic(getGraphics());

	}

	/**
	 * Lance le thread de Game
	 */
	public synchronized void start(){
		if(running) return; // if the game is actually running
		running = true;
		gameThread = new Thread(this);
		gameThread.start();
	}

	/**
	 * Arr�te proprement le thread de Game
	 */
	public synchronized void stop(){
		if(!running) return; // the game is already stopped
		running = false;
		try {
			sound.stopSound();
			gameThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void run(){
		//		long lastTime = System.nanoTime();
		//		final double amountOfTicks = 60D;
		//		double ns = 1E9 / amountOfTicks;
		//		double delta = 0; 

		init();

		//attente de la fin intro
		int counter = 0;
		
		while(counter<1000){
			counter++;
		}
		
//		while(!endIntro){
//			//mettre une animation ??
//		}		
		endIntro = false;

		sound.stopSound();

		while(running){

			if(numRoom==0){
				currentRoom = new MonsterRoom(player, 1);
				bg = currentRoom.getBackground();
				sound = currentRoom.getSound();
				sound.playSound();
				player.setXY(18*2*Game.SCALE, 67*2*Game.SCALE);
				numRoom +=1;
				}

			if(numRoom==1 && currentRoom.playerIsOut()){
				sound.stopSound();
				currentRoom = new MonsterRoom(player, 2);
				System.out.println(currentRoom.playerIsOut());
				bg = currentRoom.getBackground();
				sound = currentRoom.getSound();
				sound.playSound();
				player.setXY(18*2*Game.SCALE, 67*2*Game.SCALE); 
				// placer le player selon la porte par laquelle il est sorti � la pr�c�dente salle
				numRoom +=1;
			}
			
			if(numRoom==2 && currentRoom.playerIsOut()){
				sound.stopSound();
				currentRoom = new MonsterRoom(player, 3);
				System.out.println(currentRoom.playerIsOut());
				bg = currentRoom.getBackground();
				sound = currentRoom.getSound();
				sound.playSound();
				player.setXY(18*2*Game.SCALE, 67*2*Game.SCALE); 
				// placer le player selon la porte par laquelle il est sorti � la pr�c�dente salle
				numRoom +=1;
			}

			while(!currentRoom.playerIsOut()){
				//			// (code trouv� sur Internet ^^) -> permet d'avoir plus de fluidit� dans les animations mais parfois on dirait que Link 
				//			// fait une petite glissade (excellent ... si c'�tait voulu ^^)
				//			long now = System.nanoTime();
				//			delta += (now - lastTime / ns);
				//			lastTime = now;
				//			if(delta >= 1){
				//				move();
				//				delta--;
				//			}

				updateGraphic();
				move(); 
				System.out.println(numRoom);
				System.out.println("\t\t" + currentRoom.playerIsOut());

				// un timer buggue � donf !! comprends pas pourquoi
				try {
					Thread.sleep(14);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}
		stop();
	}

	/**
	 * G�re les actions des joueurs et des monstres
	 */
	private void move() {
		// updates all game's objects
		player.move();

	}

	/**
	 * G�re la mise � jour du Graphics contenu dans le canvas Game. Cette m�thode doit lancer la m�thode updateGraphics de tous
	 * les acteurs du jeu . La classe Game utilise un objet BufferStrategy qui permet d'�viter tous les lags lors des animations
	 * (et en plus g�re mieux le plein �cran ^^)
	 */
	private void updateGraphic() {
		// draws all game's objects

		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null){
			createBufferStrategy(3); // 
			return;
		}
		Graphics g = bs.getDrawGraphics();

		g.fillRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE); // fond noir
		//ajouter tous les mises � jour graphiques (faire gaffe � l'ordre)
		bg.updateGraphic(g);
		monster.updateGraphics(g);
		player.updateGraphic(g);
		g.dispose();
		bs.show();

	}

	public static Hero getPlayer(){
		return player;}

	public static void main(String[] args){
		Game game = new Game();
		game.setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		game.setMaximumSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		game.setMinimumSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));

		JFrame frame = new JFrame("Tile RPG");
		frame.setSize(WIDTH * SCALE, HEIGHT*SCALE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // permet de fermer la fen�tre 
		frame.setResizable(false); // pour ne pas redim la fenetre 
		frame.add(game);
		frame.setVisible(true); // sinon on ne voit pas la fen�tre (je l'avais vraiment oubli� au d�but  ^^)
		frame.setLocationRelativeTo(null); // fenetre au centre de l'�cran
		game.start();

	}


}
