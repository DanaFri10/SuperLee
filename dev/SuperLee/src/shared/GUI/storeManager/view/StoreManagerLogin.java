package shared.GUI.storeManager.view;

import shared.ControllerFactory;
import shared.Database;
import shared.GUI.storeManager.controller.StoreManagerController;
import shared.GUI.storeManager.controller.StoreManagerLoginController;
import shared.GUI.storeManager.model.StoreManagerLoginModel;
import shared.service.StoreManagerService;
import stock.business.StockController;
import suppliers.business.SuppliersController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StoreManagerLogin extends JFrame {
    private JPanel panel;
    private JLabel titleLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField branchIdField;
    private JButton submitButton;
    private JButton backButton;

    private String colorList = "b8d8d8-7a9e9f-4f6367-eef5db-eb7f78";



    public StoreManagerLogin() {
        try {
            initializeComponents();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void initializeComponents() throws Exception {
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);

        titleLabel = new JLabel("Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        panel.add(titleLabel, constraints);

        constraints.gridwidth = 1;
        constraints.gridy = 1;

        panel.add(new JLabel("Username:"), constraints);

        usernameField = new JTextField(15);
        constraints.gridx = 1;
        panel.add(usernameField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(new JLabel("Password:"), constraints);

        passwordField = new JPasswordField(15);
        constraints.gridx = 1;
        panel.add(passwordField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        panel.add(new JLabel("Branch ID:"), constraints);

        branchIdField = new JTextField(15);
        constraints.gridx = 1;
        panel.add(branchIdField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 2;

        submitButton = new JButton("Submit");
        submitButton.setHorizontalAlignment(SwingConstants.LEFT);
        String[] colors = colorList.split("-");
        Color buttonColor = Color.decode("#" + colors[0]);
        submitButton.setBackground(buttonColor);
        submitButton.setForeground(Color.WHITE);
        panel.add(submitButton, constraints);

        constraints.gridy = 5;

        StoreManagerLoginController smlc = null;

        StoreManagerLoginModel model = new StoreManagerLoginModel();

        smlc = new StoreManagerLoginController(model, usernameField, passwordField, branchIdField,  submitButton);
        submitButton.addActionListener(smlc);

        add(panel, BorderLayout.CENTER);

        setTitle("Store Manager Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame on the screen
    }


    public String getUsername() {
        return usernameField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public String getBranchId() {
        return branchIdField.getText();
    }

    public JButton getSubmitButton() {
        return submitButton;
    }

    public void addSubmitButtonListener(ActionListener listener) {
        submitButton.addActionListener(listener);
    }



}
