package dev.forslund.game.shapes;

import java.awt.*;

public class Player extends Shape {
    private final int width; // Never change size of width
    private final int height;

    public Player(int x, int y, int width, int height) {
        super(x, y);
        this.width = width;
        this.height = height;
    }

    public Player(Color c, int x, int y, int width, int height) {
        super(c, x, y);
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(getC()); // Could be moved to constructor but I may want to change the color mid gameplay sometime
        g.fillRect(getX(), getY(), width, height);
    }
}
