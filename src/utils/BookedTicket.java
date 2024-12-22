package utils;

import java.io.Serializable;
import java.util.UUID;

public class BookedTicket implements Serializable {
    private UUID eventUuid;
    private int quantity;
    private double pricePerTicket;

    /**
     * Konstruktor zur Initialisierung eines gebuchten Tickets mit Event-UUID, Menge und Preis pro Ticket.
     *
     * @param eventUuid Die UUID des Events.
     * @param quantity Die Menge der gebuchten Tickets.
     * @param pricePerTicket Der Preis pro Ticket.
     */
    public BookedTicket(UUID eventUuid, int quantity, double pricePerTicket) {
        this.eventUuid = eventUuid;
        this.quantity = quantity;
        this.pricePerTicket = pricePerTicket;
    }

    /**
     * Gibt die UUID des Events zurück.
     *
     * @return Die UUID des Events.
     */
    public UUID getEventUuid() {
        return eventUuid;
    }

    /**
     * Gibt die Menge der gebuchten Tickets zurück.
     *
     * @return Die Menge der gebuchten Tickets.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Gibt den Preis pro Ticket zurück.
     *
     * @return Der Preis pro Ticket.
     */
    public double getPricePerTicket() {
        return pricePerTicket;
    }

    /**
     * Gibt den Gesamtpreis der gebuchten Tickets zurück.
     *
     * @return Der Gesamtpreis der gebuchten Tickets.
     */
    public double getTotalPrice() {
        return pricePerTicket * quantity;
    }
}
