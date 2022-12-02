package rockpaperscissors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A server for a network multi-player tic tac toe game.  Modified and
 * extended from the class presented in Deitel and Deitel "Java How to
 * Program" book.  I made a bunch of enhancements and rewrote large sections
 * of the code.  The main change is instead of passing *data* between the
 * client and server, I made a TTTP (tic tac toe protocol) which is totally
 * plain text, so you can test the game with Telnet (always a good idea.)
 * The strings that are sent in TTTP are:
 *
 *  Client -> Server           Server -> Client
 *  ----------------           ----------------
 *  MOVE <n>  (0 <= n <= 8)    WELCOME <char>  (char in {X, O})
 *  QUIT                       VALID_MOVE
 *                             OTHER_PLAYER_MOVED <n>
 *                             VICTORY
 *                             DEFEAT
 *                             TIE
 *                             MESSAGE <text>
 *
 * A second change is that it allows an unlimited number of pairs of
 * players to play.
 */
public class RockPaperScissorsServer {

    /**
     * Runs the application. Pairs up clients that connect.
     */
    public static void main(String[] args) throws Exception {
        ServerSocket listener = new ServerSocket(8901);
        System.out.println("Rock Paper Scissors Server is Running");
        try {
            while (true) {
                Game game = new Game();
                Game.Player player1 = game.new Player(listener.accept(), 1);
                Game.Player player2 = game.new Player(listener.accept(), 2);
                player1.setOpponent(player2);
                player2.setOpponent(player1);
                game.currentPlayer = player1;
                player1.start();
                player2.start();
            }
        } finally {
            listener.close();
        }
    }
}

// A two-player game.
class Game {

    // The current player.
    Player currentPlayer;

    /**
     * The class for the helper threads in this multithreaded server
     * application.  A Player is identified by a character mark
     * which is either 'X' or 'O'.  For communication with the
     * client the player has a socket with its input and output
     * streams.  Since only text is being communicated we use a
     * reader and a writer.
     */
    
    public synchronized boolean legalChoice(int choice, Player player) {
        if (player == currentPlayer) {
            currentPlayer = currentPlayer.opponent;
            currentPlayer.otherPlayerChose(choice);
            return true;
        }
        return false;
    }
    
    class Player extends Thread {
        int player_num;
        Player opponent;
        Socket socket;
        BufferedReader input;
        PrintWriter output;

        /**
         * Constructs a handler thread for a given socket and mark
         * initializes the stream fields, displays the first two
         * welcoming messages.
         */
        public Player(Socket socket, int player_num) {
            this.socket = socket;
            this.player_num = player_num;
            try {
                input = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream(), true);
                output.println("WELCOME Player #" + player_num);
                output.println("MESSAGE Waiting for opponent to connect");
            } catch (IOException e) {
                System.out.println("Player died: " + e);
            }
        }

        // Accepts notification of who the opponent is.
        public void setOpponent(Player opponent) {
            this.opponent = opponent;
        }

        // Handles the otherPlayerMoved message.
        public void otherPlayerChose(int choice) {
            output.println("OPPONENT_CHOSE " + choice);
            /*
            output.println(
                hasWinner() ? "DEFEAT" : boardFilledUp() ? "TIE" : "");
            */
        }

        // The run method of this thread.
        public void run() {
            try {
                // The thread is only started after everyone connects.
                output.println("MESSAGE All players connected");

                // Tell the first player that it is their turn.
                if (player_num == 1) {
                	output.println("MESSAGE Your move");
                }

                // Repeatedly get commands from the client and process them.
                while (true) {
                    String command = input.readLine();
                    if (command.startsWith("CHOICE")) {
                    	int choice = Integer.parseInt(command.substring(7));
                        if (legalChoice(choice, this)) {

                        } else {
                            output.println("MESSAGE ?");
                        }
                    } else if (command.startsWith("QUIT")) {
                        return;
                    }
                }
            } catch (IOException e) {
                System.out.println("Player died: " + e);
            } finally {
                try {socket.close();} catch (IOException e) {}
            }
        }
    }
}
