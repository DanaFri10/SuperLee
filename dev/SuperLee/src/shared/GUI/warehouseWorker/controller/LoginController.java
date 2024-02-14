package shared.GUI.warehouseWorker.controller;

import shared.GUI.warehouseWorker.model.LoginModel;
import shared.GUI.warehouseWorker.view.LoginView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginController implements ActionListener {
    private LoginModel model;
    private LoginView view;

    public LoginController(LoginView view, LoginModel model){
        this.view = view;
        this.model = model;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("login")){
            try{
                int branchId = Integer.parseInt(view.getBranch());
                if(branchId < 0 || branchId > 9){
                    throw new Exception("Branch doesn't exist");
                }
                model.login(view.getUsername(), view.getPassword());
                view.loginSuccessful(branchId);
                JOptionPane.showMessageDialog(null, "Login successful, Welcome!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }catch (NumberFormatException nfe){
                JOptionPane.showMessageDialog(null, "Branch ID not a number", "Error", JOptionPane.ERROR_MESSAGE);
            }catch (Exception exception){
                JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
