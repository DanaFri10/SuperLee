package shared.GUI.suppliersManager.view;

import shared.GUI.suppliersManager.controller.DealController;
import shared.GUI.suppliersManager.controller.DealProductController;
import shared.GUI.suppliersManager.model.DealModel;
import shared.GUI.suppliersManager.model.DealProductModel;
import shared.service.SuppliersManagerService;
import suppliers.business.DealProductInformation;
import suppliers.business.ProductDiscount;
import suppliers.business.Supplier;
import suppliers.dal.ProductDiscountsDAO;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DealProductView extends JFrame{
    private DealProductModel model;
    private DealProductController controller;
    private JLabel catalogueNumLabel;
    private JLabel priceLabel;
    private JLabel amountLabel;
    private JLabel productNameLabel;
    private String supplierId;
    private JButton editButton;
    private JButton deleteButton;
    private JButton discountsButton;
    private JButton backButton;


    public DealProductView(String supplierId, int catNum, String username) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 600);
        setLayout(new FlowLayout());

        this.model = new DealProductModel(username);
        this.controller = new DealProductController(model, this, supplierId, catNum, username);
        this.supplierId = supplierId;

        JLabel title = new JLabel("DEAL PRODUCT");
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

        catalogueNumLabel = new JLabel();
        priceLabel = new JLabel();
        amountLabel = new JLabel();
        productNameLabel = new JLabel();
        editButton = new JButton("EDIT");
        deleteButton = new JButton("DELETE");
        discountsButton = new JButton("DISCOUNTS");

        catalogueNumLabel.setFont(font);
        priceLabel.setFont(font);
        amountLabel.setFont(font);
        productNameLabel.setFont(font);
        editButton.setFont(font);
        editButton.setPreferredSize(new Dimension(300, 40));
        editButton.setBackground(new Color(255, 255, 255));
        deleteButton.setFont(font);
        deleteButton.setPreferredSize(new Dimension(300, 40));
        deleteButton.setBackground(new Color(255, 255, 255));
        discountsButton.setFont(font);
        discountsButton.setPreferredSize(new Dimension(300, 40));
        discountsButton.setBackground(new Color(255, 255, 255));

        JPanel labelsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(5, 5, 5, 5);

        constraints.gridx = 0;
        constraints.gridy = 0;
        labelsPanel.add(catalogueNumLabel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        labelsPanel.add(priceLabel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        labelsPanel.add(amountLabel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        labelsPanel.add(productNameLabel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        labelsPanel.add(editButton, constraints);

        constraints.gridx = 0;
        constraints.gridy = 5;
        labelsPanel.add(deleteButton, constraints);

        constraints.gridx = 0;
        constraints.gridy = 6;
        labelsPanel.add(discountsButton, constraints);

        displayDealProduct(supplierId, catNum);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(backButtonPanel, BorderLayout.NORTH);
        mainPanel.add(titlePanel, BorderLayout.CENTER);
        mainPanel.add(labelsPanel, BorderLayout.SOUTH);
        add(mainPanel);

        editButton.setActionCommand("Edit");
        deleteButton.setActionCommand("Delete");
        discountsButton.setActionCommand("Discounts");
        backButton.setActionCommand("Back");

        editButton.addActionListener(controller);
        deleteButton.addActionListener(controller);
        discountsButton.addActionListener(controller);
        backButton.addActionListener(controller);
    }

    public void displayDealProduct(String supplierId, int catNum)
    {
        DealProductInformation product = model.getDealProduct(supplierId, catNum);
        catalogueNumLabel.setText("Catalogue number: " + product.getCatalogueNum());
        priceLabel.setText("Price: " + product.getPrice());
        amountLabel.setText("Amount: " + product.getAmount());
        productNameLabel.setText("Product name: " + model.getProductName(product.getProductId()));

    }

    public void deletedPopUpMessage(String message)
    {
        JPanel panel = new JPanel();
        JOptionPane.showMessageDialog(panel, message, "Delete", JOptionPane.INFORMATION_MESSAGE);
        add(panel);
    }

    public boolean areYouSurePopUp()
    {
        return JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this product?", "Confirmation", JOptionPane.YES_NO_OPTION);
    }
}
