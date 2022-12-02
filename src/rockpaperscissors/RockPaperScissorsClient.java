package rockpaperscissors;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class RockPaperScissorsClient {
	
	private JFrame frame = new JFrame("Rock Paper Scissors Game Client");
    private JLabel messageLabel = new JLabel("");
    private ImageIcon icon;
    private ImageIcon opponentIcon;
    
    private JButton rockButton, paperButton, scissorsButton;
	
	private static int PORT = 8901;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    // Establishes connection and sets up GUI
	public RockPaperScissorsClient(String serverAddress) throws Exception {
		
		// Setup networking
        socket = new Socket(serverAddress, PORT);
        in = new BufferedReader(new InputStreamReader(
            socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        
        // Layout GUI
        messageLabel.setBackground(Color.lightGray);
        frame.getContentPane().add(messageLabel, "South");

        JPanel panel = new JPanel();
        panel.setBackground(Color.black);
        
        //Adds buttons
        rockButton = new JButton("Rock");
        paperButton = new JButton("Paper");
        scissorsButton = new JButton("Scissors");
        
        panel.add(rockButton);
        panel.add(paperButton);
        panel.add(scissorsButton);

        frame.getContentPane().add(panel, "Center");

	}
	
	public void playGame() throws Exception {
		
		String response;
        try {
            response = in.readLine();
            if (response.startsWith("WELCOME")) {
            	/*
                char mark = response.charAt(8);
                icon = new ImageIcon(mark == 'X' ? "xsml.png" : "osml.png");
                opponentIcon  = new ImageIcon(mark == 'X' ? "osml.png" : "xsml.png");
                frame.setTitle("Rock Paper Scissors - Player " + mark);
                */
            }
            while (true) {
                response = in.readLine();
                if (response.startsWith("OPPONENT_CHOSE")) {
                    messageLabel.setText("Opponent chose, your turn");
                } else if (response.startsWith("VICTORY")) {
                    messageLabel.setText("You win");
                    break;
                } else if (response.startsWith("DEFEAT")) {
                    messageLabel.setText("You lose");
                    break;
                } else if (response.startsWith("TIE")) {
                    messageLabel.setText("You tied");
                    break;
                } else if (response.startsWith("MESSAGE")) {
                    messageLabel.setText(response.substring(8));
                }
            }
            out.println("QUIT");
        }
        finally {
            socket.close();
        }
        
	}
	
	
	private boolean wantsToPlayAgain() {
        int response = JOptionPane.showConfirmDialog(frame,
            "Want to play again?",
            "Rock Paper Scissors is Fun Fun Fun",
            JOptionPane.YES_NO_OPTION);
        frame.dispose();
        return response == JOptionPane.YES_OPTION;
    }
    
	
	public static void main(String[] args) throws Exception {
        while (true) {
            String serverAddress = (args.length == 0) ? "localhost" : args[0];
            RockPaperScissorsClient client = new RockPaperScissorsClient(serverAddress);
            
            client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            client.frame.setSize(680, 400);
            client.frame.setVisible(true);
            client.frame.setResizable(false);
            client.playGame();
            
            if (!client.wantsToPlayAgain()) {
                break;
            }
            
            
        }
    }

}
