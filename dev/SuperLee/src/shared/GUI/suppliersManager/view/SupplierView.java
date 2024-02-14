package shared.GUI.suppliersManager.view;

import shared.GUI.suppliersManager.controller.SupplierController;
import shared.GUI.suppliersManager.model.SupplierModel;
import shared.service.SuppliersManagerService;
import suppliers.business.Contact;
import suppliers.business.Deal;
import suppliers.business.Supplier;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SupplierView extends JFrame {
    private SupplierModel model;
    private SupplierController controller;
    private JButton backButton;

    public SupplierView(String supplierId, String username) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 600);
        setLayout(new BorderLayout());

        this.model = new SupplierModel(username);
        Supplier supplier = model.getSupplier(supplierId);
        this.controller = new SupplierController(model, this, supplier, username);

        JLabel title = new JLabel("SUPPLIER CARD");
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

        JPanel labelsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(5, 5, 5, 5);

        constraints.gridx = 0;
        constraints.gridy = 0;
        JLabel idLabel = new JLabel("ID: " + supplier.getCompanyId());
        idLabel.setFont(font);
        labelsPanel.add(idLabel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        JLabel nameLabel = new JLabel("Name: " + supplier.getCompanyName());
        nameLabel.setFont(font);
        labelsPanel.add(nameLabel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        JLabel locationLabel = new JLabel("Location: " + supplier.getLocation());
        locationLabel.setFont(font);
        labelsPanel.add(locationLabel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        JLabel bankLabel = new JLabel("Bank account: " + supplier.getBankAccount());
        bankLabel.setFont(font);
        labelsPanel.add(bankLabel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        JButton contactsButton = new JButton("VIEW CONTACTS");
        contactsButton.setPreferredSize(new Dimension(300, 40));
        contactsButton.setBackground(new Color(255, 255, 255)); // A1C2F1
        contactsButton.setFont(font);
        labelsPanel.add(contactsButton, constraints);

        constraints.gridx = 0;
        constraints.gridy = 5;
        JButton dealButton = new JButton("VIEW DEAL");
        dealButton.setPreferredSize(new Dimension(300, 40));
        dealButton.setBackground(new Color(255, 255, 255)); // A1C2F1
        dealButton.setFont(font);
        labelsPanel.add(dealButton, constraints);

        constraints.gridx = 0;
        constraints.gridy = 6;
        JButton editButton = new JButton("EDIT");
        editButton.setPreferredSize(new Dimension(300, 40));
        editButton.setBackground(new Color(255, 255, 255)); // A1C2F1
        editButton.setFont(font);
        labelsPanel.add(editButton, constraints);

        constraints.gridx = 0;
        constraints.gridy = 7;
        JButton deleteButton = new JButton("DELETE");
        deleteButton.setPreferredSize(new Dimension(300, 40));
        deleteButton.setBackground(new Color(255, 255, 255)); // A1C2F1
        deleteButton.setFont(font);
        labelsPanel.add(deleteButton, constraints);

        constraints.gridx = 0;
        constraints.gridy = 8;
        JButton historyButton = new JButton("ORDER HISTORY");
        historyButton.setPreferredSize(new Dimension(300, 40));
        historyButton.setBackground(new Color(255, 255, 255)); // A1C2F1
        historyButton.setFont(font);
        labelsPanel.add(historyButton, constraints);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(backButtonPanel, BorderLayout.NORTH);
        mainPanel.add(titlePanel, BorderLayout.CENTER);
        mainPanel.add(labelsPanel, BorderLayout.SOUTH);

        add(mainPanel);

        contactsButton.setActionCommand("Contacts");
        dealButton.setActionCommand("View deal");
        editButton.setActionCommand("Edit");
        deleteButton.setActionCommand("Delete");
        historyButton.setActionCommand("History");
        contactsButton.addActionListener(controller);
        dealButton.addActionListener(controller);
        editButton.addActionListener(controller);
        deleteButton.addActionListener(controller);
        historyButton.addActionListener(controller);
    }


    public void deletedPopUpMessage(String message)
    {
        JPanel panel = new JPanel();
        JOptionPane.showMessageDialog(panel, message, "Delete", JOptionPane.INFORMATION_MESSAGE);
        add(panel);
    }

    public boolean areYouSurePopUp()
    {
        return JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this supplier?", "Confirmation", JOptionPane.YES_NO_OPTION);
    }
}
