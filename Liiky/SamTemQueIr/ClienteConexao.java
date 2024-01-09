import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClienteConexao implements Runnable{

    Socket conexao;
    ObjectOutputStream saida;
    ObjectInputStream entrada;

    static ArrayList<ClienteConexao> clientes = new ArrayList<>();
    String nomeCliente;
    int ID;
    
    ClienteConexao(Socket cliente, int ID){
        this.conexao = cliente;
        this.ID = ID;
        try {
            saida = new ObjectOutputStream(conexao.getOutputStream());
            entrada = new ObjectInputStream(conexao.getInputStream());
        } catch (IOException e) {
        }

        PrimeiraVez();

        clientes.add(this);
    }

    @Override
    public void run() {
        while(conexao.isConnected()){
            ReceberCliente();

        }
        

        // while (true) {
        //     System.out.println(clientes.size());
        // }
    }

    public void PrimeiraVez(){
        try {
            saida.writeObject(ID);
        } catch (IOException e) {
        }
    }

    public void EnviarCliente(String messagem){
        try {
            for(int i=0;i<clientes.size();i++){
                if(!(ID==i)){
                    clientes.get(i).saida.writeObject(messagem);
                    clientes.get(i).saida.flush();
                }else{
                    System.out.println("nada ocorre");
                }
            
            }
            
        } catch (IOException e) {
        }

    }

    public void ReceberCliente(){
        String mensagem;

        try {
            ID= (int) entrada.readObject();
            System.out.println(ID);
            mensagem = (String) entrada.readObject();
            EnviarCliente(mensagem);
        } catch (ClassNotFoundException | IOException e) {
        }
    }
}
