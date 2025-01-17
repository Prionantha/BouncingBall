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
    private List<Thread> threads;
    private int numBalls = 10;
    private PaintCanvas canvas;

    private final int DEFAULT_SPEED = 2;
    private final int REFRESH_INTERVAL = 30;
    private final int DEFAULT_RADIUS = 60;
    
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
            float posX = getRandom(width), posY = getRandom(height);
            float speedX = getRandom(DEFAULT_SPEED), speedY = getRandom(DEFAULT_SPEED);
            int radius = (int)getRandom(DEFAULT_RADIUS) + 15;
            Ball ball = new Ball(i, posX, posY, speedX, speedY, radius, width, height, balls);
            balls.add(ball);
        }
    }

    private void initializePanel() {
        this.canvas = new PaintCanvas();
        this.canvas.setOpaque(false);
        this.setLayout(new BorderLayout());
        this.add(canvas, BorderLayout.CENTER);
    }

    public void pushBalls(int x, int y) {
        for (Ball ball: balls) {
            if (Math.pow(x - ball.getPosX(), 2) + 
                Math.pow(y - ball.getPosY(), 2) < 200 * 200) {
                    ball.push(x, y);
            }
        }
    }

    private void startThread() {
        threads = new ArrayList<>();
        for (int i = 0; i < numBalls; i++) {
            Ball ball = balls.get(i);
            Thread ballThread = new Thread(ball);
            ballThread.start();
            threads.add(ballThread);
        }
        Thread thread = new Thread() {
            public void run() {
                while (true) {
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
