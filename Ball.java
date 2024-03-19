import java.awt.*;
import java.util.List;

public class Ball implements Runnable {

    private Wall wall;
    private float posX, posY, speedX, speedY;
    private int radius;
    private List<Ball> balls;
    private int id;


    private final int REFRESH_INTERVAL = 30;

    public Ball(int id, float posX, float posY, float speedX, float speedY, int radius, Wall wall, List<Ball> balls) {
        this.id = id;
        this.posX = posX;
        this.posY = posY;
        this.speedX = speedX;
        this.speedY = speedY;
        this.radius = radius;
        this.wall = wall;
        this.balls = balls;
    }

    public void move() {
        this.posX = posX + speedX;
        this.posY = posY + speedY;
        if (posX <= radius) {
            posX = radius;
            speedX = -speedX;
        } else if (posX >= this.wall.width - radius) {
            posX = this.wall.width - radius;
            speedX = -speedX;
        }
        if (posY <= radius) {
            posY = radius;
            speedY = -speedY;
        } else if (posY >= this.wall.height - radius) {
            posY = this.wall.height - radius;
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
        return radius;
    }

    public int getId() {
        return id;
    }

    private void checkCollisions() {
        for (Ball ball: balls) {
            if (ball.getId() == this.getId()) {
                continue;
            }
            Ball first, second;
            if (ball.id < this.id) {
                first = ball;
                second = this;
            } else {
                first = this;
                second = ball;
            }
            synchronized (first) {
                synchronized(second) {
                    if (Math.pow(first.getPosX() - second.getPosX(), 2) + 
                        Math.pow(first.getPosY() - second.getPosY(), 2) <= 
                        Math.pow(first.getRadius() + second.getRadius(), 2)) {
                        first.collide(second);
                        second.collide(first);
                    }
                }
            }
        }
    }

    public void collide(Ball other) {
        float totalSpeed = speedX * speedX + speedY * speedY;
        float ratio = Math.abs((other.getPosY() - this.getPosY()) / (other.getPosX() - this.getPosX()));
        float newSpeedX = (float) Math.sqrt(totalSpeed / (ratio * ratio + 1));
        float newSpeedY = ratio * newSpeedX;
        if (other.getPosY() > this.getPosY()) {
            speedY = -1 * newSpeedY;
        } else {
            speedY = newSpeedY;
        }
        if (other.getPosX() > this.getPosX()) {
            speedX = -1 * newSpeedX;
        } else {
            speedX = newSpeedX;
        }
    }

    public void drawBall(Graphics graphics) {
        graphics.setColor(Color.YELLOW);
        graphics.fillOval((int)(posX - radius), (int)(posY - radius), radius * 2, radius * 2);
    }

    @Override
    public void run() {
        while (true) {
            checkCollisions();
            move();
            try {
                Thread.sleep(REFRESH_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
}
