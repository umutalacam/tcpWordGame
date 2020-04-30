package edu.eskisehir.ceng.tcp_word.server;
import edu.eskisehir.ceng.tcp_word.App;

import java.io.*;
import java.util.*;
import java.net.*;

public class LobbyServer implements Runnable{

    // Array of players
    protected static ArrayList<PlayerClient> players = new ArrayList<>();
    private PlayerClient admin;
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
            String playerName;
            DataInputStream clientInputStream = new DataInputStream(clientSocket.getInputStream());

            try {
                playerName = clientInputStream.readUTF();
                playerName = playerName.split(";")[1];

            } catch (Exception exception) {
                playerName = "Player"+numberOfPlayers;
                System.out.println("Cannot get nickname, assigned: " + playerName);
            }

            // Create a new handler object for handling this request.
            PlayerClient playerClient = new PlayerClient(clientSocket, playerName);

            if (numberOfPlayers == 0 && admin == null){
                admin = playerClient;
            }
            else {
                players.add(playerClient);
                System.out.printf("\r%s joined lobby. (%s)\n->", playerName, clientSocket.getInetAddress().getHostAddress());
            }

            // Create a new Thread with this object.
            Thread t = new Thread(playerClient);
            t.start();

            LobbyServer.numberOfPlayers++;
        }
    }

    public void notify(String msg, boolean prompt){
        System.out.print("\r"+msg+"\n");
        if (prompt)
            System.out.print("->");
    }

    public void broadcast(PlayerClient player, String msg){
        String message = player.getPlayerName() + ": " + msg;
        for (PlayerClient client: LobbyServer.players) {
            client.notifyClient(message);
        }
        App.lobbyServer.notify(message, false);
    }

    public void startGame() {
        players.add(admin);
        Game.startNewGame(players);
    }

    public PlayerClient getAdminClient(){
        return admin;
    }

}

