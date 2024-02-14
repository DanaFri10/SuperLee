package shared.GUI.suppliersManager.model;

import com.google.gson.Gson;
import shared.service.Response;
import shared.service.SuppliersManagerService;
import suppliers.business.Deal;

import javax.swing.*;

public class OrderModel {
    private SuppliersManagerService service;
    private String username;
    private Gson gson;

    public OrderModel(String username)
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

    public String getOrderDescription(String supplierId, int orderId)
    {
        Response res = gson.fromJson(service.orderDescription(username, supplierId, orderId), Response.class);
        if(!res.isError())
        {
            return res.getData();
        }
        return null;
    }
}
