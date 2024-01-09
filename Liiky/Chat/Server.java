import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Server extends JFrame{

    JTextField enterField = new JTextField();
    JTextArea displayField = new JTextArea();
    ObjectOutputStream output;
    ObjectInputStream input;
    ServerSocket serverSocket;
    Socket connection;
    int connectionsCount = 1;
    InetAddress address;

    Server(){
        super("Server");

        try {
            address = InetAddress.getLocalHost();
            System.out.println(address.getHostAddress());
        } catch (UnknownHostException e) {
        }

        add(enterField, BorderLayout.SOUTH);
        enterField.setEditable(false);
        enterField.addActionListener(
            new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    sendData(e.getActionCommand());
                    //sendOnly(e.getActionCommand());
                    enterField.setText("");
                }
            }
        );
        add(new JScrollPane(displayField), BorderLayout.CENTER);

        setSize(300, 300);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    public void runServer(){
        try {
            serverSocket = new ServerSocket(12345, 100);
            while (true) {
                try {
                    System.out.println("OI");
                    waitForConnection();
                    getStreams();
                    processConection();
                } catch (Exception e) {
                    displayMessage("\nServer terminated connection");
                    System.out.println("TENTEI");
                } finally{
                    System.out.println("ACABOU");
                    closeConnection();
                    ++connectionsCount;
                }
            }
        } catch (IOException e) {
        }
    }

    public void waitForConnection() throws IOException{
        displayMessage("Waiting for connection\n");
        connection = serverSocket.accept();
        displayMessage("Connection " + connectionsCount + " received from: " 
            + connection.getInetAddress().getHostName());
    }

    public void getStreams() throws IOException{
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        displayMessage("\nGot I/O streams\n");
    }

    public void processConection(){
        String message = "Connection successful";
        sendData(message);
        setTextFieldEditable(true);
        do {
            try {
                //displayMessage("\nSOME");
                message = (String) input.readObject();
                //displayMessage("THING");
                displayMessage("\n" + message);
            } catch (ClassNotFoundException e) {
                displayMessage("\nUnknown object type received");
            } catch (IOException e) {
            }
        } while (!message.equals("Client: TERMINATE"));
    }

    public void closeConnection(){
        displayMessage("\nTerminating connection\n");
        setTextFieldEditable(false);
        try {
            output.close();
            input.close();
            connection.close();
        } catch (IOException e) {
        }
    }

    public void sendData(String message){
        try {
            output.writeObject("Server: " + message);
            output.flush();
            displayMessage("\nServer: " + message);
        } catch (IOException e) {
            displayField.append("Error while SERVER sending message");
        }
    }

    public void sendOnly(String message){
        displayMessage("\nServer: " + message);
    }

    public void displayMessage(final String messageToDisplay){
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                displayField.append(messageToDisplay);
            }
        }
        );
    }

    public void setTextFieldEditable(final boolean editable){
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                enterField.setEditable(editable);
            }
        }
        );
    }

}
