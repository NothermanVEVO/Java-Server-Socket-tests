import java.util.Random;

public class Embaralha{

    public static void main(String[] args){

        int[] cartas = new int[52];

        int numbers = 0;

        for(int i = 0; i < 13; i++){
            cartas[i] = numbers;
            // System.out.print(cartas[i] + " ");
            numbers++;
        }
        // System.out.println();
        numbers = 0;
        for(int i = 13; i < 26; i++){
            cartas[i] = numbers;
            // System.out.print(cartas[i] + " ");
            numbers++;
        }
        // System.out.println();
        numbers = 0;
        for(int i = 26; i < 39; i++){
            cartas[i] = numbers;
            // System.out.print(cartas[i] + " ");
            numbers++;
        }
        // System.out.println();
        numbers = 0;
        for(int i = 39; i < 52; i++){
            cartas[i] = numbers;
            // System.out.print(cartas[i] + " ");
            numbers++;
        }

        imprimirCartas(cartas);
        embaralhar(cartas);
        imprimirCartas(cartas);

    }

    public static void embaralhar(int[] carta){
        Random random = new Random();
        for(int i = 0; i < carta.length; i++){
            int go = random.nextInt(52);
            int temp = carta[go];
            carta[go] = carta[i];
            carta[i] = temp;
        }

    }

    public static void imprimirCartas(int[] carta){
        for(int i = 0; i < carta.length; i++){
            System.out.print(carta[i] + " ");
            if(i == 12 || i == 25 || i == 38 || i == 51){
                System.out.println();
            }
        }
        System.out.println();
    }

}