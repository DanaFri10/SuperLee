package shared.GUI.suppliersManager.view;

import shared.GUI.suppliersManager.controller.DealProductController;
import shared.GUI.suppliersManager.controller.PeriodicOrderController;
import shared.GUI.suppliersManager.model.DealProductModel;
import shared.GUI.suppliersManager.model.PeriodicOrderModel;
import shared.service.SuppliersManagerService;
import suppliers.business.DealProductInformation;
import suppliers.business.PeriodicOrderAgreement;

import javax.swing.*;
import java.awt.*;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PeriodicOrderView extends JFrame{
    private PeriodicOrderModel model;
    private PeriodicOrderController controller;
    private String supplier;
    private int branchId;
    private JLabel productsLabel;
    private JLabel orderDaysLabel;
    private JLabel contactLabel;
    private JButton editButton;
    private JButton deleteButton;
    private JButton backButton;

    public PeriodicOrderView(String supplier, int branchId, String username) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 600);
        setLayout(new FlowLayout());

        this.model = new PeriodicOrderModel(username);
        this.controller = new PeriodicOrderController(supplier, branchId, this, username);
        this.supplier = supplier;
        this.branchId = branchId;

        JLabel title = new JLabel("PERIODIC ORDER");
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setFont(new Font("Calibri", Font.BOLD, 40));
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.add(title, BorderLayout.CENTER);

        Font font = new Font("Calibri", Font.BOLD, 20);

        backButton = new JButton("\u2190");
        backButton.setActionCommand("Back");
        backButton.setBackground(new Color(255, 255, 255)); // A1C2F1
        backButton.setFont(font);
        backButton.addActionListener(controller);
        JPanel backButtonPanel = new JPanel(new BorderLayout());
        backButtonPanel.add(backButton, BorderLayout.WEST);

        productsLabel = new JLabel();
        orderDaysLabel = new JLabel();
        contactLabel = new JLabel();
        editButton = new JButton("EDIT");
        deleteButton = new JButton("DELETE");

        productsLabel.setFont(font);
        orderDaysLabel.setFont(font);
        contactLabel.setFont(font);
        editButton.setFont(font);
        editButton.setPreferredSize(new Dimension(300, 40));
        editButton.setBackground(new Color(255, 255, 255));
        deleteButton.setFont(font);
        deleteButton.setPreferredSize(new Dimension(300, 40));
        deleteButton.setBackground(new Color(255, 255, 255));

        JPanel labelsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(5, 5, 5, 5);

        constraints.gridx = 0;
        constraints.gridy = 0;
        labelsPanel.add(productsLabel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        labelsPanel.add(orderDaysLabel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        labelsPanel.add(contactLabel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        labelsPanel.add(editButton, constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        labelsPanel.add(deleteButton, constraints);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(backButtonPanel, BorderLayout.NORTH);
        mainPanel.add(titlePanel, BorderLayout.CENTER);
        mainPanel.add(labelsPanel, BorderLayout.SOUTH);
        add(mainPanel);

        editButton.setActionCommand("Edit");
        deleteButton.setActionCommand("Delete");
        backButton.setActionCommand("Back");

        editButton.addActionListener(controller);
        deleteButton.addActionListener(controller);
        deleteButton.addActionListener(controller);


        displayPeriodicOrder(model.getPeriodicOrder(supplier, branchId));
    }

    public void displayPeriodicOrder(PeriodicOrderAgreement order)
    {
        String products = model.getPeriodicOrderProductsDescription(supplier, branchId);
        products = products.replaceAll("\n", "<br>") ;
        productsLabel.setText("<html>Periodic order products: " + products + "</html>");

        List<String> OrderDaysArr = new ArrayList<>();
        for(int i = 0; i < order.getOrderDays().length ; i++)
        {
            if(order.getOrderDays()[i])
            {
                String dayName = DayOfWeek.of(i + 1).name().toLowerCase();
                OrderDaysArr.add(dayName.substring(0, 1).toUpperCase() + dayName.substring(1));
            }
        }
        orderDaysLabel.setText(OrderDaysArr.stream().collect(Collectors.joining(", ")));
        contactLabel.setText("Order contact: " + model.getContactName(order.getAssignedContactId()));
    }

    public boolean areYouSurePopUp()
    {
        return JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this order?", "Confirmation", JOptionPane.YES_NO_OPTION);
    }
}
