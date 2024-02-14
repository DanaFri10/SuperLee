package shared.GUI.warehouseWorker.view;

import shared.GUI.warehouseWorker.controller.AddDiscountProductController;
import shared.GUI.warehouseWorker.controller.AddInstanceController;
import shared.GUI.warehouseWorker.model.AddDiscountProductModel;
import shared.GUI.warehouseWorker.model.AddInstanceModel;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class AddInstanceView {
    JLabel jl1;
    JLabel jl2;
    JLabel jl4;
    JLabel jl5;
    JLabel jl7;
    AddInstanceModel model;
    JFrame f;
    String username;
    int branchId;
    public AddInstanceView(String username, int branchId){
        // Create model
        this.username = username;
        this.branchId = branchId;
        try{
        model = new AddInstanceModel(username, branchId);
        f = new JFrame();
        JPanel jp1 = new JPanel();
        JPanel jp2 = new JPanel();
        JPanel jp4 = new JPanel();
        JPanel jp5 = new JPanel();
        jl1 = new JLabel("instance id: " );
        jl2 = new JLabel("product id: " );
        jl4 = new JLabel("bought Price: ");
        jl5 = new JLabel("expire date: " );
        jl7 = new JLabel();
        JLabel jl6 = new JLabel("write date in this format: yyyy-MM-dd, or write \"-\" for no expire date ;");

        JTextField jf1 = new JTextField(20);
        JTextField jf2 = new JTextField(20);
        JTextField jf4 = new JTextField(20);
        JTextField jf5 = new JTextField(20);
        JButton jb1 = new JButton("add");
        JButton jb2 = new JButton("return");

        //panel
        jp1.add(jl1);
        jp1.add(jf1);
        jp2.add(jl2);
        jp2.add(jf2);

        jp4.add(jl4);
        jp4.add(jf4);
        jp5.add(jl5);
        jp5.add(jf5);

        jp1.setBounds(200, 200, 600, 30);
        jp2.setBounds(200, 250, 600, 30);
        jp4.setBounds(200, 300, 600, 30);
        jp5.setBounds(200, 350, 600, 30);
        jb1.setBounds(200, 600, 200, 30);
        jb2.setBounds(400, 600, 200, 30);
        jl6.setBounds(100, 100, 700, 30);
        jp1.setLayout(new FlowLayout(FlowLayout.LEFT));
        jp2.setLayout(new FlowLayout(FlowLayout.LEFT));
        jp4.setLayout(new FlowLayout(FlowLayout.LEFT));
        jp5.setLayout(new FlowLayout(FlowLayout.LEFT));
        jl7.setBounds(200, 500, 600, 30);
        //frame

        f.add(jp1); f.add(jp2); f.add(jp4); f.add(jp5); f.add(jl7);
        f.add(jb1); f.add(jb2); f.add(jl6);

        f.setSize(800, 800);
        f.setLayout(null);
        f.setVisible(true);

        // Create controller
        AddInstanceController controller = new AddInstanceController(Arrays.asList(jf1,jf2, jf4,jf5), Arrays.asList(jb1,jb2), model, this);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        }catch (Exception exception){
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void refresh() {
        jl7.setText(model.getError());
    }

    public void close() {
        f.dispose();
    }
    public String getUsername() {
        return username;
    }

    public int getBranchId() {
        return branchId;
    }
}
