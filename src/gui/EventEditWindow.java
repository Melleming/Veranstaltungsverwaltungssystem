package gui;

import dataManager.EventManager;
import utils.Event;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class EventEditWindow extends Window {
    private EventManager eventManager;
    private Event event;
    private JTextField nameField;
    private JTextField locationField;
    private JTextField dateField;
    private JTextField startTimeField;
    private JTextField endTimeField;
    private JTextField priceField;
    private JTextField totalTicketsField;
    private JTextField soldTicketsField;
    private EventManagementWindow eventManagementWindow;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Konstruktor zur Initialisierung des Fensters zum Bearbeiten einer Veranstaltung.
     *
     * @param event Die Veranstaltung, die bearbeitet werden soll.
     * @param eventManager Der EventManager zur Verwaltung der Veranstaltungen.
     * @param eventManagementWindow Das Hauptfenster zur Verwaltung der Veranstaltungen.
     */
    public EventEditWindow(Event event, EventManager eventManager, EventManagementWindow eventManagementWindow) {
        super("Veranstaltung bearbeiten", 400, 300);
        this.eventManager = eventManager;
        this.event = event;

        JPanel panel = new JPanel(new GridLayout(9, 2));

        panel.add(new JLabel("Name:"));
        nameField = new JTextField(event.getName());
        panel.add(nameField);

        panel.add(new JLabel("Ort:"));
        locationField = new JTextField(event.getLocation());
        panel.add(locationField);

        panel.add(new JLabel("Datum (TT.MM.YYYY):"));
        dateField = new JTextField(event.getDate());
        panel.add(dateField);

        panel.add(new JLabel("Startzeit (HH:MM):"));
        startTimeField = new JTextField(event.getStartTime());
        panel.add(startTimeField);

        panel.add(new JLabel("Endzeit (HH:MM):"));
        endTimeField = new JTextField(event.getEndTime());
        panel.add(endTimeField);

        panel.add(new JLabel("Preis (€):"));
        priceField = new JTextField(String.valueOf(event.getPrice()));
        panel.add(priceField);

        panel.add(new JLabel("Gesamtanzahl Tickets:"));
        totalTicketsField = new JTextField(String.valueOf(event.getTotalTickets()));
        panel.add(totalTicketsField);

        panel.add(new JLabel("Bereits verkaufte Tickets:"));
        soldTicketsField = new JTextField(String.valueOf(event.getSoldTickets()));
        panel.add(soldTicketsField);

        JButton saveButton = new JButton("Speichern");
        panel.add(saveButton);
        frame.getRootPane().setDefaultButton(saveButton);

        this.eventManagementWindow = eventManagementWindow;

        frame.add(panel);
        show();

        saveButton.addActionListener(e -> saveEvent());
    }

    /**
     * Speichert die Änderungen an der Veranstaltung basierend auf den Benutzereingaben.
     */
    private void saveEvent() {
        String name = nameField.getText().trim();
        String location = locationField.getText().trim();
        String date = dateField.getText().trim();
        String startTime = startTimeField.getText().trim();
        String endTime = endTimeField.getText().trim();
        String priceText = priceField.getText().trim().replace(',', '.');
        String totalTicketsText = totalTicketsField.getText().trim();
        String soldTicketsText = soldTicketsField.getText().trim();

        if (name.isEmpty() || location.isEmpty() || date.isEmpty() || startTime.isEmpty() || endTime.isEmpty() || priceText.isEmpty() || totalTicketsText.isEmpty() || soldTicketsText.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Alle Felder müssen ausgefüllt werden.", "Fehler", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double price = Double.parseDouble(priceText);
            int totalTickets = Integer.parseInt(totalTicketsText);
            int soldTickets = Integer.parseInt(soldTicketsText);

            event.setName("TEMPORARY_EMPTY_DUPLICATION_AVOID_KEY"); //Damit die eigene Veranstaltung ohne Umbenennung gespeichert werden kann
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

            if (totalTickets <= 0) {
                JOptionPane.showMessageDialog(frame, "Gesamtanzahl der Tickets muss positiv sein.", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (soldTickets < 0 || soldTickets > totalTickets) {
                JOptionPane.showMessageDialog(frame, "Verkaufte Tickets dürfen nicht negativ oder größer als die Gesamtanzahl der Tickets sein.", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            event.setName(name);
            event.setLocation(location);
            event.setDate(date);
            event.setStartTime(startTime);
            event.setEndTime(endTime);
            event.setPrice(price);
            event.setTotalTickets(totalTickets);
            event.setSoldTickets(soldTickets);

            eventManager.saveEvents();
            eventManagementWindow.updateTable();
            JOptionPane.showMessageDialog(frame, "Veranstaltung erfolgreich aktualisiert.", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
            frame.dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Preis, Gesamtanzahl der Tickets und verkaufte Tickets müssen gültige Zahlen sein.", "Fehler", JOptionPane.ERROR_MESSAGE);
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
