package VaiQueDa;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class DoThing extends JPanel implements Runnable{

    DoThing(){

        setBounds(0, 0, 900, 600);
        setLayout(null);

    }

    @Override
    public void run() {
        while(true){
            update();
            repaint();
        }
    }

    public void update(){
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.GREEN);
        g2.fillRect(30, 30, 810, 510);
        g2.dispose();
    }

    

}
