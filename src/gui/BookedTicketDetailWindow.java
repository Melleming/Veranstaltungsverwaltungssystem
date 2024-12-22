package gui;

import controller.MainController;
import dataManager.DocumentPrinter;
import dataManager.EventManager;
import utils.BookedTicket;
import utils.Event;
import utils.Konto.CustomerAccount;

import javax.swing.*;
import java.awt.*;

public class BookedTicketDetailWindow extends Window {
    private BookedTicket ticket;
    private EventManager eventManager;

    /**
     * Konstruktor zur Initialisierung des Fensters zur Anzeige der Ticketdetails.
     *
     * @param ticket Das gebuchte Ticket, dessen Details angezeigt werden sollen.
     * @param eventManager Der EventManager zur Verwaltung der Veranstaltungen.
     */
    public BookedTicketDetailWindow(BookedTicket ticket, EventManager eventManager) {
        super("Ticketdetails", 400, 300);
        this.ticket = ticket;
        this.eventManager = eventManager;

        Event event = eventManager.getEventByUuid(ticket.getEventUuid());

        JPanel panel = new JPanel(new GridLayout(9, 2));
        panel.add(new JLabel("Veranstaltung:"));
        panel.add(new JLabel(event.getName()));

        panel.add(new JLabel("Ort:"));
        panel.add(new JLabel(event.getLocation()));

        panel.add(new JLabel("Datum:"));
        panel.add(new JLabel(event.getDate()));

        panel.add(new JLabel("Startzeit:"));
        panel.add(new JLabel(event.getStartTime()));

        panel.add(new JLabel("Endzeit:"));
        panel.add(new JLabel(event.getEndTime()));

        panel.add(new JLabel("Anzahl der Tickets:"));
        panel.add(new JLabel(String.valueOf(ticket.getQuantity())));

        panel.add(new JLabel("Preis pro Ticket:"));
        panel.add(new JLabel(String.format("%.2f €", ticket.getPricePerTicket())));

        panel.add(new JLabel("Gesamtpreis:"));
        panel.add(new JLabel(String.format("%.2f €", ticket.getTotalPrice())));

        JButton printButton = new JButton("Tickets erneut drucken");
        panel.add(printButton);

        frame.add(panel);
        show();

        // Aktion zum erneuten Drucken der Tickets
        printButton.addActionListener(e -> {
            DocumentPrinter.printTickets(event, ticket.getQuantity(), (CustomerAccount) MainController.getMainController().getCurrentUser());
            JOptionPane.showMessageDialog(frame, "Tickets wurden erneut gedruckt.", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
        });
    }
}
