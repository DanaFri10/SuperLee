package shared.GUI.suppliersManager.view;

import shared.GUI.suppliersManager.controller.AddDiscountController;
import shared.GUI.suppliersManager.controller.LoginController;
import shared.GUI.suppliersManager.model.DiscountModel;
import shared.GUI.suppliersManager.model.UserModel;
import shared.service.SuppliersManagerService;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame{
    private JTextField usernameTextField;
    private JTextField passwordTextField;
    private JButton doneButton;
    private LoginController controller;
    private UserModel model;

    public LoginView() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 600);
        setLayout(new FlowLayout());

        this.model = new UserModel();
        controller = new LoginController(model, this);

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
        passwordTextField = new JTextField(20);
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

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints1 = new GridBagConstraints();
        constraints1.fill = GridBagConstraints.HORIZONTAL;

        constraints.gridx = 0;
        constraints.gridy = 0;
        buttonPanel.add(doneButton, constraints1);

        doneButton.addActionListener(controller);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(labelsPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public String getUsername()
    {
        return usernameTextField.getText();
    }

    public String getPassword()
    {
        return passwordTextField.getText();
    }

    public void popUpMessage(String message)
    {
        JPanel panel = new JPanel();
        JOptionPane.showMessageDialog(panel, message, "Added", JOptionPane.INFORMATION_MESSAGE);
        add(panel);
    }
}
