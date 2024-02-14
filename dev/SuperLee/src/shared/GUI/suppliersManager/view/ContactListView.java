package shared.GUI.suppliersManager.view;

import shared.GUI.suppliersManager.controller.ContactListController;
import shared.GUI.suppliersManager.controller.SuppliersListController;
import shared.GUI.suppliersManager.model.ContactListModel;
import shared.GUI.suppliersManager.model.SuppliersListModel;
import shared.service.SuppliersManagerService;
import suppliers.business.Contact;
import suppliers.business.Supplier;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.*;
import java.util.List;

public class ContactListView extends JFrame{
    private String supplierId;
    private ContactListModel model;
    private ContactListController controller;
    private JList<String> contactNames;
    private JButton addNewContactButton;
    private JButton addExistingContactButton;
    private JComboBox<String> existingContacts;
    private JButton backButton;
    private JTextField filterById;
    private JTextField filterByName;

    public ContactListView(String supplierId, String username) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 600);
        setLayout(new BorderLayout());

        Font font = new Font("Calibri", Font.BOLD, 20);

        JPanel titlePanel = new JPanel();
        backButton = new JButton("\u2190");
        titlePanel.add(backButton);
        JLabel title = new JLabel("CONTACTS LIST");
        title.setFont(new Font("Calibri", Font.BOLD, 40));
        backButton.setBackground(new Color(255, 255, 255));
        backButton.setFont(font);
        titlePanel.add(title);
        add(titlePanel, BorderLayout.NORTH);

        this.supplierId = supplierId;
        this.contactNames = new JList<>();
        contactNames.setFont(font);
        JScrollPane scrollPane = new JScrollPane(contactNames);
        add(scrollPane, BorderLayout.CENTER);

        this.model = new ContactListModel(username);
        this.controller = new ContactListController(supplierId, contactNames, model, this, username);

        displayContactsNames(model.getContacts(supplierId));

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(5, 5, 5, 5);

        constraints.gridx = 0;
        constraints.gridy = 0;
        JLabel label1 = new JLabel("Filter by ID: ");
        label1.setFont(font);
        buttonPanel.add(label1, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        filterById = new JTextField(20);
        filterById.getDocument().addDocumentListener(controller);
        filterById.setFont(font);
        buttonPanel.add(filterById, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        JLabel label2 = new JLabel("Filter by Name: ");
        label2.setFont(font);
        buttonPanel.add(label2, constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        filterByName = new JTextField(20);
        filterByName.getDocument().addDocumentListener(controller);
        filterByName.setFont(font);
        buttonPanel.add(filterByName, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        addNewContactButton = new JButton("Add new contact");
        addNewContactButton.setFont(font);
        addNewContactButton.setPreferredSize(new Dimension(300, 40));
        addNewContactButton.setBackground(new Color(255, 255, 255));
        addNewContactButton.setFont(font);
        buttonPanel.add(addNewContactButton, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        addExistingContactButton = new JButton("Add existing contact");
        addExistingContactButton.setFont(font);
        addExistingContactButton.setPreferredSize(new Dimension(300, 40));
        addExistingContactButton.setBackground(new Color(255, 255, 255)); // A1C2F1
        addExistingContactButton.setFont(font);
        buttonPanel.add(addExistingContactButton, constraints);

        constraints.gridx = 1;
        constraints.gridy = 3;
        existingContacts = new JComboBox<>(model.getContactsComplement(supplierId));
        existingContacts.setFont(font);
        existingContacts.setPreferredSize(new Dimension(300, 40));
        existingContacts.setBackground(new Color(255, 255, 255)); // A1C2F1
        existingContacts.setFont(font);
        buttonPanel.add(existingContacts, constraints);

        add(buttonPanel, BorderLayout.SOUTH);

        addNewContactButton.setActionCommand("New");
        addExistingContactButton.setActionCommand("Existing");
        backButton.setActionCommand("Back");

        contactNames.addListSelectionListener(controller);
        addNewContactButton.addActionListener(controller);
        addExistingContactButton.addActionListener(controller);
        backButton.addActionListener(controller);
    }


    private void displayContactsNames(String[] contacts) {
        int counter = 1;
        DefaultListModel<String> names = new DefaultListModel<>();
        for (String contact : contacts) {
            names.addElement(counter + ". " + contact);
            counter++;
        }
        contactNames.setModel(names);
    }

    public void displayFilterById(String id) {
        displayContactsNames(model.getContactsById(supplierId, id, false));
    }

    public void displayFilterByName(String name) {
        displayContactsNames(model.getContactsByName(supplierId, name, false));
    }

    public String getFilterById()
    {
        return filterById.getText();
    }

    public String getFilterByName()
    {
        return filterByName.getText();
    }

    public Document getFilterByIdDocument()
    {
        return filterById.getDocument();
    }

    public Document getFilterByNameDocument()
    {
        return filterByName.getDocument();
    }

    public String getContactId()
    {
        try {
            String selected = existingContacts.getSelectedItem().toString();
            return selected.substring(0, selected.indexOf('-')).trim();
        }
        catch(Exception e)
        {
            return "";
        }
    }

    public void popUpMessage(String message)
    {
        JPanel panel = new JPanel();
        JOptionPane.showMessageDialog(panel, message, "Added", JOptionPane.INFORMATION_MESSAGE);
        add(panel);
    }

}
