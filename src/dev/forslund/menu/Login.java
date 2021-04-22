package dev.forslund.menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Login extends JFrame {

    JLabel lblTitle = new JLabel("Login", SwingConstants.CENTER);

    JLabel lblUsername = new JLabel("Username", SwingConstants.CENTER);
    JTextField txfUsername = new JTextField();

    JLabel lblPassword = new JLabel("Password", SwingConstants.CENTER);
    JTextField txfPassword = new JTextField();

    JButton btnLogin = new JButton("Login");

    JLabel lblRegister = new JLabel("Register", SwingConstants.LEFT);
    JLabel lblGuestLogin = new JLabel("Guest", SwingConstants.RIGHT);

    public Login() throws HeadlessException {
        super("Login");

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 2;
        c.weighty = 1;
        c.gridwidth = 2;
        lblTitle.setFont(new Font("Sans-Serif", Font.BOLD, 14));
        add(lblTitle, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.2;
        c.weighty = 1;
        add(lblUsername, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 1;
        c.weighty = 1;
        add(txfUsername, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 0.2;
        c.weighty = 1;
        add(lblPassword, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 2;
        c.weightx = 1;
        c.weighty = 1;
        add(txfPassword, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 3;
        c.weightx = 2;
        c.weighty = 1;
        c.ipady = 10;
        add(btnLogin, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 5;
        c.weightx = 1;
        c.weighty = 1;
        c.insets = new Insets(0, 5, 0, 0);
        lblRegister.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblRegister.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                lblRegister.setForeground(Color.BLUE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                lblRegister.setForeground(Color.BLACK);
            }
        });
        add(lblRegister, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 5;
        c.weightx = 0.4;
        c.weighty = 1;
        c.insets = new Insets(0, 0, 0, 5);
        lblGuestLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblGuestLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                lblGuestLogin.setForeground(Color.BLUE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                lblGuestLogin.setForeground(Color.BLACK);
            }
        });
        add(lblGuestLogin, c);

        setPreferredSize(new Dimension(300, 200));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        pack();
        setVisible(true);
    }

    private void Register() {

    }
}
