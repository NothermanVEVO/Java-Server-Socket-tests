package TesteLocoIrado;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Server extends JFrame implements Runnable{

    ServerSocket server;
    Socket client;
    JTextArea displayText = new JTextArea();
    JTextField enterText = new JTextField();
    int e = 0;
    ObjectOutputStream output;
    ObjectInputStream input;
    String maneiro;

    Server(){
        super("Server");

        add(new JScrollPane(displayText), BorderLayout.CENTER);
        add(enterText, BorderLayout.SOUTH);
        enterText.addActionListener(
            new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    String message;
                    message = e.getActionCommand();
                    displayMessage(message);
                    sendToClient(message);
                    enterText.setText("");
                }
            }
        );

        setSize(300, 300);
        setDefaultCloseOperation(3);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    public void runServer(){
        createServer();
        getStreams();
        while (client.isConnected()) {
            receiveFromClient();
        }

    }

    public void createServer(){
        displayMessage("Waiting for client to connect...");
        try {
            server = new ServerSocket(12345);
            client = server.accept();
        } catch (IOException e) {
        }
        displayMessage("Connection from " + 
            client.getInetAddress().getHostAddress());
        displayMessage("Name = " + client.getInetAddress().getHostName());
    }

    public void getStreams(){
        try {
            output = new ObjectOutputStream(client.getOutputStream());
            input = new ObjectInputStream(client.getInputStream());
        } catch (IOException e) {
        }
        displayMessage("GOT I/O STREAMS");
    }

    public void displayMessage(String message){
        displayText.append("Server: " + message + "\n");
    }

    public void sendToClient(String message){
        try {
            output.writeObject("Server: " + message + "\n");
            // output.flush();
        } catch (IOException e) {
        }
    }

    public void receiveFromClient(){
        try {
            String message;
            message = (String) input.readObject();
            displayText.append(message);
        } catch (ClassNotFoundException e) {
        } catch (IOException e) {
        }
    }

    @Override
    public void run() {
        while (true) {
            System.out.println(e);
            e++;
        }
    }

}
