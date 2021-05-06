package dev.forslund.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class PongGame extends JFrame {
    private int port;
    private Socket so;
    private DataInputStream in;
    private DataOutputStream out;

    public PongGame(String address, int port) throws HeadlessException {
        setPreferredSize(new Dimension(800, 600));
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        // Custom Close Operation
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Literally dispose except that it also closes the socket.
                try {
                    so.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

                dispose();
            }
        });
        setResizable(false);

        GamePanel gp = new GamePanel();
        add(gp);

        pack();
        gp.initiateAssets();
        setVisible(true);
        setLocationRelativeTo(null);

        // Connect to server
        try {
            so = new Socket(address, port);
            in = new DataInputStream(so.getInputStream());
            out = new DataOutputStream(so.getOutputStream());
        } catch (UnknownHostException uhe) {
            JOptionPane.showMessageDialog(this, uhe.getMessage() + ".");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
