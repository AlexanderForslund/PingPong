package dev.forslund.game.networking;

import dev.forslund.exceptions.GameFullException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientManager implements Runnable {
    private final Thread t = new Thread(this);
    private DataInputStream in;
    private DataOutputStream out;
    private final Server server;

    private final int id;

    private static int clientManagerCount;

    public ClientManager(Socket socket, int id, Server server) throws GameFullException {
        this.id = id;
        this.server = server;

        if (id > 1) {
            throw new GameFullException("Game is full");
        }

        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            out.writeInt(id);
        } catch (IOException e) {
            e.printStackTrace();
        }

        clientManagerCount++;
        Server.updateUserCount();
        t.start();
    }

    /**
     * Main thread for receiving and sending info from client.
     */
    @Override
    public void run() {
        while(true) {
            try {
                String input = in.readUTF();

                int senderID = Character.getNumericValue(input.charAt(0));
                int coordinates = Integer.parseInt(input.substring(input.indexOf('.') + 1));

                // Send info to other player
                server.sendPlayerPosition(senderID, coordinates);
            } catch (IOException e) {
                clientManagerCount--;
                Server.updateUserCount();
                break;
            }
        }
    }

    /**
     * Send player coordinates to the client. Suffix "p" for player.
     * @param coords Provided coordinates.
     */
    public void sendPlayerCoordinates(int coords) {
        try {
            out.writeUTF("p." + coords);
        } catch (IOException e) {
            System.out.println("No opponent.");
        }
    }

    /**
     * Send ball coordinates to the client. Suffix "b" for ball.
     * @param x X-Coordinate.
     * @param y Y-Coordinate.
     */
    public void sendBallCoordinates(int x, int y) {
        try {
            out.writeUTF("b." + x + "," + y);
        } catch (IOException e) {
            System.out.println("No opponent.");
        }
    }

    /**
     * Send point update to the client. Suffix "w" for win.
     * @param winnerID The ID of the winning player.
     */
    public void sendPointUpdate(int winnerID) {
        try {
            out.writeUTF("w." + winnerID);
        } catch (IOException e) {
            System.out.println("No connection.");
        }
    }

    /**
     * Client manager amount.
     * @return get client manager amount.
     */
    public static int clientManagerCount() {
        return clientManagerCount;
    }

    public int getId() {
        return id;
    }

    public Thread getThread() {
        return t;
    }
}