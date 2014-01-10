package display;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class Window extends JFrame{

	public Window(){
		super("The Game");
		setSize(CanvasGame.WIDTH * CanvasGame.SCALE, CanvasGame.HEIGHT*CanvasGame.SCALE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // permet de fermer la fenetre 
		setResizable(false); // pour ne pas redim la fenetre 
		setVisible(true);
		setLocationRelativeTo(null); // fenetre au centre de l'ecran

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnNewMenu = new JMenu("Help");
		menuBar.add(mnNewMenu);

		final JMenuItem item1 = new JMenuItem("Rules");
		item1.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(
						item1, "game rules, game rules, rules, blablablab\nblablabla\nblablbablaldsgd","Rules",
						JOptionPane.PLAIN_MESSAGE);
			}
		});
		mnNewMenu.add(item1);

		JMenuItem item2 = new JMenuItem("Commands");
		item2.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(
						item1, " Up <-> to go up \n Right <-> to go right \n etc \n Space <-> to attack","Commands",
						JOptionPane.PLAIN_MESSAGE);

			}
		});
		mnNewMenu.add(item2);


	}

}
