package display;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;


/**
 * Background manages the layout of the room (how it looks, ...)
 * 
 * @author hanane
 */
public class Background {

	BufferedImage bg;
	int scale = CanvasGame.SCALE, width = CanvasGame.WIDTH, height = CanvasGame.HEIGHT; //Size of the Canvas

	/**
	 * Creates the Background Object
	 * 
	 * @param roomType Integer reprensenting the type of the room (0 -> monster room with a difficulty of 0, 1-> ... , 4)
	 */
	public Background(int roomType){
		try {
			if(roomType<=3){
			bg = ImageIO.read(getClass().getResource("/background/background"+roomType+".png"));
			}
			else{
				int roomT = 3-roomType%3;
				bg = ImageIO.read(getClass().getResource("/background/background"+roomT+".png"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Displays the background on the Graphic g
	 * 
	 * @param Graphics g
	 */
	public void updateGraphic(Graphics g){
		g.drawImage(bg, 0, 0, (width*scale), (height*scale), null);
	}
}
