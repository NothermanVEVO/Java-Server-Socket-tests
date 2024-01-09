package VaiQueDa;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class FPanel extends JPanel{

    JButton button1 = new JButton();
    JButton button2 = new JButton();
    JLabel cartas = new JLabel();
    BufferedImage carta;
    Image image;
    ImageIcon icon;

    FPanel(Client client){

        setSize(900, 600);
        setLayout(null);

        try {
            carta = ImageIO.read(getClass().getResourceAsStream("carta.png"));
            image = carta.getScaledInstance(64, 96, Image.SCALE_DEFAULT);
            icon = new ImageIcon(image);
        } catch (IOException e) {
        }

        add(button1);
        button1.setBounds(720, 500, 70, 50);
        button1.setText("CALL");
        button1.addActionListener(
            new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    client.sendToServer("CALL");
                }
            }
        );

        add(button2);
        button2.setBounds(800, 500, 70, 50);
        button2.setText("STAY");
        button2.addActionListener(
            new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    client.sendToServer("STAY");
                }
            }
        );

        // add(cartas);
        // cartas.setBounds(350, 350, 100, 150);
        // cartas.setIcon(icon);

    }

}
