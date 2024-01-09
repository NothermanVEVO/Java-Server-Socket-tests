package TesteCartasMeuDeus;

import java.util.Random;

public class Mesa{

    Cartas[][] cartas = new Cartas[4][13];

    Mesa(){
    }

    public void criarMesa(){

        criarBaralho();
        mostrarCartas();
        embaralharCartas(cartas);
        System.out.println();
        mostrarCartas();
    }

    public void embaralharCartas(Cartas[][] cartas){
        Cartas temporario = new Cartas();
        Random random = new Random();
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 13; j++){
                int goI = random.nextInt(4);
                int goJ = random.nextInt(13);
                temporario = cartas[goI][goJ];
                cartas[goI][goJ] = cartas[i][j];
                cartas[i][j] = temporario;
            }
        }
    
    }

    public void criarBaralho(){
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 13; j++){
                cartas[i][j] = new Cartas();
            }
        }

        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 13; j++){
                cartas[i][j].naipeD = cartas[i][j].naipe[i];
                cartas[i][j].numeroD = cartas[i][j].numero[j];
            }
        }
    }

    public void mostrarCartas(){
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 13; j++){
                System.out.print(cartas[i][j].naipeD + "" + cartas[i][j].numeroD + " ");
            }
            System.out.println();
        }
    }

    public void embaralharTeste(int[] carta){
        Random random = new Random();
        for(int i = 0; i < carta.length; i++){
            int go = random.nextInt(52);
            int temp = carta[go];
            carta[go] = carta[i];
            carta[i] = temp;
        }
    }

    public static void main(String [] args){
        Mesa mesa = new Mesa();
        mesa.criarMesa();
    }

}
