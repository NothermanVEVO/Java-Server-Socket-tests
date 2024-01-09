package TicTacToe;

import javax.swing.JFrame;

public class MainClient {

    public static void main(String[] args) {
        Client application; // declara o aplicativo cliente

        // se não houver nenhum argumento de linha de comando
        if (args.length == 0)
            application = new Client("26.86.248.21"); // host local
        else
            application = new Client(args[0]); // utiliza argumentos

        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

}
