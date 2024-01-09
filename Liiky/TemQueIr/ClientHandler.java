package TemQueIr;

import java.net.Socket;
import java.util.ArrayList;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ClientHandler implements Runnable{

    Socket connection;
    ObjectOutputStream output;
    ObjectInputStream input;
    static ArrayList<ClientHandler> clients = new ArrayList<>();
    String name;

    ClientHandler(Socket socket){

        this.connection = socket;
        try {
            output = new ObjectOutputStream(connection.getOutputStream());
            input = new ObjectInputStream(connection.getInputStream());
            name = (String) input.readObject();
            sendToClient(name + " has joined the battle!\n");
        } catch (IOException | ClassNotFoundException e) {
        }

        clients.add(this);

    }

    @Override
    public void run() {
        while (true) {
            receiveFromClient();
        }
    }

    public void sendToClient(String message){
        try {
            for(int i = 0; i < clients.size(); i++){
                if(!clients.get(i).name.equals(name)){
                    clients.get(i).output.writeObject(message);
                    clients.get(i).output.flush();
                }
            }
        } catch (IOException e) {
        }
    }

    public void receiveFromClient(){

        String message;
        try {
            message = (String) input.readObject();
            sendToClient(message);
        } catch (ClassNotFoundException | IOException e) {
        }

    }

}
