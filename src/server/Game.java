package server;

import javax.management.ObjectName;
import java.util.ArrayList;

public class Game {
    private final ArrayList<PlayerClient> players;
    private PlayerClient currentPlayer;
    private static Game currentGame;
    private ArrayList<String> wordsUsed = new ArrayList<>();

    private static final String ON_TURN = "on_turn";
    private static final String NOT_ON_TURN = "not_on_turn";
    private static final String COMPLETE_TURN = "complete_turn";
    private static final String ALREADY_GUESSED = "already_guessed";
    private static final String INVALID_WORD = "invalid_word";
    private static final String GAME_OVER = "game_over";

    //Game contructor
    private Game(ArrayList<PlayerClient> players) {
        this.players = players;
        this.currentPlayer = players.get(0);
    }

    public static Game startNewGame(ArrayList<PlayerClient> players){ ;
        currentGame = new Game(players);
        for (PlayerClient client : players) {
            client.notifyClient("Started new game!");
        }
        currentGame.currentPlayer.notifyClient(ON_TURN);
        return currentGame;
    }

    public void giveWord(PlayerClient player, String word){
        if (!player.equals(currentPlayer)){
            player.notifyClient(NOT_ON_TURN);
            return;
        }

        word = word.toLowerCase();

        //Check if answer is valid
        if (wordsUsed.contains(word)){
            player.notifyClient(ALREADY_GUESSED);
            return;
        }

        //Check if valid word
        if (!wordsUsed.isEmpty()){
            int lastIndex = wordsUsed.size() - 1;
            String lastWord = wordsUsed.get(lastIndex);
            String lastChar = lastWord.substring(lastWord.length() - 1);
            if (!word.startsWith(lastChar)){
                player.notifyClient(INVALID_WORD);
                return;
            }
        }

        //Allow word, proceed game
        wordsUsed.add(word);
        player.notifyClient(COMPLETE_TURN);

        //Notify other players
        for (PlayerClient client : players) {
            if (!client.equals(currentPlayer))
                client.notifyClient(player.getPlayerName()+ ": " + word);
        }
        completeTurn();
    }

    private void completeTurn(){
        int currentIndex = players.indexOf(currentPlayer) + 1;
        if (currentIndex >= players.size()) currentIndex = 0;
        currentPlayer = players.get(currentIndex);
        currentPlayer.notifyClient(ON_TURN);
    }

    public static Game getCurrentGame() {
        return currentGame;
    }
}
