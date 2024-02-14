package shared.GUI.suppliersManager.controller;

import shared.GUI.suppliersManager.model.UserModel;
import shared.GUI.suppliersManager.view.LoginView;
import shared.GUI.suppliersManager.view.SuppliersListView;
import shared.service.Response;
import shared.service.SuppliersManagerService;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginController implements ActionListener {
    private UserModel model;
    private LoginView view;

    public LoginController(UserModel model, LoginView view)
    {
        this.model = model;
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Response res = model.login(view.getUsername(), view.getPassword());
        if(!res.isError())
        {
            SuppliersListView listView = new SuppliersListView(view.getUsername());
            listView.setVisible(true);
            view.dispose();
        }
        else
        {
            view.popUpMessage(res.getErrorMessage());
        }
    }
}
