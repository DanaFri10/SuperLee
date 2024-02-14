package shared.GUI.suppliersManager.view;

import shared.GUI.suppliersManager.controller.OrderController;
import shared.GUI.suppliersManager.controller.SupplierController;
import shared.GUI.suppliersManager.model.OrderModel;
import shared.GUI.suppliersManager.model.SupplierModel;
import shared.service.SuppliersManagerService;
import suppliers.business.Order;
import suppliers.business.Supplier;

import javax.swing.*;
import java.awt.*;

public class OrderView extends JFrame{
    private OrderModel model;
    private OrderController controller;
    private JLabel descriptionLabel;
    private JButton backButton;

    public OrderView(String supplierId, int orderId, String username) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 600);
        setLayout(new FlowLayout());

        Font font = new Font("Calibri", Font.BOLD, 20);

        JLabel title = new JLabel("ORDER CARD");
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font("Calibri", Font.BOLD, 40));
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.add(title, BorderLayout.CENTER);

        backButton = new JButton("\u2190");
        backButton.setActionCommand("Back");
        backButton.setBackground(new Color(255, 255, 255)); // A1C2F1
        backButton.setFont(font);
        backButton.addActionListener(controller);
        JPanel backButtonPanel = new JPanel(new BorderLayout());
        backButtonPanel.add(backButton, BorderLayout.WEST);

        this.model = new OrderModel(username);
        this.controller = new OrderController(supplierId, this, username);

        String desc = model.getOrderDescription(supplierId,orderId);
        desc = desc.replaceAll("\n", "<br>") ;
        descriptionLabel = new JLabel("<html>" + desc + "</html>");
        descriptionLabel.setFont(font);

        JPanel labelsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(5, 5, 5, 5);

        constraints.gridx = 0;
        constraints.gridy = 0;
        labelsPanel.add(descriptionLabel, constraints);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(backButtonPanel, BorderLayout.NORTH);
        mainPanel.add(titlePanel, BorderLayout.CENTER);
        mainPanel.add(labelsPanel, BorderLayout.SOUTH);

        add(mainPanel);


        backButton.addActionListener(controller);

    }

}
