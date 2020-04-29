package server;
// Java implementation of  server.Server side
// It contains two classes : server.Server and server.ClientHandler
// Save file as server.Server.java

import java.io.*;
import java.util.*;
import java.net.*;

// server.Server class
public class LobbyServer implements Runnable{

    // Array of players
    private static ArrayList<PlayerClient> players = new ArrayList<>();
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
            System.err.println("An error occured while player is joining: " + e.getMessage());
        }
    }

    public void accept(ServerSocket serverSocket) throws IOException {
        while (true) {
            // Accept the incoming request
            Socket clientSocket = serverSocket.accept();
            System.out.println("New player: " + clientSocket.getInetAddress().getHostName());

            // Create a new handler object for handling this request.
            PlayerClient playerClient = new PlayerClient(clientSocket,"Player " + numberOfPlayers);
            players.add(playerClient);

            // Create a new Thread with this object.
            Thread t = new Thread(playerClient);
            t.start();

            LobbyServer.numberOfPlayers++;
        }
    }

    public void startGame(){
        Game.startNewGame(players);
    }
}

