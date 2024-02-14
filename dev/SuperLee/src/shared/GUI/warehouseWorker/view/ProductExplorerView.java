package shared.GUI.warehouseWorker.view;

import shared.GUI.warehouseWorker.controller.ProductExplorerController;
import shared.GUI.warehouseWorker.model.ProductMapModel;
import shared.GUI.warehouseWorker.model.ProductModel;
import shared.GUI.warehouseWorker.model.UniversalProductModel;
import shared.GUI.warehouseWorker.model.UsernameStorage;

import javax.swing.*;
import java.awt.*;

public class ProductExplorerView {
    private ProductMapModel productMap;
    private int branchId;
    private JFrame frame;
    private ProductExplorerController controller;
    private boolean editMode;
    private JTextField goTo;
    private JButton discount;

    public ProductExplorerView(String initPath, int branchId, ProductMapModel productMap, boolean editMode){
        this.editMode = editMode;
        this.branchId = branchId;
        this.productMap = productMap;
        DefaultListModel<Object> list = new DefaultListModel<>();
        JList children = new JList(list);
        JButton button = new JButton("^");
        children.setPreferredSize(new Dimension(300,100));
        controller = new ProductExplorerController(this,branchId,list, productMap, initPath);
        children.addMouseListener(controller);
        button.setActionCommand("up");
        button.addActionListener(controller);
        JButton stop = new JButton("Go back");
        stop.setActionCommand("back");
        stop.addActionListener(controller);
        JButton add = new JButton("Add Product");
        if(editMode){
            add.setActionCommand("add");
            add.addActionListener(controller);
        }
        JLabel hint = new JLabel("Select path to go to: ");
        goTo = new JTextField();
        goTo.setColumns(10);
        JButton go = new JButton("Go to");
        go.setActionCommand("goto");
        go.addActionListener(controller);
        JPanel row = new JPanel();
        row.add(hint);
        row.add(goTo);
        row.add(go);
        JScrollPane scroll = new JScrollPane(children);
        JPanel panel = new JPanel();
        panel.add(button);
        panel.add(scroll);
        JPanel buttonRow = new JPanel();
        discount = new JButton("Add Category Discount");
        discount.setActionCommand("disc");
        discount.addActionListener(controller);
        buttonRow.add(stop);
        if(editMode){
            buttonRow.add(add);
            buttonRow.add(discount);
        }
        panel.add(buttonRow);
        panel.add(row);
        frame = new JFrame();
        frame.setTitle("Product Explorer");
        panel.setSize(500,300);
        frame.add(panel);
        frame.setSize(500,300);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void popupProductInfo(UniversalProductModel model){
        if(editMode){
            new ProductView(UsernameStorage.USERNAME, model.getId(), branchId);
            frame.dispose();
        }else {
            new ProductInfoView(controller, model);
        }
    }

    public void goBack(){
        if(editMode){
            new MainMenuView(branchId);
            frame.dispose();
        }else {
            new MakeOrderView(branchId, productMap);
            frame.dispose();
        }
    }

    public void popupChooseAdd(){
        new AddChooseView(controller);
    }

    public void goAddProduct(String path){
        new AddWorldandBranchProductView(UsernameStorage.USERNAME, path, branchId);
        frame.dispose();
    }

    public void goAddBranchProduct(String path){
        new AddBranchProductView(UsernameStorage.USERNAME, path, branchId);
        frame.dispose();
    }

    public void goAddCategoryDiscount(String path){
        new AddCategoryDiscountView(UsernameStorage.USERNAME, path, branchId);
        frame.dispose();
    }

    public void setDiscountEnabled(boolean enabled){
        discount.setEnabled(enabled);
    }

    public String getGoTo(){
        return goTo.getText();
    }
    public void setGoTo(String go){
        goTo.setText(go);
    }
}
