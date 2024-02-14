package shared.GUI.suppliersManager.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import shared.service.Response;
import shared.service.SuppliersManagerService;
import suppliers.business.*;

import javax.swing.*;
import java.util.*;

public class PeriodicOrderModel {
    private SuppliersManagerService service;
    private String username;
    private Gson gson;

    public PeriodicOrderModel(String username) {
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

    public String getContactName(String contactId)
    {
        Response res = gson.fromJson(service.getContact(username, contactId), Response.class);
        if(!res.isError())
        {
            return gson.fromJson(res.getData(), Contact.class).getName();
        }
        return null;
    }

    public String getPeriodicOrderProductsDescription(String supplierId, int catNum)
    {
        Response res = gson.fromJson(service.getPeriodicOrderProductsDescription(username, supplierId, catNum), Response.class);
        if(!res.isError())
        {
            return res.getData();
        }
        return null;
    }

    public String[] getSupplierContacts(String supplierId)
    {
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

    public PeriodicOrderAgreement getPeriodicOrder(String supplierId, int branchId)
    {
        Response res = gson.fromJson(service.getPeriodicOrders(username, supplierId), Response.class);
        if(!res.isError())
        {
            Map<Integer, PeriodicOrderAgreement> periodics = gson.fromJson(res.getData(), new TypeToken<Map<Integer, PeriodicOrderAgreement>>(){}.getType());
            if(periodics.containsKey(branchId))
                return periodics.get(branchId);
            return null;
        }
        return null;
    }

    public Response setPeriodicOrderProducts(String supplierId, int branchId, Map<Integer, Integer> products, boolean newOrder)
    {
        Map<Integer, Integer> productIds = new HashMap<>();
        for(int catalouge : products.keySet())
        {
            DealProductModel model = new DealProductModel(username);
            DealProductInformation dealProductInformation = model.getDealProduct(supplierId, catalouge);
            productIds.put(dealProductInformation.getProductId(), products.get(catalouge));
        }

        return gson.fromJson(service.setPeriodicOrderProducts(username, supplierId, branchId, productIds, newOrder), Response.class);
    }

    public List<DealProductInformation> getUnusedProducts(String supplierId, List<Integer> used)
    {
        Response res = gson.fromJson(service.getUnusedDealProducts(username, supplierId, used), Response.class);
        if(!res.isError())
        {
            return gson.fromJson(res.getData(), new TypeToken<LinkedList<DealProductInformation>>(){}.getType());
        }
        return null;
    }

    public Response setPeriodicOrderContact(String supplierId, int branchId, String contactId)
    {
        return gson.fromJson(service.setPeriodicOrderContact(username, supplierId, branchId, contactId), Response.class);
    }

    public Response setPeriodicOrderDays(String supplierId, int branchId, boolean[] days)
    {
        return gson.fromJson(service.setPeriodicOrderOrderDays(username, supplierId, branchId, days), Response.class);
    }

    public List<DealProductInformation> getProductsByName(String name) {
        /*
        List<DealProductInformation> products = get();
        if(name.equals("")) return suppliers;

        List<Supplier> filtered = new ArrayList<>();

        for(Supplier supplier : suppliers)
        {
            if(supplier.getCompanyName().toLowerCase().contains(name.toLowerCase()))
                filtered.add(supplier);
        }
        return filtered;*/
        return null;
    }

}
