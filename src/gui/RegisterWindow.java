package gui;

import dataManager.AccountManager;
import dataManager.AccountFactory;
import utils.Konto.Account;

import javax.swing.*;
import java.awt.*;

public class RegisterWindow extends Window {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JComboBox<String> accountTypeComboBox;
    private AccountManager accountManager;
    private boolean isFromLoginWindow;

    /**
     * Konstruktor zur Initialisierung des Registrierungsfensters.
     *
     * @param accountManager Der AccountManager zur Verwaltung der Konten.
     * @param isFromLoginWindow Gibt an, ob das Registrierungsfenster vom Login-Fenster aufgerufen wurde.
     */
    public RegisterWindow(AccountManager accountManager, boolean isFromLoginWindow) {
        super("Registrieren", 300, isFromLoginWindow ? 200 : 250);
        this.accountManager = accountManager;
        this.isFromLoginWindow = isFromLoginWindow;

        JPanel panel = new JPanel(new GridLayout(isFromLoginWindow ? 4 : 5, 2));
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        confirmPasswordField = new JPasswordField();
        accountTypeComboBox = new JComboBox<>(new String[]{"Kunde", "Admin"});
        JButton registerButton = new JButton("Registrieren");

        panel.add(new JLabel("Benutzername:"));
        panel.add(usernameField);
        panel.add(new JLabel("Passwort:"));
        panel.add(passwordField);
        panel.add(new JLabel("Passwort bestätigen:"));
        panel.add(confirmPasswordField);

        if (!isFromLoginWindow) {
            panel.add(new JLabel("Kontotyp:"));
            panel.add(accountTypeComboBox);
        }

        panel.add(new JLabel(""));
        panel.add(registerButton);
        frame.getRootPane().setDefaultButton(registerButton);

        frame.add(panel);

        // Aktion zur Registrierung eines neuen Kontos
        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            String accountType = isFromLoginWindow ? "Kunde" : (String) accountTypeComboBox.getSelectedItem();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Benutzername und Passwort dürfen nicht leer sein", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(frame, "Passwörter stimmen nicht überein", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (accountManager.getAccount(username) != null) {
                JOptionPane.showMessageDialog(frame, "Benutzername bereits vergeben", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Account newAccount = AccountFactory.createAccount(accountType, username, password);
            accountManager.addAccount(newAccount);
            JOptionPane.showMessageDialog(frame, "Registrierung erfolgreich", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
            frame.dispose();

            // Updatet die Tabelle im ManageAccountsWindow, sollte es existieren
            for (Window window : Window.getWindows()) {
                if (window instanceof ManageAccountsWindow) {
                    ((ManageAccountsWindow) window).updateTable();
                    break;
                }
            }
        });

        show();
    }
}
