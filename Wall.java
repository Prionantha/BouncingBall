import java.awt.*;

public class Wall {

    protected int width;
    protected int height;
    
    public Wall(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void drawWall(Graphics graphics) {
        graphics.setColor(Color.black);
        graphics.fillRect(0, 0, width, height);
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

}