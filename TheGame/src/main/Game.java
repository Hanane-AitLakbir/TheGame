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

	public static void main(String[] args){

		manager = new GameManager();
		manager.start();

	}

}
