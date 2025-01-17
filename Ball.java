import java.awt.*;
import java.util.List;
import java.util.Random;

public class Ball implements Runnable {

    private float posX, posY, speedX, speedY;
    private int radius, width, height;
    private List<Ball> balls;
    private int id;
    private int color;
    private float acc;

    private final int REFRESH_INTERVAL = 30;
    private final float DEC = 0.1f;
    private final float[][] palette = new float[][]{
        {0.73f, 0.65f, 0.69f},
        {0.7f, 0.67f, 0.75f},
        {0.694f, 0.71f, 0.784f},
        {0.75f, 0.84f, 0.89f},
        {0.78f, 0.92f, 0.94f}
    };

    public Ball(int id, float posX, float posY, float speedX, float speedY, int radius, int width, int height, List<Ball> balls) {
        this.id = id;
        this.posX = posX;
        this.posY = posY;
        this.speedX = speedX;
        this.speedY = speedY;
        this.radius = radius;
        this.width = width;
        this.height = height;
        this.balls = balls;
        this.acc = 1;
        Random r = new Random();
        this.color = r.nextInt(5);
    }

    public void move() {
        this.posX = posX + speedX * acc;
        this.posY = posY + speedY * acc;
        if (acc > 1) {
            acc -= DEC;
        }
        if (posX <= radius) {
            posX = radius;
            speedX = -speedX;
            return;
        } else if (posX >= width - radius) {
            posX = width - radius;
            speedX = -speedX;
            return;
        }
        if (posY <= radius) {
            posY = radius;
            speedY = -speedY;
            return;
        } else if (posY >= height - radius) {
            posY = height - radius;
            speedY = -speedY;
            return;
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

    public void push(float x, float y) {
        this.acc = 3f;
        this.collide(x, y);
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
                        first.collide(second.getPosX(), second.getPosY());
                        second.collide(first.getPosX(), first.getPosY());
                    }
                }
            }
        }
    }

    public void collide(float x, float y) {
        float totalSpeed = speedX * speedX + speedY * speedY;
        float ratio = Math.abs((y - this.getPosY()) / (x - this.getPosX()));
        float newSpeedX = (float) Math.sqrt(totalSpeed / (ratio * ratio + 1));
        float newSpeedY = ratio * newSpeedX;
        if (y > this.getPosY()) {
            speedY = -1 * newSpeedY;
        } else {
            speedY = newSpeedY;
        }
        if (x > this.getPosX()) {
            speedX = -1 * newSpeedX;
        } else {
            speedX = newSpeedX;
        }
    }

    public void drawBall(Graphics graphics) {
        float[] color = palette[this.color];
        graphics.setColor(new Color(color[0], color[1], color[2], 0.9f));
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
