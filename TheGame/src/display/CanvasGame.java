package display;

import gameplay.Controller;
import java.awt.Canvas;
import java.awt.Dimension;

/**
 * Creates the game's Canvas, the general window and where the components will be placed.
 */
public class CanvasGame extends Canvas{

	private static final long serialVersionUID = 1L; // auto generated
	public static final int WIDTH = 340, HEIGHT = 340, SCALE = 2;

	public CanvasGame(){
		this.setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE)); //Defines the size of the window.
		this.setMaximumSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		this.setMinimumSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		this.addKeyListener(new Controller()); //Creates the game Controller.
	}
}
