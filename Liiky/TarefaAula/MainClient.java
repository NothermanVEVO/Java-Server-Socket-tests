import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class MainClient {

    static Thread inputThread;
    static ObjectOutputStream output;
    static ObjectInputStream input;
    static String message = "";
    static Socket client;
    static boolean closeInputThread = false;
    
    public static void main(String[] args) {
        try {
            connectToServer("10.108.141.52");
            getStreams();
            startInputThread();
            processConection();
        } catch (Exception e) {
            System.out.println("Client terminated connection.");
        } finally{
            closeConnection();
        }
    }

    public static void connectToServer(String ip) throws IOException{
        System.out.println("Attempting connection...");
        client = new Socket(InetAddress.getByName(ip), 12345);
        System.out.println("Connected to: " + client.getInetAddress().getHostName());
    }

    public static void getStreams() throws IOException{
        System.out.println("Getting I/O streams...");
        output = new ObjectOutputStream(client.getOutputStream());
        output.flush();
        input = new ObjectInputStream(client.getInputStream());
        System.out.println("Got I/O streams");
    }

    public static void startInputThread(){
        closeInputThread = false;

        inputThread = new Thread(){
            @Override
                public void run() {
                    Scanner scanner = new Scanner(System.in);
                    while (!closeInputThread) {
                        String message = scanner.nextLine();
                        sendData(message);
                        if(message.toLowerCase().equals("exit")){
                            closeConnection();
                        }
                    }
                    scanner.close();
                }
            };
        inputThread.start();
    }

    public static void processConection(){
        do {
            try {
                message = (String) input.readObject();
                System.out.println(message);
            } catch (ClassNotFoundException e) {
                System.out.println("Unknown object type received");
            } catch (IOException e) {
            }
        } while (!message.toLowerCase().equals("exit"));
    }

    public static void closeConnection(){
        System.out.println("Closing connection...");
        try {
            if (client != null) {
                output.close();
                input.close();
                client.close();
            }
            closeInputThread = true;
            System.exit(0);
        } catch (IOException e) {
        }
    }

    public static void sendData(String message){
        try {
            output.writeObject(message);
            output.flush();
        } catch (IOException e) {
            System.out.println("Error while CLIENT sending message");
        }
    }

}
