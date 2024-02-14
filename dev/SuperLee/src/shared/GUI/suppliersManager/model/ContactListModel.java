package shared.GUI.suppliersManager.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import shared.service.Response;
import shared.service.SuppliersManagerService;
import suppliers.business.Contact;
import suppliers.business.Supplier;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ContactListModel {
    private SuppliersManagerService service;
    private String username;
    private Gson gson;

    public ContactListModel(String username)
    {
        try {
            this.service = SuppliersManagerService.getService();
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        this.username = username;
        this.gson = new Gson();
    }

    public String[] getContacts(String supplierId) {
        Response res = gson.fromJson(service.listContacts(username, supplierId), Response.class);
        if(!res.isError())
        {
            List<Contact> contacts = gson.fromJson(res.getData(), new TypeToken<List<Contact>>(){}.getType());
            List<String> contactIds = new ArrayList<>();
            for(Contact c : contacts)
                contactIds.add(c.getContactId() + " - " + c.getName());
            return contactIds.toArray(new String[0]);
        }
        return null;
    }

    public String[] getContactsComplement(String supplierId) {
        Response res = gson.fromJson(service.listContactsComplement(username, supplierId), Response.class);
        if(!res.isError())
        {
            return gson.fromJson(res.getData(), new TypeToken<String[]>(){}.getType());
        }
        return null;
    }

    public Response addContact(String supplierId, String contactId)
    {
        return gson.fromJson(service.addExistingContact(username, supplierId, contactId), Response.class);
    }


    public String[] getContactsById(String supplierId, String id, boolean choose) {
        String[] contacts;
        if(!choose) contacts = getContacts(supplierId);
        else contacts = getContactsComplement(supplierId);
        if(id.equals("")) return contacts;

        List<String> filtered = new ArrayList<>();

        for(String contact : contacts)
        {
            String contactId = contact.substring(0, contact.indexOf('-')).trim();
            if(contactId.contains(id))
                filtered.add(contact);
        }
        return filtered.toArray(new String[0]);
    }

    public String[] getContactsByName(String supplierId, String name, boolean choose) {
        String[] contacts;
        if(!choose) contacts = getContacts(supplierId);
        else contacts = getContactsComplement(supplierId);
        if(name.equals("")) return contacts;

        List<String> filtered = new ArrayList<>();

        for(String contact : contacts)
        {
            String contactName = contact.substring(contact.indexOf('-') + 1).trim();
            if(contactName.toLowerCase().contains(name.toLowerCase()))
                filtered.add(contact);
        }
        return filtered.toArray(new String[0]);
    }


}
