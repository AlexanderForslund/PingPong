package dev.forslund.menu;

import dev.forslund.exceptions.GameFullException;
import dev.forslund.game.PongGame;
import dev.forslund.game.networking.Server;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.SocketException;
import java.util.Scanner;


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
        txfConnectIP.setText(readLastIP());

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 2;
        c.weightx = 2;
        c.weighty = 1;
        add(txfConnectPort, c);
        txfConnectPort.setText("5000");

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
                dispose();
            } catch (GameFullException gameFullException) {
                JOptionPane.showMessageDialog(null, "Server is full.");
            } catch (IOException ioException) {
                System.out.println("Should never get here, this is due to server not existing. This creates server first.");
            }
        });

        btnConnect.addActionListener(e -> {
            String address = txfConnectIP.getText();
            int port = 5000;

            try {
                port = Integer.parseInt(txfConnectPort.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid port.");
                return;
            }

            try {
                new PongGame(address, port);
                writeLastIP(address);
                dispose();
            } catch (GameFullException gameFullException) {
                JOptionPane.showMessageDialog(null, "Server is full.");
            } catch (IOException ioex) {
                JOptionPane.showMessageDialog(null, "Connection Refused.\nPlease verify that IP and Port are correct.");
            }
        });

        setPreferredSize(new Dimension(250, 175));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
    }

    /**
     * Writes ip to file. Use readLastIP() to read last ip.
     * @param address Address that is to be written.
     */
    public void writeLastIP(String address) {
        try {
            FileWriter file = new FileWriter("saved_ip.txt");
            System.out.println("Write file lol");
            file.write(address);
            file.close();
        } catch (IOException e) {
            System.out.println("Error some reason lol");
        }
    }

    /**
     * Reads last ip written to file "saved_ip.txt".
     * Defaults to "localhost" if no file is found.
     * @return Returns saved IP, defaults to "localhost".
     */
    public String readLastIP() {
        String str = "localhost";

        try {
            Scanner s = new Scanner(new File("saved_ip.txt"));
            str = s.nextLine();
        } catch (FileNotFoundException e) {
            System.out.println("File could not be found");
        }

        return str;
    }
}
