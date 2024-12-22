package utils.Konto;

public class AdminAccount extends Account {
    private static final long serialVersionUID = 1L;

    /**
     * Konstruktor zur Initialisierung eines Admin-Kontos mit Benutzername und Passwort.
     *
     * @param benutzername Der Benutzername des Admin-Kontos.
     * @param passwort Das Passwort des Admin-Kontos.
     */
    public AdminAccount(String benutzername, String passwort) {
        super(benutzername, passwort);
    }
}
