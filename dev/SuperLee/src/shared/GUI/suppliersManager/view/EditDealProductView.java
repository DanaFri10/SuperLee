package shared.GUI.suppliersManager.view;

import shared.GUI.suppliersManager.controller.EditContactController;
import shared.GUI.suppliersManager.controller.EditDealProductController;
import shared.GUI.suppliersManager.model.ContactModel;
import shared.GUI.suppliersManager.model.DealProductModel;
import shared.service.SuppliersManagerService;
import suppliers.business.Contact;
import suppliers.business.DealProductInformation;
import suppliers.business.PaymentMethod;
import suppliers.business.ProductDiscount;
import suppliers.dal.ProductDiscountsDAO;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class EditDealProductView extends JFrame {
    private JTextField catNumField;
    private JComboBox<String> chooseProduct;
    private JTextField priceTextField;
    private JTextField amountTextField;
    private JButton doneButton;
    private JButton backButton;
    private EditDealProductController controller;
    private DealProductModel model;

    public EditDealProductView(String supplierId, int catNum, String username, String from) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 600);
        setLayout(new FlowLayout());

        this.model = new DealProductModel(username);
        controller = new EditDealProductController(supplierId, model, this, username, from);

        JLabel title = new JLabel();
        if(from.equals("DealProductsList")) title.setText("ADD DEAL PRODUCT");
        else title.setText("EDIT DEAL PRODUCT");
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

        catNumField = new JTextField(20);
        chooseProduct = new JComboBox<String>(model.getProductComplement(supplierId));
        priceTextField = new JTextField(20);
        amountTextField = new JTextField(20);
        doneButton = new JButton("DONE");

        catNumField.setFont(font);
        chooseProduct.setFont(font);
        priceTextField.setFont(font);
        amountTextField.setFont(font);
        doneButton.setFont(font);
        backButton.setFont(font);

        constraints.gridx = 0;
        constraints.gridy = 0;
        JLabel label1 = new JLabel("Catalouge number: ");
        label1.setFont(font);
        labelsPanel.add(label1, constraints);
        constraints.gridx = 1;
        constraints.gridy = 0;
        labelsPanel.add(catNumField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        JLabel label2 = new JLabel("Selected product: ");
        label2.setFont(font);
        labelsPanel.add(label2, constraints);
        constraints.gridx = 1;
        constraints.gridy = 1;
        labelsPanel.add(chooseProduct, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        JLabel label3 = new JLabel("Amount: ");
        label3.setFont(font);
        labelsPanel.add(label3, constraints);
        constraints.gridx = 1;
        constraints.gridy = 2;
        labelsPanel.add(amountTextField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        JLabel label4 = new JLabel("Price: ");
        label4.setFont(font);
        labelsPanel.add(label4, constraints);
        constraints.gridx = 1;
        constraints.gridy = 3;
        labelsPanel.add(priceTextField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 5;
        doneButton.setPreferredSize(new Dimension(300, 40));
        doneButton.setBackground(new Color(255, 255, 255));
        doneButton.setActionCommand("Done");
        labelsPanel.add(doneButton, constraints);
        doneButton.addActionListener(controller);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(backButtonPanel, BorderLayout.NORTH);
        mainPanel.add(titlePanel, BorderLayout.CENTER);
        mainPanel.add(labelsPanel, BorderLayout.SOUTH);
        add(mainPanel);

        if(!from.equals("DealProductsList"))
        {
            fillTextboxes(model.getDealProduct(supplierId, catNum));
        }

    }

    public void fillTextboxes(DealProductInformation product)
    {
        catNumField.setText(product.getCatalogueNum() + "");
        chooseProduct.setSelectedItem(product.getProductId() + " - " + model.getProductName(product.getProductId()));
        priceTextField.setText(product.getPrice() + "");
        amountTextField.setText(product.getAmount() + "");
    }

    public void editedPopUpMessage(String message)
    {
        JPanel panel = new JPanel();
        JOptionPane.showMessageDialog(panel, message, "Delete", JOptionPane.INFORMATION_MESSAGE);
        add(panel);
    }

    public int getCatNum()
    {
        try
        {
            return Integer.parseInt(catNumField.getText());
        }
        catch (Exception e)
        {
            return -1;
        }
    }

    public int getProductId()
    {
        try {
            String selectedProduct = chooseProduct.getSelectedItem().toString();
            return Integer.parseInt(selectedProduct.substring(0, selectedProduct.indexOf('-')).trim());
        }
        catch (Exception e)
        {
            return -1;
        }
    }

    public double getPrice()
    {
        try {
            return Double.parseDouble(priceTextField.getText());
        }
        catch (Exception e)
        {
            return -1;
        }
    }

    public int getAmount()
    {
        try {
            return Integer.parseInt(amountTextField.getText());
        }
        catch (Exception e)
        {
            return -1;
        }
    }

}
