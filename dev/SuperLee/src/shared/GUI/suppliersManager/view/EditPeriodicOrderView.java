package shared.GUI.suppliersManager.view;

import shared.GUI.suppliersManager.controller.EditPeriodicOrderController;
import shared.GUI.suppliersManager.model.DealModel;
import shared.GUI.suppliersManager.model.DealProductModel;
import shared.GUI.suppliersManager.model.PeriodicOrderModel;
import shared.GUI.suppliersManager.model.SupplierModel;
import shared.service.SuppliersManagerService;
import suppliers.business.Deal;
import suppliers.business.PeriodicOrderAgreement;
import suppliers.business.Supplier;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class EditPeriodicOrderView extends JFrame {
    private DefaultListModel<String> productsAmountsModel;
    private JList<String> productAmounts;
    private JCheckBox[] orderDaysCheckBox;
    private JTextField branchIdTextField;
    private JComboBox<String> chooseContact;
    private JButton addProductButton;
    private JButton removeProductButton;
    private JButton doneButton;
    private JButton backButton;
    private EditPeriodicOrderController controller;
    private PeriodicOrderModel model;
    private String supplierId;
    private int branchId;
    private String username;

    public EditPeriodicOrderView(String supplierId, int branchId, String username, String from) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1300, 600);
        setLayout(new FlowLayout());
        this.username = username;
        this.supplierId = supplierId;
        this.branchId = branchId;
        productAmounts = new JList<>();
        productsAmountsModel = new DefaultListModel<>();

        this.model = new PeriodicOrderModel(username);
        controller = new EditPeriodicOrderController(productAmounts, supplierId, model, this, username, from);

        JLabel title = new JLabel();
        if(from.equals("OrderListView")) title.setText("ADD PERIODIC ORDER");
        else title.setText("EDIT PERIODIC ORDER");
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

        branchIdTextField = new JTextField(20);
        branchIdTextField.setFont(font);
        chooseContact = new JComboBox<String>(model.getSupplierContacts(supplierId));
        chooseContact.setFont(font);

        addProductButton = new JButton("Add product");
        addProductButton.setFont(font);
        addProductButton.setActionCommand("AddProduct");
        addProductButton.addActionListener(controller);
        addProductButton.setPreferredSize(new Dimension(300, 40));
        addProductButton.setBackground(new Color(255, 255, 255));
        addProductButton.setFont(font);

        removeProductButton = new JButton("Remove product");
        removeProductButton.setFont(font);
        removeProductButton.setActionCommand("RemoveProduct");
        removeProductButton.addActionListener(controller);
        removeProductButton.setPreferredSize(new Dimension(300, 40));
        removeProductButton.setBackground(new Color(255, 255, 255));
        removeProductButton.setFont(font);

        doneButton = new JButton("Done");
        doneButton.setPreferredSize(new Dimension(300, 40));
        doneButton.setBackground(new Color(255, 255, 255));
        doneButton.setFont(font);
        doneButton.setActionCommand("Done");
        labelsPanel.add(doneButton, constraints);
        doneButton.addActionListener(controller);

        constraints.gridx = 0;
        constraints.gridy = 0;
        JLabel label1 = new JLabel("Branch ID: ");
        label1.setFont(font);
        labelsPanel.add(label1, constraints);
        constraints.gridx = 1;
        constraints.gridy = 0;
        labelsPanel.add(branchIdTextField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        JLabel label2 = new JLabel("Selected contact: ");
        label2.setFont(font);
        labelsPanel.add(label2, constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        labelsPanel.add(chooseContact, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        JLabel label3 = new JLabel("Order days: ");
        label3.setFont(font);
        labelsPanel.add(label3, constraints);

        orderDaysCheckBox = new JCheckBox[7];
        String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

        constraints.gridx = 1;
        constraints.gridy = 2;
        orderDaysCheckBox[0] = new JCheckBox(daysOfWeek[0]);
        orderDaysCheckBox[0].setFont(font);
        labelsPanel.add(orderDaysCheckBox[0], constraints);

        constraints.gridx = 2;
        constraints.gridy = 2;
        orderDaysCheckBox[1] = new JCheckBox(daysOfWeek[1]);
        orderDaysCheckBox[1].setFont(font);
        labelsPanel.add(orderDaysCheckBox[1], constraints);

        constraints.gridx = 3;
        constraints.gridy = 2;
        orderDaysCheckBox[2] = new JCheckBox(daysOfWeek[2]);
        orderDaysCheckBox[2].setFont(font);
        labelsPanel.add(orderDaysCheckBox[2], constraints);

        constraints.gridx = 4;
        constraints.gridy = 2;
        orderDaysCheckBox[3] = new JCheckBox(daysOfWeek[3]);
        orderDaysCheckBox[3].setFont(font);
        labelsPanel.add(orderDaysCheckBox[3], constraints);

        constraints.gridx = 1;
        constraints.gridy = 3;
        orderDaysCheckBox[4] = new JCheckBox(daysOfWeek[4]);
        orderDaysCheckBox[4].setFont(font);
        labelsPanel.add(orderDaysCheckBox[4], constraints);

        constraints.gridx = 2;
        constraints.gridy = 3;
        orderDaysCheckBox[5] = new JCheckBox(daysOfWeek[5]);
        orderDaysCheckBox[5].setFont(font);
        labelsPanel.add(orderDaysCheckBox[5], constraints);

        constraints.gridx = 3;
        constraints.gridy = 3;
        orderDaysCheckBox[6] = new JCheckBox(daysOfWeek[6]);
        orderDaysCheckBox[6].setFont(font);
        labelsPanel.add(orderDaysCheckBox[6], constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        JScrollPane scrollPane = new JScrollPane(productAmounts);
        scrollPane.setPreferredSize(new Dimension(400, 250));
        labelsPanel.add(scrollPane, constraints);

        constraints.gridx = 1;
        constraints.gridy = 4;
        labelsPanel.add(addProductButton, constraints);

        constraints.gridx = 2;
        constraints.gridy = 4;
        labelsPanel.add(removeProductButton, constraints);

        constraints.gridx = 1;
        constraints.gridy = 5;
        labelsPanel.add(doneButton, constraints);


        if (!from.equals("OrderListView")) {
            fillTextboxes(model.getPeriodicOrder(supplierId, branchId));
            branchIdTextField.setEditable(false);
        }
        else
        {
            branchIdTextField.setEditable(true);
        }

        setOrderDays();

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(backButtonPanel, BorderLayout.NORTH);
        mainPanel.add(titlePanel, BorderLayout.CENTER);
        mainPanel.add(labelsPanel, BorderLayout.SOUTH);

        add(mainPanel);

    }

    private void setOrderDays()
    {
        Deal deal = new DealModel(username).getDeal(supplierId);
        if(deal.hasFixedDays())
        {
            for(int i = 0; i < deal.getDeliveryDays().length; i++)
                orderDaysCheckBox[i].setSelected(deal.getDeliveryDays()[i]);
            for(int i = 0; i < orderDaysCheckBox.length; i++)
            {
                orderDaysCheckBox[i].setEnabled(false);
            }
        }
    }

    public boolean needsSetDays(){
        Deal deal = new DealModel(username).getDeal(supplierId);
        return !deal.hasFixedDays();
    }

    private void fillTextboxes(PeriodicOrderAgreement order) {
        displayProductsList(order.getProductAmounts());
        branchIdTextField.setText(branchId + "");
        chooseContact.setSelectedItem(order.getAssignedContactId() + " - " + model.getContactName(order.getAssignedContactId()));
        for(int i = 0; i < order.getOrderDays().length; i++)
            orderDaysCheckBox[i].setSelected(order.getOrderDays()[i]);
    }

    public void displayProductsList(Map<Integer, Integer> productsAmounts) {
        for (int catNum : productsAmounts.keySet()) {
            DealProductModel temp = new DealProductModel(username);
            productsAmountsModel.addElement(catNum + " - " + temp.getProductName(temp.getDealProduct(supplierId, catNum).getProductId()) + " - " + productsAmounts.get(catNum));
        }
        productAmounts.setModel(productsAmountsModel);
    }

    public void editedPopUpMessage(String message) {
        JPanel panel = new JPanel();
        JOptionPane.showMessageDialog(panel, message, "Delete", JOptionPane.INFORMATION_MESSAGE);
        add(panel);
    }

    public void addProductToList(int catNum, int amount)
    {
        DealProductModel temp = new DealProductModel(username);
        String toAdd = catNum + " - " + temp.getProductName(temp.getDealProduct(supplierId, catNum).getProductId()) + " - " + amount;
        productsAmountsModel.addElement(toAdd);
        productAmounts.setModel(productsAmountsModel);
        productAmounts.repaint();
    }

    public void removeProductFromList(int catNum)
    {
        int indexToRemove = -1;
        for (int i = 0; i < productsAmountsModel.size(); i++) {
            String product = productsAmountsModel.elementAt(i);
            if (product.contains(catNum + " -")) {
                indexToRemove = i;
                break;
            }
        }

        if (indexToRemove != -1) {
            productsAmountsModel.removeElementAt(indexToRemove);
        }

        productAmounts.setModel(productsAmountsModel);
        productAmounts.repaint();
    }

    public Map<Integer, Integer> getProductAmounts()
    {
        Map<Integer, Integer> amounts = new HashMap<>();
        for (int i = 0; i < productsAmountsModel.size(); i++) {
            String product = productsAmountsModel.elementAt(i);
            int catNum = Integer.parseInt(product.substring(0, product.indexOf('-')).trim());
            int amount = Integer.parseInt(product.substring(product.lastIndexOf('-')+1).trim());
            amounts.put(catNum, amount);
        }
        return amounts;
    }

    public String getContactId() {
        String selected = chooseContact.getSelectedItem().toString();
        return selected.substring(0, selected.indexOf('-')).trim();
    }

    public int getBranchId() {
        try {
            return Integer.parseInt(branchIdTextField.getText());
        }
        catch(Exception e)
        {
            return -1;
        }
    }

    public boolean[] getOrderDays() {
        boolean[] deliveryDays = new boolean[7];
        for (int i = 0; i < 7; i++)
            deliveryDays[i] = orderDaysCheckBox[i].isSelected();
        return deliveryDays;
    }

    public boolean areProductsEmpty()
    {
        return productAmounts.getModel().getSize() == 0;
    }



}

