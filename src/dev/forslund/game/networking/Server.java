package dev.forslund.game.networking;

import dev.forslund.menu.Login;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Server extends JFrame {
    private ServerSocket listeningSocket;
    private ArrayList<ClientManager> userList;

    private JLabel currentServerInfo;
    private static JLabel currentConnections;

    public Server(int port) throws HeadlessException {
        super("Server Console");
        userList = new ArrayList<>();

        setLayout(new GridLayout(2, 1));

        currentServerInfo = new JLabel();
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
                    System.out.println("Got here");
                    ioException.printStackTrace();
                }
                dispose();
            }
        });
        setVisible(true);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);

        try {
            listeningSocket = new ServerSocket(port);
            currentServerInfo.setText("Listening to Port " + listeningSocket.getLocalPort() + " on IP " + listeningSocket.getInetAddress().getHostName() + "...");
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Thread() {
            public void run() {
                while(true) {
                    try {
                        userList.add(new ClientManager(listeningSocket.accept()));
                    } catch(IOException ioe) {
                        System.out.println("Bing");
                        ioe.printStackTrace();
                        break;
                    }
                }
            }
        }.start();
    }

    public static void updateUserCount() {
        currentConnections.setText("Current Connections: " + ClientManager.clientManagerCount());
    }

}