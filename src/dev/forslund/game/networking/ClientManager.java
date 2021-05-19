package dev.forslund.game.networking;

import dev.forslund.exceptions.GameFullException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientManager implements Runnable {
    private Thread t = new Thread(this);
    private DataInputStream in;
    private DataOutputStream out;
    private Server server;

    private int id;

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

    public void sendPlayerCoordinates(int coords) {
        try {
            out.writeUTF("p." + coords);
        } catch (IOException e) {
            System.out.println("No opponent.");
        }
    }

    public void sendBallCoordinates(int x, int y) {
        try {
            out.writeUTF("b." + x + "," + y);
        } catch (IOException e) {
            System.out.println("No opponent.");
        }
    }

    public static int clientManagerCount() {
        return clientManagerCount;
    }

    public int getId() {
        return id;
    }
}