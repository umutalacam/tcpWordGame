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
    private String name;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;
    private Socket clientSocket;
    boolean isloggedin;

    // constructor
    public PlayerClient(Socket clientSocket, String name, DataInputStream dis, DataOutputStream dos) {
        this.clientSocket = clientSocket;
        this.inputStream = dis;
        this.outputStream = dos;
        this.name = name;
        this.isloggedin=true;
    }

    @Override
    public void run() {
        String receivedInput;
        while (true) {
            try {
                // Receive string
                receivedInput = inputStream.readUTF();
                System.out.println(receivedInput);

                if(receivedInput.equals("logout")){
                    this.isloggedin=false;
                    this.clientSocket.close();
                    break;
                }

            } catch (IOException e) {
                e.printStackTrace();
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
