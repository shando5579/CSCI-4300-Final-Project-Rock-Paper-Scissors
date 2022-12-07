package rockpaperscissors;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

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
				out.println("MR");
				gui.rockButton.setEnabled(false);
				gui.paperButton.setEnabled(false);
				gui.scissorsButton.setEnabled(false);
				gui.infoLabel.setText("You chose rock");
			}
		});
		
		// Action listener for when paper button is pressed
		gui.paperButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				out.println("MP");
				gui.rockButton.setEnabled(false);
				gui.paperButton.setEnabled(false);
				gui.scissorsButton.setEnabled(false);
				gui.infoLabel.setText("You chose paper");
			}
		});
		
		// Action listener for when scissors button is pressed
		gui.scissorsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				out.println("MS");
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
			if (serverResponse.startsWith("N")) {
				this.playerNumber = Integer.parseInt(serverResponse.substring(1));
				gui.setTitle("Rock Paper Scissors - Player #" + this.playerNumber);
				gui.infoLabel.setText("Connected - Waiting for Opponent");
			}
			
			// Awaits server response
			while (true) {
				serverResponse = in.readLine();
				if (serverResponse != null) {
					
					// Tells players start
					if (serverResponse.startsWith("G")) {
						gui.infoLabel.setText("All Players Connected - Make Your Choice");
						gui.rockButton.setEnabled(true);
						gui.paperButton.setEnabled(true);
						gui.scissorsButton.setEnabled(true);

						// Outputs to label if player won
					} else if (serverResponse.startsWith("W")) {
						gui.infoLabel.setText("You have won!");
						break;
						
						// Outputs to label if player was beaten
					} else if (serverResponse.startsWith("D")) {
						gui.infoLabel.setText("You have been beaten");
						break;
						
						// Outputs to label if both players tied
					} else if (serverResponse.startsWith("T")) {
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
		return response == JOptionPane.YES_OPTION;
	}

	// Main driver - starts client program
	public static void main(String[] args) throws Exception {
		while (true) {	
			String serverAddress = (args.length == 0) ? "localhost" : args[0];				// Sets address to "localhost"
			RockPaperScissorsClient client = new RockPaperScissorsClient(serverAddress);	// Client is created using server address and port
				client.play();							// Tells the client to play the game
				if (!client.wantsToPlayAgain()) {		// Once client is done playing, asks if it wants to play again
					break;
				}
		}
	}
}
