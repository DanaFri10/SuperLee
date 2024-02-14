package shared.GUI.suppliersManager.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import shared.service.Response;
import shared.service.SuppliersManagerService;
import suppliers.business.Deal;
import suppliers.business.PaymentMethod;
import suppliers.business.Supplier;
import suppliers.dal.SuppliersDAO;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SuppliersListModel{

    private SuppliersManagerService service;
    private String username;
    private Gson gson;

    public SuppliersListModel(String username)
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

    public List<Supplier> getSuppliers() {
        Response res = gson.fromJson(service.listSuppliers(username), Response.class);
        if(!res.isError())
        {
            return gson.fromJson(res.getData(), new TypeToken<List<Supplier>>(){}.getType());
        }
        return null;
    }

    public List<Supplier> getSuppliersById(String id) {
        List<Supplier> suppliers = getSuppliers();
        if(id.equals("")) return suppliers;

        List<Supplier> filtered = new ArrayList<>();

        for(Supplier supplier : suppliers)
        {
            if(supplier.getCompanyId().contains(id))
                filtered.add(supplier);
        }
        return filtered;
    }

    public List<Supplier> getSuppliersByName(String name) {
        List<Supplier> suppliers = getSuppliers();
        if(name.equals("")) return suppliers;

        List<Supplier> filtered = new ArrayList<>();

        for(Supplier supplier : suppliers)
        {
            if(supplier.getCompanyName().toLowerCase().contains(name.toLowerCase()))
                filtered.add(supplier);
        }
        return filtered;
    }

    public List<Supplier> getSuppliersBySupplyArea(String supplyArea) {
        List<Supplier> suppliers = getSuppliers();
        if(supplyArea.equals("")) return suppliers;

        List<Supplier> filtered = new ArrayList<>();

        for(Supplier supplier : suppliers)
        {
            for(String area : supplier.getSupplyAreas()) {
                if (area.toLowerCase().contains(supplyArea.toLowerCase()))
                    if(!filtered.contains(supplier))
                        filtered.add(supplier);
            }
        }
        return filtered;
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

}