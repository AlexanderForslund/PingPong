package dev.forslund.game.shapes;

import java.awt.*;

// THIS IS LITERALLY THE PLAYER CLASS BUT WITH ANOTHER NAME
// IK IT WAS BAD PLANNING FROM MY END

public class Rectangle extends Shape {
    private final int width; // Never change size of width
    private final int height;

    public Rectangle(int x, int y, int width, int height) {
        super(x, y);
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(getC()); // Could be moved to constructor but I may want to change the color mid gameplay sometime
        g.fillRect(getX(), getY(), width, height);
    }
}
