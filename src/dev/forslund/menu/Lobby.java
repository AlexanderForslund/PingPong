package dev.forslund.menu;

import dev.forslund.exceptions.GameFullException;
import dev.forslund.game.PongGame;
import dev.forslund.game.networking.Server;

import javax.swing.*;
import java.awt.*;
import java.net.SocketException;

public class Lobby extends JFrame {
    private final String username;

    JLabel lblTitle = new JLabel("Connect", SwingConstants.CENTER);

    JLabel lblIP = new JLabel("IP:", SwingConstants.CENTER);
    JTextField txfConnectIP = new JTextField(SwingConstants.TRAILING); // Trailing doesnt technically do what I want
    JLabel lblPort = new JLabel("Port:", SwingConstants.CENTER); // But it works lmao
    JTextField txfConnectPort = new JTextField(SwingConstants.TRAILING);
    JButton btnConnect = new JButton("Connect");

    JButton btnCreateServer = new JButton("Create Server");

    public Lobby(String username) throws HeadlessException {
        super("Lobby");
        this.username = username;

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 2;
        c.weighty = 1;
        lblTitle.setFont(new Font("Sans-Serif", Font.BOLD, 14));
        add(lblTitle, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 2;
        c.weighty = 1;
        add(lblIP, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 2;
        c.weighty = 1;
        add(lblPort, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 2;
        c.weighty = 1;
        add(txfConnectIP, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 2;
        c.weightx = 2;
        c.weighty = 1;
        add(txfConnectPort, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 3;
        c.weightx = 2;
        c.weighty = 1;
        add(btnConnect, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 4;
        c.weightx = 2;
        c.weighty = 1;
        add(btnCreateServer, c);

        btnCreateServer.addActionListener(e -> {
            try {
                new Server(5000);
            } catch (SocketException se) {
                JOptionPane.showMessageDialog(null, "Server is already running on port. Attempting to connect...");
            }
            try {
                new PongGame("localhost", 5000);
            } catch (GameFullException gameFullException) {
                JOptionPane.showMessageDialog(null, "Server is full.");
                new Lobby(username);
            }
            dispose();
        });

        setPreferredSize(new Dimension(250, 175));
        setDefaultCloseOperation(EXIT_ON_CLOSE); // TODO: Don't exit whole program.
        setResizable(false);
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
    }
}
