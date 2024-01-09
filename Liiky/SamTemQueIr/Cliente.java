import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Cliente extends JFrame implements Runnable{
    
    Socket conexao;
    ObjectInputStream entrada;
    ObjectOutputStream saida;
    String ip;
    String nome;
    JTextArea mostrarTexto = new JTextArea();
    JTextField entrarTexto = new JTextField();
    int ID;

    Cliente(String ip, String usuario){
        super("Jogador do Flamengo");

        this.ip = ip;
        this.nome= usuario;

        add(new JScrollPane(mostrarTexto), BorderLayout.CENTER);
        add(entrarTexto, BorderLayout.SOUTH);

        entrarTexto.addActionListener(
            new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    String messagem;
                    messagem = e.getActionCommand();
                    MostrarMessagem(messagem);
                    EnviarServer(messagem);
                    entrarTexto.setText("");
                }
                
            }
        );
        setSize(500, 500);
        setDefaultCloseOperation(3);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void CriarConexao(){
        mostrarTexto.append("Conectando ao servidor...\n");
        try {
            conexao = new Socket(ip, 12345);
            mostrarTexto.append("Conectado ao Servidor!\n");
        } catch (IOException e) {
        }
        
    }

    public void PrimeiraVez(){
        try {
            ID = (int) entrada.readObject();
            mostrarTexto.append("ID: "+ID+"\n");
        } catch (ClassNotFoundException | IOException e) {
        }
    }

    public void PegarES(){
        mostrarTexto.append("Pegando E/S...\n");
        try {
            entrada = new ObjectInputStream(conexao.getInputStream());
            saida = new ObjectOutputStream(conexao.getOutputStream());
            mostrarTexto.append("E/S recebida com sucesso!!\n");
        } catch (IOException e) {
        }
    }

    public void ReceberServer(){
        String messagem;
        try {
            messagem = (String) entrada.readObject();
            mostrarTexto.append(messagem);
        } catch (ClassNotFoundException | IOException e) {
        }
    }

    public void EnviarServer(String mensagem){
        try {
            saida.writeObject(ID);
            saida.writeObject(nome+": "+mensagem+"\n");
            
        } catch (IOException e) {
        }
    }

    public void MostrarMessagem(String Messagem){
        mostrarTexto.append(nome +": "+Messagem+"\n");
    }


    @Override
    public void run() {
        CriarConexao();
        PegarES();
        PrimeiraVez();
        while(conexao.isConnected()){
            ReceberServer();
        }
    }
    
}
