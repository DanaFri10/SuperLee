package shared.GUI.warehouseWorker.view;

import shared.GUI.warehouseWorker.controller.MainMenuController;
import shared.GUI.warehouseWorker.model.LoginModel;
import shared.GUI.warehouseWorker.model.UsernameStorage;

import javax.swing.*;
import java.awt.*;

public class MainMenuView {
    private int branchId;
    private JFrame frame;

    public MainMenuView(int branchId){
        this.branchId = branchId;
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        MainMenuController controller = new MainMenuController(this);

        JLabel label = new JLabel("Welcome to the Warehouse Worker Actions Menu");
        JButton explore = new JButton("Product Explorer");
        JButton order = new JButton("Make an order");
        JButton shortage = new JButton("View shortage report");
        JButton inventory = new JButton("Handle Inventory Products");

        explore.setActionCommand("explore");
        order.setActionCommand("order");
        shortage.setActionCommand("shortage");
        inventory.setActionCommand("inventory");

        explore.addActionListener(controller);
        order.addActionListener(controller);
        shortage.addActionListener(controller);
        inventory.addActionListener(controller);

        label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        explore.setAlignmentX(JButton.CENTER_ALIGNMENT);
        order.setAlignmentX(JButton.CENTER_ALIGNMENT);
        shortage.setAlignmentX(JButton.CENTER_ALIGNMENT);
        inventory.setAlignmentX(JButton.CENTER_ALIGNMENT);

        label.setPreferredSize(new Dimension(500,100));
        explore.setPreferredSize(new Dimension(500,100));
        order.setPreferredSize(new Dimension(500,100));
        shortage.setPreferredSize(new Dimension(500,100));
        inventory.setPreferredSize(new Dimension(500,100));
        explore.setMinimumSize(new Dimension(500,100));
        order.setMinimumSize(new Dimension(500,100));
        shortage.setMinimumSize(new Dimension(500,100));
        inventory.setMinimumSize(new Dimension(500,100));
        explore.setMaximumSize(new Dimension(500,100));
        order.setMaximumSize(new Dimension(500,100));
        shortage.setMaximumSize(new Dimension(500,100));
        inventory.setMaximumSize(new Dimension(500,100));

        panel.add(label);
        panel.add(explore);
        panel.add(order);
        panel.add(shortage);
        panel.add(inventory);

        JButton logout = new JButton("Logout");
        logout.setActionCommand("out");
        logout.addActionListener(controller);
        logout.setAlignmentX(JButton.CENTER_ALIGNMENT);
        panel.add(logout);
        frame = new JFrame("Warehouse Worker Menu");
        frame.add(panel);
        frame.setSize(500,500+logout.getHeight());
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void goExplore(){
        new ProductExplorerView("",branchId,null,true);
        frame.dispose();
    }
    public void goOrder(){
        new MakeOrderView(branchId);
        frame.dispose();
    }
    public void goShortage(){
        new MakeShortageOrderView(branchId);
        frame.dispose();
    }
    public void goInventory(){
        new SearchInstanceView(UsernameStorage.USERNAME, branchId);
        frame.dispose();
    }

    public void logout(){
        if(!UsernameStorage.isManager) {
            LoginView login = new LoginView();
            login.setVisible(true);
        }
        frame.dispose();
    }
}
