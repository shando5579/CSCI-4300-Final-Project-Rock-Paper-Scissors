package rockpaperscissors;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

public class ServerGUI extends JFrame {

	public JTextField PortField;
	public JLabel InfoLabel;
	public JButton ConnectButton;

	public ServerGUI() {

		JPanel mainPanel = new JPanel();
		getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(new BorderLayout(0, 0));

		JPanel connPanel = new JPanel();
		mainPanel.add(connPanel, BorderLayout.NORTH);

		ConnectButton = new JButton("Start Server");
		connPanel.add(ConnectButton);

		JPanel movePanel = new JPanel();
		mainPanel.add(movePanel);

		JPanel notifPanel = new JPanel();
		mainPanel.add(notifPanel, BorderLayout.SOUTH);

		InfoLabel = new JLabel("Server Stopped");
		InfoLabel.setToolTipText("");
		notifPanel.add(InfoLabel);

		// Set the title and default close operation.
	    this.setTitle("Rock Paper Scissors - Server");
		this.setSize(400, 100);
		this.setResizable(false);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
