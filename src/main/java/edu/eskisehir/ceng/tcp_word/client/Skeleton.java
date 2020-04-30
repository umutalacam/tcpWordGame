package edu.eskisehir.ceng.tcp_word.client;

import edu.eskisehir.ceng.tcp_word.App;

import javax.sound.midi.Soundbank;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * Listens server. Interface between the server and edu.eskisehir.ceng.tcp_word.client.
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
                System.out.println("Connection lost!");
                break;
            }
        }
    }

    private void clientInvoker(String request){
        switch (request){
            case "on_turn":
                App.gameClient.onTurn();
                break;
            case "not_on_turn":
                App.gameClient.notOnTurn();
                break;
            case "complete_turn":
                App.gameClient.completeTurn();
                break;
            case "already_guessed":
                App.gameClient.alreadyGuessed();
                break;
            case "invalid_word":
                App.gameClient.invalidWord();
                break;
            case "game_over":
                App.gameClient.gameOver();
                break;
            case "win":
                App.gameClient.win();
                break;
            default:
                System.out.print("\r"+request+"\n->");
        }
    }
}
