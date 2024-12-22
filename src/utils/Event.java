package utils;

import java.io.Serializable;
import java.util.UUID;

public class Event implements Serializable {
    private UUID uuid;
    private String name;
    private String location;
    private String date;
    private String startTime;
    private String endTime;
    private double price;
    private int totalTickets;
    private int soldTickets;

    /**
     * Konstruktor zur Initialisierung eines Events mit Namen, Ort, Datum, Startzeit, Endzeit, Preis und Gesamtanzahl der Tickets.
     *
     * @param name Der Name des Events.
     * @param location Der Ort des Events.
     * @param date Das Datum des Events.
     * @param startTime Die Startzeit des Events.
     * @param endTime Die Endzeit des Events.
     * @param price Der Preis des Events.
     * @param totalTickets Die Gesamtanzahl der Tickets.
     */
    public Event(String name, String location, String date, String startTime, String endTime, double price, int totalTickets) {
        this.uuid = UUID.randomUUID();
        this.name = name;
        this.location = location;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
        this.totalTickets = totalTickets;
        this.soldTickets = 0;
    }

    /**
     * Gibt die UUID des Events zurück.
     *
     * @return Die UUID des Events.
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Gibt den Namen des Events zurück.
     *
     * @return Der Name des Events.
     */
    public String getName() {
        return name;
    }

    /**
     * Gibt den Ort des Events zurück.
     *
     * @return Der Ort des Events.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Gibt das Datum des Events zurück.
     *
     * @return Das Datum des Events.
     */
    public String getDate() {
        return date;
    }

    /**
     * Gibt die Startzeit des Events zurück.
     *
     * @return Die Startzeit des Events.
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * Gibt die Endzeit des Events zurück.
     *
     * @return Die Endzeit des Events.
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * Gibt den Preis des Events zurück.
     *
     * @return Der Preis des Events.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Gibt die Gesamtanzahl der Tickets des Events zurück.
     *
     * @return Die Gesamtanzahl der Tickets.
     */
    public int getTotalTickets() {
        return totalTickets;
    }

    /**
     * Gibt die Anzahl der bereits verkauften Tickets des Events zurück.
     *
     * @return Die Anzahl der bereits verkauften Tickets.
     */
    public int getSoldTickets() {
        return soldTickets;
    }

    /**
     * Setzt die Anzahl der bereits verkauften Tickets des Events.
     *
     * @param soldTickets Die Anzahl der verkauften Tickets.
     */
    public void setSoldTickets(int soldTickets) {
        this.soldTickets = soldTickets;
    }

    /**
     * Setzt den Namen des Events.
     *
     * @param name Der neue Name des Events.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Setzt den Ort des Events.
     *
     * @param location Der neue Ort des Events.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Setzt das Datum des Events.
     *
     * @param date Das neue Datum des Events.
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Setzt die Startzeit des Events.
     *
     * @param startTime Die neue Startzeit des Events.
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * Setzt die Endzeit des Events.
     *
     * @param endTime Die neue Endzeit des Events.
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * Setzt den Preis des Events.
     *
     * @param price Der neue Preis des Events.
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Setzt die Gesamtanzahl der Tickets des Events.
     *
     * @param totalTickets Die neue Gesamtanzahl der Tickets.
     */
    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }
}
