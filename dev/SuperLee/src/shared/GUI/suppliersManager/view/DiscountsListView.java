package shared.GUI.suppliersManager.view;

import shared.GUI.suppliersManager.controller.DiscountsListController;
import shared.GUI.suppliersManager.model.DiscountsListModel;
import shared.service.SuppliersManagerService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DiscountsListView extends JFrame {
    private DiscountsListModel model;
    private DiscountsListController controller;
    private String supplierId;
    private int catNum;
    private JList<String> discounts;
    private JButton addTotalAmountDiscountButton;
    private JButton addTotalPriceDiscountButton;
    private JButton addProductAmountDiscountButton;
    private JButton clearDiscountsButton;
    private JButton backButton;

    public DiscountsListView(String supplierId, int catNum, String username)
    {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 600);
        setLayout(new BorderLayout());

        this.model = new DiscountsListModel(username);
        this.controller = new DiscountsListController(supplierId, catNum, model, this, username);

        Font font = new Font("Calibri", Font.BOLD, 20);

        JPanel titlePanel = new JPanel();
        backButton = new JButton("\u2190");
        titlePanel.add(backButton);
        JLabel title = new JLabel("DISCOUNTS LIST");
        title.setFont(new Font("Calibri", Font.BOLD, 40));
        backButton.setBackground(new Color(255, 255, 255));
        backButton.setFont(font);
        backButton.setActionCommand("Back");
        backButton.addActionListener(controller);
        titlePanel.add(title);
        add(titlePanel, BorderLayout.NORTH);

        this.supplierId = supplierId;
        this.catNum = catNum;
        this.discounts = new JList<>();
        discounts.setFont(font);
        JScrollPane scrollPane = new JScrollPane(discounts);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        if(catNum < 0) {
            addTotalAmountDiscountButton = new JButton("ADD TOTAL AMOUNT DISCOUNT");
            addTotalPriceDiscountButton = new JButton("ADD TOTAL PRICE DISCOUNT");
            addTotalAmountDiscountButton.setPreferredSize(new Dimension(300, 40));
            addTotalAmountDiscountButton.setBackground(new Color(255, 255, 255));
            addTotalPriceDiscountButton.setPreferredSize(new Dimension(300, 40));
            addTotalPriceDiscountButton.setBackground(new Color(255, 255, 255));
            addTotalAmountDiscountButton.setActionCommand("Products");
            addTotalPriceDiscountButton.setActionCommand("Amount");
            buttonPanel.add(addTotalAmountDiscountButton);
            buttonPanel.add(addTotalPriceDiscountButton);
            add(buttonPanel, BorderLayout.SOUTH);
            addTotalAmountDiscountButton.addActionListener(controller);
            addTotalPriceDiscountButton.addActionListener(controller);
        }
        else {
            addProductAmountDiscountButton = new JButton("ADD DISCOUNT");
            addProductAmountDiscountButton.setPreferredSize(new Dimension(300, 40));
            addProductAmountDiscountButton.setBackground(new Color(255, 255, 255));
            buttonPanel.add(addProductAmountDiscountButton);
            add(buttonPanel, BorderLayout.SOUTH);
            addProductAmountDiscountButton.addActionListener(controller);
        }

        clearDiscountsButton = new JButton("CLEAR");
        buttonPanel.add(clearDiscountsButton);
        clearDiscountsButton.setActionCommand("Clear");
        clearDiscountsButton.addActionListener(controller);
        clearDiscountsButton.setPreferredSize(new Dimension(300, 40));
        clearDiscountsButton.setBackground(new Color(255, 255, 255));

        if(catNum < 0) displayDiscounts(model.getDiscounts(supplierId));
        else displayDiscounts(model.getDiscounts(supplierId, catNum));

    }

    private void displayDiscounts(List<String> discountsDescriptions) {
        int counter = 1;
        DefaultListModel<String> names = new DefaultListModel<>();
        for (String discount : discountsDescriptions) {
            names.addElement(counter + ". " + discount);
            counter++;
        }
        discounts.setModel(names);
    }

    public void clearedPopUpMessage(String message)
    {
        JPanel panel = new JPanel();
        JOptionPane.showMessageDialog(panel, message, "Clear", JOptionPane.INFORMATION_MESSAGE);
        add(panel);
    }

    public boolean areYouSurePopUp()
    {
        return JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null, "Are you sure you want to clear discounts?", "Confirmation", JOptionPane.YES_NO_OPTION);
    }
}
