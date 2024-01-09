package MultipleChat;

import java.awt.BorderLayout;
import java.io.IOException;
import java.net.ServerSocket;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Server extends JFrame {

    JTextArea displayText = new JTextArea();
    JTextField enterText = new JTextField();
    ServerSocket server;

    Server(){

        super("MultipleChat Server");

        add(new JScrollPane(displayText), BorderLayout.CENTER);
        // add(enterText, BorderLayout.SOUTH);
        // enterText.addActionListener(
        //     new ActionListener() {
        //         @Override
        //         public void actionPerformed(ActionEvent e) {
        //             displayText.append(e.getActionCommand());
        //             enterText.setText("");
        //         }
        //     }
        // );

        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    public void startServer(){
        try {
            server = new ServerSocket(12345);
        } catch (IOException e) {
        }

        while (!server.isClosed()) {
            try {
                ClientHandler clientHandler = new ClientHandler(server.accept());
                displayText.append("New client: " + 
                    clientHandler.client.getInetAddress().getHostAddress());
                Thread thread = new Thread(clientHandler);
                thread.start();
            } catch (IOException e) {
            }
        }
    }

}
