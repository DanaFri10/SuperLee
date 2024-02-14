package shared.GUI.warehouseWorker.view;

import shared.GUI.warehouseWorker.controller.LoginController;
import shared.GUI.warehouseWorker.model.LoginModel;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame{
    private JTextField usernameTextField;
    private JPasswordField passwordTextField;
    private JTextField branchTextField;
    private JButton doneButton;
    private LoginController controller;
    private LoginModel model;

    public LoginView() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 300);
        setLayout(new FlowLayout());
        try{
            model = new LoginModel();
        controller = new LoginController(this, model);
        JLabel title = new JLabel("LOGIN");
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font("Calibri", Font.BOLD, 40));
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.add(title, BorderLayout.NORTH);

        Font font = new Font("Calibri", Font.BOLD, 20);

        JPanel labelsPanel = new JPanel();
        labelsPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;

        usernameTextField = new JTextField(20);
        usernameTextField.setFont(font);
        branchTextField = new JTextField(20);
        branchTextField.setFont(font);
        passwordTextField = new JPasswordField(20);
        passwordTextField.setFont(font);
        doneButton = new JButton("LOGIN");
        doneButton.setPreferredSize(new Dimension(300, 40));
        doneButton.setBackground(new Color(255, 255, 255));

        constraints.gridx = 0;
        constraints.gridy = 0;
        JLabel label1 = new JLabel("Username: ");
        label1.setFont(font);
        labelsPanel.add(label1, constraints);
        constraints.gridx = 1;
        constraints.gridy = 0;
        labelsPanel.add(usernameTextField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        JLabel label2 = new JLabel("Password: ");
        label2.setFont(font);
        labelsPanel.add(label2, constraints);
        constraints.gridx = 1;
        constraints.gridy = 1;
        labelsPanel.add(passwordTextField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        JLabel label3 = new JLabel("Branch ID: ");
        label3.setFont(font);
        labelsPanel.add(label3, constraints);
        constraints.gridx = 1;
        constraints.gridy = 2;
        labelsPanel.add(branchTextField, constraints);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints1 = new GridBagConstraints();
        constraints1.fill = GridBagConstraints.HORIZONTAL;

        constraints.gridx = 0;
        constraints.gridy = 0;
        buttonPanel.add(doneButton, constraints1);
        doneButton.setActionCommand("login");

        doneButton.addActionListener(controller);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(labelsPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        }catch (Exception exception){
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void loginSuccessful(int branchId){
        new MainMenuView(branchId);
        dispose();
    }

    public String getUsername()
    {
        return usernameTextField.getText();
    }

    public String getBranch(){
        return branchTextField.getText();
    }

    public String getPassword()
    {
        return new String(passwordTextField.getPassword());
    }
}
