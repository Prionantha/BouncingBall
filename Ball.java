import java.awt.*;
import java.util.List;

public class Ball implements Runnable {

    private Wall wall;
    private float posX, posY, speedX, speedY;
    private List<Ball> balls;

    private final int RADIUS = 30;
    private final int REFRESH_INTERVAL = 30;

    public Ball(float posX, float posY, float speedX, float speedY, Wall wall, List<Ball> balls) {
        this.posX = posX;
        this.posY = posY;
        this.speedX = speedX;
        this.speedY = speedY;
        this.wall = wall;
        this.balls = balls;
    }

    public void move() {
        // System.out.println(this.posX);
        checkCollisions();
        this.posX = posX + speedX;
        this.posY = posY + speedY;
        if (posX <= RADIUS) {
            posX = RADIUS;
            speedX = -speedX;
        } else if (posX >= this.wall.width - RADIUS) {
            posX = this.wall.width - RADIUS;
            speedX = -speedX;
        }
        if (posY <= RADIUS) {
            posY = RADIUS;
            speedY = -speedY;
        } else if (posY >= this.wall.height - RADIUS) {
            posY = this.wall.height - RADIUS;
            speedY = -speedY;
        }
    }

    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }

    public int getRadius() {
        return RADIUS;
    }

    private void checkCollisions() {
        synchronized (this) {
            for (Ball ball: balls) {
                if (this.equals(ball)) {
                    continue;
                }
                synchronized(ball) {
                    if (Math.pow(this.getPosX() - ball.getPosX(), 2) + 
                        Math.pow(this.getPosY() - ball.getPosY(), 2) < 
                        Math.pow(this.getRadius() + ball.getRadius(), 2)) {
                        this.collide();
                        ball.collide();
                    }
                }
            }
        }
    }

    public void collide() {
        speedX = -speedX;
        speedY = -speedY;
    }

    public void drawBall(Graphics graphics) {
        // System.out.println(this.posX);
        graphics.setColor(Color.YELLOW);
        graphics.fillOval((int)(posX - RADIUS), (int)(posY - RADIUS), RADIUS * 2, RADIUS * 2);
    }

    @Override
    public void run() {
        while (true) {
            move();
            try {
                Thread.sleep(REFRESH_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
}
