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
        System.out.println();
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
                this.client.onTurn();
                break;
            case "not_on_turn":
                this.client.notOnTurn();
                break;
            case "complete_turn":
                this.client.completeTurn();
                break;
            case "already_guessed":
                this.client.alreadyGuessed();
                break;
            case "invalid_word":
                this.client.invalidWord();
                break;
            case "game_over":
                this.client.gameOver();
                break;
            default:
                System.out.println(request);
        }
    }
}
