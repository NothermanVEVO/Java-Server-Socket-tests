package TicTacToe;

import java.awt.BorderLayout;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.net.Socket;
import java.util.Formatter;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class Server extends JFrame{

    String[] board = new String[9];
    JTextArea displayText = new JTextArea();
    Player[] players = new Player[2];
    ServerSocket server;
    int currentPlayer;
    final int playerX = 0;
    final int playerO = 1;
    final String[] MARK = {"X", "O"};
    ExecutorService runGame;
    Lock gameLock;
    Condition otherPlayerConnected;
    Condition otherPlayerTurn;

    Server(){
        super("Tic Tac Toe Server");

        runGame = Executors.newFixedThreadPool(2);
        gameLock = new ReentrantLock();
        otherPlayerConnected = gameLock.newCondition();
        otherPlayerTurn = gameLock.newCondition();

        for(int i = 0; i < 9; i++){
            board[i] = "";
        }
        currentPlayer = playerX;

        try {
            server = new ServerSocket(12345, 2);
        } catch (IOException e) {
        }

        add(displayText, BorderLayout.CENTER);
        displayText.append("Waiting for both players to connect...\n");

        setSize(300, 300);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    public void execute(){

        for(int i = 0; i < players.length; i++){
            try {
                players[i] = new Player(server.accept(), i);
                runGame.execute(players[i]);
            } catch (IOException e) {
            }
        }
        gameLock.lock();

        try {
            players[playerX].setSuspended(false);
            otherPlayerConnected.signal();
        } finally {
            gameLock.unlock();
        }

    }

    public void displayMessage(String message){

        SwingUtilities.invokeLater(
            new Runnable(){
                public void run(){
                    displayText.append(message);
                }
            }
        );

    }

    public boolean validateAndMove(int location, int player){

        while (player != currentPlayer) {
            gameLock.lock();
            try {
                otherPlayerTurn.await();
            } catch (InterruptedException e) {
            } finally{
                gameLock.unlock();
            }
        }

        if(!isOccupied(location)){
            board[location] = MARK[currentPlayer];
            currentPlayer = (currentPlayer + 1) % 2;

            players[currentPlayer].otherPlayerMoved(location);
            gameLock.lock();
            try{
                otherPlayerTurn.signal();
            } finally {
                gameLock.unlock();
            }

            return true;
        } else {
            return false;
        }

    }

    public boolean isOccupied(int location){
        if(board[location].equals(MARK[playerX]) || board[location].equals(MARK[playerO])){
            return true;
        } else{
            return false;
        }
    }

    public boolean isGameOver(){
        return false;
    }

    public class Player implements Runnable{

        Socket client;
        Scanner input;
        Formatter output;
        int playerNumber;
        String mark;
        boolean suspend = true;

        Player(Socket socket, int number){

            playerNumber = number;
            mark = MARK[playerNumber];
            client = socket;

            try {
                input = new Scanner(client.getInputStream());
                output = new Formatter(client.getOutputStream());
            } catch (IOException e) {   
            }

        }

        public void otherPlayerMoved(int location){
            output.format("Opponent moved\n");
            output.format("%d\n", location);
            output.flush();
        }

        @Override
        public void run() {
            try{
                displayMessage("Player" + mark + "connected\n");
                output.format("%s\n", mark);
                output.flush();

                if(playerNumber == playerX){
                    output.format("%s\n%s","Player X connected", "Waiting for another player\n");
                    output.flush();

                    gameLock.lock();

                    try{
                        while(suspend){
                            otherPlayerConnected.await();
                        }
                    }catch(InterruptedException exception){
                        exception.printStackTrace();
                    }
                    finally
                    {
                        gameLock.unlock();
                    }
                    output.format("Other player connected. Your move.\n");
                    output.flush();
                } else {
                    output.format("Player O connected. please wait\n");
                    output.flush();
                } 

                while(!isGameOver()){
                    int location = 0;

                    if(input.hasNext())
                    location = input.nextInt();

                    if(validateAndMove(location, playerNumber)){
                        displayMessage("\nlocation: "+ location);
                        output.format("Valid move.\n");
                        output.flush();
                    } else {
                        output.format("Invalid move, try again!\n");
                        output.flush();
                    }
                }
            } finally{
                try{
                    client.close();
                }catch(IOException ioException){
                    ioException.printStackTrace();
                    System.exit(1);
                }
            }
        }

        public void setSuspended(boolean status){
            suspend = status;
        }

    }

}
