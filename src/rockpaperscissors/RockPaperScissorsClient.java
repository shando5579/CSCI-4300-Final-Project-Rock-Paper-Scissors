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

	private ClientGUI gui;
	private Socket socket;
	private static int PORT = 8081;
	private boolean connected;
	
	private BufferedReader in;
	private PrintWriter out;
	
	private int playerNumber;

	public RockPaperScissorsClient(String serverAddress) throws Exception {
		gui = new ClientGUI();
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try {
			if (!connected) {
				gui.infoLabel.setText("Attempting to connect to " + serverAddress + ": " + PORT);
				socket = new Socket(serverAddress, PORT);
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);
				if (socket.isConnected())
					connected = true;
				gui.infoLabel.setText("Connected");
			} else {
				gui.infoLabel.setText("Disconnected");
				connected = false;
			}
		} catch (Exception e) {
			gui.infoLabel.setText("A connection error has occured - check if server is running");
			e.printStackTrace();
		}
		
		gui.rockButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				out.println("MR");
				gui.rockButton.setEnabled(false);
				gui.paperButton.setEnabled(false);
				gui.scissorsButton.setEnabled(false);
				gui.infoLabel.setText("You selected Rock; Waiting for Opponent");
			}
		});
		gui.paperButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				out.println("MP");
				gui.rockButton.setEnabled(false);
				gui.paperButton.setEnabled(false);
				gui.scissorsButton.setEnabled(false);
				gui.infoLabel.setText("You selected Paper; Waiting for Opponent");
			}
		});
		gui.scissorsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				out.println("MS");
				gui.rockButton.setEnabled(false);
				gui.paperButton.setEnabled(false);
				gui.scissorsButton.setEnabled(false);
				gui.infoLabel.setText("You selected Scissors; Waiting for Opponent");
			}
		});
	}

	public void play() throws Exception {
		String serverResponse;
		try {
			serverResponse = in.readLine();
			if (serverResponse.startsWith("N")) {
				System.out.println("play() - server sent N");
				this.playerNumber = Integer.parseInt(serverResponse.substring(1));
				gui.setTitle("Rock Paper Scissors - Player #" + this.playerNumber);
				gui.infoLabel.setText("Connected - Waiting for Opponent");
			}
			while (true) {
				serverResponse = in.readLine();
				System.out.println(serverResponse);
				if (serverResponse != null) {
					if (serverResponse.startsWith("G")) {
						System.out.println("play() - server sent G");
						gui.infoLabel.setText("All Players Connected - Make Your Choice");
						gui.rockButton.setEnabled(true);
						gui.paperButton.setEnabled(true);
						gui.scissorsButton.setEnabled(true);

					} else if (serverResponse.startsWith("W")) {
						gui.infoLabel.setText("You have won!");
						TimeUnit.SECONDS.sleep(1);
						break;
					} else if (serverResponse.startsWith("D")) {
						gui.infoLabel.setText("You have been beaten");
						TimeUnit.SECONDS.sleep(1);
						break;
					} else if (serverResponse.startsWith("T")) {
						gui.infoLabel.setText("You have both tied");
						TimeUnit.SECONDS.sleep(1);
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean rematch() {
		int response = JOptionPane.showConfirmDialog(gui, "Do you want to play again?", "Rock Paper Scissors - Rematch", JOptionPane.YES_NO_OPTION);
		return response == JOptionPane.YES_OPTION;
	}

	public static void main(String[] args) throws Exception {
		System.out.println("Starting client program...");
		String serverAddress = (args.length == 0) ? "localhost" : args[0];
		System.out.println(serverAddress);
		RockPaperScissorsClient client = new RockPaperScissorsClient(serverAddress);
		
		while (true) {
			if (client.connected) {
				System.out.println("main() - client connected; starting play");
				client.play();
				if (!client.rematch()) {
					System.out.println("main() - user declined rematch");
					client.out.println("Q");
					client.socket.close();
					client.gui.setVisible(false);
					client.gui.dispose();
					break;
				} else {
					System.out.println("main() - user accepted rematch");
				}
			}
			TimeUnit.SECONDS.sleep(1);
		}
	}
}
