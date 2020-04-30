package edu.eskisehir.ceng.tcp_word;

import edu.eskisehir.ceng.tcp_word.client.GameClient;
import edu.eskisehir.ceng.tcp_word.client.Skeleton;
import edu.eskisehir.ceng.tcp_word.server.LobbyServer;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Scanner;

public class App {

    public static GameClient gameClient;
    public static LobbyServer lobbyServer;

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
            System.exit(0);
        }

        //Start game client skeleton
        Skeleton listener = new Skeleton(gameClient, gameClient.getInputStream());
        Thread skeletonThread = new Thread(listener);
        skeletonThread.start();

        gameClient.start();
    }

    private static void createLobby(){
        System.out.print("Please enter your nickname.\n");
        String nicknameInput = prompt();

        //Start server thread
        lobbyServer = new LobbyServer();
        Thread lobbyThread = new Thread(lobbyServer);
        lobbyThread.start();
        String ipStr = "Unknown";

        try {
            InetAddress localHost = InetAddress.getLocalHost();
            Enumeration<NetworkInterface> n = NetworkInterface.getNetworkInterfaces();
            NetworkInterface primaryInterface = n.nextElement();
            Enumeration<InetAddress> inetAddresses = primaryInterface.getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                InetAddress address = inetAddresses.nextElement();
                if (address instanceof Inet4Address) {
                    ipStr = address.getHostAddress();
                }
            }
            //Join own server
            gameClient = new GameClient(localHost, nicknameInput);

        } catch (IOException e) {
            System.out.println("Cannot start lobby server properly");
            System.exit(0);
        }

        //Lobby pane
        System.out.println("Created game lobby at "+ ipStr);
        System.out.println("Waiting players.. Write 'start' to start game.");

        while (true) {
            String userInput = prompt();
            if (userInput.equals("start")) {
                lobbyServer.startGame();
                break;
            }
            else {
                lobbyServer.broadcast(lobbyServer.getAdminClient(), userInput);
            }
        }

    }

    private static void joinLobby(){
        System.out.print("Please enter the network address of the game lobby.\n");
        String ipInput = prompt();
        System.out.print("Please enter a nickname.\n");
        String nicknameInput = prompt();
        try{
            InetAddress address = InetAddress.getByName(ipInput);
            gameClient = new GameClient(address, nicknameInput);
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
