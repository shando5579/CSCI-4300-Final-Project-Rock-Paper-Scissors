package rockpaperscissors;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

/*
 * A class that sets up the server for rock paper scissors and determines things such as
 * game rules. These rules include which player one, if both players tied,
 * and if both players have made a selection. It will also set the values
 * for the server status messages and the connect/disconnect button. Along
 * with this, it will check for errors relating to the connection.
 */

// Setting up the server and server rules for rock paper scissors
public class RockPaperScissorsServer {
	
	// Data fields
	private ServerGUI server;			// Server GUI object
	private ServerSocket socket;		// Server socket object
	private boolean active;				// Boolean for seeing if socket is active

	// Constructor for RockPaperScissorsServer
	public RockPaperScissorsServer() throws Exception {
		
		// Setting up ServerGUI object
		server = new ServerGUI();
		server.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Adding the connect/disconnect button and server status messages
		server.connect.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				try {
					// Check if server is running
					if (!active) {
						
						// Creating new socket on port 8081
						socket = new ServerSocket(8081);
						
						// Confirms that server is currently running
						if (!socket.isClosed())
							active = true;
							
						// Display current status of server
						server.status.setText("Server has been started");
						server.status.setForeground(Color.GREEN);
						
						// Button option to stop server
						server.connect.setText("Stop Server");
					} 
					
					// Stopping server
					else {
						
						// Close the socket
						socket.close();
						active = false;

						// Display current status of server
						server.status.setText("Server has been stopped");
						server.status.setForeground(Color.RED);
						
						// Button option to connect
						server.connect.setText("Start Server");
					}
				} 
				
				// Checking for connection error
				catch (Exception exc) {
					server.status.setText("ERROR: Connection error occurred");
					server.connect.setVisible(false);
					exc.printStackTrace();
				}
			}
		});
	}

	// Main class to set up the rock paper scissors game and players
	public static void main(String[] args) throws Exception {
		
		// Creating rock paper scissors server object
		RockPaperScissorsServer rockpaperscissors = new RockPaperScissorsServer();
		rockpaperscissors.server.connect.doClick();
		
		// While the server is active, do the following
		try {
			while (true) {
				if (rockpaperscissors.active) {
					
					// Defining game object
					Game game = new Game();
				
					// Setting default values for player choices
					game.player1choice = "ZERO";
					game.player2choice = "ZERO";
				
					// Ensure both players are connected
					Game.Player player1 = game.new Player(rockpaperscissors.socket.accept(), 1);
					Game.Player player2 = game.new Player(rockpaperscissors.socket.accept(), 2);
				
					// Set both players as opponents
					player1.setOpponent(player2);
					player2.setOpponent(player1);
				
					// Start both player threads
					player1.start();
					player2.start();
				}
			}	
		} finally {
			rockpaperscissors.socket.close();
		}
	}
}

// Game class that defines game logic and winner
class Game {

	// Define player choice values
	public String player1choice;
	public String player2choice;

	// Determine if the game has a winner
	public boolean hasWinner() {
			
			return ((this.player1choice == "CHOICE_ROCK" && this.player2choice == "CHOICE_SCISSORS") || 
					(this.player1choice == "CHOICE_PAPER" && this.player2choice == "CHOICE_ROCK") || 
					(this.player1choice == "CHOICE_SCISSORS" && this.player2choice == "CHOICE_PAPER"));
			
	}

	// Determine if both players tied
	public boolean tied() {
		
		return ((player1choice == "CHOICE_ROCK" && player2choice == "CHOICE_ROCK") || 
				(player1choice == "CHOICE_PAPER" && player2choice == "CHOICE_PAPER") || 
				(player1choice == "CHOICE_SCISSORS" && player2choice == "CHOICE_SCISSORS"));
	}

	// Determine if both players have selected their choice
	public boolean bothChose(Player player, String choice) {
		
		// Set player 1 choice
		if (player.playerNumber == 1)
			player1choice = choice;
		
		// Set player 2 choice
		else
			player2choice = choice;
		
		// Determine if other player selected
		player.otherPlayerChose(player.opponent);
		
		return true;
		
	}

	// Confirm choice is not default if both players chose
	public boolean  bothChose() {
		return player1choice != "ZERO" && player2choice != "ZERO";
	}

	// Begin creating threads for players
	class Player extends Thread {
		
		// Set up player/opponent values and input/output
		int playerNumber;
		Player opponent;
		Socket socket;
		BufferedReader in;
		PrintWriter out;

		// Setting up socket
		public Player(Socket socket, int number) {
			
			// Set socket and player number values
			this.socket = socket;
			this.playerNumber = number;
			
			// Setting up the input/output stream
			try {
				
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);
				out.println("NEW_" + number);
			} 
			
			// Error checking
			catch (Exception e) {
				e.printStackTrace();
			}
			
		}

		// Set player's opponent
		public void setOpponent(Player opponent) {
			this.opponent = opponent;
		}
		
		// Check for opponent's choice
		public void otherPlayerChose(Player player) {
			player.out.println("OPP");
			
			if (bothChose()) {
				
				if (tied()) 
					player.out.println("TIE");
				else 
					player.out.println(hasWinner() ? "WIN" : "DEFEAT");
			}
		}

		// Process for the thread when it is running
		public void run() {
			try {
				
				// Tells the player to start making a choice
				out.println("START");
				
				// Gets commands from clients and processes
				while (true) {	
					
					// Gets the choice from the player
					String choice = in.readLine();
					if (choice != null) {
						
						// If the choice is not "ZERO"
						if (choice.startsWith("CHOICE_")) {
							
							// Gets the 
							if (bothChose(this, choice.substring(6))) {
								
								if (bothChose()) {
									
									if (tied()) 
										out.println("TIE");
									else 
										out.println(hasWinner() ? "WIN" : "DEFEAT");
								}
							}
						} 
						
						// Close socket if game ends
						else if (choice.startsWith("QUIT")) {
							this.socket.close();
							return;
						} 
					}
				}
			} 
			
			// Error checking
			catch (Exception e) {
				e.printStackTrace();
			}
			
			finally {
				try {socket.close();} catch (IOException e) {}
			}
		}
	}
}