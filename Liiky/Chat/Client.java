import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Client extends JFrame{

    JTextField enterField = new JTextField();
    JTextArea displayField = new JTextArea();
    ObjectOutputStream output;
    ObjectInputStream input;
    String message = "";
    String chatServer;
    Socket client;

    Client(String host){
        super("Client");

        chatServer = host;

        enterField.setEditable(false);
        enterField.addActionListener(
            new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    sendData(e.getActionCommand());
                    enterField.setText("");
                }
            }
        );
        add(enterField, BorderLayout.SOUTH);
        add(new JScrollPane(displayField), BorderLayout.CENTER);

        setSize(300, 300);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    public void runClient(){
                try {
                    System.out.println("OI");
                    connectToServer();
                    getStreams();
                    processConection();
                } catch (Exception e) {
                    displayMessage("\nClient terminated connection");
                    System.out.println("TENTEI");
                } finally{
                    System.out.println("ACABOU");
                    closeConnection();
                }
    }

    public void connectToServer() throws IOException{
        displayMessage("Attempting connection\n");
        client = new Socket(InetAddress.getByName(chatServer), 12345);
        displayMessage("Connected to: " + client.getInetAddress().getHostName());
    }

    public void getStreams() throws IOException{
        output = new ObjectOutputStream(client.getOutputStream());
        output.flush();
        input = new ObjectInputStream(client.getInputStream());
        displayMessage("\nGot I/O streams\n");
    }

    public void processConection(){
        setTextFieldEditable(true);
        do {
            try {
                message = (String) input.readObject();
                displayMessage("\n" + message);
            } catch (ClassNotFoundException e) {
                displayMessage("\nUnknown object type received");
            } catch (IOException e) {
            }
        } while (!message.equals("Server: TERMINATE"));
    }

    public void closeConnection(){
        displayMessage("\nClosing connection\n");
        setTextFieldEditable(false);
        try {
            output.close();
            input.close();
            client.close();
        } catch (IOException e) {
        }
    }

    public void sendData(String message){
        try {
            output.writeObject("Client: " + message);
            output.flush();
            displayMessage("\nClient: " + message);
        } catch (IOException e) {
            displayField.append("Error while CLIENT sending message");
        }
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
