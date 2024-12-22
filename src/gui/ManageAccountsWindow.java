package gui;

import controller.MainController;
import dataManager.AccountManager;
import utils.Konto.AdminAccount;
import utils.Konto.Account;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ManageAccountsWindow extends Window {
    private JTable table;
    private DefaultTableModel tableModel;
    private AccountManager accountManager;
    private Account currentUser;

    /**
     * Konstruktor zur Initialisierung des Fensters zur Verwaltung der Konten.
     *
     * @param accountManager Der AccountManager zur Verwaltung der Konten.
     * @param currentUser Der aktuell angemeldete Benutzer.
     */
    public ManageAccountsWindow(AccountManager accountManager, Account currentUser) {
        super("Konten verwalten", 600, 400);
        this.accountManager = accountManager;
        this.currentUser = currentUser;

        tableModel = new DefaultTableModel(new String[]{"Benutzername", "Typ"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabelle nicht bearbeitbar
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JButton createAccountButton = new JButton("Konto erstellen");
        JButton deleteAccountButton = new JButton("Konto löschen");
        JButton editAccountButton = new JButton("Konto bearbeiten");
        JButton closeButton = new JButton("Schließen");

        JPanel buttonPanel = new JPanel();
        if (currentUser instanceof AdminAccount) {
            buttonPanel.add(createAccountButton);
            buttonPanel.add(editAccountButton);
            buttonPanel.add(deleteAccountButton);
        }
        buttonPanel.add(closeButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Aktion zum Erstellen eines neuen Kontos
        createAccountButton.addActionListener(e -> {
            new RegisterWindow(accountManager, false);
            updateTable();
        });

        // Aktion zum Bearbeiten eines ausgewählten Kontos
        editAccountButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String username = (String) table.getValueAt(table.convertRowIndexToModel(selectedRow), 0);
                Account account = accountManager.getAccount(username);
                if (account != null) {
                    new AccountEditWindow(accountManager, account, MainController.getMainController().getCurrentUser());
                    updateTable();
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Bitte wählen Sie ein Konto zum Bearbeiten aus", "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Aktion zum Löschen eines ausgewählten Kontos
        deleteAccountButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int confirmation = JOptionPane.showConfirmDialog(frame, "Sind Sie sicher, dass Sie dieses Konto löschen möchten?", "Löschen bestätigen", JOptionPane.YES_NO_OPTION);
                if (confirmation == JOptionPane.YES_OPTION) {
                    String username = (String) table.getValueAt(table.convertRowIndexToModel(selectedRow), 0);
                    Account accountToRemove = accountManager.getAccount(username);
                    try {
                        accountManager.removeAccount(accountToRemove, MainController.getMainController().getCurrentUser());
                        updateTable();
                    } catch (IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(frame, ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Bitte wählen Sie ein Konto zum Löschen aus", "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Aktion zum Schließen des Fensters
        closeButton.addActionListener(e -> frame.dispose());

        // Doppelklick-Event zum Anzeigen der Kontodetails
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        String username = (String) table.getValueAt(table.convertRowIndexToModel(selectedRow), 0);
                        Account account = accountManager.getAccount(username);
                        if (account != null) {
                            new AccountDetailsWindow(account);
                        }
                    }
                }
            }
        });

        frame.add(panel);
        show();
        updateTable();
    }

    /**
     * Aktualisiert die Tabelle mit den Konten.
     */
    public void updateTable() {
        tableModel.setRowCount(0);
        for (Account account : accountManager.getAccounts()) {
            tableModel.addRow(new Object[]{account.getBenutzername(), account instanceof AdminAccount ? "Admin" : "Kunde"});
        }
    }
}
