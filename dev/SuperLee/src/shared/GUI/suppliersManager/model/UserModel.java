package shared.GUI.suppliersManager.model;

import com.google.gson.Gson;
import shared.service.Response;
import shared.service.SuppliersManagerService;

import javax.swing.*;

public class UserModel {
    private SuppliersManagerService service;
    private Gson gson;

    public UserModel()
    {
                try {
            this.service = SuppliersManagerService.getService();
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, e.getMessage());

        }
        this.gson = new Gson();
    }

    public Response login(String loginUsername, String password)
    {
        return gson.fromJson(service.login(loginUsername, password), Response.class);
    }


}
