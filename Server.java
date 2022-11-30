package osproj;

import java.io.*;
import java.net.*;
  
// RPS Server

class Server 
{
	
	// Main Function
	
    public static void main(String[] args)
    {
    	// Set server socket to null value
    	
        ServerSocket server = null;
  
        try {
  
        		// Set the port value to listen for
        	
            	server = new ServerSocket(1234);
            	server.setReuseAddress(true); // Reusing address
  
            	// Continue to try for connection until established
            	
            	while (true) 
            	{
  
            		// Have the socket accept the request from the 
            		
            		Socket client = server.accept();
  
            		// Notify that the client has been connected
            		
            		System.out.println("Client connection established: "+client.getInetAddress().getHostAddress()+"\n");
  
            		// Making a new object for the client thread
            		
            		ClientHandler cSocket = new ClientHandler(client);
  
            		// Creating a new thread that uses the object
            		
            		new Thread(cSocket).start();
            }
            	
        }
        
        // Error checking
        
        catch (IOException e) 
        {
        	
            e.printStackTrace();
            
        }
        
        finally 
        {
        	
            if (server != null) 
            {
            	
                try 
                {
                	
                    server.close();
                    
                }
                
                catch (IOException e) 
                {
                	
                    e.printStackTrace();
                    
                }
                
            }
            
        }
        
    }
  
    // ClientHandler class
    
    private static class ClientHandler implements Runnable 
    {
    	
    	// Initialize client socket
    	
        private final Socket cSocket;
  
        // Constructor for client socket
        
        public ClientHandler(Socket socket)
        {
        	
            this.cSocket = socket;
            
        }
  
        // Begin writing input/output
        
        public void run()
        {
        	// Initializing input/output values
        	
            PrintWriter output = null;
            BufferedReader input = null;
            
            try {
                    
                    // Get output from client
            	
                	output = new PrintWriter(cSocket.getOutputStream(),true);
  
                    // Get input from client
                	
                	input = new BufferedReader(new InputStreamReader(cSocket.getInputStream()));
  
                	String line;
                	while ((line = input.readLine()) != null) {
  
                    // Display client message
                		
                    System.out.printf("Sent from the client: %s\n",line);
                    output.println(line);
                    
                }
                	
            }
            
            // Error checking
            
            catch (IOException e) 
            {
            	
                e.printStackTrace();
                
            }
            
            finally {
            	
                		try {
                			
                				if (output != null) 
                				{
                					
                					output.close();
                					
                				}
                				
                				if (input != null) 
                				{
                					
                					input.close();
                					cSocket.close();
                					
                				}
                			}
                		
                catch (IOException e) 
                {
                    e.printStackTrace();
                }
                		
            }
            
        }
        
    }
    
}