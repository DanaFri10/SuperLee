package shared.GUI.suppliersManager.view;

import shared.GUI.suppliersManager.controller.OrderHistoryController;
import shared.GUI.suppliersManager.controller.SuppliersListController;
import shared.GUI.suppliersManager.model.OrderHistoryModel;
import shared.GUI.suppliersManager.model.SuppliersListModel;
import shared.service.SuppliersManagerService;
import suppliers.business.Contact;
import suppliers.business.Supplier;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.*;
import java.util.List;

public class OrderHistoryView extends JFrame{
    private String supplierId;
    private OrderHistoryModel model;
    private OrderHistoryController controller;
    private JList<String> orders;
    private JButton backButton;
    private JTextField filterById;
    private JTextField filterByDate;

    public OrderHistoryView(String supplierId, String username) {
        setTitle("Suppliers List");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 600);
        setLayout(new BorderLayout());

        Font font = new Font("Calibri", Font.BOLD, 20);

        JPanel titlePanel = new JPanel();
        backButton = new JButton("\u2190");
        titlePanel.add(backButton);
        JLabel title = new JLabel("ORDERS HISTORY");
        title.setFont(new Font("Calibri", Font.BOLD, 40));
        backButton.setBackground(new Color(255, 255, 255)); // A1C2F1
        backButton.setFont(font);
        titlePanel.add(title);
        add(titlePanel, BorderLayout.NORTH);

        this.supplierId = supplierId;
        this.orders = new JList<>();
        orders.setFont(font);
        this.model = new OrderHistoryModel(username);
        this.controller = new OrderHistoryController(orders, this, supplierId, username);

        JScrollPane scrollPane = new JScrollPane(orders);
        add(scrollPane, BorderLayout.CENTER);

        JPanel filterPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(5, 5, 5, 5);

        constraints.gridx = 0;
        constraints.gridy = 0;
        JLabel label1 = new JLabel("Filter by ID: ");
        label1.setFont(font);
        filterPanel.add(label1, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        filterById = new JTextField(20);
        filterById.getDocument().addDocumentListener(controller);
        filterById.setFont(font);
        filterPanel.add(filterById, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        JLabel label2 = new JLabel("Filter by date: ");
        label2.setFont(font);
        filterPanel.add(label2, constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        filterByDate = new JTextField(20);
        filterByDate.getDocument().addDocumentListener(controller);
        filterByDate.setFont(font);
        filterPanel.add(filterByDate, constraints);

        add(filterPanel, BorderLayout.SOUTH);

        displayOrderIdsAndDates(model.orderIdsAndDates(supplierId));

        orders.addListSelectionListener(controller);
        backButton.addActionListener(controller);

    }

    public void displayOrderIdsAndDates(List<String> orderIdsAndNames)
    {
        DefaultListModel<String> l = new DefaultListModel<>();
        for (String order : orderIdsAndNames) {
            l.addElement(order);
        }
        orders.setModel(l);
    }

    public void displayFilterById(String id) {
        List<String> orders = model.filterById(supplierId, id);
        displayOrderIdsAndDates(orders);
    }

    public void displayFilterByDate(String date) {
        List<String> orders = model.filterByDate(supplierId, date);
        displayOrderIdsAndDates(orders);
    }

    public String getFilterById()
    {
        return filterById.getText();
    }

    public String getFilterByDate()
    {
        return filterByDate.getText();
    }

    public Document getFilterByIdDocument()
    {
        return filterById.getDocument();
    }

    public Document getFilterByDateDocument()
    {
        return filterByDate.getDocument();
    }


}
