package shared.GUI.suppliersManager.controller;

import shared.GUI.suppliersManager.model.ContactListModel;
import shared.GUI.suppliersManager.model.DealProductsListModel;
import shared.GUI.suppliersManager.view.*;
import shared.service.SuppliersManagerService;
import suppliers.business.Contact;
import suppliers.business.DealProductInformation;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Document;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DealProductsListController implements ListSelectionListener, ActionListener, DocumentListener {
    private String supplierId;
    private JList<String> dealProductsCatNums;
    private DealProductsListModel productsListModel;
    private DealProductsListView view;
    private String username;

    public DealProductsListController(String supplierId, JList<String> dealProductsCatNums, DealProductsListModel productsListModel, DealProductsListView view, String username)
    {
        this.supplierId = supplierId;
        this.dealProductsCatNums = dealProductsCatNums;
        this.productsListModel = productsListModel;
        this.view = view;
        this.username = username;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            String selected = dealProductsCatNums.getSelectedValue();
            int selectedCatNum = Integer.parseInt(selected.substring(selected.indexOf('.') + 1, selected.indexOf('-')).trim());
            DealProductInformation selectedProduct = productsListModel.getDealProduct(supplierId, selectedCatNum);
            DealProductView dealProductView = new DealProductView(supplierId, selectedCatNum, username);
            dealProductView.setVisible(true);
            view.dispose();
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
            EditDealProductView editDealProductView = new EditDealProductView(supplierId, -1, username, "DealProductsList");
            editDealProductView.setVisible(true);
            view.dispose();
        }
    }

    private void filterByCatNum()
    {
        String id = view.getFilterByCatNum();
        view.displayFilterByCatNum(id);
    }

    private void filterByProductId()
    {
        String id = view.getFilterByProductId();
        view.displayFilterByProductId(id);
    }

    private void filterByName()
    {
        String name = view.getFilterByName();
        view.displayFilterByName(name);
    }

    private void filter(DocumentEvent e)
    {
        Document source = e.getDocument();
        if(source == view.getFilterByCatNumDocument()) filterByCatNum();
        else if(source == view.getFilterByProductIdDocument()) filterByProductId();
        else filterByName();
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
