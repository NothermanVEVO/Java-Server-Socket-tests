package TesteCartasMeuDeus;

public class Cartas{
    //naipe + numero

    String[] naipe = new String[4];
    String[] numero = new String[13];

    String naipeD;
    String numeroD;
    int ID;

    Cartas(){

        for(int i = 0; i < 4; i++){
            naipe[i] = "";
        }

        for(int i = 0; i < 13; i++){
            numero[i] = "";
        }

        //Criar naipes
        naipe[0] = "Ouro";
        naipe[1] = "Copas";
        naipe[2] = "Damas";
        naipe[3] = "Paus";

        //Criando o numero da carta
        numero[0] = "As";
        numero[1] = "Dois";
        numero[2] = "Tres";
        numero[3] = "Quatro";
        numero[4] = "Cinco";
        numero[5] = "Seis";
        numero[6] = "Sete";
        numero[7] = "Oito";
        numero[8] = "Nove";
        numero[9] = "Dez";
        numero[10] = "Q";
        numero[11] = "J";
        numero[12] = "K";
    }

    //pegar carta
    public void getCarta(){
        System.out.println("");
    }

    public void criarBaralho(){
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 13; j++){
                
            }
        }
    }

}