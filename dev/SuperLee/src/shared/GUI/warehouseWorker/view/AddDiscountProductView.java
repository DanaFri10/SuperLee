package shared.GUI.warehouseWorker.view;

import shared.GUI.warehouseWorker.controller.AddDiscountProductController;
import shared.GUI.warehouseWorker.controller.UpdateWorldProductController;
import shared.GUI.warehouseWorker.model.AddDiscountProductModel;
import shared.GUI.warehouseWorker.model.UpdateWorldProductModel;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class AddDiscountProductView {
    JLabel jl1;
    JLabel jl2;
    JLabel jl3;
    JLabel jl4;
    JTextField jf1;
    JTextField jf2;
    JTextField jf3;
    AddDiscountProductModel model;
    JFrame f;
    String username;
    int productId;
    int branchId;
    public AddDiscountProductView(String username, int productId, int branchId){
        // Create model
        this.username = username;
        this.productId = productId;
        this.branchId = branchId;
        try{
        model = new AddDiscountProductModel(username, productId, branchId);
        f = new JFrame();
        JPanel jp1 = new JPanel();
        JPanel jp2 = new JPanel();
        JPanel jp3 = new JPanel();
        jl1 = new JLabel("current start date: " + model.getDiscountFrom() + "; new start date: ");
        jl2 = new JLabel("current end date: " + model.getDiscountTo() + "; new end date: ");
        jl3 = new JLabel("current value: " + model.getDiscountValue() + "; new value: ");
        jl4 = new JLabel();
        JLabel jl5 = new JLabel("write in this format: yyyy-MM-dd; value is between 1 and 0");

        jf1 = new JTextField(20);
        jf2 = new JTextField(20);
        jf3 = new JTextField(20);
        JButton jb1 = new JButton("add/replace");
        JButton jb2 = new JButton("return");

        //panel
        jp1.add(jl1);
        jp1.add(jf1);
        jp2.add(jl2);
        jp2.add(jf2);
        jp3.add(jl3);
        jp3.add(jf3);

        jp1.setBounds(200, 200, 600, 30);
        jp2.setBounds(200, 250, 600, 30);
        jp3.setBounds(200, 300, 600, 30);
        jb1.setBounds(200, 600, 200, 30);
        jb2.setBounds(400, 600, 200, 30);
        jp1.setLayout(new FlowLayout(FlowLayout.LEFT));
        jp2.setLayout(new FlowLayout(FlowLayout.LEFT));
        jp3.setLayout(new FlowLayout(FlowLayout.LEFT));
        jl4.setBounds(300, 500, 600, 30);
        jl5.setBounds(100, 100, 500, 30);
        //frame

        f.add(jp1); f.add(jp2); f.add(jp3);
        f.add(jb1); f.add(jb2); f.add(jl4); f.add(jl5);

        f.setSize(800, 800);
        f.setLayout(null);
        f.setVisible(true);

        // Create controller
        AddDiscountProductController controller = new AddDiscountProductController(Arrays.asList(jf1,jf2, jf3), Arrays.asList(jb1,jb2), model, this);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        }catch (Exception exception){
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void refresh() {
        jl1.setText("current start date: " + model.getDiscountFrom() + "; new start date: ");
        jl2.setText("current end date: " + model.getDiscountTo() + "; new end date: ");
        jl3.setText("current value: " + model.getDiscountValue() + "; new value: ");
        jf1.setText("");
        jf2.setText("");
        jf3.setText("");
        jl4.setText(model.getError());
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
