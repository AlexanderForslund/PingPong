package dev.forslund.game.networking;

import dev.forslund.exceptions.GameFullException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class Server extends JFrame {
    private final int GAME_LOOP_INTERVAL = 17; // ms, 8 = 120hz, 17 = 60hz ish

    private final ServerSocket listeningSocket;
    private final ArrayList<User> userList;
    private final Server server;

    private final JLabel currentServerInfo = new JLabel();
    private static JLabel currentConnections;

    private final GameThread gameThread;

    private int id;

    public Server(int port) throws HeadlessException, SocketException {
        super("Server Console");

        // TODO Some thread keeps running after server window is closed
        // IDK if this is going to be fixed because it is effort
        // It dies when both clients disconnects anyways so shouldn't be a prob

        server = this;

        try {
            listeningSocket = new ServerSocket(port);
            currentServerInfo.setText("Listening to Port " + listeningSocket.getLocalPort() + " on IP " + listeningSocket.getInetAddress().getHostName() + "...");
        } catch (IOException e) {
            throw new SocketException("Socket already bound.");
        }

        userList = new ArrayList<>();
        gameThread = new GameThread(server, GAME_LOOP_INTERVAL, userList);

        setLayout(new GridLayout(2, 1));

        add(currentServerInfo);

        currentConnections = new JLabel("Current Connections: 0");
        add(currentConnections);

        setPreferredSize(new Dimension(300, 100));
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        // Custom Close Operation
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Literally dispose except that it also closes the socket.
                try {
                    listeningSocket.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

                gameThread.interrupt();
                for (User u : userList) {
                    u.getClientManager().getThread().interrupt();
                }
                dispose();
            }
        });
        setVisible(true);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);

        new Thread() {
            public void run() {
                while(true) {
                    try {
                        Socket socket = listeningSocket.accept();
                        if (id > 1) {
                            new DataOutputStream(socket.getOutputStream()).writeInt(-1);
                            socket.close();
                            continue;
                        }
                        userList.add(new User(new ClientManager(socket, id++, server)));
                        if (id == 2 && gameThread != null && !gameThread.isAlive()) {
                            gameThread.start();
                        }
                    } catch(IOException ioe) {
                        System.out.println("Socket Closed.");
                        break;
                    } catch (GameFullException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    /**
     * Sends player position to client managers.
     * @param senderID Sender ID.
     * @param coordinates Coordinates.
     */
    public void sendPlayerPosition(int senderID, int coordinates) {
        for (User u : userList) {
            if (u.getClientManager().getId() != senderID) {
                u.getClientManager().sendPlayerCoordinates(coordinates);
                u.setyCoordinate(coordinates);
            }
        }
    }

    /**
     * Send ball position to client managers.
     * @param x X-Coordinate.
     * @param y Y-Coordinate.
     */
    public void sendBallPosition(int x, int y) {
        for (User u : userList) {
            u.getClientManager().sendBallCoordinates(x, y);
        }
    }

    /**
     * Updates server panel user count.
     */
    public static void updateUserCount() {
        currentConnections.setText("Current Connections: " + ClientManager.clientManagerCount());
    }

    /**
     * Send point update to client managers.
     * @param winnerID Winner ID.
     */
    public void sendPointUpdate(int winnerID) {
        for (User u : userList) {
            u.getClientManager().sendPointUpdate(winnerID);
        }
    }
}