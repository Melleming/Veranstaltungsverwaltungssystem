package gui;

import controller.CartController;
import controller.MainController;
import dataManager.AccountManager;
import dataManager.CartManager;
import dataManager.EventManager;
import utils.Event;
import utils.Konto.AdminAccount;
import utils.Konto.Account;
import utils.Konto.CustomerAccount;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EventManagementWindow extends Window {
    private JTable table;
    private DefaultTableModel tableModel;
    private EventManager eventManager;
    private AccountManager accountManager;
    private CartController cartController;
    private Account currentUser;

    /**
     * Konstruktor zur Initialisierung des Hauptfensters zur Veranstaltungsverwaltung.
     *
     * @param eventManager Der EventManager zur Verwaltung der Veranstaltungen.
     * @param currentUser Der aktuell angemeldete Benutzer.
     * @param cartManager Der CartManager zur Verwaltung der Warenkörbe.
     */
    public EventManagementWindow(EventManager eventManager, Account currentUser, CartManager cartManager) {
        super("Veranstaltungsverwaltungssystem", 1000, 600);

        this.eventManager = eventManager;
        this.accountManager = MainController.getMainController().getAccountManager();
        this.currentUser = currentUser;
        if (currentUser instanceof CustomerAccount) {
            this.cartController = new CartController(eventManager, this, (CustomerAccount) currentUser, cartManager, accountManager);
        }

        tableModel = new DefaultTableModel(new String[]{"Name", "Datum", "Ort", "Preis (€)", "Verfügbare Tickets"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JButton createEventButton = new JButton("Veranstaltung erstellen");
        JButton editEventButton = new JButton("Veranstaltung bearbeiten");
        JButton deleteEventButton = new JButton("Veranstaltung löschen");
        JButton logoutButton = new JButton("Abmelden");
        JButton viewCartButton = new JButton("Warenkorb anzeigen");
        JButton manageAccountsButton = new JButton("Konten verwalten");
        JButton bookedTicketsButton = new JButton("Gekaufte Tickets anzeigen");
        JButton exitButton = new JButton("Beenden");

        JPanel buttonPanel = new JPanel();
        if (currentUser instanceof AdminAccount) {
            buttonPanel.add(createEventButton);
            buttonPanel.add(editEventButton);
            buttonPanel.add(deleteEventButton);
            buttonPanel.add(manageAccountsButton);
        } else {
            buttonPanel.add(viewCartButton);
            buttonPanel.add(bookedTicketsButton);
        }
        buttonPanel.add(logoutButton);
        buttonPanel.add(exitButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Aktion zur Erstellung einer neuen Veranstaltung
        createEventButton.addActionListener(e -> new EventCreateWindow(eventManager, this));

        // Aktion zur Bearbeitung einer ausgewählten Veranstaltung
        editEventButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                Event event = eventManager.getEventByName((String) tableModel.getValueAt(table.convertRowIndexToModel(selectedRow), 0));
                new EventEditWindow(event, eventManager, EventManagementWindow.this);
            } else {
                JOptionPane.showMessageDialog(frame, "Bitte wählen Sie eine Veranstaltung zum Bearbeiten aus", "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Aktion zum Löschen einer ausgewählten Veranstaltung
        deleteEventButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int confirmation = JOptionPane.showConfirmDialog(frame, "Sind Sie sicher, dass Sie diese Veranstaltung löschen möchten?", "Löschen bestätigen", JOptionPane.YES_NO_OPTION);
                if (confirmation == JOptionPane.YES_OPTION) {
                    eventManager.removeEventByName((String) tableModel.getValueAt(table.convertRowIndexToModel(selectedRow), 0));
                    updateTable();
                }

            } else {
                JOptionPane.showMessageDialog(frame, "Bitte wählen Sie eine Veranstaltung zum Löschen aus", "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Aktion zur Abmeldung des Benutzers
        logoutButton.addActionListener(e -> {
            frame.dispose();
            MainController.getMainController().showLoginScreen();
        });

        // Aktion zur Verwaltung der Konten
        manageAccountsButton.addActionListener(e -> new ManageAccountsWindow(accountManager, currentUser));

        // Aktion zum Beenden des Programms
        exitButton.addActionListener(e -> System.exit(0));

        // Aktion zur Anzeige des Warenkorbs
        viewCartButton.addActionListener(e -> new CartWindow(cartController, eventManager));

        // Aktion zur Anzeige der gekauften Tickets
        bookedTicketsButton.addActionListener(e -> new BookedTicketWindow((CustomerAccount) currentUser, eventManager));

        // Doppelklick-Event zum Anzeigen der Veranstaltungsdetails
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        new EventDetailWindow(eventManager.getEventByName((String) tableModel.getValueAt(table.convertRowIndexToModel(selectedRow), 0)), cartController, currentUser);
                    }
                }
            }
        });

        frame.add(panel);
        show();
        updateTable();
    }

    /**
     * Aktualisiert die Tabelle mit den Veranstaltungen.
     */
    public void updateTable() {
        tableModel.setRowCount(0);
        for (Event event : eventManager.getEvents()) {
            tableModel.addRow(new Object[]{event.getName(), event.getDate(), event.getLocation(), String.format("%.2f", event.getPrice()), event.getTotalTickets() - event.getSoldTickets()});
        }
    }
}
