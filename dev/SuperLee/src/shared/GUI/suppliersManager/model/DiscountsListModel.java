package shared.GUI.suppliersManager.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import shared.service.Response;
import shared.service.SuppliersManagerService;

import javax.swing.*;
import java.util.List;

public class DiscountsListModel {
    private SuppliersManagerService service;
    private String username;
    private Gson gson;

    public DiscountsListModel(String username)
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

    public List<String> getDiscounts(String supplierId) {
        Response res = gson.fromJson(service.getDealDiscountsDescription(username, supplierId), Response.class);
        if(!res.isError())
        {
            return gson.fromJson(res.getData(), new TypeToken<List<String>>(){}.getType());
        }
        return null;
    }

    public List<String> getDiscounts(String supplierId, int catNum) {
        Response res = gson.fromJson(service.getProductDiscounts(username, supplierId, catNum), Response.class);
        if(!res.isError())
        {
            return gson.fromJson(res.getData(), new TypeToken<List<String>>(){}.getType());
        }
        return null;
    }

    public Response clearDealDiscounts(String supplierId)
    {
        return gson.fromJson(service.clearDealDiscounts(username, supplierId), Response.class);
    }

    public Response clearProductDiscounts(String supplierId, int catNum)
    {
        return gson.fromJson(service.clearProductDiscounts(username, supplierId, catNum), Response.class);
    }
}
