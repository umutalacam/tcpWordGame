package client;
// Java implementation for multithreaded chat client
// Save file as client.Client.java

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    final static int ServerPort = 1234;
    static Socket gameSocket;
    public Client(InetAddress serverAddress) throws IOException {
        // establish the connection
        Client.gameSocket = new Socket(serverAddress, ServerPort);

    }


    public static void main(String args[]) throws UnknownHostException, IOException {
        Scanner scn = new Scanner(System.in);

        // obtaining input and out streams
        DataInputStream inputStream = new DataInputStream(gameSocket.getInputStream());
        DataOutputStream outputStream = new DataOutputStream(gameSocket.getOutputStream());

        // sendMessage thread
        Thread sendMessage = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                    // read the message to deliver.
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
