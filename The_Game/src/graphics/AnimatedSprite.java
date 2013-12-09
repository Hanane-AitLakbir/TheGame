package graphics;

import java.awt.image.BufferedImage;

public class AnimatedSprite {

	private BufferedImage[] animation;
	private int current = 0;
	private int counter = 0;
	private int speed = 0;
	private int nbSprites;

	public AnimatedSprite(String path, int nbSprites, int width, int height, int speed){

		BufferedImage sheet = new ImageLoader().load(path);
		this.speed = speed;
		counter = speed;
		this.nbSprites = nbSprites;
		animation = new BufferedImage[nbSprites];
		for(int i = 0; i<nbSprites;i++){
			animation[i] = sheet.getSubimage(i*width,0,width,height);
		}
	}

	public BufferedImage next(){
		if(counter==0){
			counter=speed;
			if(current<nbSprites-1){
				current++;
				return animation[current];
			}
			current = 0;
			return animation[0];
		}
		counter--;
		return animation[current];
	}

	public void reset(){
		current = 0;
	}

	public void setSpeed(int speed){
		this.speed = speed;
	}
}