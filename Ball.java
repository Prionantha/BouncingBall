import java.awt.*;
import java.util.List;

public class Ball implements Runnable {

    private Wall wall;
    private float posX, posY, speedX, speedY;
    private List<Ball> balls;
    private int id;

    private final int RADIUS = 30;
    private final int REFRESH_INTERVAL = 30;

    public Ball(int id, float posX, float posY, float speedX, float speedY, Wall wall, List<Ball> balls) {
        this.id = id;
        this.posX = posX;
        this.posY = posY;
        this.speedX = speedX;
        this.speedY = speedY;
        this.wall = wall;
        this.balls = balls;
    }

    public void move() {
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
                        Math.pow(first.getPosY() - second.getPosY(), 2) < 
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
        float ratio = (other.getPosY() - this.getPosY()) / (other.getPosX() - this.getPosX());
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
        move();
    }

    public void drawBall(Graphics graphics) {
        graphics.setColor(Color.YELLOW);
        graphics.fillOval((int)(posX - RADIUS), (int)(posY - RADIUS), RADIUS * 2, RADIUS * 2);
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
