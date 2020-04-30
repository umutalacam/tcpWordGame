package edu.eskisehir.ceng.tcp_word.server;

import edu.eskisehir.ceng.tcp_word.App;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Manages player communication
 */
class PlayerClient implements Runnable {
    private final String playerName;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;
    private static int playerCount;

    // constructor
    public PlayerClient(Socket clientSocket, String playerName) throws IOException {
        this.inputStream = new DataInputStream(clientSocket.getInputStream());
        this.outputStream = new DataOutputStream(clientSocket.getOutputStream());
        this.playerName = playerName;
        outputStream.writeUTF("You joined the lobby.");
    }

    @Override
    public void run() {
        String receivedInput;
        while (true) {
            try {
                // Receive string from user.
                receivedInput = inputStream.readUTF();
                if (Game.getCurrentGame() != null) {
                    Game.getCurrentGame().giveWord(this, receivedInput);
                }
                else {
                    String message = this.getPlayerName() + ": " + receivedInput;
                    App.lobbyServer.broadcast(this, receivedInput);
                }
            } catch (IOException ignored) {

            }
        }
    }

    /**
     * Notify edu.eskisehir.ceng.tcp_word.client, invoke methods remotely
     */
    protected void notifyClient(String msg){
        try {
            outputStream.writeUTF(msg);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public String getPlayerName() {
        return playerName;
    }
}
