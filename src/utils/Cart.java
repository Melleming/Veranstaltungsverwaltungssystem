package utils;

import dataManager.EventManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Cart implements Serializable {
    private List<CartItem> items;

    /**
     * Konstruktor zur Initialisierung eines Warenkorbs.
     */
    public Cart() {
        items = new ArrayList<>();
    }

    /**
     * Fügt ein Item mit der gegebenen Event-UUID und Menge zum Warenkorb hinzu.
     * Wenn das Event bereits im Warenkorb existiert, wird die Menge erhöht.
     *
     * @param eventUuid Die UUID des Events.
     * @param quantity Die Menge der hinzuzufügenden Tickets.
     */
    public void addItem(UUID eventUuid, int quantity) {
        for (CartItem item : items) {
            if (item.getEventUuid().equals(eventUuid)) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        items.add(new CartItem(eventUuid, quantity));
    }

    /**
     * Gibt die Liste der Items im Warenkorb zurück.
     *
     * @return Eine Liste der Items im Warenkorb.
     */
    public List<CartItem> getItems() {
        return items;
    }

    /**
     * Leert den Warenkorb.
     */
    public void clear() {
        items.clear();
    }

    /**
     * Führt den Kauf der Items im Warenkorb aus und leert ihn.
     */
    public void purchase() {
        items.clear();
    }

    /**
     * Berechnet den Gesamtpreis der Items im Warenkorb basierend auf den Event-Preisen.
     *
     * @param eventManager Der EventManager zur Verwaltung der Events.
     * @return Der Gesamtpreis der Items im Warenkorb.
     */
    public double getTotalPrice(EventManager eventManager) {
        double total = 0.0;
        for (CartItem item : items) {
            total += item.getTotalPrice(eventManager);
        }
        return total;
    }
}
