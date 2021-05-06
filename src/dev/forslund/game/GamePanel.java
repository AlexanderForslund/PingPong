package dev.forslund.game;

import dev.forslund.game.shapes.Ball;
import dev.forslund.game.shapes.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GamePanel extends JPanel {
    private final int PLAYER_HEIGHT = 75; // Default 75
    private final int BALL_SIZE = 40; // Default 40
    private final int PLAYER_MOVEMENT_SPEED = 10; // Default

    private Ball ball;
    private Player localPlayer;
    private Player opponentPlayer;

    public GamePanel() {
        setBackground(Color.BLACK);
        setFocusable(true);

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == 'w' || e.getKeyChar() == 's') {
                    move(e.getKeyChar());
                }
            }
        });
    }

    // Initiate Assets
    public void initiateAssets() {
        // Create assets
        ball = new Ball(getWidth()/2, 20, BALL_SIZE);
        localPlayer = new Player(10, 50, 20, PLAYER_HEIGHT);
        opponentPlayer = new Player(getWidth()-(10+20), 50, 20, PLAYER_HEIGHT);
        repaint();
    }

    // Graphics
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawGame(g);
    }

    // Game Logic
    private void drawGame(Graphics g) {
        ball.draw(g);
        localPlayer.draw(g);
        opponentPlayer.draw(g);
    }

    private void move(char inputChar) {
        System.out.println(inputChar);
        // TODO: Send information about movement to server
        if (inputChar == 'w') {
            if (!(localPlayer.getY()-10 <= 0)) {
                localPlayer.setY(localPlayer.getY()-10);
            }
        } else if (inputChar == 's') {
            if (!(localPlayer.getY()+10 > (getHeight()-PLAYER_HEIGHT))) {
                localPlayer.setY(localPlayer.getY()+10);
            }
        }

        repaint();
    }
}
