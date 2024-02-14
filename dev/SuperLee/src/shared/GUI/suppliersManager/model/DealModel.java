package shared.GUI.suppliersManager.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import shared.service.Response;
import shared.service.SuppliersManagerService;
import suppliers.business.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DealModel {
    private SuppliersManagerService service;
    private String username;
    private Gson gson;

    public DealModel(String username)
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

    public Deal getDeal(String supplierId)
    {
        Response res = gson.fromJson(service.getDeal(username, supplierId), Response.class);
        if(!res.isError())
        {
            return gson.fromJson(res.getData(), Deal.class);
        }
        return null;
    }

    public List<DealProductInformation> getDealProducts(String supplierId)
    {
        Response res = gson.fromJson(service.getDealProducts(username, supplierId), Response.class);
        if(!res.isError())
        {
            return gson.fromJson(res.getData(), new TypeToken<List<DealProductInformation>>(){}.getType());
        }
        return null;
    }

    public String getDealDiscounts(String supplierId)
    {
        Response res = gson.fromJson(service.getDealDiscounts(username, supplierId), Response.class);
        if(!res.isError())
        {
            return gson.fromJson(res.getData(), new TypeToken<List<String>>(){}.getType());
        }
        return null;
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

}
