package gui;

import controller.MainController;
import utils.Konto.Account;

import javax.swing.*;
import java.awt.*;

public class LoginWindow extends Window {
    private JTextField usernameField;
    private JPasswordField passwordField;

    /**
     * Konstruktor zur Initialisierung des Login-Fensters.
     */
    public LoginWindow() {
        super("Login", 400, 200);

        JPanel panel = new JPanel(new GridLayout(3, 2));

        panel.add(new JLabel("Benutzername:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Passwort:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Registrieren");
        JButton exitButton = new JButton("Beenden");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        buttonPanel.add(exitButton);

        frame.add(panel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        show();

        // Aktion für den Login-Button
        loginButton.addActionListener(e -> login());

        // Aktion für den Registrieren-Button
        registerButton.addActionListener(e -> new RegisterWindow(MainController.getMainController().getAccountManager(), true));

        // Aktion für den Beenden-Button
        exitButton.addActionListener(e -> System.exit(0));

        frame.getRootPane().setDefaultButton(loginButton); //Dadurch ist Login mit Enter-Taste möglich
    }

    /**
     * Methode zum Einloggen des Benutzers.
     */
    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Benutzername und Passwort dürfen nicht leer sein.", "Fehler", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Account account = MainController.getMainController().getAccountManager().getAccount(username);
        if (account != null && account.getPasswort().equals(password)) {
            frame.dispose();
            MainController.getMainController().showEventManagementWindow(account);
        } else {
            JOptionPane.showMessageDialog(frame, "Ungültiger Benutzername oder Passwort.", "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }
}
