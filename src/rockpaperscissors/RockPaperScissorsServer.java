package rockpaperscissors;

import java.awt.*;
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
		
		// Creating new socket on port 8081
		socket = new ServerSocket(8081);
		
		// Confirms that server is currently running
		if (!socket.isClosed())
			active = true;
			
		// Display current status of server
		server.status.setText("Server has been started");
		server.status.setForeground(Color.GREEN);
		
	}

	// Main class to set up the rock paper scissors game and players
	public static void main(String[] args) throws Exception {
		
		// Creating rock paper scissors server object
		RockPaperScissorsServer rockpaperscissors = new RockPaperScissorsServer();
		
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

	// The current player
	Player currentplayer;
	
	// Define player choice values
	public String player1choice;
	public String player2choice;

	// Determine if the game has a winner
	public int getWinner() {
		
		if ((player1choice.equals("CHOICE_ROCK")  && player2choice.equals("CHOICE_SCISSORS")) || 
			(player1choice.equals("CHOICE_PAPER") && player2choice.equals("CHOICE_ROCK")) || 
			(player1choice.equals("CHOICE_SCISSORS") && player2choice.equals("CHOICE_PAPER"))) {
			
			return 1;
		}
		else if ((player2choice.equals("CHOICE_ROCK") && player1choice.equals("CHOICE_SCISSORS")) || 
				 (player2choice.equals("CHOICE_PAPER") && player1choice.equals("CHOICE_ROCK")) || 
				 (player2choice.equals("CHOICE_SCISSORS") && player1choice.equals("CHOICE_PAPER"))) {
			return 2;
		}
		
		else {
			return 0;
		}
	}

	// Determine if players have selected their choice
	public boolean setChoice(Player player, String choice) {
		
		// Set player 1 choice
		if (player.playerNumber == 1)
			player1choice = choice;
		
		// Set player 2 choice
		else
			player2choice = choice;
		
		return true;
	}
	
	public boolean playerOneChose () {
		return player1choice.startsWith("CHOICE_");
	}
	
	public boolean playerTwoChose() 
	{
		return player2choice.startsWith("CHOICE_");
	}
	
	// Begin creating threads for players
	class Player extends Thread {
		
		// Set up player/opponent values and input/output
		int playerNumber;
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

		// Process for the thread when it is running
		public void run() {
			try {
				
				// Tells the player to start making a choice
				out.println("START");
				
				// Gets the choice from the player
				String choice = in.readLine();
				
				// Gets commands from clients and processes
				while (true) {	
					if (choice != null) {
						
						// If the choice is not "QUIT"
						if (choice.startsWith("CHOICE_")) {
							
							// Sets the player's choice
							setChoice(this, choice);
							
							if (playerOneChose() && playerTwoChose()) {
								
								if (getWinner() > 0) {
									if (getWinner() == this.playerNumber) {
										System.out.println("WIN #" + this.playerNumber);
										out.println("WIN");
										break;
									}
									
									else if (getWinner() != this.playerNumber) {
										System.out.println("DEFEAT #" + this.playerNumber);
										out.println("DEFEAT");
										break;
									}
								}
								else {
									System.out.println("TIE");
									out.println("TIE");
									break;
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
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {socket.close();} catch (IOException e) {}
			}
		} 
	}
}