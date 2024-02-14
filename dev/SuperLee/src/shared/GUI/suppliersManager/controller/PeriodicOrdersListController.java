package shared.GUI.suppliersManager.controller;

import shared.GUI.suppliersManager.model.DealProductsListModel;
import shared.GUI.suppliersManager.model.PeriodicOrdersListModel;
import shared.GUI.suppliersManager.view.*;
import shared.service.Response;
import shared.service.SuppliersManagerService;
import suppliers.business.Deal;
import suppliers.business.DealProductInformation;
import suppliers.business.PeriodicOrderAgreement;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Document;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PeriodicOrdersListController implements ListSelectionListener, ActionListener, DocumentListener {
    private String supplierId;
    private JList<String> orders;
    private PeriodicOrdersListModel ordersListModel;
    private PeriodicOrdersListView view;
    private String username;

    public PeriodicOrdersListController(String supplierId, JList<String> orders, PeriodicOrdersListModel ordersListModel, PeriodicOrdersListView view, String username)
    {
        this.supplierId = supplierId;
        this.orders = orders;
        this.ordersListModel = ordersListModel;
        this.view = view;
        this.username = username;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            String selected = orders.getSelectedValue();
            int branchId = Integer.parseInt(selected.substring(selected.indexOf(':') + 1).trim());
            PeriodicOrderAgreement selectedOrder = ordersListModel.getPeriodicOrder(supplierId, branchId);
            PeriodicOrderView view = new PeriodicOrderView(supplierId, branchId, username);
            view.setVisible(true);
            //suppliersListView.dispose();
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Back"))
        {
            DealView dealView = new DealView(supplierId, username);
            dealView.setVisible(true);
            view.dispose();
        }
        else {
            EditPeriodicOrderView editPeriodicOrderView = new EditPeriodicOrderView(supplierId, -1, username, "OrderListView");
            editPeriodicOrderView.setVisible(true);
            view.dispose();
        }
    }

    private void filter(DocumentEvent e)
    {
        String id = view.getFilterByBranchId();
        view.displayFilterByBranchId(id);
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        filter(e);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        filter(e);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        filter(e);
    }

}
