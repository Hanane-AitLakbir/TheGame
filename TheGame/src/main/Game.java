package main;

import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.io.IOException;

import networking.Client;
import networking.Server;
import gameplay.GameManager;
import display.CanvasGame;

public class Game {
	private static CanvasGame canvas;
	private static GameManager manager;
	//private Thread communicator; 

//	public Game(boolean multiplayerMode) throws IOException{
//		canvas = new CanvasGame();
//		manager = new GameManager(canvas);
//		if(multiplayerMode){
//			if(server){
//				communicator = new Thread(new Server(2879));
//			}
//			else{
//				//communicator = new Thread(new Client(,2879));
//			}
//		}

		//manager.go();
//		communicator.start();
//	}
	
	public static void main(String[] args){
		
		canvas = new CanvasGame();
		manager = new GameManager(canvas);
		manager.start();
		
	}

}