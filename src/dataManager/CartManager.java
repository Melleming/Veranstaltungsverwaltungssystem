package dataManager;

import utils.Cart;
import utils.Konto.Account;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class CartManager implements Serializable {
    private Map<String, Cart> carts;
    private static final String SAVE_DIR = "Saves";
    private static final String FILE_NAME = SAVE_DIR + "/carts.ser";

    /**
     * Konstruktor zur Initialisierung des CartManagers und Laden der Warenkörbe.
     */
    public CartManager() {
        carts = new HashMap<>();
        createSaveDirectory();
        loadCarts();
    }

    /**
     * Erstellt das Verzeichnis zum Speichern der Warenkörbe, falls es nicht existiert.
     */
    private void createSaveDirectory() {
        File saveDir = new File(SAVE_DIR);
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
    }

    /**
     * Fügt einen Warenkorb für ein bestimmtes Konto hinzu und speichert die Warenkörbe.
     *
     * @param account Das Konto, dem der Warenkorb hinzugefügt wird.
     * @param cart Der hinzuzufügende Warenkorb.
     */
    public void addCart(Account account, Cart cart) {
        carts.put(account.getBenutzername(), cart);
        saveCarts();
    }

    /**
     * Entfernt den Warenkorb eines bestimmten Kontos und speichert die Warenkörbe.
     *
     * @param account Das Konto, dessen Warenkorb entfernt wird.
     */
    public void removeCart(Account account) {
        carts.remove(account.getBenutzername());
        saveCarts();
    }

    /**
     * Gibt den Warenkorb eines bestimmten Kontos zurück. Falls keiner existiert, wird ein neuer Warenkorb erstellt.
     *
     * @param account Das Konto, dessen Warenkorb zurückgegeben wird.
     * @return Der Warenkorb des angegebenen Kontos.
     */
    public Cart getCart(Account account) {
        return carts.getOrDefault(account.getBenutzername(), new Cart());
    }

    /**
     * Speichert die Warenkörbe in einer Datei.
     */
    private void saveCarts() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(carts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lädt die Warenkörbe aus einer Datei.
     */
    private void loadCarts() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            carts = (Map<String, Cart>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            carts = new HashMap<>();
        }
    }
}
