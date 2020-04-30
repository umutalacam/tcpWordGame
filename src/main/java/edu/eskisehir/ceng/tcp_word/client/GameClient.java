package edu.eskisehir.ceng.tcp_word.client;
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
    private String playerName;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private boolean inGame;

    public GameClient(InetAddress serverAddress, String playerName) throws IOException {
        // establish the connection with the game socket
        this.playerName = playerName;
        GameClient.gameSocket = new Socket(serverAddress, ServerPort);
        inputStream = new DataInputStream(gameSocket.getInputStream());
        outputStream = new DataOutputStream(gameSocket.getOutputStream());
        outputStream.writeUTF("nickname;"+playerName);
    }

    public void start(){
        inGame = true;
        while (inGame){
            String input = prompt();
            if (inGame) {
                System.out.print("-> ");
                sendWord(input);
            }
        }
        System.out.println("Goodbye!");
        System.exit(0);
    }


    public DataInputStream getInputStream() {
        return inputStream;
    }

    public DataOutputStream getOutputStream() {
        return outputStream;
    }

    /* Implementation of different states */
    protected  void onTurn(){
        //Turn of this player.
        System.out.print("\rYour turn->");
    }

    protected void completeTurn(){
        System.out.print("\r");
    }

    protected void notOnTurn() {
        System.out.print("\r");
    }

    protected void gameOver(){
        System.out.println("\rTime Up! You lost the game.\nPress enter the quit game.");
        this.inGame = false;
    }

    protected void alreadyGuessed(){
        System.out.println("\rYour answer have guessed before. Please enter another word.");
        onTurn();
    }

    protected void invalidWord(){
        System.out.println("\rPlease enter a valid word. " +
                "Your answer's first character needs to be same with the last character of the last answer.");
        onTurn();
    }

    protected void win(){
        System.out.println("\r--------------------\n" +
                "!!!Congratulations!!!\n" +
                "You won. Press enter the quit game");
        this.inGame = false;
    }

    private  void sendWord(String word){
        try{
            outputStream.writeUTF(word);
        }catch (IOException exception){
            System.out.println("Error: Cannot send word. Network error.");
        }
    }

    private static String prompt() {
        Scanner keyboardScn = new Scanner(System.in);
        return keyboardScn.nextLine();
    }
}
