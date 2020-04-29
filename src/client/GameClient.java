package client;
// Java implementation for multithreaded chat client
// Save file as client.Client.java

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
    private static GameClient client;
    private Thread skeletonThread;

    public GameClient(InetAddress serverAddress) throws IOException {
        // establish the connection with the game socket
        GameClient.gameSocket = new Socket(serverAddress, ServerPort);
        inputStream = new DataInputStream(gameSocket.getInputStream());
        outputStream = new DataOutputStream(gameSocket.getOutputStream());
    }

    public void init(){
        //Start skeleton thread
        Skeleton listener = new Skeleton(this, inputStream);
        skeletonThread = new Thread(listener);
        skeletonThread.start();
    }

    protected void onTurn(){
        //Turn of this player.
        System.out.print("Your turn");
        String answer = prompt();
        sendWord(answer);

    }

    protected void completeTurn(){
    }

    protected void notOnTurn(){
        System.out.println("It's not your turn.");
    }

    protected void gameOver(){
        try {
            skeletonThread.wait();
            System.out.println("Game Over: You lost.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sendWord(String word){
        try{
            outputStream.writeUTF(word);
        }catch (IOException exception){
            System.out.println("Error: Cannot send word. Network error.");
        }
    }

    public static String prompt(){
        Scanner keyboardScn = new Scanner(System.in);
        System.out.print("-> ");
        return keyboardScn.nextLine();
    }
}
