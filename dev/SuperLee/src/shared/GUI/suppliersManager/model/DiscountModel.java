package shared.GUI.suppliersManager.model;

import com.google.gson.Gson;
import shared.service.Response;
import shared.service.SuppliersManagerService;

import javax.swing.*;

public class DiscountModel {
    private SuppliersManagerService service;
    private String username;
    private Gson gson;

    public DiscountModel(String username)
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

    public Response addTotalPriceDiscount(String supplierId, double percentage, double minPrice)
    {
        return gson.fromJson(service.addTotalPriceDiscount(username, supplierId, percentage/100, minPrice), Response.class);
    }

    public Response addTotalProductDiscount(String supplierId, double percentage, int minAmount)
    {
        return gson.fromJson(service.addTotalProductsDiscount(username, supplierId, percentage/100, minAmount), Response.class);
    }

    public Response addProductAmountDiscount(String supplierId, int catNum, double percentage, int minAmount)
    {
        return gson.fromJson(service.addProductDiscount(username, supplierId, catNum, percentage/100, minAmount), Response.class);
    }
}
