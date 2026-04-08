import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerRunnable implements Runnable {
    
    Server server;

    ObjectOutputStream output;
    ObjectInputStream input;
    Socket connection;

    int ID;

    ServerRunnable(Socket connection, Server server, int ID){
        this.connection = connection;
        this.server = server;
        this.ID = ID;
    }

    @Override
    public void run() {
        try {
            getStreams();
            processConection();
        } catch (Exception e) {
            System.out.println("Algum erro aconteceu. Fechando conexão!");
            System.out.println(e.getMessage());
        } finally{
            closeConnection();
        }
    }

    public void getStreams() throws IOException{
        System.out.println("Getting I/O streams...");
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        System.out.println("Got I/O streams");
    }

    public void processConection(){
        String message = "Connection successful!";
        sendData("Server: " + message);
        System.out.println("Server: " + message);
        do {
            try {
                message = (String) input.readObject();
                System.out.println(message);
                receiveData(message);
            } catch (ClassNotFoundException e) {
                System.out.println("Unknown object type received");
            } catch (IOException e) {
            }
        } while (!message.toLowerCase().equals("exit"));
    }

    public void closeConnection(){
        System.out.println("Terminating connection");
        try {
            output.close();
            input.close();
            connection.close();
        } catch (IOException e) {
        }
    }

    public void receiveData(String message){
        if(message.equals("exit")){
            return;
        }

        server.receivedMessageFromID(ID, message);

    }

    public void sendData(String message){
        try {
            output.writeObject(message);
            output.flush();
        } catch (IOException e) {
            System.out.println("Error while SERVER sending message");
        }
    }

}
