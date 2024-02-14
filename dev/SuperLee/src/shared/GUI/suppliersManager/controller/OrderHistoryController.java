package shared.GUI.suppliersManager.controller;

import shared.GUI.suppliersManager.model.OrderHistoryModel;
import shared.GUI.suppliersManager.model.SuppliersListModel;
import shared.GUI.suppliersManager.view.OrderHistoryView;
import shared.GUI.suppliersManager.view.OrderView;
import shared.GUI.suppliersManager.view.SupplierView;
import shared.GUI.suppliersManager.view.SuppliersListView;
import shared.service.SuppliersManagerService;
import suppliers.business.Supplier;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Document;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OrderHistoryController implements ListSelectionListener, ActionListener, DocumentListener {
    private JList<String> orderIdsAndNames;
    private OrderHistoryView view;
    private String username;
    private String supplierId;

    public OrderHistoryController(JList<String> orderIdsAndNames, OrderHistoryView view, String supplierId, String username)
    {
        this.orderIdsAndNames = orderIdsAndNames;
        this.view = view;
        this.supplierId = supplierId;
        this.username = username;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            String selected = orderIdsAndNames.getSelectedValue();
            int selectedId = Integer.parseInt(selected.substring(0, selected.indexOf('-')).trim());
            OrderView orderView = new OrderView(supplierId, selectedId, username);
            orderView.setVisible(true);
            view.dispose();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SupplierView supplierView = new SupplierView(supplierId, username);
        supplierView.setVisible(true);
        view.dispose();
    }

    private void filterById()
    {
        String id = view.getFilterById();
        view.displayFilterById(id);
    }

    private void filterByDate()
    {
        String date = view.getFilterByDate();
        view.displayFilterByDate(date);
    }

    private void filter(DocumentEvent e)
    {
        Document source = e.getDocument();
        if(source == view.getFilterByIdDocument()) filterById();
        else if(source == view.getFilterByDateDocument()) filterByDate();
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
