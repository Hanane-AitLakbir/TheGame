package display;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * This class creates the game window, where the player will play and may find the treasure.
 */
@SuppressWarnings("serial")
public class Window extends JFrame{

	/**
	 * Creates the game window itself, with the dimensions set in CanvasGame HEIGHT = {@value display.CanvasGame#HEIGHT},
	 *  WIDTH = {@value display.CanvasGame#WIDTH} and SCALE = {@value display.CanvasGame#SCALE}.
	 */
	public Window(){
		super("The Dungeon of Zelda");
		setSize(CanvasGame.WIDTH * CanvasGame.SCALE, CanvasGame.HEIGHT*CanvasGame.SCALE); 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Allows to close the window.
		setResizable(false); // To prevent user from resizing the window.
		setVisible(true);
		setLocationRelativeTo(null); // Window in the center of the screen

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnNewMenu = new JMenu("Help");
		menuBar.add(mnNewMenu);

		//Adds the Rules menu to the game Window.
		final JMenuItem item1 = new JMenuItem("Rules");
		item1.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(
						item1, "You must find the treasure hidden inside this labyrinth. \nYou must kills the monsters in each room you cross" +
								"in order to be strong.\nBecause guarding the treasure is a mighty and powerful Monster that awaits your arrival.\n" +
								"You may find three items dropped from regular monsters :\n" +
								"- A heart which regens 20 HP\n" +
								"- A sword which increases your attack\n" +
								"- Boots which increases your movement speed\n\n " +
								"Good Luck Link.","Rules",
						JOptionPane.PLAIN_MESSAGE);
			}
		});
		mnNewMenu.add(item1);

		//Adds the Commands menu to the game Window
		JMenuItem item2 = new JMenuItem("Commands");
		item2.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(
						item1, " Movement <-> Directionnal Arrows (Up Down Left Right) \n Attack <-> Space","Commands",
						JOptionPane.PLAIN_MESSAGE);

			}
		});
		mnNewMenu.add(item2);


	}

}
