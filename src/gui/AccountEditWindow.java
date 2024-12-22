package gui;

import dataManager.AccountManager;
import dataManager.AccountFactory;
import utils.Konto.Account;
import utils.Konto.AdminAccount;

import javax.swing.*;
import java.awt.*;

public class AccountEditWindow extends Window {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> accountTypeComboBox;
    private AccountManager accountManager;
    private Account account;
    private Account currentUser;

    /**
     * Konstruktor zur Initialisierung des Fensters zum Bearbeiten eines Kontos.
     *
     * @param accountManager Der AccountManager zur Verwaltung der Konten.
     * @param account Das zu bearbeitende Konto.
     * @param currentUser Das aktuell angemeldete Konto.
     */
    public AccountEditWindow(AccountManager accountManager, Account account, Account currentUser) {
        super("Konto bearbeiten", 300, 250);
        this.accountManager = accountManager;
        this.account = account;
        this.currentUser = currentUser;

        if (account.equals(currentUser)) {
            JOptionPane.showMessageDialog(null, "Sie können Ihr eigenes Konto nicht bearbeiten.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (account instanceof AdminAccount && account.getBenutzername().equals("admin")) {
            JOptionPane.showMessageDialog(null, "Das Admin-Konto kann nicht bearbeitet werden.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JPanel panel = new JPanel(new GridLayout(4, 2));
        usernameField = new JTextField(account.getBenutzername());
        passwordField = new JPasswordField(account.getPasswort());
        accountTypeComboBox = new JComboBox<>(new String[]{"Kunde", "Admin"});
        accountTypeComboBox.setSelectedItem(account instanceof AdminAccount ? "Admin" : "Kunde");
        JButton saveButton = new JButton("Speichern");

        panel.add(new JLabel("Benutzername:"));
        panel.add(usernameField);
        panel.add(new JLabel("Passwort:"));
        panel.add(passwordField);
        panel.add(new JLabel("Kontotyp:"));
        panel.add(accountTypeComboBox);
        panel.add(new JLabel(""));
        panel.add(saveButton);
        frame.getRootPane().setDefaultButton(saveButton);

        frame.add(panel);

        // Aktion zum Speichern der Änderungen am Konto
        saveButton.addActionListener(e -> {
            String newUsername = usernameField.getText();
            String newPassword = new String(passwordField.getPassword());
            String newAccountType = (String) accountTypeComboBox.getSelectedItem();

            if (newUsername.isEmpty() || newPassword.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Benutzername und Passwort dürfen nicht leer sein", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!newUsername.equals(account.getBenutzername()) && accountManager.getAccount(newUsername) != null) {
                JOptionPane.showMessageDialog(frame, "Benutzername bereits vergeben", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            account.setBenutzername(newUsername);
            account.setPasswort(newPassword);

            if (!newAccountType.equals(account instanceof AdminAccount ? "Admin" : "Kunde")) {
                accountManager.removeAccount(account, currentUser);
                Account updatedAccount = AccountFactory.createAccount(newAccountType, newUsername, newPassword);
                accountManager.addAccount(updatedAccount);
            }

            JOptionPane.showMessageDialog(frame, "Konto erfolgreich bearbeitet", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
            frame.dispose();

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
