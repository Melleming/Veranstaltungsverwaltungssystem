package controller;

import dataManager.AccountManager;
import dataManager.CartManager;
import dataManager.EventManager;
import gui.EventManagementWindow;
import gui.LoginWindow;
import utils.Konto.Account;

public class MainController {
    private AccountManager accountManager;
    private EventManager eventManager;
    private CartManager cartManager;
    private Account currentUser;
    private static MainController mainController;

    public MainController() {
        cartManager = new CartManager();
        accountManager = new AccountManager(cartManager);
        eventManager = new EventManager();
        mainController = this;
    }

    /**
     * Gibt die Singleton-Instanz des MainControllers zurück.
     *
     * @return Die Singleton-Instanz des MainControllers.
     */
    public static MainController getMainController() {
        return mainController;
    }

    /**
     * Zeigt das Login-Fenster an.
     */
    public void showLoginScreen() {
        new LoginWindow();
    }

    /**
     * Zeigt das Fenster zur Veranstaltungsverwaltung an.
     *
     * @param currentUser Der aktuell angemeldete Benutzer.
     */
    public void showEventManagementWindow(Account currentUser) {
        this.currentUser = currentUser;
        new EventManagementWindow(eventManager, currentUser, cartManager);
    }

    /**
     * Gibt den AccountManager zurück.
     *
     * @return Der AccountManager.
     */
    public AccountManager getAccountManager() {
        return accountManager;
    }

    /**
     * Gibt den aktuell angemeldeten Benutzer zurück.
     *
     * @return Der aktuell angemeldete Benutzer.
     */
    public Account getCurrentUser() {
        return currentUser;
    }

    /**
     * Startet die Anwendung und zeigt das Login-Fenster an.
     *
     * @param args Die Befehlszeilenargumente.
     */
    public static void main(String[] args) {
        MainController controller = new MainController();
        controller.showLoginScreen();
    }
}
