package dev.forslund.game.shapes;

import java.awt.*;

public class Ball extends Shape {
    private final int radius; // Never change size of ball

    public Ball(int x, int y, int radius) {
        super(x, y);
        this.radius = radius;
    }

    public Ball(Color c, int x, int y, int radius) {
        super(c, x, y);
        this.radius = radius;
    }

    public int getRadius() {
        return radius;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(getC()); // Could be moved to constructor but I may want to change the color mid gameplay sometime
        g.fillOval(getX(), getY(), radius, radius);
    }
}
