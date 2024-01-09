package VaiQueDa;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JFrame;

public class Client extends JFrame implements Runnable{

    FPanel fPanel = new FPanel(this);
    DoThing thing = new DoThing();

    Socket connection;
    ObjectOutputStream output;
    ObjectInputStream input;
    int ID;
    String IP;
    String name;

    Client(String ip, String username){

        super("Client");

        this.IP = ip;
        this.name = username;

        add(thing);
        add(fPanel);
        setSize(900, 600);
        setDefaultCloseOperation(3);
        setLocationRelativeTo(null);
        setVisible(true);

        Thread thread = new Thread(thing);
        thread.start();

    }

    public void establishConnection(){
        connectToServer();
        getStreams();
        first();
        run();
    }

    @Override
    public void run() {
        while (connection.isConnected()) {
            getFromServer();
        }
    }

    public void first(){
        try {
            ID = (int) input.readObject();
        } catch (ClassNotFoundException | IOException e) {
        }
    }

    public void connectToServer(){
        try {
            connection = new Socket(IP, 12345);
        } catch (IOException e) {
        }
    }

    public void getStreams(){
        try {
            input = new ObjectInputStream(connection.getInputStream());
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
        } catch (IOException e) {
        }
    }

    public void sendToServer(String thing){
        try {
            output.writeObject(ID);
            output.writeObject(thing);
            output.flush();
        } catch (IOException e) {
        }
    }

    public void getFromServer(){
        String thing;
        try {
            thing = (String) input.readObject();
            System.out.println(thing);
        } catch (ClassNotFoundException | IOException e) {
        }
    }

}
