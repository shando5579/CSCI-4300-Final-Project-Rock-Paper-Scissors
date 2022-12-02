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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class RockPaperScissorsClient {
	
	private JFrame frame = new JFrame("Tic Tac Toe");
    private JLabel messageLabel = new JLabel("");
    private ImageIcon icon;
    private ImageIcon opponentIcon;
	
	private static int PORT = 8901;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    // Establishes connection, sets up GUI, and 
	public RockPaperScissorsClient(String serverAddress) throws Exception {
		// Setup networking
        socket = new Socket(serverAddress, PORT);
        in = new BufferedReader(new InputStreamReader(
            socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        
        /*
        // Layout GUI
        messageLabel.setBackground(Color.lightGray);
        frame.getContentPane().add(messageLabel, "South");

        JPanel boardPanel = new JPanel();
        boardPanel.setBackground(Color.black);
        boardPanel.setLayout(new GridLayout(3, 3, 2, 2));
        for (int i = 0; i < board.length; i++) {
            final int j = i;
            board[i] = new Square();
            board[i].addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    currentSquare = board[j];
                    out.println("MOVE " + j);}});
            boardPanel.add(board[i]);
        }
        frame.getContentPane().add(boardPanel, "Center");
        */
        
	}
	
	public void playGame() throws Exception {
		/*
		String response;
        try {
            response = in.readLine();
            if (response.startsWith("WELCOME")) {
                char mark = response.charAt(8);
                icon = new ImageIcon(mark == 'X' ? "xsml.png" : "osml.png");
                opponentIcon  = new ImageIcon(mark == 'X' ? "osml.png" : "xsml.png");
                frame.setTitle("Rock Paper Scissors - Player " + mark);
            }
            while (true) {
                response = in.readLine();
                if (response.startsWith("VALID_MOVE")) {
                    messageLabel.setText("Valid move, please wait");
                    currentSquare.setIcon(icon);
                    currentSquare.repaint();
                } else if (response.startsWith("OPPONENT_MOVED")) {
                    int loc = Integer.parseInt(response.substring(15));
                    board[loc].setIcon(opponentIcon);
                    board[loc].repaint();
                    messageLabel.setText("Opponent moved, your turn");
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
        */
	}
	
	/*
	private boolean wantsToPlayAgain() {
        int response = JOptionPane.showConfirmDialog(frame,
            "Want to play again?",
            "Rock Paper Scissors is Fun Fun Fun",
            JOptionPane.YES_NO_OPTION);
        frame.dispose();
        return response == JOptionPane.YES_OPTION;
    }
    */
	
	public static void main(String[] args) throws Exception {
        while (true) {
        	// 23. What is the purpose of the following line?        	
            String serverAddress = (args.length == 0) ? "localhost" : args[0];
            RockPaperScissorsClient client = new RockPaperScissorsClient(serverAddress);
            
            /*
            client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            client.frame.setSize(240, 160);
            client.frame.setVisible(true);
            client.frame.setResizable(false);
            client.play();
            if (!client.wantsToPlayAgain()) {
                break;
            }
            */
        }
    }

}
