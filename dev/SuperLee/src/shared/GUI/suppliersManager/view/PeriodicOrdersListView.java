package shared.GUI.suppliersManager.view;

import shared.GUI.suppliersManager.controller.DealProductsListController;
import shared.GUI.suppliersManager.controller.PeriodicOrdersListController;
import shared.GUI.suppliersManager.model.DealProductsListModel;
import shared.GUI.suppliersManager.model.PeriodicOrdersListModel;
import shared.service.SuppliersManagerService;
import suppliers.business.Contact;
import suppliers.business.DealProductInformation;
import suppliers.business.PeriodicOrderAgreement;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class PeriodicOrdersListView extends JFrame{
    private PeriodicOrdersListModel model;
    private PeriodicOrdersListController controller;
    private JList<String> orders;
    private JButton addOrderButton;
    private JButton backButton;
    private JTextField filterByBranch;
    private String supplierId;

    public PeriodicOrdersListView(String supplierId, String username) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 600);
        setLayout(new BorderLayout());

        Font font = new Font("Calibri", Font.BOLD, 20);

        JPanel titlePanel = new JPanel();
        backButton = new JButton("\u2190");
        titlePanel.add(backButton);
        JLabel title = new JLabel("PERIODIC ORDERS");
        title.setFont(new Font("Calibri", Font.BOLD, 40));
        backButton.setBackground(new Color(255, 255, 255));
        backButton.setFont(font);
        titlePanel.add(title);
        add(titlePanel, BorderLayout.NORTH);

        this.supplierId = supplierId;
        this.orders = new JList<>();
        orders.setFont(font);
        JScrollPane scrollPane = new JScrollPane(orders);
        add(scrollPane, BorderLayout.CENTER);

        this.model = new PeriodicOrdersListModel(username);
        this.controller = new PeriodicOrdersListController(supplierId, orders, model, this, username);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(5, 5, 5, 5);

        constraints.gridx = 0;
        constraints.gridy = 0;
        JLabel label1 = new JLabel("Filter by branch: ");
        label1.setFont(font);
        buttonPanel.add(label1, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        filterByBranch = new JTextField(20);
        filterByBranch.getDocument().addDocumentListener(controller);
        filterByBranch.setFont(font);
        buttonPanel.add(filterByBranch, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        addOrderButton = new JButton("NEW PERIODIC ORDER");
        addOrderButton.setFont(font);
        addOrderButton.setPreferredSize(new Dimension(300, 40));
        addOrderButton.setBackground(new Color(255, 255, 255));
        addOrderButton.setFont(font);
        buttonPanel.add(addOrderButton, constraints);

        displayPeriodicOrders(model.getPeriodicOrders(supplierId));

        orders.addListSelectionListener(controller);
        addOrderButton.addActionListener(controller);
        backButton.setActionCommand("Back");
        backButton.addActionListener(controller);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void displayPeriodicOrders(Map<Integer, PeriodicOrderAgreement> periodicOrders)
    {
        int counter = 1;
        DefaultListModel<String> orderBranches = new DefaultListModel<>();
        for (int branch : periodicOrders.keySet()) {
            orderBranches.addElement(counter + ". Branch number: " + branch);
            counter++;
        }
        orders.setModel(orderBranches);
    }

    public void displayFilterByBranchId(String branchId) {
        Map<Integer, PeriodicOrderAgreement> orders = model.filterByBranch(supplierId, branchId);
        displayPeriodicOrders(orders);
    }

    public String getFilterByBranchId()
    {
        return filterByBranch.getText();
    }

}
