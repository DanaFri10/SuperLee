package shared.GUI.suppliersManager.controller;

import shared.GUI.suppliersManager.model.ContactModel;
import shared.GUI.suppliersManager.model.SupplierModel;
import shared.GUI.suppliersManager.view.*;
import shared.service.SuppliersManagerService;
import suppliers.business.Contact;
import suppliers.business.Supplier;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ContactController implements ActionListener {
    private ContactModel model;
    private ContactView view;
    private Contact selectedContact;
    private String supplierId;
    private String username;

    public ContactController(ContactModel model, ContactView view, Contact selectedContact, String supplierId, String username) {
        this.model = model;
        this.view = view;
        this.selectedContact = selectedContact;
        this.supplierId = supplierId;
        this.username = username;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Edit"))
        {
            EditContactView editView = new EditContactView(supplierId, username, "ContactView", selectedContact);
            editView.setVisible(true);
            view.dispose();
        }
        else if(e.getActionCommand().equals("Delete"))
        {
            if(view.areYouSurePopUp()) {
                String res = model.deleteContact(supplierId, selectedContact.getContactId());
                view.deletedPopUpMessage(res);
                view.dispose();
                ContactListView listView = new ContactListView(supplierId, username);
                listView.setVisible(true);
            }
        }
        else if(e.getActionCommand().equals("Back"))
        {
            ContactListView listView = new ContactListView(supplierId, username);
            listView.setVisible(true);
            view.dispose();
        }
    }
}
