package gui;

import controller.CartController;
import utils.Event;
import utils.Konto.AdminAccount;
import utils.Konto.Account;

import javax.swing.*;
import java.awt.*;

public class EventDetailWindow extends Window {
    private Event event;
    private CartController cartController;
    private Account currentUser;

    /**
     * Konstruktor zur Initialisierung des Fensters zur Anzeige der Veranstaltungsdetails.
     *
     * @param event Die Veranstaltung, deren Details angezeigt werden sollen.
     * @param cartController Der Controller zur Verwaltung des Warenkorbs.
     * @param currentUser Der aktuell angemeldete Benutzer.
     */
    public EventDetailWindow(Event event, CartController cartController, Account currentUser) {
        super("Veranstaltungsdetails", 400, 400);
        this.event = event;
        this.cartController = cartController;
        this.currentUser = currentUser;

        JPanel panel = new JPanel(new GridLayout(9, 1));

        panel.add(new JLabel("Name: " + event.getName()));
        panel.add(new JLabel("Ort: " + event.getLocation()));
        panel.add(new JLabel("Datum: " + event.getDate().toString()));
        panel.add(new JLabel("Uhrzeit Anfang: " + event.getStartTime().toString()));
        panel.add(new JLabel("Uhrzeit Ende: " + event.getEndTime().toString()));
        panel.add(new JLabel("Preis (€): " + String.format("%.2f", event.getPrice())));
        int availableTickets = event.getTotalTickets() - event.getSoldTickets();
        panel.add(new JLabel("Verfügbare Tickets: " + availableTickets));

        JButton bookButton = null;
        if (!(currentUser instanceof AdminAccount)) {
            JTextField ticketQuantityField = new JTextField();
            panel.add(ticketQuantityField);

            bookButton = new JButton("Buchen");
            panel.add(bookButton);

            // Aktion zum Buchen der Tickets
            bookButton.addActionListener(e -> {
                try {
                    int quantity = Integer.parseInt(ticketQuantityField.getText());
                    if (quantity > 0 && quantity <= availableTickets) {
                        cartController.addToCart(event, quantity);
                        JOptionPane.showMessageDialog(frame, "Tickets erfolgreich zum Warenkorb hinzugefügt.", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
                        frame.dispose();
                    } else {
                        JOptionPane.showMessageDialog(frame, "Ungültige Anzahl von Tickets.", "Fehler", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Bitte geben Sie eine gültige Zahl ein.", "Fehler", JOptionPane.ERROR_MESSAGE);
                }
            });
        }

        frame.getRootPane().setDefaultButton(bookButton);
        frame.add(panel);
        show();
    }
}
