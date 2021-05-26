package dev.forslund.game.networking;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class GameThread extends Thread {
    private int incrementX = 30;
    private int incrementY = incrementX/6;

    private final Server s;

    private final int interval;
    private int x;
    private int y;

    private final int BALL_RADIUS = 40;
    private final int BALL_SPAWN_X = 3000;
    private final int BALL_SPAWN_Y = 100;

    private final ArrayList<User> users;

    public GameThread(Server s, int interval, ArrayList<User> users) {
        super("Game Loop");
        this.s = s;
        this.interval = interval;
        this.users = users;

        this.x = BALL_SPAWN_X;
        this.y = BALL_SPAWN_Y;
    }

    private User getUser(int id) {
        for (User u : users) {
            if (u.getClientManager().getId() == id) {
                return u;
            }
        }
        return null;
    }

    // Very bad code, everythingh is hardcoded
    @Override
    public void run() {
        int lastBounceID = 0;

        while (true) {
            try {
                sleep(interval);
            } catch (InterruptedException e) {
                System.out.println("Interrupted sleep.");
            }

            if ((lastBounceID == 1 && x <= 200) || (lastBounceID == 0 && x >= 7300)) {
                s.sendPointUpdate(lastBounceID);
                reset();
                continue;
            }

            // Y-Bounce
            if (y >= (600 - (BALL_RADIUS*2)) || y <= 0) { // height - ball radius * 2, effort to remove hardcode
                Random r = new Random();
                incrementY = (incrementY / Math.abs(incrementY)) * -1; // TODO: Fix infinite bounce.
                incrementY = (Math.abs(incrementX)/(9 - r.nextInt(3))) * incrementY; // Sheesh all this for randomized Y-speed.

                // It is probably broken by it not incrementing enough when bouncing back before the next attempt.
            }

            if (lastBounceID == 0) {
                User u = getUser(0);

                if (y >= (u.getyCoordinate()-(BALL_RADIUS/2)) && y <= u.getyCoordinate()+75 && x == 7200) {
                    incrementX *= -1;
                    lastBounceID = 1;
                }
            }

            if (lastBounceID == 1) {
                User u = getUser(1);

                if (y >= (u.getyCoordinate()-(BALL_RADIUS/2)) && y <= u.getyCoordinate()+75 && x == 300) {
                    incrementX *= -1;
                    lastBounceID = 0;
                }
            }

            // 7150 = left player x coord

            x += incrementX;
            y += incrementY;
            s.sendBallPosition(x, y);
        }
    }

    public void reset() {
        x = BALL_SPAWN_X;
        y = BALL_SPAWN_Y;
        try {
            sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
