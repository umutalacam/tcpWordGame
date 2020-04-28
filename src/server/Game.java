package server;

import java.util.ArrayList;

public class Game {
    private ArrayList<PlayerClient> players;
    private int currentPlayerIndex;
    private static Game currentGame;
    private ArrayList<String> wordsUsed = new ArrayList<>();

    //Game contructor
    private Game(ArrayList<PlayerClient> players) {
        this.players = players;
        this.currentPlayerIndex = 0;
    }

    public static Game startNewGame(ArrayList<PlayerClient> players){
        System.out.println("Starting new game...");
        currentGame = new Game(players);
        return currentGame;
    }

    public int giveWord(String word){
        //Try to give word
        return 0;
    }

    private void completeTurn(){

    }

    public static Game getCurrentGame() {
        return currentGame;
    }
}
