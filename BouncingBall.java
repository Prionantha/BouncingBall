import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;

public class BouncingBall extends JPanel {

    class PaintCanvas extends JPanel {
        @Override
        public void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            wall.drawWall(graphics);
            for (int i = 0; i < numBalls; i++) {
                Ball ball = balls.get(i);
                ball.drawBall(graphics);
            }  
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(width, height);
        }
    }

    private final int width, height;
    private Wall wall;
    private List<Ball> balls;
    private int numBalls = 10;
    private PaintCanvas canvas;

    private final int DEFAULT_SPEED = 5;
    private final int REFRESH_INTERVAL = 30;
    
    public BouncingBall(int width, int height) {
        this.width = width;
        this.height = height;
        initializeWall();
        initializeBalls();
        initializePanel();
        startThread();
    }

    private void initializeWall() {
        this.wall = new Wall(width, height);
    }

    private float getRandom(int range) {
        Random r = new Random();
        return r.nextFloat() * range;
    }
    
    private void initializeBalls() {
        balls = new ArrayList<>();
        for (int i = 0; i < numBalls; i++) {
            float posX = getRandom(width), posY = getRandom(height), speedX = getRandom(DEFAULT_SPEED), speedY = getRandom(DEFAULT_SPEED);
            Ball ball = new Ball(posX, posY, speedX, speedY, wall);
            balls.add(ball);
        }
    }

    private void initializePanel() {
        this.canvas = new PaintCanvas();
        this.setLayout(new BorderLayout());
        this.add(canvas, BorderLayout.CENTER);
    }

    private void ballControl() {
        for (int i = 0; i < numBalls; i++) {
            for (int j = 0; j < numBalls; j++) {
                if (i == j) {
                    continue;
                }
                Ball a = balls.get(i);
                Ball b = balls.get(j);
                if (Math.pow(a.getPosX() - b.getPosX(), 2) + 
                    Math.pow(a.getPosY() - b.getPosY(), 2) < 
                    Math.pow(a.getRadius() + b.getRadius(), 2)) {
                    a.collide();
                    b.collide();
                }
            }
        }
        for (int i = 0; i < numBalls; i++) {
            Ball ball = balls.get(i);
            ball.move();
        }
    }

    private void startThread() {
        Thread thread = new Thread() {
            public void run() {
                while (true) {
                    ballControl();
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