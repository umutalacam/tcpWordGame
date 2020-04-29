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
    private final String playerName;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;
    private final Socket clientSocket;
    boolean isloggedin;

    // constructor
    public PlayerClient(Socket clientSocket, String playerName) throws IOException {
        this.clientSocket = clientSocket;
        this.inputStream = new DataInputStream(clientSocket.getInputStream());
        this.outputStream = new DataOutputStream(clientSocket.getOutputStream());
        this.playerName = playerName;
        this.isloggedin=true;

        outputStream.writeUTF("You joined lobby.");
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
                    System.out.println("Game not started.");
                }

            } catch (IOException ignored) {

            }

        }
    }

    /**
     * Notify client, invoke methods remotely
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
