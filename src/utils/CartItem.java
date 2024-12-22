package utils;

import java.io.Serializable;
import java.util.UUID;
import dataManager.EventManager;

public class CartItem implements Serializable {
    private UUID eventUuid;
    private int quantity;

    /**
     * Konstruktor zur Initialisierung eines Warenkorb-Items mit Event-UUID und Menge.
     *
     * @param eventUuid Die UUID des Events.
     * @param quantity Die Menge der Tickets.
     */
    public CartItem(UUID eventUuid, int quantity) {
        this.eventUuid = eventUuid;
        this.quantity = quantity;
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
     * Gibt die Menge der Tickets zurück.
     *
     * @return Die Menge der Tickets.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Setzt die Menge der Tickets.
     *
     * @param quantity Die neue Menge der Tickets.
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Berechnet den Gesamtpreis für dieses Warenkorb-Item basierend auf den Event-Preisen.
     *
     * @param eventManager Der EventManager zur Verwaltung der Events.
     * @return Der Gesamtpreis für dieses Warenkorb-Item.
     */
    public double getTotalPrice(EventManager eventManager) {
        Event event = eventManager.getEventByUuid(eventUuid);
        return event.getPrice() * quantity;
    }
}
