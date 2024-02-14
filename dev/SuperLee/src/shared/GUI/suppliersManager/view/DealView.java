package shared.GUI.suppliersManager.view;

import shared.GUI.suppliersManager.controller.DealController;
import shared.GUI.suppliersManager.controller.SupplierController;
import shared.GUI.suppliersManager.model.DealModel;
import shared.GUI.suppliersManager.model.SupplierModel;
import shared.service.SuppliersManagerService;
import suppliers.business.*;

import javax.swing.*;
import java.awt.*;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DealView extends JFrame{
    private DealModel model;
    private DealController controller;
    private JLabel deliveryDaysLabel;
    private JLabel deliversLabel;
    private JLabel daysToDeliverLabel;
    private JLabel paymentMethodLabel;
    private JButton dealProductsButton;
    private JButton periodicOrdersButton;
    private JButton dealDiscountsButton;
    private JButton backButton;

    public DealView(String supplierId, String username) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 600);
        setLayout(new FlowLayout());

        JLabel title = new JLabel("DEAL CARD");
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

        deliveryDaysLabel = new JLabel();
        deliversLabel = new JLabel();
        daysToDeliverLabel = new JLabel();
        paymentMethodLabel = new JLabel();
        dealDiscountsButton = new JButton("DEAL DISCOUNTS");
        dealProductsButton = new JButton("DEAL PRODUCTS");
        periodicOrdersButton = new JButton("PERIODIC ORDERS");

        deliveryDaysLabel.setFont(font);
        deliversLabel.setFont(font);
        daysToDeliverLabel.setFont(font);
        paymentMethodLabel.setFont(font);
        dealDiscountsButton.setFont(font);
        dealDiscountsButton.setPreferredSize(new Dimension(300, 40));
        dealDiscountsButton.setBackground(new Color(255, 255, 255));
        dealProductsButton.setFont(font);
        dealProductsButton.setPreferredSize(new Dimension(300, 40));
        dealProductsButton.setBackground(new Color(255, 255, 255));
        periodicOrdersButton.setFont(font);
        periodicOrdersButton.setPreferredSize(new Dimension(300, 40));
        periodicOrdersButton.setBackground(new Color(255, 255, 255));

        JPanel labelsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(5, 5, 5, 5);

        constraints.gridx = 0;
        constraints.gridy = 0;
        labelsPanel.add(deliveryDaysLabel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        labelsPanel.add(deliversLabel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        labelsPanel.add(daysToDeliverLabel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        labelsPanel.add(paymentMethodLabel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        labelsPanel.add(dealProductsButton, constraints);

        constraints.gridx = 0;
        constraints.gridy = 5;
        labelsPanel.add(periodicOrdersButton, constraints);

        constraints.gridx = 0;
        constraints.gridy = 6;
        labelsPanel.add(dealDiscountsButton, constraints);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(backButtonPanel, BorderLayout.NORTH);
        mainPanel.add(titlePanel, BorderLayout.CENTER);
        mainPanel.add(labelsPanel, BorderLayout.SOUTH);

        add(mainPanel);

        this.model = new DealModel(username);
        this.controller = new DealController(supplierId, model, this, username);

        dealProductsButton.setActionCommand("DealProducts");
        periodicOrdersButton.setActionCommand("PeriodicOrders");
        dealDiscountsButton.setActionCommand("DealDiscounts");
        backButton.setActionCommand("Back");

        dealProductsButton.addActionListener(controller);
        periodicOrdersButton.addActionListener(controller);
        dealDiscountsButton.addActionListener(controller);
        backButton.addActionListener(controller);

        displayDeal(supplierId);
    }

    public void displayDeal(String supplierId) {
        Deal deal = model.getDeal(supplierId);

        List<String> deliveryDaysArr = new ArrayList<>();
        for(int i = 0; i < deal.getDeliveryDays().length ; i++)
        {
            if(deal.getDeliveryDays()[i])
            {
                String dayName = DayOfWeek.of(i + 1).name().toLowerCase();
                deliveryDaysArr.add(dayName.substring(0, 1).toUpperCase() + dayName.substring(1));
            }
        }
        deliveryDaysLabel.setText(deliveryDaysArr.stream().collect(Collectors.joining(", ")));

        deliversLabel.setText("Delivers: " + deal.getDelivers());
        if(!deal.hasFixedDays()) {
            daysToDeliverLabel.setText("Days to deliver: " + deal.getDaysToDeliver());
        }
        paymentMethodLabel.setText("Payment method: " + deal.getPaymentMethod());
    }


}




