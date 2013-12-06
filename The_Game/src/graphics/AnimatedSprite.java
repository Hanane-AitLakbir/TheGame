package graphics;

import java.awt.image.BufferedImage;

public class AnimatedSprite {

	private BufferedImage[] animation;
	private int current = -1;
	private int nbSprites;

	public AnimatedSprite(String path, int nbSprites, int width, int height){

		BufferedImage sheet = new ImageLoader().load(path);
		this.nbSprites = nbSprites;
		animation = new BufferedImage[nbSprites];
		for(int i = 0; i<nbSprites;i++){
			animation[i] = sheet.getSubimage(i*width,0,width,height);
		}
	}

	public BufferedImage next(){
		if(current<nbSprites-1){
			current++;
			return animation[current];
		}
		current = 0;
		return  animation[0];
	}

	public void reset(){
		current = -1;
	}
}
