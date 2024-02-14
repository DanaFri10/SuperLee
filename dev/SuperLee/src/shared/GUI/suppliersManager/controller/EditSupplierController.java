package shared.GUI.suppliersManager.controller;

import shared.GUI.suppliersManager.model.SupplierModel;
import shared.GUI.suppliersManager.model.SuppliersListModel;
import shared.GUI.suppliersManager.view.EditSupplierView;
import shared.GUI.suppliersManager.view.SupplierView;
import shared.GUI.suppliersManager.view.SuppliersListView;
import shared.service.Response;
import shared.service.SuppliersManagerService;
import suppliers.business.PaymentMethod;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

public class EditSupplierController implements ActionListener, ItemListener {
    private SupplierModel model;
    private EditSupplierView view;
    private String username;
    private String from;

    public EditSupplierController(SupplierModel model, EditSupplierView view, String username, String from)
    {
        this.model = model;
        this.view = view;
        this.username = username;
        this.from = from;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String id = view.getSupplierId();
        if(e.getActionCommand().equals("Done")) {
            String name = view.getSupplierName();
            String location = view.getSupplierLocation();
            String bankAccount = view.getSupplierBankAccount();
            List<String> supplyAreas = view.getSupplierSupplyAreas();
            PaymentMethod pm = view.getSupplierPaymentMethod();
            boolean delivers = view.getDelivers();
            int daysToDeliver = view.getDaysToDeliver();
            boolean[] deliveryDays = view.getDeliveryDays();

            Response res;
            if (from.equals("SuppliersListView")) {
                res = model.addSupplier(id, name, location, bankAccount, pm, supplyAreas, delivers, daysToDeliver, deliveryDays);
                if (!res.isError()) {
                    view.editedPopUpMessage("Supplier was added successfully!");
                }
            } else {
                res = model.updateSupplier(id, name, location, bankAccount, pm, supplyAreas, delivers, daysToDeliver, deliveryDays);
                if (!res.isError()) {
                    view.editedPopUpMessage("Supplier was updated successfully!");
                }
            }

            if (res.isError()) {
                view.editedPopUpMessage(res.getErrorMessage());
            } else {
                view.dispose();
                SuppliersListView listView = new SuppliersListView(username);
                listView.setVisible(true);
                view.dispose();
            }
        }
        else if(e.getActionCommand().equals("Back"))
        {
            if (from.equals("SuppliersListView")) {
                SuppliersListView listView = new SuppliersListView(username);
                listView.setVisible(true);
                view.dispose();
            }
            else {
                SupplierView supplierView = new SupplierView(id, username);
                supplierView.setVisible(true);
                view.dispose();
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            view.enableDeliveryDaysCheckBox();
        } else if (e.getStateChange() == ItemEvent.DESELECTED) {
            view.disableDeliveryDaysCheckBox();
        }
    }
}
