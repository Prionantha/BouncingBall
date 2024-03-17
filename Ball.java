import java.awt.*;

public class Ball {

    private Wall wall;
    private float posX, posY, speedX, speedY;
    private final int RADIUS = 30;

    public Ball(float posX, float posY, float speedX, float speedY, Wall wall) {
        this.posX = posX;
        this.posY = posY;
        this.speedX = speedX;
        this.speedY = speedY;
        this.wall = wall;
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

    public void collide() {
        speedX = -speedX;
        speedY = -speedY;
        move();
    }

    public void drawBall(Graphics graphics) {
        graphics.setColor(Color.YELLOW);
        graphics.fillOval((int)(posX - RADIUS), (int)(posY - RADIUS), RADIUS * 2, RADIUS * 2);
    }
    
}
