import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.awt.BorderLayout;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends JFrame{
    
    JTextArea displayText = new JTextArea();
    ServerSocket server;
    Socket cliente;
    int ID = 0;
    
    Server(){
        super("Server do ronaldinho");

        add(new JScrollPane(displayText), BorderLayout.CENTER);
        
        setSize(300, 300);
        setDefaultCloseOperation(3);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void MensagemMotivacional(){
        displayText.append("você não presta\n");
        displayText.append("se mata\n");
    }

    public void IniciarServer(){
        CriarServer();
        MensagemMotivacional();
        EstabelecerConexao();
    }

    public void EstabelecerConexao(){
        while(!server.isClosed()){
            try {
                cliente = server.accept();
                displayText.append("Uma nova conexão foi estabelecida\n");

                ClienteConexao clienteConexao = new ClienteConexao(cliente, ID);
                ID++;
                Thread thread = new Thread(clienteConexao);
                thread.start();
                

            } catch (IOException e) {
            }
        }
    }

    public void CriarServer(){
        displayText.append("Criando um servidor...\n");
        try {
            server = new ServerSocket(12345);
        } catch (IOException e) {
            System.out.println("DEU ERRO PORA");
        }
        displayText.append("Server Criado com Sucesso!!\n");
    }

}
