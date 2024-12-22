package controller;

import dataManager.CartManager;
import dataManager.DocumentPrinter;
import dataManager.EventManager;
import dataManager.AccountManager;
import gui.EventManagementWindow;
import utils.BookedTicket;
import utils.Cart;
import utils.CartItem;
import utils.Event;
import utils.Konto.CustomerAccount;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;

public class CartController {
    private EventManager eventManager;
    private EventManagementWindow eventManagementWindow;
    private CustomerAccount currentUser;
    private CartManager cartManager;
    private AccountManager accountManager;

    public CartController(EventManager eventManager, EventManagementWindow eventManagementWindow, CustomerAccount currentUser, CartManager cartManager, AccountManager accountManager) {
        this.eventManager = eventManager;
        this.eventManagementWindow = eventManagementWindow;
        this.currentUser = currentUser;
        this.cartManager = cartManager;
        this.accountManager = accountManager;
    }

    /**
     * Fügt ein Event mit einer bestimmten Anzahl von Tickets zum Warenkorb hinzu.
     *
     * @param event    Die Veranstaltung, zu der Tickets hinzugefügt werden.
     * @param quantity Die Anzahl der Tickets, die hinzugefügt werden sollen.
     */
    public void addToCart(Event event, int quantity) {
        Cart cart = cartManager.getCart(currentUser);
        cart.addItem(event.getUuid(), quantity);
        cartManager.addCart(currentUser, cart);
    }

    /**
     * Gibt den aktuellen Warenkorb des Benutzers zurück.
     *
     * @return Der aktuelle Warenkorb des Benutzers.
     */
    public Cart getCart() {
        return cartManager.getCart(currentUser);
    }

    /**
     * Leert den Warenkorb des aktuellen Benutzers.
     */
    public void clearCart() {
        Cart cart = cartManager.getCart(currentUser);
        cart.clear();
        cartManager.addCart(currentUser, cart);
    }

    /**
     * Führt den Kauf der im Warenkorb befindlichen Tickets durch.
     */
    public void purchaseCart() {
        Cart cart = cartManager.getCart(currentUser);
        List<BookedTicket> bookedTickets = new ArrayList<>();

        // Überprüfen, ob genügend Tickets verfügbar sind
        for (CartItem item : cart.getItems()) {
            Event event = eventManager.getEventByUuid(item.getEventUuid());
            int quantity = item.getQuantity();
            if (event.getTotalTickets() - event.getSoldTickets() < quantity) {
                JOptionPane.showMessageDialog(null, "Nicht genügend Tickets verfügbar für " + event.getName(), "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Tickets buchen und drucken
        for (CartItem item : cart.getItems()) {
            Event event = eventManager.getEventByUuid(item.getEventUuid());
            int quantity = item.getQuantity();
            event.setSoldTickets(event.getSoldTickets() + quantity);
            BookedTicket bookedTicket = new BookedTicket(event.getUuid(), quantity, event.getPrice());
            currentUser.addBookedTicket(bookedTicket);
            bookedTickets.add(bookedTicket);
            DocumentPrinter.printTickets(event, quantity, currentUser);
        }

        // Beleg drucken
        DocumentPrinter.printReceipt(bookedTickets, currentUser, eventManager);

        // Änderungen speichern
        eventManager.saveEvents();
        accountManager.saveAccounts();

        // Warenkorb abschließen und leeren
        cart.purchase();
        cartManager.addCart(currentUser, cart);
        eventManagementWindow.updateTable();
        JOptionPane.showMessageDialog(null, "Tickets erfolgreich gekauft.", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
    }
}
