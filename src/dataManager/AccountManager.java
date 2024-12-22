package dataManager;

import utils.Cart;
import utils.Konto.AdminAccount;
import utils.Konto.Account;
import utils.Konto.CustomerAccount;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AccountManager {
    private List<Account> accounts;
    private static final String SAVE_DIR = "Saves";
    private static final String FILE_NAME = SAVE_DIR + "/accounts.ser";
    private CartManager cartManager;

    /**
     * Konstruktor zur Initialisierung des AccountManagers mit einem CartManager.
     *
     * @param cartManager Der CartManager zur Verwaltung der Warenkörbe.
     */
    public AccountManager(CartManager cartManager) {
        this.cartManager = cartManager;
        accounts = new ArrayList<>();
        createSaveDirectory();
        loadAccounts();
        ensureAdminAccount();
    }

    /**
     * Erstellt das Verzeichnis zum Speichern der Kontodaten, falls es nicht existiert.
     */
    private void createSaveDirectory() {
        File saveDir = new File(SAVE_DIR);
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
    }

    /**
     * Fügt ein neues Konto hinzu und speichert die Kontodaten.
     *
     * @param account Das hinzuzufügende Konto.
     */
    public void addAccount(Account account) {
        accounts.add(account);
        if (account instanceof CustomerAccount) {
            cartManager.addCart(account, new Cart());
        }
        saveAccounts();
    }

    /**
     * Entfernt ein Konto, sofern es nicht das eigene oder das Admin-Konto ist, und speichert die Kontodaten.
     *
     * @param account Das zu entfernende Konto.
     * @param currentUser Das aktuell angemeldete Konto.
     */
    public void removeAccount(Account account, Account currentUser) {
        if (account.equals(currentUser)) {
            throw new IllegalArgumentException("Sie können Ihr eigenes Konto nicht löschen.");
        }
        if (account.getBenutzername().equals("admin")) {
            throw new IllegalArgumentException("Das Admin-Konto kann nicht gelöscht werden.");
        }
        accounts.remove(account);
        if (account instanceof CustomerAccount) {
            cartManager.removeCart(account);
        }
        saveAccounts();
    }

    /**
     * Gibt ein Konto anhand des Benutzernamens zurück.
     *
     * @param username Der Benutzername des Kontos.
     * @return Das Konto mit dem angegebenen Benutzernamen oder null, wenn es nicht existiert.
     */
    public Account getAccount(String username) {
        for (Account account : accounts) {
            if (account.getBenutzername().toLowerCase().equals(username.toLowerCase())) {
                return account;
            }
        }
        return null;
    }

    /**
     * Gibt die Liste aller Konten zurück.
     *
     * @return Die Liste der Konten.
     */
    public List<Account> getAccounts() {
        return accounts;
    }

    /**
     * Speichert die Kontodaten in einer Datei.
     */
    public void saveAccounts() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(accounts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lädt die Kontodaten aus einer Datei.
     */
    public void loadAccounts() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            accounts = (List<Account>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            accounts = new ArrayList<>();
        }
    }

    /**
     * Stellt sicher, dass ein Admin-Konto existiert, und erstellt es gegebenenfalls.
     */
    private void ensureAdminAccount() {
        boolean adminExists = false;
        for (Account account : accounts) {
            if (account.getBenutzername().equals("admin")) {
                adminExists = true;
                break;
            }
        }
        if (!adminExists) {
            AdminAccount adminAccount = new AdminAccount("admin", "admin");
            accounts.add(adminAccount);
            saveAccounts();
        }
    }
}
