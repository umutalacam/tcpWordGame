package client;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Listens server. Interface between the server and client.
 */
public class Skeleton implements Runnable{

    GameClient client;
    DataInputStream serverInputStream;

    public Skeleton(GameClient client, DataInputStream serverInputStream){
        this.client = client;
        this.serverInputStream = serverInputStream;
    }

    @Override
    public void run() {
        while (true) {
            try {
                // Read message sent to this client
                String msg = serverInputStream.readUTF();
                clientInvoker(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void clientInvoker(String request){
        switch (request){
            case "on_turn":
                client.onTurn();
                break;
            case "not_on_turn":
                client.notOnTurn();
                break;
            case "complete_turn":
                client.completeTurn();
                break;
            case "already_guessed":
                System.out.println("Already guessed..");
                break;
            case "game_over":
                client.gameOver();
                break;
        }
    }
}
