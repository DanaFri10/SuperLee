package shared.GUI.suppliersManager.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import shared.service.Response;
import shared.service.SuppliersManagerService;
import stock.business.Product;
import suppliers.business.Contact;
import suppliers.business.Deal;
import suppliers.business.DealProductInformation;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class DealProductModel {
    private SuppliersManagerService service;
    private String username;
    private Gson gson;

    public DealProductModel(String username)
    {
        try {
            this.service = SuppliersManagerService.getService();
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        this.username = username;
        this.gson = new Gson();
    }

    public DealProductInformation getDealProduct(String supplierId, int catNum)
    {
        Response res = gson.fromJson(service.getDealProduct(username, supplierId, catNum), Response.class);
        if(!res.isError())
        {
            return gson.fromJson(res.getData(), DealProductInformation.class);
        }
        return null;
    }

    public String getProductName(int productId)
    {
        Response res = gson.fromJson(service.getProductName(username, productId), Response.class);
        if(!res.isError())
        {
            return res.getData();
        }
        return null;
    }

    public String getDiscountsDescription(String supplierId, int catNum)
    {
        Response res = gson.fromJson(service.getProductDiscountsDescription(username, supplierId, catNum), Response.class);
        if(!res.isError())
        {
            return res.getData();
        }
        return null;
    }

    public Response addDealProduct(String supplierId, int catNum, int productId, double price, int amount)
    {
        return gson.fromJson(service.addDealProduct(username, supplierId, catNum, productId, price, amount), Response.class);
    }

    public Response editDealProduct(String supplierId, int catNum, double price, int amount)
    {
        return gson.fromJson(service.updateDealProduct(username, supplierId, catNum, price, amount), Response.class);
    }

    public Response removeProductFromDeal(String supplierId, int catNum)
    {
        return gson.fromJson(service.removeDealProduct(username, supplierId, catNum), Response.class);
    }

    public String[] getProductComplement(String supplierId) {
        Response res = gson.fromJson(service.getProductsComplement(username, supplierId), Response.class);
        if(!res.isError())
        {
            return gson.fromJson(res.getData(), new TypeToken<String[]>(){}.getType());
        }
        return null;
    }

    /*
    public List<Product> getProductsById(String supplierId, String productId)
    {
        List<Product> products = getProductComplement(supplierId);
        if(productId.equals("")) return products;
        List<Product> filtered = new ArrayList<>();

        for(Product product : products)
        {
            String idString = product.getProductId() + "";
            if(idString.contains(productId))
                filtered.add(product);
        }
        return filtered;
    }

    public List<Product> getProductsByName(String supplierId, String name)
    {
        List<Product> products = getProductComplement(supplierId);
        if(name.equals("")) return products;
        List<Product> filtered = new ArrayList<>();

        for(Product product : products)
        {
            if(product.getName().toLowerCase().contains(name.toLowerCase()))
                filtered.add(product);
        }
        return filtered;
    }*/
}
