package rockpaperscissors;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;

public class RockPaperScissorsServer {

	private ServerGUI gui;
	private ServerSocket socket;
	private boolean running;

	public RockPaperScissorsServer() throws Exception {
		gui = new ServerGUI();
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gui.ConnectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (!running) {
						System.out.println("RPSServer() - Connect button selected, starting server");
						socket = new ServerSocket(Integer.parseInt(gui.PortField.getText()));
						if (!socket.isClosed())
							running = true;
						gui.ConnectButton.setText("Stop Server");
						gui.InfoLabel.setText("Server Started");
						gui.PortField.setEditable(false);
					} else {
						System.out.println("RPSServer() - Connect button selected, stopping server");
						socket.close();
						running = false;
						gui.ConnectButton.setText("Start Server");
						gui.InfoLabel.setText("Server Stopped");
						gui.PortField.setEditable(true);
					}
				} catch (Exception ex) {
					System.out.println("RPSServer() - Unknown connection error");
					gui.InfoLabel.setText("Connection error");
					ex.printStackTrace();
				}
			}
		});
	}

	public static void main(String[] args) throws Exception {
		System.out.println("main() - SERVER PROGRAM STARTED");
		RockPaperScissorsServer server = new RockPaperScissorsServer();
		//
		server.gui.PortField.setText("7274");
		server.gui.ConnectButton.doClick();
		//
		while (true) {
			if (server.running) {
				System.out.println("main() - Server started and running - creating game instance");
				Game game = new Game();
				game.p1move = 'Z';
				game.p2move = 'Z';
				Game.Player player1 = game.new Player(server.socket.accept(), 1);
				Game.Player player2 = game.new Player(server.socket.accept(), 2);
				player1.setOpponent(player2);
				player2.setOpponent(player1);
				player1.start();
				player2.start();
			}
			TimeUnit.SECONDS.sleep(1);
		}
	}
}

class Game {

	public char p1move;
	public char p2move;

	public boolean winner(int playernum) {
		if (playernum == 1) {
			return ((this.p1move == 'R' && this.p2move == 'S') || (this.p1move == 'P' && this.p2move == 'R')
					|| (this.p1move == 'S' && this.p2move == 'P'));
		} else
			return ((this.p2move == 'R' && this.p1move == 'S') || (this.p2move == 'P' && this.p1move == 'R')
					|| (this.p2move == 'S' && this.p1move == 'P'));
	}

	public boolean tied() {
		System.out.println("tied() - Tie: " + ((p1move == 'R' && p2move == 'R') || (p1move == 'P' && p2move == 'P')
				|| (p1move == 'S' && p2move == 'S')));
		return ((p1move == 'R' && p2move == 'R') || (p1move == 'P' && p2move == 'P')
				|| (p1move == 'S' && p2move == 'S'));
	}

	public boolean shoot(Player player, char decision) {
		System.out.println("shoot() - Player " + player.playernumber + " just moved " + decision);
		if (player.playernumber == 1) {
			p1move = decision;
		} else {
			p2move = decision;
		}
		player.otherPlayerMoved(player.opponent);
		return true;
	}

	public boolean bothMoved() {
		return p1move != 'Z' && p2move != 'Z';
	}

	class Player extends Thread {
		int playernumber;
		Player opponent;
		Socket socket;
		BufferedReader in;
		PrintWriter out;

		public Player(Socket socket, int number) {
			this.socket = socket;
			this.playernumber = number;
			try {
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);
				out.println("N" + number);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void setOpponent(Player opponent) {
			this.opponent = opponent;
		}

		public void otherPlayerMoved(Player player) {
			player.out.println("O");
			if (bothMoved()) {
				if (tied()) {
					player.out.println("T");
				} else {
					player.out.println(winner(player.playernumber) ? "W" : "D");
				}
			}
		}

		public void run() {
			try {
				out.println("G");
				System.out.println("PLAYER " + this.playernumber + " THREAD ==> run() - Sending Go sequence to player "
						+ this.playernumber);
				while (true) {
					String move = in.readLine();
					if (move.startsWith("M")) {
						if (shoot(this, move.charAt(1))) {
							System.out.println("PLAYER " + this.playernumber + " THREAD ==> run()  - Player "
									+ this.playernumber + " chose " + move.charAt(1));
							if (bothMoved()) {
								if (tied()) {
									out.println("T");
								} else {
									out.println(winner(this.playernumber) ? "W" : "D");
								}
							}
						}
					} else if (move.startsWith("Q")) {
						this.socket.close();
						System.out.println("PLAYER " + this.playernumber + " THREAD ==> run()  - Player "
								+ this.playernumber + " chose to quit");
						return;
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}