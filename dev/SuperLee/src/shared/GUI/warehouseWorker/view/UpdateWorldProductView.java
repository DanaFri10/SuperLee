package shared.GUI.warehouseWorker.view;
import shared.GUI.warehouseWorker.controller.UpdateWorldProductController;
import shared.GUI.warehouseWorker.model.UpdateWorldProductModel;
import shared.service.WarehouseWorkerService;
import stock.business.Product;
import stock.presentation.WarehouseWorkerView;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;

public class UpdateWorldProductView {
    JLabel jl1;
    JLabel jl2;
    JLabel jl3;
    UpdateWorldProductModel model;
    JFrame f;
    String username;
    int productId;
    int branchId;
    public UpdateWorldProductView(String username, int productId, int branchId){
        // Create model
        this.username = username;
        this.productId = productId;
        this.branchId = branchId;
        try{
        model = new UpdateWorldProductModel(username, productId);
        f = new JFrame();
        JPanel jp1 = new JPanel();
        JPanel jp2 = new JPanel();
        jl1 = new JLabel("current name: " + model.getName() + "; new name: ");
        jl2 = new JLabel("current manufacturer: " + model.getManufacturer() + "; new manufacturer: ");
        jl3 = new JLabel();
        JTextField jf1 = new JTextField(20);
        JTextField jf2 = new JTextField(20);
        JButton jb1 = new JButton("update");
        JButton jb2 = new JButton("return");

        //panel
        jp1.add(jl1);
        jp1.add(jf1);
        jp2.add(jl2);
        jp2.add(jf2);


        jp1.setBounds(200, 200, 600, 30);
        jp2.setBounds(200, 250, 600, 30);
        jb1.setBounds(200, 600, 200, 30);
        jb2.setBounds(400, 600, 200, 30);
        jp1.setLayout(new FlowLayout(FlowLayout.LEFT));
        jp2.setLayout(new FlowLayout(FlowLayout.LEFT));
        jl3.setBounds(300, 500, 600, 30);
        //frame

        f.add(jp1); f.add(jp2);
        f.add(jb1); f.add(jb2); f.add(jl3);

        f.setSize(800, 800);
        f.setLayout(null);
        f.setVisible(true);

        // Create controller
        UpdateWorldProductController controller = new UpdateWorldProductController(Arrays.asList(jf1,jf2), Arrays.asList(jb1,jb2), model, this);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        }catch (Exception exception){
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void refresh() {
        jl1.setText("current name: " + model.getName() + "; new name: ");
        jl2.setText("current manufacturer: " + model.getManufacturer() + "; new manufacturer: ");
        jl3.setText(model.getError());
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
