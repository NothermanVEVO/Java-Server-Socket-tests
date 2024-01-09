package MultipleChat;

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
import javax.swing.SwingUtilities;

public class Client extends JFrame{

    Socket client;
    ObjectOutputStream output;
    ObjectInputStream input;
    String name;
    JTextArea displayText = new JTextArea();
    JTextField enterText = new JTextField();

    Client(Socket client, String name){

        super("MultipleChat");

        try {
            this.client = client;
            this.output = new ObjectOutputStream(client.getOutputStream());
            this.input = new ObjectInputStream(client.getInputStream());
            this.name = name;
        } catch (IOException e) {
        }

        add(new JScrollPane(displayText), BorderLayout.CENTER);
        add(enterText, BorderLayout.SOUTH);
        enterText.addActionListener(
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    sendMessage(e.getActionCommand());
                    enterText.setText("");
                }
            }
        );

        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    public void runClient() throws ClassNotFoundException, IOException{
        // displayMessage();
        // while(!client.isClosed()){
        //     String message;
        //     message = (String) input.readObject();
        //     sendMessage(message);
        // }
    }

    public void displayMessage(){
        SwingUtilities.invokeLater(
            new Runnable(){
                @Override
                public void run() {
                    String message;
                    try {
                        message = (String) input.readObject();
                        displayText.append(message);
                    } catch (ClassNotFoundException e) {
                    } catch (IOException e) {
                    }
                }
            }
        );
    }

    public void sendMessage(String message){
        try {
            output.writeObject(name + ": " + message);
            output.flush();
        } catch (IOException e) {
        }
    }

}
