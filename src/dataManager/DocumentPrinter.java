package dataManager;

import utils.BookedTicket;
import utils.Event;
import utils.Konto.CustomerAccount;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class DocumentPrinter {

    /**
     * Druckt die Tickets für eine Veranstaltung.
     *
     * @param event Das Event, für das die Tickets gedruckt werden.
     * @param quantity Die Anzahl der zu druckenden Tickets.
     * @param customerAccount Das Kundenkonto des Käufers.
     */
    public static void printTickets(Event event, int quantity, CustomerAccount customerAccount) {
        try {
            File dir = new File("Ticketdruck");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            long currentMillis = System.currentTimeMillis();
            File ticketFile = new File(dir, event.getName() + "_tickets_" + currentMillis + ".txt");
            try (FileWriter writer = new FileWriter(ticketFile, true)) {
                writer.write("Veranstaltung: " + event.getName() + "\n");
                writer.write("Ort: " + event.getLocation() + "\n");
                writer.write("Datum: " + event.getDate() + "\n");
                writer.write("Startzeit: " + event.getStartTime() + "\n");
                writer.write("Endzeit: " + event.getEndTime() + "\n");
                writer.write("Preis pro Ticket: " + String.format("%.2f €", event.getPrice()) + "\n");
                writer.write("Anzahl der Tickets: " + quantity + "\n");
                writer.write("Käufer: " + customerAccount.getBenutzername() + "\n");
                writer.write("Gesamtpreis: " + String.format("%.2f €", quantity * event.getPrice()) + "\n");
                writer.write("------------------------------------------------------------\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Druckt eine Quittung für die gebuchten Tickets.
     *
     * @param bookedTickets Die Liste der gebuchten Tickets.
     * @param customerAccount Das Kundenkonto des Käufers.
     * @param eventManager Der EventManager zur Verwaltung der Veranstaltungen.
     */
    public static void printReceipt(List<BookedTicket> bookedTickets, CustomerAccount customerAccount, EventManager eventManager) {
        try {
            File dir = new File("Ticketdruck");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            long currentMillis = System.currentTimeMillis();
            File receiptFile = new File(dir, "receipt_" + currentMillis + ".txt");
            try (FileWriter writer = new FileWriter(receiptFile, true)) {
                writer.write("Quittung\n");
                writer.write("------------------------------------------------------------\n");

                double totalCost = 0.0;
                for (BookedTicket ticket : bookedTickets) {
                    Event event = eventManager.getEventByUuid(ticket.getEventUuid());
                    double eventTotalPrice = ticket.getQuantity() * ticket.getPricePerTicket();
                    totalCost += eventTotalPrice;

                    writer.write("Veranstaltung: " + event.getName() + "\n");
                    writer.write("Ort: " + event.getLocation() + "\n");
                    writer.write("Datum: " + event.getDate() + "\n");
                    writer.write("Startzeit: " + event.getStartTime() + "\n");
                    writer.write("Endzeit: " + event.getEndTime() + "\n");
                    writer.write("Preis pro Ticket: " + String.format("%.2f €", ticket.getPricePerTicket()) + "\n");
                    writer.write("Anzahl der Tickets: " + ticket.getQuantity() + "\n");
                    writer.write("Gesamtpreis: " + String.format("%.2f €", eventTotalPrice) + "\n");
                    writer.write("------------------------------------------------------------\n");
                }

                writer.write("Käufer: " + customerAccount.getBenutzername() + "\n");
                writer.write("Gesamtbetrag: " + String.format("%.2f €", totalCost) + "\n");
                writer.write("------------------------------------------------------------\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
