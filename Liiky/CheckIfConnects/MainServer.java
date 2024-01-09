package CheckIfConnects;

import javax.swing.JFrame;

public class MainServer {

    public static void main(String[] args){

        Server server = new Server();

        server.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        server.runServer();

    }

}
