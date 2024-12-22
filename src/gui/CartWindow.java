package gui;

import controller.CartController;
import dataManager.EventManager;
import utils.Cart;
import utils.CartItem;
import utils.Event;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CartWindow extends Window {
    private CartController cartController;
    private EventManager eventManager;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel totalPriceLabel;

    /**
     * Konstruktor zur Initialisierung des Warenkorbfesnters.
     *
     * @param cartController Der Controller zur Verwaltung des Warenkorbs.
     * @param eventManager Der EventManager zur Verwaltung der Veranstaltungen.
     */
    public CartWindow(CartController cartController, EventManager eventManager) {
        super("Warenkorb", 600, 400);
        this.cartController = cartController;
        this.eventManager = eventManager;

        tableModel = new DefaultTableModel(new String[]{"Veranstaltung", "Menge", "Preis (€)"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        totalPriceLabel = new JLabel("Gesamtsumme: €0.00");
        panel.add(totalPriceLabel, BorderLayout.NORTH);

        JButton purchaseButton = new JButton("Kaufen");
        JButton clearButton = new JButton("Leeren");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(purchaseButton);
        buttonPanel.add(clearButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Aktion zum Kauf der Tickets im Warenkorb
        purchaseButton.addActionListener(e -> {
            Cart cart = cartController.getCart();
            if (cart.getItems().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Der Warenkorb ist leer.", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            cartController.purchaseCart();
            frame.dispose();
        });

        // Aktion zum Leeren des Warenkorbs
        clearButton.addActionListener(e -> {
            cartController.clearCart();
            updateTable();
        });

        frame.add(panel);
        show();
        updateTable();
    }

    /**
     * Aktualisiert die Tabelle mit den Artikeln im Warenkorb und berechnet die Gesamtsumme.
     */
    public void updateTable() {
        tableModel.setRowCount(0);
        Cart cart = cartController.getCart();
        for (CartItem item : cart.getItems()) {
            Event event = eventManager.getEventByUuid(item.getEventUuid());
            tableModel.addRow(new Object[]{event.getName(), item.getQuantity(), String.format("%.2f", item.getTotalPrice(eventManager))});
        }
        totalPriceLabel.setText(String.format("Gesamtsumme: €%.2f", cart.getTotalPrice(eventManager)));
    }
}
