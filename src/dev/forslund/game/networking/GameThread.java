package dev.forslund.game.networking;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GameThread extends Thread {
    private int incrementX = 50;
    private int incrementY = incrementX/6;

    private Server s;

    private int interval;
    private int x;
    private int y;

    private ArrayList<User> users;

    public GameThread(Server s, int interval, ArrayList<User> users) {
        super("Game Loop");
        this.s = s;
        this.interval = interval;
        this.users = users;

        this.x = 100;
        this.y = 100;
    }

    @Override
    public void run() {
        while (true) {
            try {
                sleep(interval);
            } catch (InterruptedException e) {
                System.out.println("Interrupted sleep.");
            }
            if (y >= (600 - (40*2)) || y <= 0) { // height - ball radius * 2, effort to remove hardcode
                incrementY = incrementY * -1;
            }

            for (User u : users) {
                if (y >= u.getyCoordinate() && y <= u.getyCoordinate()*75 && x == 30) { // Player height = 75
                    System.out.println("Bong");
                    incrementX *= -1;
                }
                if (y >= u.getyCoordinate() && y <= u.getyCoordinate()*75 && x == (800 - 30)) {
                    System.out.println("Bong");
                    incrementX *= -1;
                }
            }


            x += incrementX;
            y += incrementY;
            s.sendBallPosition(x, y);
        }
    }

    public int getInterval() {
        return interval;
    }
}
