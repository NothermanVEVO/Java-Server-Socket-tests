import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainServer {

    static ObjectOutputStream output;
    static ObjectInputStream input;
    static ServerSocket serverSocket;
    static Socket connection;

    public static void main(String[] args) {
        
        try {
            InetAddress address = InetAddress.getLocalHost();
            System.out.println(address.getHostAddress());
        } catch (UnknownHostException e) {
        }

        try {
            serverSocket = new ServerSocket(12345, 100);
            waitForConnection();    
            getStreams();
            processConection();
        } catch (Exception e) {
            System.out.println("Algum erro aconteceu. Fechando conexão!");
            System.out.println(e.getMessage());
        } finally{
            closeConnection();
        }

    }

    public static void waitForConnection() throws IOException{
        System.out.println("Waiting for connection...");
        connection = serverSocket.accept();
        System.out.println("Connection received from: " + connection.getInetAddress().getHostName());
    }

    public static void getStreams() throws IOException{
        System.out.println("Getting I/O streams...");
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        System.out.println("Got I/O streams");
    }

    public static void processConection(){
        String message = "Connection successful!";
        sendData(message);
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

    public static void closeConnection(){
        System.out.println("Terminating connection");
        try {
            output.close();
            input.close();
            connection.close();
        } catch (IOException e) {
        }
    }

    public static void receiveData(String message){
        if(message.equals("exit")){
            return;
        }
        
        if(message.matches("^\\d+ +[+\\-*/] +\\d+$")){
            message = message.replaceAll("\\s+", " ");
            String[] values = message.split(" ");
            float n1 = Float.valueOf(values[0]);
            String op = values[1];
            float n2 = Float.valueOf(values[2]);
            
            float result = 0;

            if(op.equals("+")){
                result = n1 + n2;
            } else if(op.equals("-")){
                result = n1 - n2;
            } else if(op.equals("*")){
                result = n1 * n2;
            } else{ // "/"
                result = n1 / n2;
            }

            sendData("" + result);

        } else if(message.matches("^.+;.+$")){
            String[] values = message.split(";");
            String str1 = values[0];
            String str2 = values[1];
            int index = str2.indexOf(str1);
            if(index != -1){
                sendData("Foi encontrada no index: " + index);
            } else{
                sendData("Não foi possível achar a primeira String dentro da segunda String!");
            }
        }
        
        else{
            sendData("String inválida!");
        }
    }

    public static void sendData(String message){
        try {
            output.writeObject("Server: " + message);
            output.flush();
            System.out.println("Server: " + message);
        } catch (IOException e) {
            System.out.println("Error while SERVER sending message");
        }
    }

}
