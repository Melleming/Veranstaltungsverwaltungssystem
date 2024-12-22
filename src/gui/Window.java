package gui;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Window {
    private static List<Window> windows = new ArrayList<>();
    protected JFrame frame;

    /**
     * Konstruktor zur Initialisierung eines Fensters mit Titel, Breite und Höhe.
     *
     * @param title Der Titel des Fensters.
     * @param width Die Breite des Fensters.
     * @param height Die Höhe des Fensters.
     */
    public Window(String title, int width, int height) {
        frame = new JFrame(title);
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Fenster in der Mitte des Bildschirms anzeigen
        windows.add(this);
    }

    /**
     * Zeigt das Fenster an.
     */
    public void show() {
        frame.setVisible(true);
    }

    /**
     * Gibt eine Liste aller offenen Fenster zurück.
     *
     * @return Eine Liste der offenen Fenster.
     */
    public static List<Window> getWindows() {
        return windows;
    }
}
