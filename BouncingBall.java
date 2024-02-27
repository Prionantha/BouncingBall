import javax.swing.*;
import java.awt.*;

public class BouncingBall extends JPanel {

    class PaintCanvas extends JPanel {
        @Override
        public void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            wall.drawWall(graphics);
            ball.drawBall(graphics);
        }
    }

    private final int width, height;
    private Wall wall;
    private Ball ball;
    private PaintCanvas canvas;

    private final int DEFAULT_SPEED = 5;
    private final int REFRESH_INTERVAL = 30;
    
    public BouncingBall(int width, int height) {
        this.width = width;
        this.height = height;
        initializeWall();
        initializeBall();
        initializePanel();
        startThread();
    }

    private void initializeWall() {
        this.wall = new Wall(width, height);
    }
    
    private void initializeBall() {
        float posX = 0, posY = 0, speedX = DEFAULT_SPEED, speedY = DEFAULT_SPEED;
        this.ball = new Ball(posX, posY, speedX, speedY, wall);
    }

    private void initializePanel() {
        this.canvas = new PaintCanvas();
        this.setLayout(new BorderLayout());
        this.add(canvas, BorderLayout.CENTER);
    }

    private void startThread() {
        Thread thread = new Thread() {
            public void run() {
                while (true) {
                    ball.move();
                    repaint();
                    try {
                        Thread.sleep(REFRESH_INTERVAL);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
    }
}