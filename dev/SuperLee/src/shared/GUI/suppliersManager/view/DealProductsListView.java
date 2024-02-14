package shared.GUI.suppliersManager.view;

import shared.GUI.suppliersManager.controller.ContactListController;
import shared.GUI.suppliersManager.controller.DealProductsListController;
import shared.GUI.suppliersManager.model.ContactListModel;
import shared.GUI.suppliersManager.model.DealProductsListModel;
import shared.service.SuppliersManagerService;
import suppliers.business.Contact;
import suppliers.business.DealProductInformation;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.*;
import java.util.List;

public class DealProductsListView extends JFrame{
    private DealProductsListModel model;
    private DealProductsListController controller;
    private JList<String> products;
    private JButton addProductButton;
    private JButton backButton;
    private JTextField filterByCatNum;
    private JTextField filterByProductId;
    private JTextField filterByName;
    private String supplierId;

    public DealProductsListView(String supplierId, String username) {
        setTitle("Contacts List");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 600);
        setLayout(new BorderLayout());

        Font font = new Font("Calibri", Font.BOLD, 20);

        JPanel titlePanel = new JPanel();
        backButton = new JButton("\u2190");
        titlePanel.add(backButton);
        JLabel title = new JLabel("DEAL PRODUCTS");
        title.setFont(new Font("Calibri", Font.BOLD, 40));
        backButton.setBackground(new Color(255, 255, 255));
        backButton.setFont(font);
        titlePanel.add(title);
        add(titlePanel, BorderLayout.NORTH);

        this.products = new JList<>();
        products.setFont(font);
        JScrollPane scrollPane = new JScrollPane(products);
        add(scrollPane, BorderLayout.CENTER);

        this.supplierId = supplierId;
        this.model = new DealProductsListModel(username);
        this.controller = new DealProductsListController(supplierId, products, model, this, username);

        addProductButton = new JButton("ADD NEW PRODUCT");
        addProductButton.setPreferredSize(new Dimension(300, 40));
        addProductButton.setBackground(new Color(255, 255, 255)); // A1C2F1
        addProductButton.setFont(font);

        addProductButton.addActionListener(controller);
        products.addListSelectionListener(controller);
        backButton.addActionListener(controller);
        backButton.setActionCommand("Back");

        displayDealProducts(model.getDealProducts(supplierId));

        JPanel filterPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(5, 5, 5, 5);

        constraints.gridx = 0;
        constraints.gridy = 0;
        JLabel label1 = new JLabel("Filter by catalog number: ");
        label1.setFont(font);
        filterPanel.add(label1, constraints);
        constraints.gridx = 1;
        constraints.gridy = 0;
        filterByCatNum = new JTextField(20);
        filterByCatNum.getDocument().addDocumentListener(controller);
        filterByCatNum.setFont(font);
        filterPanel.add(filterByCatNum, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        JLabel label2 = new JLabel("Filter by product ID: ");
        label2.setFont(font);
        filterPanel.add(label2, constraints);
        constraints.gridx = 1;
        constraints.gridy = 1;
        filterByProductId = new JTextField(20);
        filterByProductId.getDocument().addDocumentListener(controller);
        filterByProductId.setFont(font);
        filterPanel.add(filterByProductId, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        JLabel label3 = new JLabel("Filter by product ID: ");
        label3.setFont(font);
        filterPanel.add(label3, constraints);
        constraints.gridx = 1;
        constraints.gridy = 2;
        filterByName = new JTextField(20);
        filterByName.getDocument().addDocumentListener(controller);
        filterByName.setFont(font);
        filterPanel.add(filterByName, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        filterPanel.add(addProductButton, constraints);

        add(filterPanel, BorderLayout.SOUTH);
    }



    public void displayDealProducts(List<DealProductInformation> dealProducts)
    {
        int counter = 1;
        DefaultListModel<String> productNames = new DefaultListModel<>();
        for (DealProductInformation product : dealProducts) {
            String productName = model.getProductName(product.getProductId());
            productNames.addElement(counter + ". " + product.getCatalogueNum() + " - " + productName);
            counter++;
        }
        products.setModel(productNames);
    }

    public void displayFilterByCatNum(String catNum) {
        List<DealProductInformation> products = model.getProductsByCatNum(supplierId, catNum);
        displayDealProducts(products);
    }

    public void displayFilterByProductId(String productId) {
        List<DealProductInformation> products = model.getProductsByProductId(supplierId, productId);
        displayDealProducts(products);
    }

    public void displayFilterByName(String name) {
        List<DealProductInformation> products = model.getProductsByName(supplierId, name);
        displayDealProducts(products);
    }

    public String getFilterByCatNum()
    {
        return filterByCatNum.getText();
    }

    public String getFilterByProductId()
    {
        return filterByProductId.getText();
    }

    public String getFilterByName()
    {
        return filterByName.getText();
    }

    public Document getFilterByCatNumDocument()
    {
        return filterByCatNum.getDocument();
    }

    public Document getFilterByProductIdDocument()
    {
        return filterByProductId.getDocument();
    }

    public Document getFilterByNameDocument()
    {
        return filterByName.getDocument();
    }


}
