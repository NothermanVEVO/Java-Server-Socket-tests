import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Server {
    
    static ServerSocket serverSocket;
    static Socket connection;

    static int usersLimit = 100;

    static ArrayList<ServerRunnable> serversRunnables = new ArrayList<>();

    Server(){
        try {
            InetAddress address = InetAddress.getLocalHost();
            System.out.println(address.getHostAddress());
        } catch (UnknownHostException e) {
        }

        try {
            serverSocket = new ServerSocket(12345, 100);
            waitForConnections();
        } catch (Exception e) {
            System.out.println("Algum erro aconteceu. Fechando conexão!");
            System.out.println(e.getMessage());
        } finally{
        }
    }

    public void waitForConnections() throws IOException{
        int usersQuant = 0;
        while(usersQuant < usersLimit){
            System.out.println("Waiting for new connection...");
            
            ServerRunnable serverRunnable = new ServerRunnable(serverSocket.accept(), this, usersQuant);
            serversRunnables.add(serverRunnable);
            new Thread(serverRunnable).start();
            
            String message = "New connection received from: " + serverRunnable.connection.getInetAddress().getHostName();
            receivedMessageFromID(usersQuant, message);
            System.out.println(message);
            
            usersQuant += 1;
        }
    }

    public void receivedMessageFromID(int ID, String message){
        for (ServerRunnable serverRunnable : serversRunnables){
            if(serverRunnable.ID == ID){
                continue;
            }

            serverRunnable.sendData(message);
        }
    }

}
