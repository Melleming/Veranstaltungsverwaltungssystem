package gui;

import utils.Konto.AdminAccount;
import utils.Konto.Account;

import javax.swing.*;
import java.awt.*;

public class AccountDetailsWindow extends Window {
    private Account account;

    /**
     * Konstruktor zur Initialisierung des Fensters mit Kontodetails.
     *
     * @param account Das Konto, dessen Details angezeigt werden sollen.
     */
    public AccountDetailsWindow(Account account) {
        super("Kontodetails", 300, 200);
        this.account = account;

        JPanel panel = new JPanel(new GridLayout(3, 2));
        JLabel usernameLabel = new JLabel("Benutzername:");
        JLabel usernameValue = new JLabel(account.getBenutzername());
        JLabel accountTypeLabel = new JLabel("Kontotyp:");
        JLabel accountTypeValue = new JLabel(account instanceof AdminAccount ? "Admin" : "Kunde");

        panel.add(usernameLabel);
        panel.add(usernameValue);
        panel.add(accountTypeLabel);
        panel.add(accountTypeValue);

        frame.add(panel);
        show();
    }
}
