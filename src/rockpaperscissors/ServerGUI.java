package rockpaperscissors;

import java.awt.*;
import javax.swing.*;

/*
 * A class that sets up the Server GUI for rock paper scissors. Here, we create the panel and add
 * to it a button that allows it to connect and disconnect from the server, along
 * with a status message that displays the current state of the server. This state 
 * will either show that the user is connected to the server or disconnected.
 */

// Setting up the server GUI for rock paper scissors server
public class ServerGUI extends JFrame 
{

	// Data Fields
    private JPanel center; 	// Center panel
    private JPanel north; 	// North panel
    private JPanel south; 	// South panel
    JLabel status;

	public ServerGUI() 
	{

		// Setting panel defaults
	    this.setTitle("Rock Paper Scissors - Server");
		this.setSize(400, 100);
		this.setResizable(false);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Create and add central panel
		center = new JPanel();
		getContentPane().add(center, BorderLayout.CENTER);
		center.setLayout(new BorderLayout(0, 0));

		// Creating panel for connection button
		north = new JPanel();
		center.add(north, BorderLayout.NORTH);

		// Creating panel to display the status of server
		JPanel south = new JPanel();
		center.add(south, BorderLayout.SOUTH);

		status = new JLabel();
		south.add(status);

	}

}