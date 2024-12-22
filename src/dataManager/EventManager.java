package dataManager;

import utils.Event;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EventManager implements Serializable {
    private List<Event> events;
    private static final String SAVE_DIR = "Saves";
    private static final String FILE_NAME = SAVE_DIR + "/events.ser";

    /**
     * Konstruktor zur Initialisierung des EventManagers und Laden der Veranstaltungen.
     */
    public EventManager() {
        events = new ArrayList<>();
        createSaveDirectory();
        loadEvents();
    }

    /**
     * Erstellt das Verzeichnis zum Speichern der Veranstaltungen, falls es nicht existiert.
     */
    private void createSaveDirectory() {
        File saveDir = new File(SAVE_DIR);
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
    }

    /**
     * Fügt eine neue Veranstaltung hinzu und speichert die Veranstaltungen.
     *
     * @param event Die hinzuzufügende Veranstaltung.
     */
    public void addEvent(Event event) {
        events.add(event);
        saveEvents();
    }

    /**
     * Entfernt eine Veranstaltung und speichert die Veranstaltungen.
     *
     * @param event Die zu entfernende Veranstaltung.
     */
    public void removeEvent(Event event) {
        events.remove(event);
        saveEvents();
    }

    /**
     * Entfernt eine Veranstaltung anhand des Namens und speichert die Veranstaltungen.
     *
     * @param name Der Name der zu entfernenden Veranstaltung.
     */
    public void removeEventByName(String name) {
        removeEvent(getEventByName(name));
    }

    /**
     * Gibt die Liste der Veranstaltungen zurück.
     *
     * @return Die Liste der Veranstaltungen.
     */
    public List<Event> getEvents() {
        return events;
    }

    /**
     * Überprüft, ob eine Veranstaltung mit dem angegebenen Namen existiert.
     *
     * @param name Der Name der Veranstaltung.
     * @return true, wenn die Veranstaltung existiert, andernfalls false.
     */
    public boolean doEventExist(String name) {
        return getEventByName(name) != null;
    }

    /**
     * Gibt eine Veranstaltung anhand ihres Namens zurück.
     *
     * @param name Der Name der Veranstaltung.
     * @return Die Veranstaltung mit dem angegebenen Namen oder null, wenn sie nicht existiert.
     */
    public Event getEventByName(String name) {
        for (Event event : events) {
            if (event.getName().equals(name))
                return event;
        }
        return null;
    }

    /**
     * Gibt eine Veranstaltung anhand ihrer UUID zurück.
     *
     * @param uuid Die UUID der Veranstaltung.
     * @return Die Veranstaltung mit der angegebenen UUID oder null, wenn sie nicht existiert.
     */
    public Event getEventByUuid(UUID uuid) {
        for (Event event : events) {
            if (event.getUuid().equals(uuid)) {
                return event;
            }
        }
        return null;
    }

    /**
     * Speichert die Veranstaltungen in einer Datei.
     */
    public void saveEvents() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(events);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lädt die Veranstaltungen aus einer Datei.
     */
    public void loadEvents() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            events = (List<Event>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            events = new ArrayList<>();
        }
    }
}
