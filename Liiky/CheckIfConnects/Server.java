package CheckIfConnects;

import java.awt.BorderLayout;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Server extends JFrame{

    JTextArea displayText = new JTextArea();
    ObjectOutputStream output;
    ObjectInputStream input;
    ServerSocket server;
    Socket client;
    InetAddress address; 
    boolean canRun = true;

    Server(){

        super("Server");

        add(new JScrollPane(displayText), BorderLayout.CENTER);
        setSize(300, 300);
        setVisible(true);

    }

    public void runServer(){

        try {
            server = new ServerSocket(12345);
            address = InetAddress.getLocalHost();
            try {
                while (canRun) {
                    displayMessage("Server created!");
                    displayMessage("Server ip: " + address.getHostAddress());
                    waitForConnection();
                    getStreams();
                    processConection();
                }
            } catch (Exception e) {
                displayMessage("\nServer ended connection!");
            } finally{
                closeConnection();
            }
        } catch (IOException e) {
        }

    }

    public void waitForConnection() throws IOException{

        displayMessage("Waiting for connection...");
        // client = server.accept();
        displayMessage("Connection received from: " + "\"" 
            + client.getInetAddress().getHostAddress() + "\"");
    }

    public void getStreams() throws IOException{

        output = new ObjectOutputStream(client.getOutputStream());
        input = new ObjectInputStream(client.getInputStream());
        output.flush();
        displayMessage("Got I/O streams!\n");

    }

    public void processConection(){

        String message = "";
        sendData("Connection successful!!!");

        do {
            try {
                message = (String) input.readObject();
                displayMessage(message);
            } catch (ClassNotFoundException e) {
                displayMessage("Unknow object type received");
            } catch (IOException e) {
            }
        } while (!message.equals("Server: END"));

    }

    public void closeConnection() throws IOException{

        displayMessage("Closing connection");

        output.close();
        input.close();
        client.close();

    }

    public void sendData(String message){



    }

    public void displayMessage(String message){
        displayText.append(message + "\n");
    }

}
