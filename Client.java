package osproj;

import java.io.*;
import java.net.*;
import java.util.*;
  
// RPS Client

class Client 
{
    
    // Main function
	
    public static void main(String[] args)
    {
        // Begin connection to server
    	
        try (Socket socket = new Socket("localhost", 1234)) 
        {
            
            // Write to server
        	
            PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
  
            // Read message from server
            
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
  
            // Create an object for the scanner
            
            Scanner scan = new Scanner(System.in);
            String line = null;
  
            // Processing input
            
            while (!"exit".equalsIgnoreCase(line)) 
            {
                
                // Read lines
            	
                line = scan.nextLine();
  
                // Send user input to the server
                
                out.println(line);
                out.flush();
  
                // Notify that server response has been obtained
                
                System.out.println("Server response acquired: "+ in.readLine()+"\n");
                
            }
            
            // Close the scanner
            
            scan.close();
            
        }
        
        // Error checking
        
        catch (IOException e) 
        {
        	
            e.printStackTrace();
            
        }
        
    }
    
}
