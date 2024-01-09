package TesteLocoIrado;

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

public class Client extends JFrame{

    Socket client;
    JTextArea displayText = new JTextArea();
    JTextField enterText = new JTextField();
    ObjectOutputStream output;
    ObjectInputStream input;
    String ip;
    String hum;

    Client(String ip){

        super("Client");

        this.ip = ip;

        add(new JScrollPane(displayText), BorderLayout.CENTER);
        add(enterText, BorderLayout.SOUTH);
        enterText.addActionListener(
            new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    String message;
                    message = e.getActionCommand();
                    displayMessage(message);
                    sendToServer(message);
                    enterText.setText("");
                }
            }
        );

        setSize(300, 300);
        setDefaultCloseOperation(3);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    public void runClient(){
        connectToServer();
        getStreams();
        while (client.isConnected()) {
            receiveFromServer();
        }
    }

    public void connectToServer(){
        displayMessage("Connecting to server...");
        try {
            client = new Socket(ip, 12345);
        } catch (IOException e) {
        }
        displayMessage("Connected to server!");
    }

    public void getStreams(){
        try {
            output = new ObjectOutputStream(client.getOutputStream());
            input = new ObjectInputStream(client.getInputStream());
        } catch (IOException e) {
        }
    }

    public void receiveFromServer(){
        try {
            String message;
            message = (String) input.readObject();
            displayText.append(message);
        } catch (ClassNotFoundException | IOException e) {
        }
    }

    public void sendToServer(String message){
        try {
            output.writeObject("Client: " + message + "\n");
            // output.flush();
        } catch (IOException e) {
        }
    }

    public void displayMessage(String message){
        displayText.append("Client: " + message + "\n");
    }

}
