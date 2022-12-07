package rockpaperscissors;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.FlowLayout;

/*
 * A class that sets up the client GUI for rock paper scissors. It contains a panel
 * with three buttons, which represent the player's choices, and a notification panel 
 * that is populated with an information label. The notification panel is used for 
 * displaying basic game information, like displaying when a player is connected,
 * when a player can choose, and who the winner is.
 */

public class ClientGUI extends JFrame {
	
	public JLabel infoLabel;			// Info label
	public JButton rockButton;			// Rock button
	public JButton paperButton;			// Paper button
	public JButton scissorsButton;		// Scissors button
	
	public ClientGUI() {
		
		// Setting panel defaults
		this.setTitle("Rock Paper Scissors - Client");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(600, 300);
		this.setResizable(false);
		this.setVisible(true);
		
		// Create and add main panel
		JPanel mainPanel = new JPanel();
		getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(new BorderLayout(0, 0));
		
		// Create and add the choice panel that holds all the buttons
		JPanel choicePanel = new JPanel();
		mainPanel.add(choicePanel, BorderLayout.CENTER);
		choicePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		// Creating the rock choice button
		rockButton = new JButton("");
		rockButton.setIcon(new ImageIcon(RockPaperScissorsClient.class.getResource("/images/rock.png")));
		rockButton.setEnabled(false);
		choicePanel.add(rockButton);
		
		// Creating the paper choice button
		paperButton = new JButton("");
		paperButton.setIcon(new ImageIcon(RockPaperScissorsClient.class.getResource("/images/paper.png")));
		paperButton.setEnabled(false);
		choicePanel.add(paperButton);
		
		// Creating the scissors choice button
		scissorsButton = new JButton("");
		scissorsButton.setIcon(new ImageIcon(RockPaperScissorsClient.class.getResource("/images/scissors.png")));
		scissorsButton.setEnabled(false);
		choicePanel.add(scissorsButton);
		
		// Create and add the notification panel
		JPanel notifPanel = new JPanel();
		mainPanel.add(notifPanel, BorderLayout.SOUTH);
		
		// Sets the info text to "disconnected" automatically
		infoLabel = new JLabel("Disconnected");
		notifPanel.add(infoLabel);
		
	}
}
