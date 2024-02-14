package shared.GUI.warehouseWorker.view;
import shared.GUI.warehouseWorker.controller.UpdateBranchProductController;
import shared.GUI.warehouseWorker.controller.UpdateWorldProductController;
import shared.GUI.warehouseWorker.model.UpdateBranchProductModel;
import shared.GUI.warehouseWorker.model.UpdateWorldProductModel;
import shared.service.WarehouseWorkerService;
import stock.business.Product;
import stock.presentation.WarehouseWorkerView;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;

public class UpdateBranchProductView {
    JLabel jl1;
    JLabel jl2;
    JLabel jl3;
    JLabel jl4;
    JLabel jl5;
    JLabel jl6;
    UpdateBranchProductModel model;
    JFrame f;
    String username;
    int productId;
    int branchId;
    public UpdateBranchProductView(String username, int productId, int branchId){
        // Create model
        this.username = username;
        this.productId = productId;
        this.branchId = branchId;
        try{
        model = new UpdateBranchProductModel(username, productId, branchId);
        f = new JFrame();
        JPanel jp1 = new JPanel();
        JPanel jp2 = new JPanel();
        JPanel jp3 = new JPanel();
        JPanel jp4 = new JPanel();
        JPanel jp5 = new JPanel();
        jl1 = new JLabel("current price sold: " + model.getOriginalSold() + "; new price sold: ");
        jl2 = new JLabel("current demand: " + model.getDemand() + "; new demand: ");
        jl3 = new JLabel("current minimum: " + model.getMinimum() + "; new minimum: ");
        jl4 = new JLabel("current place in store: " + model.getPlaceInStore() + "; new place in store: ");
        jl5 = new JLabel("current path: " + model.getPath() + "; new path: ");
        jl6 = new JLabel();
        JTextField jf1 = new JTextField(20);
        JTextField jf2 = new JTextField(20);
        JTextField jf3 = new JTextField(20);
        JTextField jf4 = new JTextField(20);
        JTextField jf5 = new JTextField(20);
        JButton jb1 = new JButton("update");
        JButton jb2 = new JButton("return");

        //panel
        jp1.add(jl1);
        jp1.add(jf1);
        jp2.add(jl2);
        jp2.add(jf2);
        jp3.add(jl3);
        jp3.add(jf3);
        jp4.add(jl4);
        jp4.add(jf4);
        jp5.add(jl5);
        jp5.add(jf5);

        jp1.setBounds(200, 200, 600, 30);
        jp2.setBounds(200, 250, 600, 30);
        jp3.setBounds(200, 300, 600, 30);
        jp4.setBounds(200, 350, 600, 30);
        jp5.setBounds(200, 400, 600, 30);
        jb1.setBounds(200, 600, 200, 30);
        jb2.setBounds(400, 600, 200, 30);
        jp1.setLayout(new FlowLayout(FlowLayout.LEFT));
        jp2.setLayout(new FlowLayout(FlowLayout.LEFT));
        jp3.setLayout(new FlowLayout(FlowLayout.LEFT));
        jp4.setLayout(new FlowLayout(FlowLayout.LEFT));
        jp5.setLayout(new FlowLayout(FlowLayout.LEFT));
        jl6.setBounds(300, 500, 600, 30);
        //frame

        f.add(jp1); f.add(jp2); f.add(jp3); f.add(jp4); f.add(jp5);
        f.add(jb1); f.add(jb2); f.add(jl6);

        f.setSize(800, 800);
        f.setLayout(null);
        f.setVisible(true);

        // Create controller
        UpdateBranchProductController controller = new UpdateBranchProductController(Arrays.asList(jf1,jf2,jf3,jf4,jf5), Arrays.asList(jb1,jb2), model, this);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        }catch (Exception exception){
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void refresh() {
        jl1.setText("current price sold: " + model.getOriginalSold() + "; new price sold: ");
        jl2.setText("current demand: " + model.getDemand() + "; new demand: ");
        jl3.setText("current minimum: " + model.getMinimum() + "; new minimum: ");
        jl4.setText("current place in store: " + model.getPlaceInStore() + "; new place in store: ");
        jl5.setText("current path: " + model.getPath() + "; new path: ");
        jl6.setText(model.getError());
    }

    public void close() {
        f.dispose();
    }
    public String getUsername() {
        return username;
    }

    public int getProductId() {
        return productId;
    }

    public int getBranchId() {
        return branchId;
    }


}
