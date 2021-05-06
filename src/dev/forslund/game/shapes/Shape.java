package dev.forslund.game.shapes;

import java.awt.*;

abstract class Shape {
    private Color c;
    private int x;
    private int y;

    public Shape(int x, int y) {
        this.x = x;
        this.y = y;
        this.c = Color.white;
    }

    public Shape(Color c, int x, int y) {
        this.c = c;
        this.x = x;
        this.y = y;
    }

    public Color getC() {
        return c;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setC(Color c) {
        this.c = c;
    }

    // Abstract funcs
    public abstract void draw(Graphics g);
}
