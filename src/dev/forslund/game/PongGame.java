package dev.forslund.game;

import dev.forslund.exceptions.GameFullException;
import dev.forslund.game.networking.ClientManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

public class PongGame extends JFrame {
    private final int port;
    private int networkID;

    private final String address;
    private Socket so;
    private DataInputStream in;
    private DataOutputStream out;

    private final JLabel playerScore = new JLabel("0");
    private final JLabel opponentScore = new JLabel("0");

    GamePanel gp;

    public PongGame(String address, int port) throws HeadlessException, GameFullException, IOException {
        super("PingPong");

        this.port = port;
        this.address = address;

        // Create game font
        try {
            Font blockFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getResource("/dev/forslund/Dropy.ttf").openStream());

            // Saker jag tog från internet angående fonts
            // https://stackoverflow.com/questions/21081586/using-a-custom-font-for-a-jlabel
            GraphicsEnvironment genv = GraphicsEnvironment.getLocalGraphicsEnvironment();
            genv.registerFont(blockFont);
            blockFont = blockFont.deriveFont(80f);

            playerScore.setFont(blockFont);
            playerScore.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 50));

            opponentScore.setFont(blockFont);

            playerScore.setForeground(Color.WHITE);
            opponentScore.setForeground(Color.WHITE);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Font does not exist.");
        }


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

        gp = new GamePanel(this);
        add(gp);

        gp.add(playerScore);
        gp.add(opponentScore);

        connectToServer(); // Exit
        getGameData();

        pack();
        gp.initiateAssets();
        setVisible(true);
        setLocationRelativeTo(null);
    }

    private void connectToServer() throws GameFullException, IOException {
        // Connect to server
        try {
            so = new Socket(address, port);
            in = new DataInputStream(so.getInputStream());
            out = new DataOutputStream(so.getOutputStream());

            networkID = in.readInt();
            if (networkID == -1) {
                throw new GameFullException("Game is full.");
            }
        } catch (UnknownHostException uhe) {
            System.out.println("Connection failed");
            throw uhe;
        }
    }

    private void getGameData() {
        new Thread() {
            public void run() {
                while(true) {
                    try {
                        String input = in.readUTF();
                        if (input.charAt(0) == 'p') {
//                            System.out.println(networkID + " recieved coordinates: " + input.substring(input.indexOf('.')+1));
                            gp.moveOpponent(Integer.parseInt(input.substring(input.indexOf('.')+1)));
                            repaint();
                        }
                        if (input.charAt(0) == 'b') {
//                            System.out.println(networkID + " recieved coordinates: " + input.substring(input.indexOf('.')+1));
                            int x = Integer.parseInt(input.substring(input.indexOf('.')+1, input.indexOf(',')-1));
                            int y = Integer.parseInt(input.substring(input.indexOf(',')+1));
                            if(networkID == 0) {
                                gp.moveBall(x, y);
                            } else {
                                gp.moveBall(gp.getWidth() - (x + gp.getBall().getRadius()), y);
                            }
                            repaint();
                        }
                        if (input.charAt(0) == 'w') {
                            if (networkID == Character.getNumericValue(input.charAt(input.indexOf('.')+1))) {
                                playerScore.setText((Integer.parseInt(playerScore.getText())+1) + "");
                            } else {
                                opponentScore.setText((Integer.parseInt(opponentScore.getText())+1) + "");
                            }

                            gp.getBall().setY(1000);
                            repaint();
                        }

                    } catch (IOException e) {
                        System.out.println("Client disconnected.");
                        break;
                    }
                }
            }
        }.start();
    }

    // Package functions
    protected void sendMoveData(int a) {
        try {
            out.writeUTF(networkID + "." + a);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Data could not be written, check your connection.");
        }
    }

}
