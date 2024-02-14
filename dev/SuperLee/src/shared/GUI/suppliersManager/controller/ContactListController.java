package shared.GUI.suppliersManager.controller;

import shared.GUI.suppliersManager.model.ContactListModel;
import shared.GUI.suppliersManager.model.SuppliersListModel;
import shared.GUI.suppliersManager.view.*;
import shared.service.Response;
import shared.service.SuppliersManagerService;
import suppliers.business.Contact;
import suppliers.business.Supplier;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Document;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ContactListController implements ListSelectionListener, ActionListener, DocumentListener {
    private String supplierId;
    private JList<String> contactsNames;
    private ContactListModel model;
    private ContactListView view;
    private String username;

    public ContactListController(String supplierId, JList<String> contactsNames, ContactListModel model, ContactListView view, String username)
    {
        this.supplierId = supplierId;
        this.contactsNames = contactsNames;
        this.model = model;
        this.view = view;
        this.username = username;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            String selected = contactsNames.getSelectedValue();
            String selectedId = selected.substring(selected.indexOf('.') + 1, selected.indexOf('-')).trim();
            ContactView contactView = new ContactView(selectedId, supplierId, username);
            contactView.setVisible(true);
            view.dispose();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("New")) {
            EditContactView editView = new EditContactView(supplierId, username, "ContactListView", null);
            editView.setVisible(true);
            view.dispose();
        }
        else if(e.getActionCommand().equals("Existing"))
        {
            Response res = model.addContact(supplierId, view.getContactId());
            if(res.isError())
            {
                view.popUpMessage(res.getErrorMessage());
            }
            else {
                ContactListView newListView = new ContactListView(supplierId, username);
                newListView.setVisible(true);
                view.dispose();
            }
        }
        else if(e.getActionCommand().equals("Back"))
        {
            SupplierView supplierView = new SupplierView(supplierId, username);
            supplierView.setVisible(true);
            view.dispose();
        }
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


    private void filter(DocumentEvent e)
    {
        Document source = e.getDocument();
        if(source == view.getFilterByIdDocument()) filterById();
        else if(source == view.getFilterByNameDocument()) filterByName();
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
