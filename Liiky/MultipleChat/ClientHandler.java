package MultipleChat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{

    Socket client;
    ObjectOutputStream output;
    ObjectInputStream input;
    String clientName;
    ArrayList<ClientHandler> clients = new ArrayList<>();

    ClientHandler(Socket socket){

        this.client = socket;
        try {
            this.output = new ObjectOutputStream(client.getOutputStream());
            output.flush();
            this.input = new ObjectInputStream(client.getInputStream());
            try {
                this.clientName = (String) input.readObject();
            } catch (ClassNotFoundException e) {
                finishClient(client, output, input);
            }
            clients.add(this);
            sendMessage("SERVER: " + clientName + " has entered the chat!");
        } catch (IOException e) {
            finishClient(client, output, input);
        }

    }

    @Override
    public void run() {
        while (!client.isClosed()) {
            String message;
            try {
                message = (String) input.readObject();
                sendMessage(message);
            } catch (ClassNotFoundException e) {
                finishClient(client, output, input);
            } catch (IOException e) {
                finishClient(client, output, input);
            }
        }
    }

    public void sendMessage(String message){
        for(int i = 0; i < clients.size(); i++){
            if(!clients.get(i).clientName.equals(clientName)){
                try {
                    clients.get(i).output.writeObject(message);
                    clients.get(i).output.flush();
                } catch (IOException e) {
                    finishClient(client, output, input);
                }
            }
        }

    }

    public void finishClient(Socket client, ObjectOutputStream output, ObjectInputStream input){
        sendMessage("SERVER: " + clientName + " went to the moon!");
        clients.remove(this);

        try {
            if(output != null){
                output.close();
            }
            if(input != null){
                input.close();
            }
            if(client != null){
                client.close();
            }
        } catch (Exception e) {
        }
    }

}
