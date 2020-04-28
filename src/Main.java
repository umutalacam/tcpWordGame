import client.Client;
import server.Game;
import server.GameLobbyServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    private static Client gameClient;

    public static void main(String[] args) {
        Scanner keyboardInput = new Scanner(System.in);

        System.out.println("Welcome to Online TCP word game.\nHost or join a game to play.");
        System.out.println("1) Host Game");
        System.out.println("2) Join Game");
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

        try {
            gameClient.main(null);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void createLobby(){
        GameLobbyServer lobbyServer = new GameLobbyServer();
        Thread lobbyThread = new Thread(lobbyServer);
        lobbyThread.start();
        String ipStr = "Unknown";
        try {
            InetAddress ip = InetAddress.getByName("localhost");
            gameClient = new Client(ip);
            ipStr = ip.getHostAddress();

        } catch (UnknownHostException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        }
        System.out.println("Created game lobby at "+ ipStr);
        System.out.println("Waiting players.. Press enter to start game.");
    }

    private static void joinLobby(){
        Scanner keyboardScn = new Scanner(System.in);
        System.out.print("Please enter the IP address of the game lobby.\n-> ");
        String userInput = keyboardScn.nextLine();
        try{
            InetAddress address = InetAddress.getByName(userInput);
            gameClient = new Client(address);
        } catch (UnknownHostException e) {
            System.out.println("Error: Unknown host.");
            joinLobby();
        } catch (IOException e) {
            System.out.println("Error: Couldn't connect server.");
            e.printStackTrace();
        }
    }
}
