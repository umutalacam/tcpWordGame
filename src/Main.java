import client.GameClient;
import client.Skeleton;
import server.Game;
import server.LobbyServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Main {

    private static GameClient gameClient;

    public static void main(String[] args) {
        Scanner keyboardInput = new Scanner(System.in);

        System.out.println("Welcome to Online TCP word game.\nHost or join a game to play.");
        System.out.println("[1] Host Game");
        System.out.println("[2] Join Game");
        int menuChoice;

        //Prompt
        while (true){
            System.out.print("-> ");
            String userInput = keyboardInput.nextLine();

            try{
                menuChoice = Integer.parseInt(userInput);
                break;
            }catch (Exception exception){
                System.out.println("Please enter a valid input!");
                continue;
            }
        }

        if (menuChoice == 1){
            //Create lobby
            createLobby();
        }else if (menuChoice == 2){
            //Join game
            joinLobby();
        }else{
            System.out.println("Invalid choice. Please select 1 or 2.");
        }

        //Start game client skeleton
        Skeleton listener = new Skeleton(gameClient, gameClient.getInputStream());
        Thread skeletonThread = new Thread(listener);
        skeletonThread.start();
    }

    private static void createLobby(){
        LobbyServer lobbyServer = new LobbyServer();
        Thread lobbyThread = new Thread(lobbyServer);
        lobbyThread.start();
        String ipStr = "Unknown";
        try {
            InetAddress ip = InetAddress.getByName("localhost");
            gameClient = new GameClient(ip);
            ipStr = ip.getHostAddress();

        } catch (IOException e) {
            e.printStackTrace();

        }
        System.out.println("Created game lobby at "+ ipStr);
        System.out.println("Waiting players.. Write 'start' to start game.");
        while (true) {
            String userInput = prompt();
            if (userInput.equals("start")) {
                lobbyServer.startGame();
                break;
            }
        }

    }

    private static void joinLobby(){

        System.out.print("Please enter the IP address of the game lobby.\n");
        String userInput = prompt();
        try{
            InetAddress address = InetAddress.getByName(userInput);
            gameClient = new GameClient(address);
        } catch (UnknownHostException e) {
            System.out.println("Error: Unknown host.");
            joinLobby();
        } catch (IOException e) {
            System.out.println("Error: Couldn't connect server.");
            e.printStackTrace();
        }
    }


    public static String prompt(){
        Scanner keyboardScn = new Scanner(System.in);
        System.out.print("->");
        return keyboardScn.nextLine();
    }

}
