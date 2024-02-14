package shared.GUI.suppliersManager.controller;

import shared.GUI.suppliersManager.model.ContactModel;
import shared.GUI.suppliersManager.model.SupplierModel;
import shared.GUI.suppliersManager.view.*;
import shared.service.Response;
import shared.service.SuppliersManagerService;
import suppliers.business.PaymentMethod;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.util.List;

public class EditContactController implements ActionListener {
    private ContactModel model;
    private EditContactView view;
    private String username;
    private String from;
    private String supplierId;

    public EditContactController(String supplierId, ContactModel model, EditContactView view, String username, String from)
    {
        this.model = model;
        this.view = view;
        this.username = username;
        this.from = from;
        this.supplierId = supplierId;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String id = view.getId();
        if(e.getActionCommand().equals("Done")) {
            String name = view.getName();
            String location = view.getContactLocation();
            String phoneNumber = view.getPhoneNumber();
            String email = view.getEmail();

            Response res;
            if (from.equals("ContactListView")) {
                res = model.addContact(supplierId, id, name, location, phoneNumber, email);
                if (!res.isError()) {
                    view.editedPopUpMessage("Contact was added successfully!");
                }
            } else {
                res = model.updateContact(id, name, location, phoneNumber, email);
                if (!res.isError()) {
                    view.editedPopUpMessage("Contact was updated successfully!");
                }
            }

            if (res.isError()) {
                view.editedPopUpMessage(res.getErrorMessage());
            } else {
                view.dispose();
                ContactListView listView = new ContactListView(supplierId, username);
                listView.setVisible(true);
                view.dispose();
            }
        }
        else if(e.getActionCommand().equals("Back"))
        {
            if (from.equals("ContactListView")) {
                ContactListView contactListView = new ContactListView(supplierId, username);
                contactListView.setVisible(true);
                view.dispose();
            }
            else {
            ContactView contactView = new ContactView(id, supplierId, username);
            contactView.setVisible(true);
            view.dispose();
            }
        }
    }

}
