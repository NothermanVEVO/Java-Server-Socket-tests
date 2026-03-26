import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JFrame;

public class MainClient {
    
    static InetAddress address;

    public static void main(String[] args){
        Client client;

        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
        }

        if(args.length == 0){
            client = new Client("10.108.141.52");
        } else {
            client  = new Client(args[0]);
        }

        client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.runClient();

    }

}
