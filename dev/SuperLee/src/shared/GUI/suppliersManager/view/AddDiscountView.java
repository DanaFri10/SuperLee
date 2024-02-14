package shared.GUI.suppliersManager.view;

import shared.GUI.suppliersManager.controller.AddDiscountController;
import shared.GUI.suppliersManager.model.DiscountModel;
import shared.service.SuppliersManagerService;

import javax.swing.*;
import java.awt.*;

public class AddDiscountView extends JFrame {
    private JTextField percentageTextField;
    private JTextField minTextField;
    private JLabel discountTypeLabel;
    private JButton doneButton;
    private AddDiscountController controller;
    private DiscountModel model;
    private JButton backButton;

    public AddDiscountView(String supplierId, int catNum, int type, String username) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 600);
        setLayout(new FlowLayout());

        this.model = new DiscountModel(username);
        controller = new AddDiscountController(supplierId, catNum, type, model, this, username);

        JLabel title = new JLabel("ADD DISCOUNT");
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font("Calibri", Font.BOLD, 40));
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.add(title, BorderLayout.NORTH);

        Font font = new Font("Calibri", Font.BOLD, 20);

        backButton = new JButton("\u2190");
        backButton.setActionCommand("Back");
        backButton.setBackground(new Color(255, 255, 255)); // A1C2F1
        backButton.setFont(font);
        backButton.addActionListener(controller);
        JPanel backButtonPanel = new JPanel(new BorderLayout());
        backButtonPanel.add(backButton, BorderLayout.WEST);

        JPanel labelsPanel = new JPanel();
        labelsPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;

        percentageTextField = new JTextField(20);
        percentageTextField.setFont(font);
        minTextField = new JTextField(20);
        minTextField.setFont(font);
        discountTypeLabel = new JLabel();
        discountTypeLabel.setFont(font);
        doneButton = new JButton("Done:");
        doneButton.setPreferredSize(new Dimension(300, 40));
        doneButton.setBackground(new Color(255, 255, 255));

        constraints.gridx = 0;
        constraints.gridy = 0;
        JLabel label1 = new JLabel("Percentage: ");
        label1.setFont(font);
        labelsPanel.add(label1, constraints);
        constraints.gridx = 1;
        constraints.gridy = 0;
        labelsPanel.add(percentageTextField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        if(type == 0) discountTypeLabel.setText("Minimum price:");
        else discountTypeLabel.setText("Minimum amount:");
        labelsPanel.add(discountTypeLabel, constraints);
        constraints.gridx = 1;
        constraints.gridy = 1;
        labelsPanel.add(minTextField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        labelsPanel.add(doneButton, constraints);

        doneButton.addActionListener(controller);
        backButton.addActionListener(controller);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(backButtonPanel, BorderLayout.NORTH);
        mainPanel.add(titlePanel, BorderLayout.CENTER);
        mainPanel.add(labelsPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    public double getMinPrice()
    {
        try {
            return Double.parseDouble(minTextField.getText());
        }
        catch (Exception e)
        {
            return -1;
        }
    }

    public int getMinAmount()
    {
        try {
            return Integer.parseInt(minTextField.getText());
        }
        catch (Exception e)
        {
            return -1;
        }
    }

    public double getPercentage()
    {
        try {
            return Double.parseDouble(percentageTextField.getText());
        }
        catch (Exception e)
        {
            return -1;
        }
    }

    public void addedPopUpMessage(String message)
    {
        JPanel panel = new JPanel();
        JOptionPane.showMessageDialog(panel, message, "Added", JOptionPane.INFORMATION_MESSAGE);
        add(panel);
    }
}
