package shared.GUI.suppliersManager.view;

import shared.GUI.suppliersManager.controller.SuppliersListController;
import shared.GUI.suppliersManager.model.SuppliersListModel;
import shared.GUI.warehouseWorker.model.UsernameStorage;
import shared.service.SuppliersManagerService;
import suppliers.business.Supplier;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.*;
import java.util.List;

public class SuppliersListView extends JFrame {
    private SuppliersListModel model;
    private SuppliersListController controller;
    private JList<String> suppliersNames;
    private JButton addSupplierButton;
    private JTextField filterById;
    private JTextField filterByName;
    private JTextField filterBySupplyArea;

    public SuppliersListView(String username) {
        setTitle("Suppliers List");
        if(UsernameStorage.isManager){
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }else {
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
        setSize(1200, 600);
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        headerPanel.setPreferredSize(new Dimension(getWidth(), 50));
        JLabel headerLabel = new JLabel("SUPPLIERS LIST");
        headerLabel.setFont(new Font("Calibri", Font.BOLD, 40));
        headerPanel.add(headerLabel);
        add(headerPanel, BorderLayout.NORTH);

        suppliersNames = new JList<>();
        suppliersNames.setFont(new Font("Calibri", Font.BOLD, 20));
        JScrollPane scrollPane = new JScrollPane(suppliersNames);
        //scrollPane.setPreferredSize(new Dimension(1000, 100));
        add(scrollPane, BorderLayout.CENTER);

        model = new SuppliersListModel(username);
        controller = new SuppliersListController(suppliersNames, model, this, username);

        JPanel filtersPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(5, 5, 5, 5);

        addSupplierButton = new JButton("ADD SUPPLIER");
        addSupplierButton.setPreferredSize(new Dimension(300, 40));
        addSupplierButton.setBackground(new Color(255, 255, 255)); // A1C2F1
        addSupplierButton.setFont(new Font("Calibri", Font.BOLD, 20));
        c.gridx = 0;
        c.gridy = 0;
        filtersPanel.add(addSupplierButton, c);

        JLabel text1 = new JLabel("Filter by id: ");
        text1.setFont(new Font("Calibri", Font.BOLD, 20));
        c.gridx = 0;
        c.gridy = 1;
        filtersPanel.add(text1, c);

        filterById = new JTextField(20);
        filterById.setFont(new Font("Calibri", Font.BOLD, 20));
        filterById.getDocument().addDocumentListener(controller);
        c.gridx = 1;
        c.gridy = 1;
        filtersPanel.add(filterById, c);

        JLabel text2 = new JLabel("Filter by name: ");
        text2.setFont(new Font("Calibri", Font.BOLD, 20));
        c.gridx = 0;
        c.gridy = 2;
        filtersPanel.add(text2, c);

        filterByName = new JTextField(20);
        filterByName.getDocument().addDocumentListener(controller);
        filterByName.setFont(new Font("Calibri", Font.BOLD, 20));
        c.gridx = 1;
        c.gridy = 2;
        filtersPanel.add(filterByName, c);

        JLabel text3 = new JLabel("Filter by supply area: ");
        text3.setFont(new Font("Calibri", Font.BOLD, 20));
        c.gridx = 0;
        c.gridy = 3;
        filtersPanel.add(text3, c);

        filterBySupplyArea = new JTextField(20);
        filterBySupplyArea.getDocument().addDocumentListener(controller);
        filterBySupplyArea.setFont(new Font("Calibri", Font.BOLD, 20));
        c.gridx = 1;
        c.gridy = 3;
        filtersPanel.add(filterBySupplyArea, c);


        add(filtersPanel, BorderLayout.SOUTH);

        displaySuppliersNames(model.getSuppliers());

        suppliersNames.addListSelectionListener(controller);
        addSupplierButton.addActionListener(controller);
    }

    private void displaySuppliersNames(List<Supplier> suppliers) {
        int counter = 1;
        DefaultListModel<String> names = new DefaultListModel<>();
        for (Supplier supplier : suppliers) {
            names.addElement(counter + ". " + supplier.getCompanyId() + " - " + supplier.getCompanyName());
            counter++;
        }
        suppliersNames.setModel(names);
    }

    public void displayFilterById(String id) {
        List<Supplier> suppliers = model.getSuppliersById(id);
        displaySuppliersNames(suppliers);
    }

    public void displayFilterByName(String name) {
        List<Supplier> suppliers = model.getSuppliersByName(name);
        displaySuppliersNames(suppliers);
    }

    public void displayFilterBySupplyArea(String supplyArea) {
        List<Supplier> suppliers = model.getSuppliersBySupplyArea(supplyArea);
        displaySuppliersNames(suppliers);
    }

    public String getFilterById()
    {
        return filterById.getText();
    }

    public String getFilterByName()
    {
        return filterByName.getText();
    }

    public String getFilterBySupplyArea()
    {
        return filterBySupplyArea.getText();
    }

    public Document getFilterByIdDocument()
    {
        return filterById.getDocument();
    }

    public Document getFilterByNameDocument()
    {
        return filterByName.getDocument();
    }

    public Document getFilterBySupplyAreaDocument()
    {
        return filterBySupplyArea.getDocument();
    }
}
