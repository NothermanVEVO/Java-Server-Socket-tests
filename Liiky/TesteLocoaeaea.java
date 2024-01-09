public class TesteLocoaeaea implements Runnable {

    static int e = 0;

    public static void main(String[] args){
        Thread thread = new Thread(new TesteLocoaeaea());
        thread.start();
        while (true) {
            System.out.println(e);
        }
    }

    @Override
    public void run() {
        while (true) {
            e++;
        }
    }

}
