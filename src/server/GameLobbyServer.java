package server;
// Java implementation of  server.Server side
// It contains two classes : server.Server and server.ClientHandler
// Save file as server.Server.java

import java.io.*;
import java.util.*;
import java.net.*;

// server.Server class
public class GameLobbyServer implements Runnable{

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
            System.out.println("New player joined: " + clientSocket);

            // obtain input and output streams
            DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());

            // Create a new handler object for handling this request.
            PlayerClient playerClient = new PlayerClient(clientSocket,"Player" + numberOfPlayers, inputStream, outputStream);
            players.add(playerClient);

            // Create a new Thread with this object.
            Thread t = new Thread(playerClient);
            t.start();

            GameLobbyServer.numberOfPlayers++;
        }
    }
}

