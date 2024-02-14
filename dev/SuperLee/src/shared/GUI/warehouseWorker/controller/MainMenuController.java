package shared.GUI.warehouseWorker.controller;

import shared.GUI.warehouseWorker.model.LoginModel;
import shared.GUI.warehouseWorker.model.UsernameStorage;
import shared.GUI.warehouseWorker.view.MainMenuView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuController implements ActionListener {
    private MainMenuView view;

    public MainMenuController(MainMenuView view){
        this.view = view;
    }


    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getActionCommand().equals("explore")){
            view.goExplore();
        }else if(e.getActionCommand().equals("inventory")){
            view.goInventory();
        }else if(e.getActionCommand().equals("order")){
            view.goOrder();
        }else if(e.getActionCommand().equals("shortage")){
            view.goShortage();
        }else if(e.getActionCommand().equals("out")){
            try{
                if(!UsernameStorage.isManager) {
                    UsernameStorage.logoutHandler.logout();
                }
                view.logout();
                if(!UsernameStorage.isManager) {
                    JOptionPane.showMessageDialog(null, "Logout successful, goodbye!", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            }catch (Exception exception){
                JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
