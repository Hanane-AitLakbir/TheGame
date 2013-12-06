package graphics;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.Game;

/**
 * La classe Background g�re le d�cor de la salle. (ajout des tr�sors ??)
 * 
 * @author hanane
 *
 */
public class Background {

	BufferedImage bg;
	
	/**
	 * Constructeur de Background
	 * @param roomType l'entier repr�sentant le type de salle (0 -> monster room difficulty 0, 1-> ... , 4 -> riddle room, ...)
	 */
	public Background(int roomType){
		try {
			bg = ImageIO.read(getClass().getResource("/background/background"+roomType+".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * Affiche l'arri�re-plan sur le Graphics g
	 * 
	 * @param g 
	 */
	public void updateGraphic(Graphics g){
		g.drawImage(bg, 0, 0, (Game.WIDTH*Game.SCALE), (Game.HEIGHT*Game.SCALE), null);
	}
}
