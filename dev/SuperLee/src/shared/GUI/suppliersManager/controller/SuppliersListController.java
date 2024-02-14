package shared.GUI.suppliersManager.controller;

import shared.GUI.suppliersManager.model.SuppliersListModel;
import shared.GUI.suppliersManager.view.EditSupplierView;
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

public class SuppliersListController implements ListSelectionListener, ActionListener, DocumentListener {
    private JList<String> suppliersNames;
    private SuppliersListModel model;
    private SuppliersListView view;
    private String username;

    public SuppliersListController(JList<String> suppliersNames, SuppliersListModel model, SuppliersListView view, String username)
    {
        this.suppliersNames = suppliersNames;
        this.model = model;
        this.view = view;
        this.username = username;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            String selected = suppliersNames.getSelectedValue();
            String selectedId = selected.substring(selected.indexOf('.') + 1, selected.indexOf('-')).trim();
            Supplier selectedSupplier = model.getSupplier(selectedId);
            SupplierView supplierView = new SupplierView(selectedSupplier.getCompanyId(), username);
            supplierView.setVisible(true);
            view.dispose();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        EditSupplierView editView = new EditSupplierView(username, "SuppliersListView", null);
        editView.setVisible(true);
        view.dispose();
    }

    private void filterById()
    {
        String id = view.getFilterById();
        view.displayFilterById(id);
    }

    private void filterByName()
    {
        String name = view.getFilterByName();
        view.displayFilterByName(name);
    }

    private void filterBySupplyArea()
    {
        String supplyArea = view.getFilterBySupplyArea();
        view.displayFilterBySupplyArea(supplyArea);
    }

    private void filter(DocumentEvent e)
    {
        Document source = e.getDocument();
        if(source == view.getFilterByIdDocument()) filterById();
        else if(source == view.getFilterByNameDocument()) filterByName();
        else filterBySupplyArea();
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

