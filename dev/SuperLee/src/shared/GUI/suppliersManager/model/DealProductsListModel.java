package shared.GUI.suppliersManager.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import shared.service.Response;
import shared.service.SuppliersManagerService;
import stock.business.Product;
import suppliers.business.Contact;
import suppliers.business.DealProductInformation;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class DealProductsListModel {
    private SuppliersManagerService service;
    private String username;
    private Gson gson;

    public DealProductsListModel(String username)
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

    public List<DealProductInformation> getDealProducts(String supplierId) {
        Response res = gson.fromJson(service.getDealProducts(username, supplierId), Response.class);
        if(!res.isError())
        {
            return gson.fromJson(res.getData(), new TypeToken<List<DealProductInformation>>(){}.getType());
        }
        return null;
    }

    public List<DealProductInformation> getProductsByCatNum(String supplierId, String catNum) {
        List<DealProductInformation> products = getDealProducts(supplierId);
        if(catNum.equals("")) return products;

        List<DealProductInformation> filtered = new ArrayList<>();

        for(DealProductInformation product : products)
        {
            if((product.getCatalogueNum() + "").contains(catNum))
                filtered.add(product);
        }
        return filtered;
    }

    public List<DealProductInformation> getProductsByProductId(String supplierId, String productId) {
        List<DealProductInformation> products = getDealProducts(supplierId);
        if(productId.equals("")) return products;

        List<DealProductInformation> filtered = new ArrayList<>();

        for(DealProductInformation product : products)
        {
            if((product.getProductId() + "").contains(productId))
                filtered.add(product);
        }
        return filtered;
    }

    public List<DealProductInformation> getProductsByName(String supplierId, String name) {
        List<DealProductInformation> products = getDealProducts(supplierId);
        if(name.equals("")) return products;

        List<DealProductInformation> filtered = new ArrayList<>();

        for(DealProductInformation product : products)
        {
            if(getProductName(product.getProductId()).toLowerCase().contains(name.toLowerCase()))
                filtered.add(product);
        }
        return filtered;
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
}
