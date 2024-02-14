package shared.GUI.suppliersManager.view;

import shared.GUI.suppliersManager.controller.EditContactController;
import shared.GUI.suppliersManager.model.ContactModel;
import shared.service.SuppliersManagerService;
import suppliers.business.Contact;
import suppliers.business.Deal;

import javax.swing.*;
import java.awt.*;

public class EditContactView extends JFrame{
    private JTextField IdTextField;
    private JTextField nameTextField;
    private JTextField locationTextField;
    private JTextField phoneNumberTextField;
    private JTextField emailTextField;
    private JButton doneButton;
    private JButton backButton;
    private EditContactController controller;
    private ContactModel model;

    public EditContactView(String supplierId, String username, String from, Contact contact) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 600);
        setLayout(new FlowLayout());

        this.model = new ContactModel(username);
        controller = new EditContactController(supplierId, model, this, username, from);

        JLabel title = new JLabel();
        if(from.equals("ContactListView")) title.setText("ADD CONTACT");
        else title.setText("EDIT CONTACT");
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

        IdTextField = new JTextField(20);
        IdTextField.setFont(font);
        nameTextField = new JTextField(20);
        nameTextField.setFont(font);
        locationTextField = new JTextField(20);
        locationTextField.setFont(font);
        phoneNumberTextField = new JTextField(20);
        phoneNumberTextField.setFont(font);
        emailTextField = new JTextField(20);
        emailTextField.setFont(font);
        doneButton = new JButton("Done");
        doneButton.setPreferredSize(new Dimension(300, 40));
        doneButton.setBackground(new Color(255, 255, 255));

        constraints.gridx = 0;
        constraints.gridy = 0;
        JLabel label1 = new JLabel("ID: ");
        label1.setFont(font);
        labelsPanel.add(label1, constraints);
        constraints.gridx = 1;
        constraints.gridy = 0;
        labelsPanel.add(IdTextField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        JLabel label2 = new JLabel("Name: ");
        label2.setFont(font);
        labelsPanel.add(label2, constraints);
        constraints.gridx = 1;
        constraints.gridy = 1;
        labelsPanel.add(nameTextField, constraints);

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
        JLabel label4 = new JLabel("Phone number: ");
        label4.setFont(font);
        labelsPanel.add(label4, constraints);
        constraints.gridx = 1;
        constraints.gridy = 3;
        labelsPanel.add(phoneNumberTextField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        JLabel label5 = new JLabel("Email: ");
        label5.setFont(font);
        labelsPanel.add(label5, constraints);
        labelsPanel.add(label5, constraints);
        constraints.gridx = 1;
        constraints.gridy = 4;
        labelsPanel.add(emailTextField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.gridwidth = 2;
        doneButton.setPreferredSize(new Dimension(300, 40));
        doneButton.setBackground(new Color(255, 255, 255));
        doneButton.setFont(font);
        labelsPanel.add(doneButton, constraints);
        doneButton.setActionCommand("Done");
        doneButton.addActionListener(controller);

        if(!from.equals("ContactListView"))
        {
            IdTextField.setEditable(false);
            fillTextboxes(contact);
        }

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(backButtonPanel, BorderLayout.NORTH);
        mainPanel.add(titlePanel, BorderLayout.CENTER);
        mainPanel.add(labelsPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    public void fillTextboxes(Contact contact)
    {
        IdTextField.setText(contact.getContactId());
        nameTextField.setText((contact.getName()));
        locationTextField.setText(contact.getAddress());
        phoneNumberTextField.setText(contact.getPhoneNumber());
        emailTextField.setText(contact.getEmail());
    }

    public void editedPopUpMessage(String message)
    {
        JPanel panel = new JPanel();
        JOptionPane.showMessageDialog(panel, message, "Delete", JOptionPane.INFORMATION_MESSAGE);
        add(panel);
    }

    public String getId()
    {
        return IdTextField.getText();
    }


    public String getName()
    {
        return nameTextField.getText();
    }

    public String getContactLocation()
    {
        return locationTextField.getText();
    }

    public String getPhoneNumber()
    {
        return phoneNumberTextField.getText();
    }

    public String getEmail()
    {
        return emailTextField.getText();
    }
}
