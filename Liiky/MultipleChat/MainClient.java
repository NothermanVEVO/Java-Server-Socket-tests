package MultipleChat;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainClient {

    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException{
        Client client = new Client(new Socket("26.86.248.21", 12345), "Jeremias");
        client.runClient();
    }

}
