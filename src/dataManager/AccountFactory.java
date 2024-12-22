package dataManager;

import utils.Konto.Account;
import utils.Konto.CustomerAccount;
import utils.Konto.AdminAccount;

//Factory Pattern
public class AccountFactory {
    /**
     * Erstellt ein Benutzerkonto basierend auf dem angegebenen Typ.
     *
     * @param typ Der Typ des Kontos (z.B. "kunde" oder "admin").
     * @param benutzername Der Benutzername des Kontos.
     * @param passwort Das Passwort des Kontos.
     * @return Das erstellte Konto.
     */
    public static Account createAccount(String typ, String benutzername, String passwort) {
        switch (typ.toLowerCase()) {
            case "kunde":
                return new CustomerAccount(benutzername, passwort);
            case "admin":
                return new AdminAccount(benutzername, passwort);
            default:
                throw new IllegalArgumentException("Unbekannter Kontotyp: " + typ);
        }
    }
}
