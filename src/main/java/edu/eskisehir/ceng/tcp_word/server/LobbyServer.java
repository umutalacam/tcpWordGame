package edu.eskisehir.ceng.tcp_word.server;
import java.io.*;
import java.util.*;
import java.net.*;

public class LobbyServer implements Runnable{

    // Array of players
    protected static ArrayList<PlayerClient> players = new ArrayList<>();
    private static int numberOfPlayers = 0;

    @Override
    public void run() {
        //Start server at port 1234
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(1234);
        } catch (IOException e) {
            System.out.println("Couldn't host game. Server couldn't started.");
            return;
        }

        try {
            //Accept incoming players
            accept(serverSocket);
        } catch (IOException e) {
            System.err.println("An error occurred while player is joining: " + e.getMessage());
        }
    }

    public void accept(ServerSocket serverSocket) throws IOException {
        while (true) {
            // Accept the incoming request
            Socket clientSocket = serverSocket.accept();
            String playerName = "Player"+numberOfPlayers;
            if (numberOfPlayers>0){
                System.out.printf("\r%s joined lobby. (%s)\n->", playerName, clientSocket.getInetAddress().getHostAddress());
            }

            // Create a new handler object for handling this request.
            PlayerClient playerClient = new PlayerClient(clientSocket, playerName);
            players.add(playerClient);

            // Create a new Thread with this object.
            Thread t = new Thread(playerClient);
            t.start();

            LobbyServer.numberOfPlayers++;
        }
    }

    public void startGame() {
        Game.startNewGame(players);
    }
}

