
package shared.GUI.suppliersManager.view;

import shared.GUI.suppliersManager.controller.EditSupplierController;
import shared.GUI.suppliersManager.model.SupplierModel;
import shared.service.SuppliersManagerService;
import suppliers.business.Deal;
import suppliers.business.PaymentMethod;
import suppliers.business.Supplier;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditSupplierView extends JFrame {
    private JTextField companyIdTextField;
    private JTextField companyNameTextField;
    private JTextField locationTextField;
    private JTextField bankAccountTextField;
    private JTextField supplyAreasTextField;
    private JComboBox<PaymentMethod> paymentMethodJComboBox;
    private JCheckBox deliversCheckBox;
    private JCheckBox[] deliveryDaysCheckBox;
    private JTextField daysToDeliverTextField;
    private JButton doneButton;
    private JButton backButton;
    private EditSupplierController controller;
    private SupplierModel model;

    public EditSupplierView(String username, String from, Supplier supplier) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 600);
        setLayout(new FlowLayout());

        JLabel title = new JLabel();
        if(from.equals("SuppliersListView")) title.setText("ADD SUPPLIER");
        else title.setText("EDIT SUPPLIER");
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

        this.model = new SupplierModel(username);
        controller = new EditSupplierController(model, this, username, from);

        JPanel labelsPanel = new JPanel();
        labelsPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;

        companyIdTextField = new JTextField(20);
        companyNameTextField = new JTextField(20);
        locationTextField = new JTextField(20);
        bankAccountTextField = new JTextField(20);
        supplyAreasTextField = new JTextField(20);
        paymentMethodJComboBox = new JComboBox<>(PaymentMethod.values());
        deliversCheckBox = new JCheckBox();
        deliveryDaysCheckBox = new JCheckBox[7];
        daysToDeliverTextField = new JTextField(20);
        doneButton = new JButton("Done");
        doneButton.setPreferredSize(new Dimension(300, 40));
        doneButton.setBackground(new Color(255, 255, 255));
        doneButton.setFont(font);

        constraints.gridx = 0;
        constraints.gridy = 0;
        JLabel label1 = new JLabel("ID: ");
        label1.setFont(font);
        labelsPanel.add(label1, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        labelsPanel.add(companyIdTextField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        JLabel label2 = new JLabel("Name: ");
        label2.setFont(font);
        labelsPanel.add(label2, constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        labelsPanel.add(companyNameTextField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        JLabel label3 = new JLabel("Location: ");
        label3.setFont(font);
        labelsPanel.add(label3, constraints);

        constraints.gridx = 1;
        constraints.gridy = 2;
        labelsPanel.add(locationTextField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        JLabel label4 = new JLabel("Bank account: ");
        label4.setFont(font);
        labelsPanel.add(label4, constraints);

        constraints.gridx = 1;
        constraints.gridy = 3;
        labelsPanel.add(bankAccountTextField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        JLabel label5 = new JLabel("Supply areas: ");
        label5.setFont(font);
        labelsPanel.add(label5, constraints);

        constraints.gridx = 1;
        constraints.gridy = 4;
        labelsPanel.add(supplyAreasTextField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 5;
        JLabel label6 = new JLabel("Payment method: ");
        label6.setFont(font);
        labelsPanel.add(label6, constraints);

        constraints.gridx = 1;
        constraints.gridy = 5;
        labelsPanel.add(paymentMethodJComboBox, constraints);

        constraints.gridx = 0;
        constraints.gridy = 6;
        JLabel label7 = new JLabel("Delivers: ");
        label7.setFont(font);
        labelsPanel.add(label7, constraints);

        constraints.gridx = 1;
        constraints.gridy = 6;
        labelsPanel.add(deliversCheckBox, constraints);

        constraints.gridx = 0;
        constraints.gridy = 7;
        JLabel label8 = new JLabel("Delivery days: ");
        label8.setFont(font);
        labelsPanel.add(label8, constraints);

        String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        for (int i = 0; i < 7; i++) {
            deliveryDaysCheckBox[i] = new JCheckBox(daysOfWeek[i]);
            constraints.gridx = i % 3 + 1;
            constraints.gridy = i / 3 + 8;
            labelsPanel.add(deliveryDaysCheckBox[i], constraints);
        }
        disableDeliveryDaysCheckBox();

        constraints.gridx = 0;
        constraints.gridy = 11;
        JLabel label9 = new JLabel("Days to deliver: ");
        label9.setFont(font);
        labelsPanel.add(label9, constraints);

        constraints.gridx = 1;
        constraints.gridy = 11;
        labelsPanel.add(daysToDeliverTextField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 12;
        constraints.gridwidth = 2;
        labelsPanel.add(doneButton, constraints);
        doneButton.setActionCommand("Done");

        doneButton.addActionListener(controller);
        backButton.addActionListener(controller);
        deliversCheckBox.addItemListener(controller);


        if(!from.equals("SuppliersListView"))
        {
            companyIdTextField.setEditable(false);
            fillTextboxes(supplier);
        }

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(backButtonPanel, BorderLayout.NORTH);
        mainPanel.add(titlePanel, BorderLayout.CENTER);
        mainPanel.add(labelsPanel, BorderLayout.SOUTH);

        add(mainPanel);

    }

    public void fillTextboxes(Supplier supplier)
    {
        companyIdTextField.setText(supplier.getCompanyId());
        companyNameTextField.setText((supplier.getCompanyName()));
        locationTextField.setText(supplier.getLocation());
        bankAccountTextField.setText(supplier.getBankAccount());
        supplyAreasTextField.setText(String.join(", ", supplier.getSupplyAreas()));

        Deal deal = model.getDeal(supplier.getCompanyId());
        paymentMethodJComboBox.setSelectedItem(deal.getPaymentMethod());
        deliversCheckBox.setSelected(deal.getDelivers());
        for(int i = 0; i < deal.getDeliveryDays().length; i++)
            deliveryDaysCheckBox[i].setSelected(deal.getDeliveryDays()[i]);
        daysToDeliverTextField.setText(deal.getDaysToDeliver() + "");
    }


    public String getSupplierId()
    {
        return companyIdTextField.getText();
    }

    public String getSupplierName()
    {
        return companyNameTextField.getText();
    }

    public String getSupplierLocation()
    {
        return locationTextField.getText();
    }

    public String getSupplierBankAccount()
    {
        return bankAccountTextField.getText();
    }

    public List<String> getSupplierSupplyAreas()
    {
        return Arrays.asList(supplyAreasTextField.getText().split(","));
    }

    public PaymentMethod getSupplierPaymentMethod()
    {
        return (PaymentMethod)paymentMethodJComboBox.getSelectedItem();
    }

    public boolean getDelivers()
    {
        return deliversCheckBox.isSelected();
    }

    public boolean[] getDeliveryDays()
    {
        boolean[] deliveryDays = new boolean[7];
        for(int i = 0; i < 7; i++)
            deliveryDays[i] = deliveryDaysCheckBox[i].isSelected();
        return deliveryDays;
    }

    public int getDaysToDeliver()
    {
        try
        {
            return Integer.parseInt(daysToDeliverTextField.getText());
        }
        catch (Exception e)
        {
            return -1;
        }
    }

    public void editedPopUpMessage(String message)
    {
        JPanel panel = new JPanel();
        JOptionPane.showMessageDialog(panel, message, "Delete", JOptionPane.INFORMATION_MESSAGE);
        add(panel);
    }

    public void enableDeliveryDaysCheckBox()
    {
        for(JCheckBox checkBox : deliveryDaysCheckBox) {
            checkBox.setEnabled(true);
            checkBox.setForeground(Color.BLACK);
        }
    }

    public void disableDeliveryDaysCheckBox()
    {
        for(JCheckBox checkBox : deliveryDaysCheckBox) {
            checkBox.setSelected(false);
            checkBox.setEnabled(false);
            checkBox.setForeground(Color.GRAY);
        }
    }
}
