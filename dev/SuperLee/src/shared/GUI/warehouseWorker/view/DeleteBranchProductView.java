package shared.GUI.warehouseWorker.view;

import shared.GUI.warehouseWorker.controller.DeleteBranchProductController;
import shared.GUI.warehouseWorker.model.DeleteBranchProductModel;
import shared.GUI.warehouseWorker.model.ProductModel;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class DeleteBranchProductView {
    JFrame f;
    ProductView preView;
    DeleteBranchProductModel model;
    JButton jb1new;
    String username;
    int productId;
    int branchId;

    public DeleteBranchProductView(ProductView preView, String username, int productId, int branchId) {
        this.username = username;
        this.productId = productId;
        this.branchId = branchId;
        this.preView = preView;
        try{
        model = new DeleteBranchProductModel(username, productId, branchId);
        JLabel jl1 = new JLabel("<html>You are about to locally delete the very definition of this product (from this branch) <br/>  Are you ABSOLUTELY sure?");
        JButton jb1 = new JButton("Yes, I have accepted the risks and wish to continue");
        JButton jb2 = new JButton("No, Please take me back to safety");
        f = new JFrame();
        jb1.setBounds(100, 250, 500, 20);
        jb2.setBounds(100, 300, 500, 20);
        jl1.setBounds(50, 50, 500, 80);
        jl1.setHorizontalAlignment(SwingConstants.CENTER);
        f.add(jb1);
        f.add(jb2);
        f.getContentPane().add(jl1);
        f.setSize(700, 400);
        f.setLayout(null);
        f.setVisible(true);

        jb1new = new JButton("whatever");
        DeleteBranchProductController controller = new DeleteBranchProductController(Arrays.asList(jb1,jb2,jb1new), model, this);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        }catch (Exception exception){
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void confirm(){
        f.dispose();
        JLabel jl1new = new JLabel(model.getAnswer());//add
        f = new JFrame();
        jl1new.setBounds(10, 100, 300, 20);
        jb1new.setBounds(110, 200, 100, 20);
        f.add(jl1new);
        f.add(jb1new);
        f.setSize(400, 400);
        f.setLayout(null);
        f.setVisible(true);
    }
    public void closeDueDelete() {
        f.dispose();
        preView.close();
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
