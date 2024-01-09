package VaiQueDa;

import java.awt.BorderLayout;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Server extends JFrame{

    ServerSocket server;
    JTextArea displayText = new JTextArea();
    int ID = 0;

    Server(){

        super("Server");

        add(new JScrollPane(displayText), BorderLayout.CENTER);

        setSize(300, 300);
        setDefaultCloseOperation(3);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    public void runServer(){
        createServer();
        waitForConnections();
    }

    public void createServer(){
        displayText.append("Creating server...\n");
        try {
            server = new ServerSocket(12345);
            displayText.append("Server created!\n");
        } catch (IOException e) {
        }
    }


    public void waitForConnections(){
        while (!server.isClosed()) {
            try {
                Socket client = server.accept();
                displayText.append("A new client has connected!\n");
                ClientHandler clientHandler = new ClientHandler(client, ID);
                ID++;
                Thread thread = new Thread(clientHandler);
                thread.start();
            } catch (IOException e) {
            }
        }
    }

}
