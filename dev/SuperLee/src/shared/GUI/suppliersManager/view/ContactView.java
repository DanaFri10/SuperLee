package shared.GUI.suppliersManager.view;

import shared.GUI.suppliersManager.controller.ContactController;
import shared.GUI.suppliersManager.controller.DealController;
import shared.GUI.suppliersManager.model.ContactModel;
import shared.GUI.suppliersManager.model.DealModel;
import shared.service.SuppliersManagerService;
import suppliers.business.Contact;
import suppliers.business.Supplier;

import javax.swing.*;
import java.awt.*;

public class ContactView extends JFrame {
    private ContactModel model;
    private ContactController controller;
    private JLabel contactIdLabel;
    private JLabel nameLabel;
    private JLabel phoneNumberLabel;
    private JLabel emailLabel;
    private JLabel addressLabel;
    private JButton editButton;
    private JButton deleteButton;
    private JButton backButton;


    public ContactView(String contactId, String supplierId, String username) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 600);
        setLayout(new FlowLayout());

        this.model = new ContactModel(username);
        Contact contact = model.getContact(contactId);
        this.controller = new ContactController(model, this, contact, supplierId, username);

        JLabel title = new JLabel("CONTACT CARD");
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

        contactIdLabel = new JLabel("ID: " + contact.getContactId());
        nameLabel = new JLabel("Name: " + contact.getName());
        phoneNumberLabel = new JLabel("Phone number: " + contact.getPhoneNumber());
        emailLabel = new JLabel("Email: " + contact.getEmail());
        addressLabel = new JLabel("Address: " + contact.getAddress());
        editButton = new JButton("EDIT");
        deleteButton = new JButton("DELETE");

        contactIdLabel.setFont(font);
        nameLabel.setFont(font);
        phoneNumberLabel.setFont(font);
        emailLabel.setFont(font);
        addressLabel.setFont(font);
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
        labelsPanel.add(contactIdLabel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        labelsPanel.add(nameLabel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        labelsPanel.add(phoneNumberLabel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        labelsPanel.add(emailLabel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        labelsPanel.add(addressLabel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 5;
        labelsPanel.add(editButton, constraints);

        constraints.gridx = 0;
        constraints.gridy = 6;
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
        backButton.addActionListener(controller);
    }

    public void deletedPopUpMessage(String message)
    {
        JPanel panel = new JPanel();
        JOptionPane.showMessageDialog(panel, message, "Delete", JOptionPane.INFORMATION_MESSAGE);
        add(panel);
    }

    public boolean areYouSurePopUp()
    {
        return JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this contact?", "Confirmation", JOptionPane.YES_NO_OPTION);
    }
}
