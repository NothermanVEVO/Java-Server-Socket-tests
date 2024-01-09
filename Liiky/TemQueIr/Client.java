package TemQueIr;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Client extends JFrame implements Runnable{

    Socket connection;
    ObjectOutputStream output;
    ObjectInputStream input;
    JTextArea displayText = new JTextArea();
    JTextField enterText = new JTextField();
    String IP;
    String name;

    Client(String ip, String username){
        super("Client");

        this.IP = ip;
        this.name = username;

        add(new JScrollPane(displayText), BorderLayout.CENTER);
        add(enterText, BorderLayout.SOUTH);
        enterText.addActionListener(
            new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    String message;
                    message = e.getActionCommand();
                    sendToServer(message);
                    displayMessage(message + "\n");
                    enterText.setText("");
                }
            }
        );

        setSize(300, 300);
        setDefaultCloseOperation(3);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    public void startClient(){

        createConnection();
        getStreams();
        first();
        run();

    }

    public void createConnection(){
        // displayText.append("Connecting to server..." + "\n");
        try {
            connection = new Socket(IP, 12345);
            displayText.append("Connected to server!" + "\n");
        } catch (IOException e) {
        }
    }

    public void getStreams(){
        // displayText.append("Getting I/O..." + "\n");
        try {
            output = new ObjectOutputStream(connection.getOutputStream());
            input = new ObjectInputStream(connection.getInputStream());
            // displayText.append("Successfuly got I/O" + "\n");
        } catch (IOException e) {
        }
    }

    public void first(){
        try {
            output.writeObject(name);
        } catch (IOException e) {
        }
    }

    public void sendToServer(String message){
        try {
            output.writeObject(name + ": " + message + "\n");
        } catch (IOException e) {
        }
    }

    public void receiveFromServer(){

        String message;
        try {
            message = (String) input.readObject();
            displayText.append(message);
        } catch (ClassNotFoundException | IOException e) {
        }

    }

    public void displayMessage(String message){
        displayText.append(name + ": " + message);
    }

    @Override
    public void run() {

        while (connection.isConnected()) {
            receiveFromServer();
        }

    }

}
