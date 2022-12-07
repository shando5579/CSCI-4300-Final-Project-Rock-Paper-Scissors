package rockpaperscissors;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/*
 * A class that sets up the client for rock paper scissors and handles communications 
 * to and from the server. These communications are sent through an BufferedReader and
 * PrintWriter object. Each piece of data communicated will be a string that begins with
 * a keyword to help identify what the client / server should do. Once the game has concl-
 * uded, a winner will be chosen. The winner will be indicated only on the GUI of the pla-
 * yer who chose first. This is a bug with the server communication. Both users will be
 * prompted for a rematch at the end of the game and the clients will update successfully.
 */

public class RockPaperScissorsClient {

	private ClientGUI gui;				// GUI object
	private Socket socket;				// Socket object
	private static int PORT = 8081;		// Port information for socket
	
	private BufferedReader in;			// Server input reader
	private PrintWriter out;			// Client output writer
	
	private int playerNumber;			// Player number (1 or 2)

	// Rock paper scissors client that takes in a server address
	public RockPaperScissorsClient(String serverAddress) throws Exception {
		
		// Setup Networking
		socket = new Socket(serverAddress, PORT);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);
		
		// GUI Setup
		gui = new ClientGUI();
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Update label to reflect connection
		if (socket.isConnected())
			gui.infoLabel.setText("Connected");
		else
			gui.infoLabel.setText("Disconnected");
		
		// Action listener for when rock button is pressed
		gui.rockButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				out.println("CHOICE_ROCK");
				gui.rockButton.setEnabled(false);
				gui.paperButton.setEnabled(false);
				gui.scissorsButton.setEnabled(false);
				gui.infoLabel.setText("You chose rock");
			}
		});
		
		// Action listener for when paper button is pressed
		gui.paperButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				out.println("CHOICE_PAPER");
				gui.rockButton.setEnabled(false);
				gui.paperButton.setEnabled(false);
				gui.scissorsButton.setEnabled(false);
				gui.infoLabel.setText("You chose paper");
			}
		});
		
		// Action listener for when scissors button is pressed
		gui.scissorsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				out.println("CHOICE_SCISSORS");
				gui.rockButton.setEnabled(false);
				gui.paperButton.setEnabled(false);
				gui.scissorsButton.setEnabled(false);
				gui.infoLabel.setText("You chose scissors");
			}
		});
	}

	// Plays the two-player game
	public void play() throws Exception {
		String serverResponse;
		try {
			
			serverResponse = in.readLine();
			
			// Sets up new player connection and updates GUI
			if (serverResponse.startsWith("NEW_")) {
				this.playerNumber = Integer.parseInt(serverResponse.substring(4));
				gui.setTitle("Rock Paper Scissors - Player #" + this.playerNumber);
				gui.infoLabel.setText("Connected - Waiting for Opponent");
			}
			
			// Awaits server response
			while (true) {
				serverResponse = in.readLine();
				if (serverResponse != null) {
					
					// Tells players start
					if (serverResponse.startsWith("START")) {
						gui.infoLabel.setText("All Players Connected - Make Your Choice");
						gui.rockButton.setEnabled(true);
						gui.paperButton.setEnabled(true);
						gui.scissorsButton.setEnabled(true);

						// Outputs to label if player won
					} else if (serverResponse.startsWith("WIN")) {
						gui.infoLabel.setText("You have won!");
						break;
						
						// Outputs to label if player was defeated
					} else if (serverResponse.startsWith("DEFEAT")) {
						gui.infoLabel.setText("You have been defeated");
						break;
						
						// Outputs to label if both players tied
					} else if (serverResponse.startsWith("TIE")) {
						gui.infoLabel.setText("You have both tied");
						break;
					}
				}
			}
		} 
		finally {
			socket.close();		// Closes socket at the end
		}
	}

	// Checks if the player wants to play again. Returns a boolean value while also disposing of the GUI.
	private boolean wantsToPlayAgain() {
		int response = JOptionPane.showConfirmDialog(gui, 
				"Do you want to play again?", 
				"Rock Paper Scissors - Rematch", 
				JOptionPane.YES_NO_OPTION);
		gui.dispose();
		out.println("QUIT");
		return response == JOptionPane.YES_OPTION;
	}

	// Main driver - starts client program
	public static void main(String[] args) throws Exception {
		while (true) {	
			String serverAddress = (args.length == 0) ? "localhost" : args[0];				// Sets address to "localhost"
			RockPaperScissorsClient client = new RockPaperScissorsClient(serverAddress);	// Client is created using server address and port
			client.play();																	// Tells the client to play the game
			
				if (!client.wantsToPlayAgain()) {		// Once client is done playing, asks if it wants to play again
					break;
				}
		}
	}
}
