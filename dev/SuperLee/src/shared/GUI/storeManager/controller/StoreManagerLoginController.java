package shared.GUI.storeManager.controller;

import shared.GUI.storeManager.view.StoreManagerHome;
import shared.service.Response;
import shared.service.StoreManagerService;
import shared.GUI.storeManager.view.StoreManagerLogin;
import shared.GUI.storeManager.model.StoreManagerLoginModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class StoreManagerLoginController implements ActionListener {

    private StoreManagerLoginModel model;
    private JTextField username;
    private JTextField password;
    private JTextField branchId;
    private JButton submitButton;

    public StoreManagerLoginController(StoreManagerLoginModel model, JTextField username, JTextField password, JTextField branchId, JButton submitButton) {
        this.model = model;
        this.username = username;
        this.password = password;
        this.branchId = branchId;
        this.submitButton = submitButton;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            String myUsername = username.getText();
            String myPassowrd = password.getText();

            Response response = model.login(myUsername, myPassowrd);

            if (!response.isError()) {
                // User authenticated, navigate to StoreManagerHome GUI
                JOptionPane.showMessageDialog(null, "Login Successful", "Success", JOptionPane.INFORMATION_MESSAGE);
                StoreManagerHome homeScreen = new StoreManagerHome(username, password, branchId, response);
                homeScreen.setVisible(true);
                // Close the login screen
                SwingUtilities.getWindowAncestor(submitButton).dispose();
            }
            else {
                // Authentication failed, display error message
                JOptionPane.showMessageDialog(null, response.getErrorMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
