package VaiQueDa;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{

    Socket connection;
    static ArrayList<ClientHandler> clients = new ArrayList<>();
    ObjectOutputStream output;
    ObjectInputStream input;
    int ID;

    ClientHandler(Socket client, int id){
        this.connection = client;
        this.ID = id;
        try {
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            output.writeObject(ID);
        } catch (IOException e) {
        }
        clients.add(this);
    }

    @Override
    public void run() {
        while (connection.isConnected()) {
            receiveFromClient();
        }
    }

    public void receiveFromClient(){
        String thing;
        try {
            ID = (int) input.readObject();
            System.out.println(ID);
            thing = (String) input.readObject();
            System.out.println(thing);
            sendToClient(thing, ID);
        } catch (ClassNotFoundException | IOException e) {
        }
    }

    public void sendToClient(String thing, int id){
        try {
            for(int i = 0; i < clients.size(); i++){
                if(!(id == i)){
                    clients.get(i).output.writeObject(thing);
                    clients.get(i).output.flush();
                }
            }
        } catch (IOException e) {
        }
    }

}
