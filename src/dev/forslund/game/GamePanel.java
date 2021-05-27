package dev.forslund.game;

import dev.forslund.game.shapes.Ball;
import dev.forslund.game.shapes.Player;
import dev.forslund.game.shapes.Rectangle;
import org.w3c.dom.css.Rect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class GamePanel extends JPanel {
    private final int PLAYER_HEIGHT = 75; // Default 75
    private final int BALL_SIZE = 40; // Default 40
    private final int PLAYER_MOVEMENT_SPEED = 10; // Default

    private Ball ball;
    private Player localPlayer;
    private Player opponentPlayer;
    private ArrayList<Rectangle> divisor;

    private final PongGame game;

    public GamePanel(PongGame game) {
        this.game = game;

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

    /**
     * Initiates game visual assets.
     */
    // Initiate Assets
    public void initiateAssets() {
        // Create assets
        ball = new Ball(getWidth()/2, 1000, BALL_SIZE);
        localPlayer = new Player(10, 50, 20, PLAYER_HEIGHT);
        opponentPlayer = new Player(getWidth()-(10+20), 50, 20, PLAYER_HEIGHT);

        divisor = new ArrayList<>();
        for (int i = getHeight()/10; i > 0; i--) {
            if (i % 2 != 0) {
                divisor.add(new Rectangle((getWidth()/2) - 10, (i*10), 10, 10));
            }
        }

        repaint();
    }

    // Graphics
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawGame(g);
    }

    public Ball getBall() {
        return ball;
    }

    // Game Logic

    /**
     * Draws game
     * @param g Graphics g.
     */
    private void drawGame(Graphics g) {
        for (Rectangle r : divisor) {
            r.draw(g);
        }

        ball.draw(g);
        localPlayer.draw(g);
        opponentPlayer.draw(g);
    }

    /**
     * Move character.
     * @param inputChar Input character from keyboard.
     */
    private void move(char inputChar) {
        if (inputChar == 'w') {
            if (!(localPlayer.getY()-10 <= 0)) {
                localPlayer.setY(localPlayer.getY()-10);
                game.sendMoveData(localPlayer.getY());
            }
        } else if (inputChar == 's') {
            if (!(localPlayer.getY()+10 > (getHeight()-PLAYER_HEIGHT))) {
                localPlayer.setY(localPlayer.getY()+10);
                game.sendMoveData(localPlayer.getY());
            }
        }

        repaint();
    }

    /**
     * MOve other player.
     * @param coordinates Opponent coordinates.
     */
    public void moveOpponent(int coordinates) {
        opponentPlayer.setY(coordinates);
    }

    /**
     * Moves ball
     * @param x X-coordinate.
     * @param y Y-coordinate.
     */
    public void moveBall(int x, int y) {
        if (ball != null) {
            ball.setX(x);
            ball.setY(y);
        }
    }


}
