package utils.Konto;

import java.io.Serializable;

public abstract class Account implements Serializable {
    private static final long serialVersionUID = 1L;
    protected String benutzername;
    protected String passwort;

    /**
     * Konstruktor zur Initialisierung eines Kontos mit Benutzername und Passwort.
     *
     * @param benutzername Der Benutzername des Kontos.
     * @param passwort Das Passwort des Kontos.
     */
    public Account(String benutzername, String passwort) {
        this.benutzername = benutzername;
        this.passwort = passwort;
    }

    /**
     * Gibt den Benutzernamen des Kontos zurück.
     *
     * @return Der Benutzername.
     */
    public String getBenutzername() {
        return benutzername;
    }

    /**
     * Gibt das Passwort des Kontos zurück.
     *
     * @return Das Passwort.
     */
    public String getPasswort() {
        return passwort;
    }

    /**
     * Setzt den Benutzernamen des Kontos.
     *
     * @param benutzername Der neue Benutzername.
     */
    public void setBenutzername(String benutzername) {
        this.benutzername = benutzername;
    }

    /**
     * Setzt das Passwort des Kontos.
     *
     * @param passwort Das neue Passwort.
     */
    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    /**
     * Gibt eine String-Darstellung des Kontos zurück.
     *
     * @return Eine String-Darstellung des Kontos.
     */
    @Override
    public String toString() {
        return "Benutzername: " + benutzername;
    }
}
