package shared.GUI.warehouseWorker.view;

import shared.GUI.warehouseWorker.controller.ProductController;
import shared.GUI.warehouseWorker.model.ProductModel;
import shared.service.WarehouseWorkerService;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class ProductView {
    JFrame f;
     String username;
    private ProductModel model;
    int productId;
     int branchId;
    public ProductView(String username, int productId, int branchId) {
        // Create model
        this.username = username;
        this.productId = productId;
        this.branchId = branchId;
        try{
        model = new ProductModel(username, productId, branchId);

        f = new JFrame();
        JPanel jp1 = new JPanel();
        JPanel jp2 = new JPanel();
        JPanel jp3 = new JPanel();
        JTextArea info = new JTextArea(model.getReport() + "\n" + model.getDiscount());
        info.setFont(new Font("Courier New", Font.PLAIN, 20));
        info.setSize(info.getPreferredSize());
        info.setEditable(false);
        JButton jb1 = new JButton("update branch product");
        JButton jb2 = new JButton("update world product");
        JButton jb3 = new JButton("delete branch product");
        JButton jb4 = new JButton("delete world product");
        JButton jb5 = new JButton("add product discount");
        JButton jb7 = new JButton("return");

        //info bounds
        info.setBounds(30, 160, info.getWidth(), info.getHeight());
        //buttons bound
        jb1.setBounds(200, 600, 180, 30);
        jb2.setBounds(400, 600, 180, 30);
        jb3.setBounds(200, 500, 180, 30);
        jb4.setBounds(400, 550, 180, 30);
        jb5.setBounds(200, 550, 180, 30);
        jb7.setBounds(600, 600, 180, 30);
        //frame
        f.add(info);
        f.add(jb1);
        f.add(jb2);
        f.add(jb3);
        f.add(jb4);
        f.add(jb5);

        f.add(jb7);


        f.setSize(800, 800);
        f.setLayout(null);
        f.setVisible(true);

        // Create controller
        ProductController controller = new ProductController(Arrays.asList(jb1,jb2,jb3,jb4,jb5,jb7), model, this);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        }catch (Exception exception){
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void back(){
        new ProductExplorerView(model.getPath(),branchId, null, true);
        close();
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
