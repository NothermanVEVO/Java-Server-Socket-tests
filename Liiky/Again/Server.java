package Again;

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

public class Server extends JFrame{

    JTextArea displayText = new JTextArea();
    JTextField enterText = new JTextField();
    ObjectOutputStream output;
    ObjectInputStream input;
    ServerSocket server;
    Socket client;

    Server(){

        super("Chat: Server");

        add(new JScrollPane(displayText), BorderLayout.CENTER);
        add(enterText, BorderLayout.SOUTH);
        enterText.addActionListener(
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        sendData(e.getActionCommand());
                        enterText.setText("");
                    } catch (IOException e1) {
                    }
                }
            }
        );
        this.setSize(300, 300);
        this.setDefaultCloseOperation(3);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

    }

    public void runServer(){
        waitForConnection();
        try {
            getStreams();
        } catch (IOException e) {
        }
        try {
            processConection();
        } catch (ClassNotFoundException e) {
        } catch (IOException e) {
        }
    }

    public void waitForConnection(){
        displayMessage("Waiting for connections...");
        try {
            client = server.accept();
            sendData("Connection accepted!");
        } catch (IOException e) {
        }
        displayMessage("Connection from IP: " + client.getInetAddress().getHostAddress());
        displayMessage("It's name is: " + client.getInetAddress().getHostName());
    }

    public void getStreams() throws IOException{
        output = new ObjectOutputStream(client.getOutputStream());
        input = new ObjectInputStream(client.getInputStream());
        displayMessage("Got I/O streams!");
    }

    public void processConection() throws ClassNotFoundException, IOException{
        sendData("Connection fully successful!!!!");
        while (client.isConnected()) {
            String message;
            message = (String) input.readObject();
            sendData(message);
        }
    }

    public void sendData(String message) throws IOException{
        output.writeObject("Server: " + message);
        output.flush();
        displayMessage(message);
    }

    public void displayMessage(String message){
        displayText.append("SERVER: " + message);
    }

}
