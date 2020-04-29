package client;
// Java implementation for multithreaded chat client
// Save file as client.Client.java

import javax.xml.crypto.Data;
import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * Communicate with the game server
 * Play game
 */
public class GameClient {
    final static int ServerPort = 1234;
    static Socket gameSocket;
    private static DataInputStream inputStream;
    private static DataOutputStream outputStream;


    public GameClient(InetAddress serverAddress) throws IOException {
        // establish the connection with the game socket
        GameClient.gameSocket = new Socket(serverAddress, ServerPort);
        inputStream = new DataInputStream(gameSocket.getInputStream());
        outputStream = new DataOutputStream(gameSocket.getOutputStream());
    }


    public static void main(String args[]) {
        Scanner scn = new Scanner(System.in);

        // sendMessage thread
        Thread sendMessage = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    // read the message to deliver.
                    System.out.print("-> ");
                    String msg = scn.nextLine();

                    try {
                        // write on the output stream
                        outputStream.writeUTF(msg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // readMessage thread
        Thread readMessage = new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {
                    try {
                        // read the message sent to this client
                        String msg = inputStream.readUTF();
                        System.out.println(msg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        sendMessage.start();
        readMessage.start();
    }
}
