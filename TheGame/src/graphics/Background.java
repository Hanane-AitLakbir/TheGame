package graphics;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import display.CanvasGame;


/**
 * La classe Background gere le decor de la salle.
 * 
 * @author hanane
 *
 */
public class Background {

	BufferedImage bg;
	int scale = CanvasGame.SCALE, width = CanvasGame.WIDTH, height = CanvasGame.HEIGHT;

	/**
	 * Constructeur de Background
	 * @param roomType l'entier representant le type de salle (0 -> monster room difficulty 0, 1-> ... , 4
	 */
	public Background(int roomType){
		try {
			bg = ImageIO.read(getClass().getResource("/background/background"+roomType+".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Affiche l'arriere-plan sur le Graphics g
	 * 
	 * @param g 
	 */
	public void updateGraphic(Graphics g){
		g.drawImage(bg, 0, 0, (width*scale), (height*scale), null);
	}
}
