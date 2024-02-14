package shared.GUI.suppliersManager.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import shared.service.Response;
import shared.service.SuppliersManagerService;
import suppliers.business.Contact;
import suppliers.business.Order;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderHistoryModel {
    private SuppliersManagerService service;
    private String username;
    private Gson gson;

    public OrderHistoryModel(String username)
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

    public List<String> orderIdsAndDates(String supplierId)
    {
        Response res = gson.fromJson(service.getOrderIdsAndDates(username, supplierId), Response.class);
        if(!res.isError())
        {
            return gson.fromJson(res.getData(), new TypeToken<List<String>>() {}.getType());
        }
        return null;
    }

    public List<String> filterById(String supplierId, String id)
    {
        List<String> orders = orderIdsAndDates(supplierId);
        if(id.equals("")) return orders;

        List<String> filtered = new ArrayList<>();

        for(String order : orders)
        {
            String orderId = order.substring(0, order.indexOf('-')).trim();
            if(orderId.contains(id))
                filtered.add(order);
        }

        return filtered;
    }

    public List<String> filterByDate(String supplierId, String dateFilter)
    {
        List<String> orders = orderIdsAndDates(supplierId);
        if(dateFilter.equals("")) return orders;

        List<String> filtered = new ArrayList<>();

        for(String order : orders)
        {
            String date = order.substring(order.indexOf('-') + 1, 14).trim();
            if(date.contains(dateFilter))
                filtered.add(order);
        }

        return filtered;
    }
}
