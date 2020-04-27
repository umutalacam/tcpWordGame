package server;

import java.util.ArrayList;

public class Game {
    private ArrayList<PlayerClient> players;
    private int currentPlayerIndex;

    private ArrayList<String> wordsUsed = new ArrayList<>();

    public Game(ArrayList<PlayerClient> players) {
        this.players = players;
    }

    public int giveWord(){
        return 0;
    }

    private void completeTurn(){

    }
}
