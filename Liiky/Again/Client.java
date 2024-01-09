package Again;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

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
                    // sendData(e.getActionCommand());
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

    }

}
