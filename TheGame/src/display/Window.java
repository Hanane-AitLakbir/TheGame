package display;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Window extends JFrame{

	public Window(){
		super("The Game");
		setSize(CanvasGame.WIDTH * CanvasGame.SCALE, CanvasGame.HEIGHT*CanvasGame.SCALE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // permet de fermer la fenetre 
		setResizable(false); // pour ne pas redim la fenetre 
		setVisible(true);
		setLocationRelativeTo(null); // fenetre au centre de l'ecran
	}
}


//ajout des menus, boutons, ...
class Panel extends JPanel{
	
}