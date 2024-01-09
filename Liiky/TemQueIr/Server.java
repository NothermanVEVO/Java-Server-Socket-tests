package TemQueIr;

import java.awt.BorderLayout;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Server extends JFrame{

    JTextArea displayText = new JTextArea();
    ServerSocket server;
    Socket client;
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

        while (!server.isClosed()) {
            try {
                Socket client = server.accept();
                ClientHandler clientHandler = new ClientHandler(client);
                Thread thread = new Thread(clientHandler);
                thread.start();
                displayText.append("A new client has joined!" + "\n");
            } catch (IOException e) {
            }
        }

    }

    public void createServer(){

        displayText.append("Creating server..." + "\n");
        try {
            server = new ServerSocket(12345);
        } catch (IOException e) {
        }
        displayText.append("Server created!" + "\n");

    }

}