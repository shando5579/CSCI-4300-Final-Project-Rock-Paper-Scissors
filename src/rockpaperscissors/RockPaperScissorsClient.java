package rockpaperscissors;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.Toolkit;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class RockPaperScissorsClient {
	
	private JFrame frame = new JFrame("Rock Paper Scissors Game Client");
    private JLabel messageLabel = new JLabel("");
    private JButton rockButton, paperButton, scissorsButton;
	
	private static int PORT = 8901;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private JLabel lblNewLabel;
    private JLabel lblNewLabel_1;
    private JLabel label;
    private JPanel panel_1;

    // Establishes connection and sets up GUI
	/**
	 * @wbp.parser.entryPoint
	 */
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
        panel.setBackground(Color.white);
        frame.getContentPane().add(panel, "Center");
        
        label = new JLabel("");
        label.setIcon(new ImageIcon(RockPaperScissorsClient.class.getResource("/images/scissors.png")));
        
        lblNewLabel_1 = new JLabel("");
        lblNewLabel_1.setIcon(new ImageIcon(RockPaperScissorsClient.class.getResource("/images/rock.png")));
        
        lblNewLabel = new JLabel("");
        lblNewLabel.setIcon(new ImageIcon(RockPaperScissorsClient.class.getResource("/images/paper.png")));
        
        panel_1 = new JPanel();
        
        
        //Adds buttons
        rockButton = new JButton("Rock");
        panel_1.add(rockButton);
        paperButton = new JButton("Paper");
        panel_1.add(paperButton);
        scissorsButton = new JButton("Scissors");
        panel_1.add(scissorsButton);
        GroupLayout gl_panel = new GroupLayout(panel);
        gl_panel.setHorizontalGroup(
        	gl_panel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_panel.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING, false)
        				.addComponent(panel_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        				.addGroup(Alignment.LEADING, gl_panel.createSequentialGroup()
        					.addComponent(lblNewLabel_1)
        					.addGap(6)
        					.addComponent(lblNewLabel)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(label)))
        			.addContainerGap(227, Short.MAX_VALUE))
        );
        gl_panel.setVerticalGroup(
        	gl_panel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_panel.createSequentialGroup()
        			.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
        				.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
        					.addComponent(lblNewLabel_1)
        					.addComponent(label))
        				.addComponent(lblNewLabel))
        			.addGap(18)
        			.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        			.addGap(220))
        );
        panel.setLayout(gl_panel);

	}
	
	public void playGame() throws Exception {
		
		String response;
        try {
            response = in.readLine();
            if (response.startsWith("WELCOME")) {
                char mark = response.charAt(16);
                frame.setTitle("Rock Paper Scissors - Player #" + mark);
                frame.setIconImage(Toolkit.getDefaultToolkit().getImage(RockPaperScissorsClient.class.getResource("/images/" + mark + ".png")));
                
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
            "Rock Paper Scissors - Play Again?",
            JOptionPane.YES_NO_OPTION);
        frame.dispose();
        return response == JOptionPane.YES_OPTION;
    }
    
	
	public static void main(String[] args) throws Exception {
        while (true) {
            String serverAddress = (args.length == 0) ? "localhost" : args[0];
            RockPaperScissorsClient client = new RockPaperScissorsClient(serverAddress);
            
            client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            client.frame.setSize(435, 250);
            client.frame.setVisible(true);
            client.frame.setResizable(false);
            client.playGame();
            
            if (!client.wantsToPlayAgain()) {
                break;
            }
            
            
        }
    }

}
