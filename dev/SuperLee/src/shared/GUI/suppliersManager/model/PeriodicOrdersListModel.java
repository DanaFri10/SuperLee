package shared.GUI.suppliersManager.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import shared.service.Response;
import shared.service.SuppliersManagerService;
import suppliers.business.DealProductInformation;
import suppliers.business.PeriodicOrderAgreement;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PeriodicOrdersListModel {
    private SuppliersManagerService service;
    private String username;
    private Gson gson;

    public PeriodicOrdersListModel(String username)
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

    public Map<Integer, PeriodicOrderAgreement> getPeriodicOrders(String supplierId) {
        Response res = gson.fromJson(service.getPeriodicOrders(username, supplierId), Response.class);
        if(!res.isError())
        {
            return gson.fromJson(res.getData(), new TypeToken<Map<Integer, PeriodicOrderAgreement>>(){}.getType());
        }
        return null;
    }

    public Map<Integer, PeriodicOrderAgreement> filterByBranch(String supplierId, String filterBranch)
    {
        Map<Integer, PeriodicOrderAgreement> allOrders = getPeriodicOrders(supplierId);
        if(filterBranch.equals("")) return allOrders;

        Map<Integer, PeriodicOrderAgreement> filtered = new HashMap<>();
        for(int branchId : allOrders.keySet())
        {
            if((branchId + "").contains(filterBranch))
                filtered.put(branchId, allOrders.get(branchId));
        }
        return filtered;
    }

    public PeriodicOrderAgreement getPeriodicOrder(String supplierId, int branchId) {
        Response res = gson.fromJson(service.getPeriodicOrder(username, supplierId, branchId), Response.class);
        if(!res.isError())
        {
            return gson.fromJson(res.getData(), PeriodicOrderAgreement.class);
        }
        return null;
    }

    public Response setPeriodicOrderProducts(String supplierId, int branchId, Map<Integer, Integer> productsMap)
    {
        return gson.fromJson(service.setPeriodicOrderProducts(username, supplierId, branchId, productsMap, true), Response.class);
    }

    public Response setPeriodicOrderDays(String supplierId, int branchId, boolean[] days)
    {
        return gson.fromJson(service.setPeriodicOrderOrderDays(username, supplierId, branchId, days), Response.class);
    }

    public Response setPeriodicOrderContact(String supplierId, int branchId, String contactId)
    {
        return gson.fromJson(service.setPeriodicOrderContact(username, supplierId, branchId, contactId), Response.class);
    }
}
