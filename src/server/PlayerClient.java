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
    public PlayerClient(Socket clientSocket, String name, DataInputStream dis, DataOutputStream dos) {
        this.clientSocket = clientSocket;
        this.inputStream = dis;
        this.outputStream = dos;
        this.playerName = name;
        this.isloggedin=true;
    }

    @Override
    public void run() {
        String receivedInput;
        while (true) {
            try {
                // Receive string
                receivedInput = inputStream.readUTF();
                System.out.println("Player sent:" + receivedInput);

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
