package dev.forslund.game.networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientManager implements Runnable {
    private Thread t = new Thread(this);
    private DataInputStream in;
    private DataOutputStream out;

    private static int clientManagerCount;

    public ClientManager(Socket socket) {
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
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

                // Create new thread after input is received to start game.
                out.writeUTF(new StringBuilder(input).reverse().toString());
            } catch (IOException e) {
                clientManagerCount--;
                Server.updateUserCount();
                break;
            }
        }

    }

    public static int clientManagerCount() {
        return clientManagerCount;
    }
}