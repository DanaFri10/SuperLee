package shared.GUI.suppliersManager.model;

import com.google.gson.Gson;
import shared.service.Response;
import shared.service.SuppliersManagerService;
import suppliers.business.Contact;
import suppliers.business.PaymentMethod;

import javax.swing.*;
import java.util.List;

public class ContactModel {
    private SuppliersManagerService service;
    private String username;
    private Gson gson;

    public ContactModel(String username)
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

    public Contact getContact(String contactId)
    {
        Response res = gson.fromJson(service.getContact(username, contactId), Response.class);
        if(!res.isError())
        {
            return gson.fromJson(res.getData(), Contact.class);
        }
        return null;
    }

    public Response addContact(String supplierId, String contactId, String name, String location, String phoneNumber, String email)
    {
        return gson.fromJson(service.addNewContact(username, supplierId, contactId, name, phoneNumber, email, location), Response.class);
    }



    public Response updateContact(String contactId, String name, String location, String phoneNumber, String email)
    {
        return gson.fromJson(service.updateContact(username, contactId, name, phoneNumber, email, location), Response.class);
    }

    public String deleteContact(String supplierId, String contactId)
    {
        Response res = gson.fromJson(service.removeContactFromSupplier(username, supplierId, contactId), Response.class);
        if (!res.isError()) {
            return "Contact was removed successfully!";
        }
        return res.getErrorMessage();
    }


}
