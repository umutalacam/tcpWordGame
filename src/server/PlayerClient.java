package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;

/**
 * Manages player communication
 */
class PlayerClient implements Runnable {
    private String playerName;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;
    private Socket clientSocket;
    boolean isloggedin;

    // constructor
    public PlayerClient(Socket clientSocket, String playerName) throws IOException {
        this.clientSocket = clientSocket;
        this.inputStream = new DataInputStream(clientSocket.getInputStream());
        this.outputStream = new DataOutputStream(clientSocket.getOutputStream());
        this.playerName = playerName;
        this.isloggedin=true;

        outputStream.writeUTF(playerName + " joined lobby.");
    }

    @Override
    public void run() {
        String receivedInput;
        while (true) {
            try {
                // Receive string from user.
                receivedInput = inputStream.readUTF();
                outputStream.writeUTF("Player sent:" + receivedInput);;

                if(receivedInput.equals("logout")){
                    this.isloggedin=false;
                    this.clientSocket.close();
                    break;
                }

            } catch (IOException ignored) {

            }

        }
        try {
            // closing resources
            this.inputStream.close();
            this.outputStream.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
