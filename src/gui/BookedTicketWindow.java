package gui;

import dataManager.EventManager;
import utils.BookedTicket;
import utils.Event;
import utils.Konto.CustomerAccount;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BookedTicketWindow extends Window {
    private CustomerAccount currentUser;
    private EventManager eventManager;
    private JTable table;
    private DefaultTableModel tableModel;

    /**
     * Konstruktor zur Initialisierung des Fensters zur Anzeige der gekauften Tickets.
     *
     * @param currentUser Der aktuell angemeldete Benutzer (Kundenkonto).
     * @param eventManager Der EventManager zur Verwaltung der Veranstaltungen.
     */
    public BookedTicketWindow(CustomerAccount currentUser, EventManager eventManager) {
        super("Gekaufte Tickets", 600, 400);
        this.currentUser = currentUser;
        this.eventManager = eventManager;

        tableModel = new DefaultTableModel(new String[]{"Veranstaltung", "Anzahl", "Gesamtpreis (€)", "UUID"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.getColumnModel().getColumn(3).setMinWidth(0);
        table.getColumnModel().getColumn(3).setMaxWidth(0);
        table.getColumnModel().getColumn(3).setWidth(0);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        frame.add(panel);
        show();
        updateTable();

        // Doppelklick-Event zum Anzeigen der Ticketdetails
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        String uuidString = (String) tableModel.getValueAt(selectedRow, 3);
                        UUID eventUuid = UUID.fromString(uuidString);
                        BookedTicket ticket = currentUser.getBookedTickets().stream()
                                .filter(t -> t.getEventUuid().equals(eventUuid))
                                .findFirst()
                                .orElse(null);
                        if (ticket != null) {
                            new BookedTicketDetailWindow(ticket, eventManager);
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "Bitte wählen Sie ein Ticket aus.", "Fehler", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }

    /**
     * Aktualisiert die Tabelle mit den gekauften Tickets des aktuellen Benutzers.
     */
    private void updateTable() {
        tableModel.setRowCount(0);
        List<BookedTicket> ticketsToRemove = new ArrayList<>(); //Befüllung der Liste außerhalb der Schleife zum Vorbeugen ConcurrentModificationException
        for (BookedTicket ticket : currentUser.getBookedTickets()) {
            Event event = eventManager.getEventByUuid(ticket.getEventUuid());
            try {
                tableModel.addRow(new Object[]{event.getName(), ticket.getQuantity(), String.format("%.2f", ticket.getTotalPrice()), ticket.getEventUuid().toString()});
            } catch (NullPointerException exception) {
                ticketsToRemove.add(ticket); //Entfernt das Ticket wenn es nicht mehr zur Verfügung steht
            }
        }
        currentUser.getBookedTickets().remove(ticketsToRemove);
    }
}
