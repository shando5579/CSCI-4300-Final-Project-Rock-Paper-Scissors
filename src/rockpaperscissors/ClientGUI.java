package rockpaperscissors;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import java.awt.CardLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.FlowLayout;

public class ClientGUI extends JFrame {
	
	public JLabel infoLabel;
	public JButton rockButton;
	public JButton paperButton;
	public JButton scissorsButton;
	
	public ClientGUI() {
		
		JPanel mainPanel = new JPanel();
		getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel movePanel = new JPanel();
		mainPanel.add(movePanel, BorderLayout.CENTER);
		movePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		rockButton = new JButton("");
		rockButton.setIcon(new ImageIcon(RockPaperScissorsClient.class.getResource("/images/rock.png")));
		rockButton.setEnabled(false);
		movePanel.add(rockButton);
		
		paperButton = new JButton("");
		paperButton.setIcon(new ImageIcon(RockPaperScissorsClient.class.getResource("/images/paper.png")));
		paperButton.setEnabled(false);
		movePanel.add(paperButton);
		
		scissorsButton = new JButton("");
		scissorsButton.setIcon(new ImageIcon(RockPaperScissorsClient.class.getResource("/images/scissors.png")));
		scissorsButton.setEnabled(false);
		movePanel.add(scissorsButton);
		
		JPanel notifPanel = new JPanel();
		mainPanel.add(notifPanel, BorderLayout.SOUTH);
		
		infoLabel = new JLabel("Disconnected");
		notifPanel.add(infoLabel);
		
		this.setTitle("Rock Paper Scissors - Client");
		this.setSize(600, 300);
		this.setResizable(false);
		this.setVisible(true);
		
	}
}
