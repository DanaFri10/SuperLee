package shared.GUI.suppliersManager.controller;

import shared.GUI.suppliersManager.view.OrderHistoryView;
import shared.GUI.suppliersManager.view.OrderView;
import shared.service.SuppliersManagerService;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OrderController implements ActionListener {
    private String supplierId;
    private OrderView view;
    private String username;

    public OrderController(String supplierId, OrderView view, String username)
    {
        this.supplierId = supplierId;
        this.view = view;
        this.username = username;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        OrderHistoryView historyView = new OrderHistoryView(supplierId, username);
        historyView.setVisible(true);
        view.dispose();
    }
}
