package TicTacToe;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Formatter;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Client extends JFrame implements Runnable {

    private JTextField idField; // campo de texto para exibir a marca do jogador
    private JTextArea displayArea; // JTextArea para exibir a saída
    private JPanel boardPanel; // painel para o tabuleiro do jogo da velha
    private JPanel panel2; // painel para conter o tabuleiro
    private Square[][] board; // tabuleiro do jogo da velha
    private Square currentSquare; // quadrado atual
    private Socket connection; // conexão com o servidor
    private Scanner input; // entrada a partir do servidor
    private Formatter output; // saída para o servidor
    private String ticTacToeHost; // nome do host para o servidor
    private String myMark; // marca desse cliente
    private boolean myTurn; // determina de qual cliente é a vez
    private final String X_MARK = "X"; // marca para o primeiro cliente
    private final String O_MARK = "O"; // marca para o segundo cliente

    Client(String host) {
        ticTacToeHost = host; // configura o nome do servidor
        displayArea = new JTextArea(4, 30); // configura JTextArea
        displayArea.setEditable(false);
        add(new JScrollPane(displayArea), BorderLayout.SOUTH);
        boardPanel = new JPanel(); // configura o painel para os quadrados no tabuleiro
        boardPanel.setLayout(new GridLayout(3, 3, 0, 0));
        board = new Square[3][3]; // cria o tabuleiro
        // faz um loop pelas linhas no tabuleiro
        for (int row = 0; row < board.length; row++) {
            // faz um loop pelas colunas no tabuleiro
            for (int column = 0; column < board[row].length; column++) {
                // cria um quadrado
                board[row][column] = new Square(" ", row * 3 + column);
                boardPanel.add(board[row][column]); // adiciona um quadrado
            } // fim do for interno
        } // fim do for externo
        idField = new JTextField(); // configura o campo de texto
        idField.setEditable(false);
        add(idField, BorderLayout.NORTH);

        panel2 = new JPanel(); // configure o painel que irá conter o boardPanel
        panel2.add(boardPanel, BorderLayout.CENTER); // adiciona o painel do tabuleiro
        add(panel2, BorderLayout.CENTER); // adiciona o painel contêiner
        setSize(300, 225); // configura o tamanho da janela
        setVisible(true); // mostra a janela
        startClient();
    }

    public void startClient() {
        // conecta-se ao servidor e obtém fluxos
        try {
            // faz uma conexão com o servidor
            connection = new Socket(
                    InetAddress.getByName(ticTacToeHost), 12345);
            // obtém os fluxos de entrada e saída
            input = new Scanner(connection.getInputStream());
            output = new Formatter(connection.getOutputStream());
        } // fim do try
        catch (IOException ioException) {
            ioException.printStackTrace();
        } // fim do catch
          // cria e inicia a thread de trabalhador para esse cliente
        ExecutorService worker = Executors.newFixedThreadPool(1);
        worker.execute(this); // executa o cliente
    } // fim do método startClient

    public void run() {
        myMark = input.nextLine(); // obtém a marca do jogador (X ou O)

        SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() {
                        idField.setText("You are player \"" + myMark + "\"");
                    } // fim do método run
                } // fim da classe interna anônima
        ); // fim da chamada para SwingUtilities.invokeLater

        myTurn = (myMark.equals(X_MARK)); // determina se a vez do cliente

        // recebe as mensagens enviadas para o cliente e gera saída delas
        while (true) {
            if (input.hasNextLine())
                processMessage(input.nextLine());
        } // fim do while
    }

    private void processMessage(String message) {
        // ocorreu uma jogada válida
        if (message.equals("Valid move.")) {
            displayMessage("Valid move, please wait.\n");
            setMark(currentSquare, myMark); // configura a marca no quadrado
        } // fim do if
        else if (message.equals("Invalid move, try again")) {
            displayMessage(message + "\n"); // exibe jogada inválida
            myTurn = true; // ainda é a vez desse cliente
        } // fim de else if
        else if (message.equals("Opponent moved")) {
            int location = input.nextInt(); // obtém a posição da jogada
            input.nextLine(); // pula uma nova linha depois da posição de int
            int row = location / 3; // calcula a linha
            int column = location % 3; // calcula a coluna

            setMark(board[row][column],
                    (myMark.equals(X_MARK) ? O_MARK : X_MARK)); // marca a jogada
            displayMessage("Opponent moved. Your turn.\n");
            myTurn = true; // agora é a vez desse cliente
        } // fim de else if
        else
            displayMessage(message + "\n"); // exibe a mensagem
    } // fim do método processMessage

    private void displayMessage(final String messageToDisplay) {
        SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() {
                        displayArea.append(messageToDisplay); // atualiza a saída
                    } // fim do método run
                } // fim da classe inner
        ); // fim da chamada para SwingUtilities.invokeLater
    } // fim do método displayMessage

    private void setMark(final Square squareToMark, final String mark) {
        SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() {
                        squareToMark.setMark(mark); // configura a marca no quadrado
                    } // fim do método run
                } // fim da classe interna anônima
        ); // fim da chamada para SwingUtilities.invokeLater
    } // fim do método setMark

    public void sendClickedSquare(int location) {
        // se for minha vez
        if (myTurn) {
            output.format("%d\n", location); // envia a posição ao servidor
            output.flush();
            myTurn = false; // não é mais a minha vez
        } // fim do if
    } // fim do método sendClickedSquare

    public void setCurrentSquare(Square square) {
        currentSquare = square; // configura o quadrado atual para o argumento
    }

    private class Square extends JPanel {
        private String mark; // marca a ser desenhada nesse quadrado
        private int location; // posição do quadrado

        public Square(String squareMark, int squareLocation) {
            mark = squareMark; // configura a marca para esse quadrado
            location = squareLocation; // configura a posição desse quadrado

            addMouseListener(
                    new MouseAdapter() {
                        public void mouseReleased(MouseEvent e) {
                            setCurrentSquare(Square.this); // configura o quadrado atual

                            // envia a posição desse quadrado
                            sendClickedSquare(getSquareLocation());
                        } // fim do método mouseReleased
                    } // fim da classe interna anônima
            ); // fim da chamada para addMouseListener
        }
    

    public Dimension getPreferredSize() {
        return new Dimension(30, 30); // retorna o tamanho preferido
    } // fim do método getPreferredSize

    // retorna o tamanho mínimo de Square
    public Dimension getMinimumSize() {
        return getPreferredSize(); // retorna o tamanho preferido
    } // fim do método getMinimumSize

    public void setMark(String newMark) {
        mark = newMark; // configura a marca do quadrado
        repaint();
    }

    public int getSquareLocation() {
        return location; // retorna a posição do quadrado
    } // fim do método getSquareLocation

    // desenha Square
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawRect(0, 0, 29, 29); // desenha o quadrado
        g.drawString(mark, 11, 20); // desenha a marca
    } // fim do método paintComponent
} // fim da classe interna Square
}
