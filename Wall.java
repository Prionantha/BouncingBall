import java.awt.*;

public class Wall {

    protected int width;
    protected int height;
    
    public Wall(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void drawWall(Graphics graphics) {
        graphics.setColor(new Color(1, 1, 1, 0.9f));
        graphics.fillRect(0, 0, width, height);
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

}