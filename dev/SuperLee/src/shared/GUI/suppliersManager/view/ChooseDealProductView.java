package shared.GUI.suppliersManager.view;

import shared.GUI.suppliersManager.controller.ChooseDealProductController;
import shared.GUI.suppliersManager.model.DealProductModel;
import shared.GUI.suppliersManager.model.PeriodicOrderModel;
import shared.service.SuppliersManagerService;
import suppliers.business.DealProductInformation;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.*;
import java.util.Enumeration;
import java.util.List;

public class ChooseDealProductView extends JFrame {
    private PeriodicOrderModel model;
    private ChooseDealProductController controller;
    private JPanel radioPanel;
    private ButtonGroup buttonGroup;
    private JButton okButton;
    private JButton backButton;
    private JTextField amount;
    private String username;

    public ChooseDealProductView(String supplierId, EditPeriodicOrderView view, String username, boolean newOrder, List<Integer> used)
    {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 600);
        setLayout(new BorderLayout());

        radioPanel = new JPanel();
        radioPanel.setLayout(new BoxLayout(radioPanel, BoxLayout.Y_AXIS));
        buttonGroup = new ButtonGroup();
        this.username = username;
        this.model = new PeriodicOrderModel(username);
        this.controller = new ChooseDealProductController(buttonGroup, this, view);

        JLabel title = new JLabel();
        title.setText("CHOOSE PRODUCT");
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

        displayProducts(model.getUnusedProducts(supplierId, used));
        setLayout(new FlowLayout());
        add(radioPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(5, 5, 5, 5);

        constraints.gridx = 0;
        constraints.gridy = 0;
        JLabel label1 = new JLabel("Amount: ");
        label1.setFont(font);
        buttonPanel.add(label1, constraints);
        constraints.gridx = 1;
        constraints.gridy = 0;
        amount = new JTextField(20);
        amount.setFont(font);
        buttonPanel.add(amount, constraints);
        constraints.gridx = 2;
        constraints.gridy = 0;
        okButton = new JButton("CHOOSE");
        okButton.setPreferredSize(new Dimension(300, 40));
        okButton.setBackground(new Color(255, 255, 255));
        buttonPanel.add(okButton, constraints);
        okButton.setEnabled(false);
        okButton.addActionListener(controller);
        okButton.setActionCommand("Ok");

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(backButtonPanel, BorderLayout.NORTH);
        mainPanel.add(titlePanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);

    }

    private void displayProducts(List<DealProductInformation> products)
    {
        Font font = new Font("Calibri", Font.BOLD, 20);

        radioPanel.removeAll();
        buttonGroup.clearSelection();

        for (DealProductInformation product : products) {
            DealProductModel dealProductModel = new DealProductModel(username);
            JRadioButton radioButton = new JRadioButton(product.getCatalogueNum() + " - " + dealProductModel.getProductName(product.getProductId()));
            radioButton.setActionCommand(product.getCatalogueNum() + "");
            radioButton.setFont(font);
            radioPanel.add(radioButton);
            buttonGroup.add(radioButton);
        }
        for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();
            button.addActionListener(controller);
        }

        radioPanel.revalidate();
        radioPanel.repaint();
    }

    public void addedPopUpMessage(String message)
    {
        JPanel panel = new JPanel();
        JOptionPane.showMessageDialog(panel, message, "Delete", JOptionPane.INFORMATION_MESSAGE);
        add(panel);
    }

    public void enableOkButton()
    {
        okButton.setEnabled(true);
    }

    public void displayFilterById(String id) {
        //List<DealProductInformation> products = model.getProductsById(supplierId, id);
        //displayProducts(products);
    }

    public void displayFilterByName(String name) {
        //List<Product> products = model.get(supplierId, name);
        //displayProducts(products);
    }

    public int getAmount()
    {
        return Integer.parseInt(amount.getText());
    }

}
