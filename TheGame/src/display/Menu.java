package display;

import gameplay.GameManager;
import sound.SoundPlayer;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;


/**
 * This class displays a menu to choice the game mode (solo or multiplayer) and the game difficulty,
 * and creates the appropriate GameManager object. 
 * There is a background sound which is played. 
 * 
 * @author hanane
 *
 */
public class Menu {

	private JFrame frame;
	private JTextField serverName;
	private JTextField serverPort;
	private SoundPlayer sound;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Menu window = new Menu();
					window.frame.setVisible(true);
					window.frame.setLocationRelativeTo(null);
					window.frame.setResizable(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Menu() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("The Game");
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Menu.class.getResource("/background/icon.jpg")));
		frame.setBounds(100, 100, 600, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Add the layout : a layered layout to display the background (layer 0) and the components (layer 1)
		JLayeredPane layeredPane = new JLayeredPane();
		frame.getContentPane().add(layeredPane, BorderLayout.CENTER);

		sound = new SoundPlayer("Adventure_Title");
		sound.playSound();
		//Display the background 
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setIcon(new ImageIcon(Menu.class.getResource("/background/Menu.jpg")));
		lblNewLabel.setBounds(0, 0, 638, 415);
		layeredPane.add(lblNewLabel);


		//Display "Game Mode" 
		JTextPane txtpnGameMode = new JTextPane();
		txtpnGameMode.setForeground(Color.WHITE);
		txtpnGameMode.setFont(new Font("Dialog", Font.BOLD, 14));
		txtpnGameMode.setOpaque(false);
		txtpnGameMode.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		txtpnGameMode.setBackground(UIManager.getColor("Button.background"));
		txtpnGameMode.setEditable(false);
		txtpnGameMode.setText("Game mode");
		layeredPane.setLayer(txtpnGameMode, 1); // -> layer 1 
		txtpnGameMode.setBounds(12, 135, 100, 22);
		layeredPane.add(txtpnGameMode);


		//choice of game mode : solo mode or multiplayer
		final Choice gameMode = new Choice();
		gameMode.setForeground(Color.BLACK);
		gameMode.addItemListener(new ItemListener() {
			//ItemListener game Mode config 
			public void itemStateChanged(ItemEvent e) {
				switch(gameMode.getSelectedItem()){
				case "Solo" : 
					GameManager.multiplayer = false; 
					System.out.println("mode solo choisi"); // Ligne de test
					break;
				case "Multiplayer":
					GameManager.multiplayer = true;
					System.out.println("mode multijoueur choisi"); // Ligne de test
					break;
				}
			}
		});
		gameMode.add("");
		gameMode.add("Solo");
		gameMode.add("Multiplayer");
		gameMode.setFont(new Font("Dialog", Font.BOLD, 12));
		gameMode.setBackground(Color.WHITE);
		layeredPane.setLayer(gameMode, 1);
		gameMode.setBounds(119, 135, 111, 21);
		layeredPane.add(gameMode);


		//Display "Difficulty"
		JTextPane txtDifficulty = new JTextPane();
		txtDifficulty.setFont(new Font("Dialog", Font.BOLD, 14));
		txtDifficulty.setName("");
		txtDifficulty.setForeground(Color.WHITE);
		txtDifficulty.setEditable(false);
		txtDifficulty.setToolTipText("");
		txtDifficulty.setSelectedTextColor(Color.WHITE);
		txtDifficulty.setBackground(UIManager.getColor("Button.background"));
		layeredPane.setLayer(txtDifficulty, 1);
		txtDifficulty.setText("Difficulty");
		txtDifficulty.setBounds(12, 197, 83, 22);
		txtDifficulty.setOpaque(false);
		layeredPane.add(txtDifficulty);

		// Choice of the game difficulty : Novice, Normal or Expert
		final Choice difficulty = new Choice();
		difficulty.setForeground(Color.BLACK);
		difficulty.setFont(new Font("Dialog", Font.BOLD, 12));
		difficulty.setBackground(Color.WHITE);
		difficulty.addItemListener(new ItemListener() {

			//ItemListener difficulty config

			@Override
			public void itemStateChanged(ItemEvent arg0) {
				switch(difficulty.getSelectedItem()){
				case "Novice" :
					GameManager.difficulty = 1;
//					System.out.println("Difficulty : Novice"); // Ligne de test
					break;
				case "Normal" : 
					GameManager.difficulty = 2;
//					System.out.println("Difficulty : Normal"); // Ligne de test
					break;
				case "Expert" : 
					GameManager.difficulty = 3;
//					System.out.println("Difficulty : Expert"); // Ligne de test
					break;
				}
				System.out.println("difficulty " + GameManager.difficulty);
			}
		});
		difficulty.add("");
		difficulty.add("Novice");
		difficulty.add("Normal");
		difficulty.add("Expert");
		layeredPane.setLayer(difficulty, 1);
		difficulty.setBounds(119, 197, 111, 21);
		layeredPane.add(difficulty);


		// Display "Enter the server name"
		JTextPane txtpnEnterTheServer = new JTextPane();
		txtpnEnterTheServer.setForeground(Color.WHITE);
		txtpnEnterTheServer.setFont(new Font("Dialog", Font.BOLD, 14));
		txtpnEnterTheServer.setOpaque(false);
		txtpnEnterTheServer.setBackground(UIManager.getColor("Button.background"));
		txtpnEnterTheServer.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		txtpnEnterTheServer.setEditable(false);
		txtpnEnterTheServer.setText("Enter the server name :");
		layeredPane.setLayer(txtpnEnterTheServer, 1);
		txtpnEnterTheServer.setBounds(440, 63, 132, 44);
		layeredPane.add(txtpnEnterTheServer);

		// Edition zone for the server name
		serverName = new JTextField();
		serverName.setDropMode(DropMode.INSERT);
		layeredPane.setLayer(serverName, 1);
		serverName.setBounds(440, 119, 132, 20);
		layeredPane.add(serverName);
		serverName.setColumns(10);

		//Display "Enter the port number"
		JTextPane txtpnPleaseEnterThe = new JTextPane();
		txtpnPleaseEnterThe.setOpaque(false);
		txtpnPleaseEnterThe.setForeground(Color.WHITE);
		txtpnPleaseEnterThe.setFont(new Font("Dialog", Font.BOLD, 14));
		txtpnPleaseEnterThe.setText("Enter the server port : ");
		txtpnPleaseEnterThe.setEditable(false);
		txtpnPleaseEnterThe.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		txtpnPleaseEnterThe.setBackground(UIManager.getColor("Button.background"));
		layeredPane.setLayer(txtpnPleaseEnterThe, 1);
		txtpnPleaseEnterThe.setBounds(440, 160, 132, 44);
		layeredPane.add(txtpnPleaseEnterThe);

		// Edition zone for the port number
		serverPort = new JTextField();
		layeredPane.setLayer(serverPort, 1);
		serverPort.setBounds(440, 216, 132, 20);
		layeredPane.add(serverPort);
		serverPort.setColumns(10);

		//Button for starting the game
		Button startGame = new Button("Start game");
		startGame.addActionListener( new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				frame.setVisible(false); // close the menu window
				sound.stopSound();
				new GameManager().start();
				
			}
		});
		layeredPane.setLayer(startGame, 1);
		startGame.setBounds(73, 290, 76, 23);
		layeredPane.add(startGame);

		Button joinGame = new Button("Join a game");
		joinGame.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new GameManager(serverName.getText(), Integer.parseInt(serverPort.getText())).start();
				frame.setVisible(false); // close the menu window
				sound.stopSound();
			}
		});
		layeredPane.setLayer(joinGame, 1);
		joinGame.setBounds(459, 290, 76, 23);
		layeredPane.add(joinGame);
	}
}
