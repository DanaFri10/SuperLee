package shared.GUI.suppliersManager.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import shared.service.Response;
import shared.service.SuppliersManagerService;
import suppliers.business.*;

import javax.swing.*;
import java.util.List;
import java.util.Map;

public class SupplierModel {
    private SuppliersManagerService service;
    private String username;
    private Gson gson;

    public SupplierModel(String username)
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

    public Supplier getSupplier(String supplierId)
    {
        Response res = gson.fromJson(service.getSupplier(username, supplierId), Response.class);
        if(!res.isError())
        {
            return gson.fromJson(res.getData(), Supplier.class);
        }
        return null;
    }

    public Deal getDeal(String supplierId)
    {
        Response res = gson.fromJson(service.getDeal(username, supplierId), Response.class);
        if(!res.isError())
        {
            return gson.fromJson(res.getData(), Deal.class);
        }
        return null;
    }

    public List<Contact> getContacts(String supplierId)
    {
        Response res = gson.fromJson(service.listContacts(username, supplierId), Response.class);
        if(!res.isError())
        {
            return gson.fromJson(res.getData(), new TypeToken<List<Contact>>(){}.getType());
        }
        return null;
    }

    public Response addSupplier(String companyId, String companyName, String location, String bankAccount, PaymentMethod paymentMethod, List<String> supplyAreas, boolean delivers, int daysToDeliver, boolean[] deliveryDays)
    {
        return gson.fromJson(service.addSupplier(username, companyId, companyName, location, bankAccount, paymentMethod, supplyAreas, delivers, daysToDeliver, deliveryDays), Response.class);
    }

    public Response updateSupplier(String companyId, String companyName, String location, String bankAccount, PaymentMethod paymentMethod, List<String> supplyAreas, boolean delivers, int daysToDeliver, boolean[] deliveryDays)
    {
        return gson.fromJson(service.updateSupplier(username, companyId, companyName, location, bankAccount, paymentMethod, supplyAreas, delivers, daysToDeliver, deliveryDays), Response.class);
    }

    public String deleteSupplier(String companyId)
    {
        Response res = gson.fromJson(service.removeSupplier(username, companyId), Response.class);
        if (!res.isError()) {
            return "Supplier was removed successfully!";
        }
        return res.getErrorMessage();
    }
}
