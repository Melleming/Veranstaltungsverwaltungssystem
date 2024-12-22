package utils.Konto;

import utils.BookedTicket;

import java.util.ArrayList;
import java.util.List;

public class CustomerAccount extends Account {
    private List<BookedTicket> bookedTickets;

    /**
     * Konstruktor zur Initialisierung eines Kundenkontos mit Benutzername und Passwort.
     *
     * @param benutzername Der Benutzername des Kundenkontos.
     * @param passwort Das Passwort des Kundenkontos.
     */
    public CustomerAccount(String benutzername, String passwort) {
        super(benutzername, passwort);
        this.bookedTickets = new ArrayList<>();
    }

    /**
     * Gibt die Liste der gebuchten Tickets zurück.
     *
     * @return Eine Liste der gebuchten Tickets.
     */
    public List<BookedTicket> getBookedTickets() {
        return bookedTickets;
    }

    /**
     * Fügt ein gebuchtes Ticket zur Liste der gebuchten Tickets hinzu.
     *
     * @param ticket Das hinzuzufügende gebuchte Ticket.
     */
    public void addBookedTicket(BookedTicket ticket) {
        bookedTickets.add(ticket);
    }
}
