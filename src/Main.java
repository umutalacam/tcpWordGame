import server.Game;
import server.GameLobbyServer;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner keyboardInput = new Scanner(System.in);

        System.out.println("Welcome to Online TCP word game.\nHost or join a game to play.");
        System.out.println("1) Host Game");
        System.out.println("2) Join Game");

        //Prompt
        while (true){
            System.out.print("-> ");
            String userInput = keyboardInput.nextLine();
            int userChoice;
            try{
                userChoice = Integer.parseInt(userInput);
            }catch (Exception exception){
                System.out.println("Please enter a valid input!");
                continue;
            }

            if (userChoice == 1){
                //Create lobby
                createLobby();
                System.out.println("Lobby created. Waiting players to join.");
            }else if (userChoice == 2){
                //Join game
            }else{
                System.out.println("Invalid choice. Please select 1 or 2.");
            }
        }

    }

    private static void createLobby(){
        GameLobbyServer lobbyServer = new GameLobbyServer();
        Thread lobbyThread = new Thread(lobbyServer);
        lobbyThread.start();
        System.out.println("Created game lobby. Waiting players to join.");
        System.out.println("Press enter to start game");


    }

    private static void joinLobby(){

    }
}
