package edu.eskisehir.ceng.tcp_word.server;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Game {
    private final ArrayList<PlayerClient> players;
    private PlayerClient currentPlayer;
    private static Game currentGame;
    private ArrayList<String> wordsUsed;
    private Timer countDownTimer;

    private static final String ON_TURN = "on_turn";
    private static final String NOT_ON_TURN = "not_on_turn";
    private static final String COMPLETE_TURN = "complete_turn";
    private static final String ALREADY_GUESSED = "already_guessed";
    private static final String INVALID_WORD = "invalid_word";
    private static final String GAME_OVER = "game_over";
    private static final String WIN = "win";

    //Game constructor
    private Game(ArrayList<PlayerClient> players) {
        this.players = players;
        this.currentPlayer = players.get(players.size()-1);
        this.countDownTimer = new Timer();
        this.wordsUsed = new ArrayList<>();
    }

    public static Game startNewGame(ArrayList<PlayerClient> players){ ;
        currentGame = new Game(players);
        for (PlayerClient client : players) {
            //Send banner
            client.notifyClient("Started new game!\n" +
                    "---------------------\n" +
                    " * You have 10 seconds to guess each words. When time out, you lose the game.\n" +
                    " * Each word' s first character need to be same with the previous word's last character.\n" +
                    " * Wait for 'Your Turn' command.\n" +
                    "---------------------");
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
            String lastChar = lastWord.substring(lastWord.length() - 2);
            if (!word.startsWith(lastChar)){
                player.notifyClient(INVALID_WORD);
                return;
            }
        }

        //Allow word, proceed game
        stopCountdown();
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
        startCountdown();
    }

    private void gameOver(PlayerClient client){
        int clientIndex = players.indexOf(client);
        if (clientIndex != -1){
            players.remove(clientIndex);
            //Notify other players
            for (PlayerClient player : players) {
                player.notifyClient(client.getPlayerName() + " defeated! (Time out)");
            }
            if (players.size() == 1) {
                players.get(0).notifyClient(WIN);
            }
            else if (clientIndex >= players.size()) {
                clientIndex = 0;
                currentPlayer = players.get(clientIndex);
                currentPlayer.notifyClient(ON_TURN);
            }
        }
    }

    private void startCountdown(){
        try {
            countDownTimer = new Timer();
            countDownTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    currentPlayer.notifyClient(GAME_OVER);
                    gameOver(currentPlayer);
                }
            }, 10000);
        } catch (IllegalStateException ignore){

        }
    }

    private void stopCountdown(){
        try {
            countDownTimer.cancel();
            countDownTimer.purge();
        } catch (IllegalStateException ignore){
            System.out.println("err");
        }
    }

    public static Game getCurrentGame() {
        return currentGame;
    }
}
