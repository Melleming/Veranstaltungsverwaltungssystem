package gui;

import dataManager.EventManager;
import utils.Event;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class EventCreateWindow extends Window {
    private EventManager eventManager;
    private JTextField nameField;
    private JTextField locationField;
    private JTextField dateField;
    private JTextField startTimeField;
    private JTextField endTimeField;
    private JTextField priceField;
    private JTextField totalTicketsField;
    private EventManagementWindow eventManagementWindow;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Konstruktor zur Initialisierung des Fensters zum Erstellen einer neuen Veranstaltung.
     *
     * @param eventManager Der EventManager zur Verwaltung der Veranstaltungen.
     * @param eventManagementWindow Das Hauptfenster zur Verwaltung der Veranstaltungen.
     */
    public EventCreateWindow(EventManager eventManager, EventManagementWindow eventManagementWindow) {
        super("Veranstaltung erstellen", 400, 300);
        this.eventManager = eventManager;

        JPanel panel = new JPanel(new GridLayout(8, 2));

        panel.add(new JLabel("Name:"));
        nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Ort:"));
        locationField = new JTextField();
        panel.add(locationField);

        panel.add(new JLabel("Datum (TT.MM.YYYY):"));
        dateField = new JTextField();
        panel.add(dateField);

        panel.add(new JLabel("Startzeit (HH:MM):"));
        startTimeField = new JTextField();
        panel.add(startTimeField);

        panel.add(new JLabel("Endzeit (HH:MM):"));
        endTimeField = new JTextField();
        panel.add(endTimeField);

        panel.add(new JLabel("Preis (€):"));
        priceField = new JTextField();
        panel.add(priceField);

        panel.add(new JLabel("Gesamtanzahl Tickets:"));
        totalTicketsField = new JTextField();
        panel.add(totalTicketsField);

        JButton createButton = new JButton("Erstellen");
        panel.add(createButton);
        this.eventManagementWindow = eventManagementWindow;
        frame.getRootPane().setDefaultButton(createButton);

        frame.add(panel);
        show();

        createButton.addActionListener(e -> createEvent());
    }

    /**
     * Erstellt eine neue Veranstaltung basierend auf den Benutzereingaben.
     */
    private void createEvent() {
        String name = nameField.getText().trim();
        String location = locationField.getText().trim();
        String date = dateField.getText().trim();
        String startTime = startTimeField.getText().trim();
        String endTime = endTimeField.getText().trim();
        String priceText = priceField.getText().trim().replace(',', '.');
        String totalTicketsText = totalTicketsField.getText().trim();

        if (name.isEmpty() || location.isEmpty() || date.isEmpty() || startTime.isEmpty() || endTime.isEmpty() || priceText.isEmpty() || totalTicketsText.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Alle Felder müssen ausgefüllt werden.", "Fehler", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double price = Double.parseDouble(priceText);
            int totalTickets = Integer.parseInt(totalTicketsText);

            if (eventManager.doEventExist(name)) {
                JOptionPane.showMessageDialog(frame, "Veranstaltung mit gleichem Namen existiert bereits.", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!isValidDate(date)) {
                JOptionPane.showMessageDialog(frame, "Datum muss im Format TT.MM.YYYY sein und darf nicht in der Vergangenheit liegen.", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!isValidTime(startTime) || !isValidTime(endTime)) {
                JOptionPane.showMessageDialog(frame, "Startzeit und Endzeit müssen im Format HH:MM sein und gültige Zeiten darstellen.", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (price < 0) {
                JOptionPane.showMessageDialog(frame, "Preis darf nicht negativ sein.", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (totalTickets < 0) {
                JOptionPane.showMessageDialog(frame, "Gesamtanzahl der Tickets muss positiv sein.", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Event event = new Event(name, location, date, startTime, endTime, price, totalTickets);
            eventManager.addEvent(event);
            eventManagementWindow.updateTable();
            JOptionPane.showMessageDialog(frame, "Veranstaltung erfolgreich erstellt.", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
            frame.dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Preis und Gesamtanzahl der Tickets müssen gültige Zahlen sein.", "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Überprüft, ob ein Datum gültig ist und nicht in der Vergangenheit liegt.
     *
     * @param date Das zu überprüfende Datum.
     * @return true, wenn das Datum gültig ist und nicht in der Vergangenheit liegt, andernfalls false.
     */
    private boolean isValidDate(String date) {
        try {
            LocalDate parsedDate = LocalDate.parse(date, DATE_FORMATTER);
            return !parsedDate.isBefore(LocalDate.now());
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Überprüft, ob eine Uhrzeit gültig ist.
     *
     * @param time Die zu überprüfende Uhrzeit.
     * @return true, wenn die Uhrzeit gültig ist, andernfalls false.
     */
    private boolean isValidTime(String time) {
        try {
            LocalTime.parse(time, TIME_FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
