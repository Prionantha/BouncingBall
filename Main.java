import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;

public class Main {
   public static void main(String[] args) {
      javax.swing.SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            JFrame frame = new JFrame("Bouncing Ball");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            BouncingBall bouncingBall = new BouncingBall(640, 480);
            frame.setContentPane(bouncingBall);
            frame.setUndecorated(true);
            frame.setBackground(new Color(0, 0, 0, 0));
            frame.pack();
            frame.setVisible(true);

            bouncingBall.addMouseListener(new MouseAdapter() {
               @Override
               public void mouseClicked(MouseEvent e) {
                  int x = e.getX();
                  int y = e.getY();
                  bouncingBall.pushBalls(x, y);
               }
            });
         }
      });
   }
}