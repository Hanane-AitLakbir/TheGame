package utilities;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Allows the other classes to load an image and be able to use it afterwards.
 */
public class ImageLoader {

	public BufferedImage load(String path){
		try {
			return ImageIO.read(getClass().getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
