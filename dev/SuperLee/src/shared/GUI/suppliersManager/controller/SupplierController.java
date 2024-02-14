package shared.GUI.suppliersManager.controller;

import shared.GUI.suppliersManager.model.SupplierModel;
import shared.GUI.suppliersManager.model.SuppliersListModel;
import shared.GUI.suppliersManager.view.*;
import shared.service.SuppliersManagerService;
import suppliers.business.Supplier;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SupplierController implements ActionListener{
    private SupplierModel model;
    private SupplierView view;
    private Supplier selectedSupplier;
    private String username;

    public SupplierController(SupplierModel model, SupplierView view, Supplier selectedSupplier, String username) {
        this.model = model;
        this.view = view;
        this.selectedSupplier = selectedSupplier;
        this.username = username;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Contacts"))
        {
            ContactListView contactListView = new ContactListView(selectedSupplier.getCompanyId(), username);
            contactListView.setVisible(true);
            view.dispose();
        }
        if(e.getActionCommand().equals("View deal"))
        {
            DealView dealView = new DealView(selectedSupplier.getCompanyId(), username);
            dealView.setVisible(true);
            view.dispose();
        }
        if(e.getActionCommand().equals("Edit"))
        {
            EditSupplierView editSupplierView = new EditSupplierView(username, "SupplierView", selectedSupplier);
            editSupplierView.setVisible(true);
            view.dispose();
        }
        if(e.getActionCommand().equals("Delete"))
        {
            if(view.areYouSurePopUp()) {
                String res = model.deleteSupplier(selectedSupplier.getCompanyId());
                view.deletedPopUpMessage(res);
                view.dispose();
                SuppliersListView listView = new SuppliersListView(username);
                listView.setVisible(true);
            }
        }
        if(e.getActionCommand().equals("History"))
        {
            OrderHistoryView historyView = new OrderHistoryView(selectedSupplier.getCompanyId(), username);
            historyView.setVisible(true);
            view.dispose();
        }
        if(e.getActionCommand().equals("Back"))
        {
            SuppliersListView listView = new SuppliersListView(username);
            listView.setVisible(true);
            view.dispose();
        }
    }


}
