package shared.GUI.warehouseWorker.view;

import shared.GUI.warehouseWorker.controller.AddWorldandBranchProductController;
import shared.GUI.warehouseWorker.controller.UpdateBranchProductController;
import shared.GUI.warehouseWorker.model.AddWorldandBranchProductModel;
import shared.GUI.warehouseWorker.model.UpdateBranchProductModel;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class AddWorldandBranchProductView {
    JLabel jl1;
    JLabel jl2;
    JLabel jl3;
    JLabel jl4;
    JLabel jl5;
    JLabel jl6;
    JLabel jl7;
    JLabel jl9;
    JTextField jf1;
    JTextField jf2;
    JTextField jf3;
    JTextField jf4;
    JTextField jf5;
    JTextField jf6;
    JTextField jf7;
    AddWorldandBranchProductModel model;
    JFrame f;
    String username;
    String path;
    int branchId;
    public AddWorldandBranchProductView(String username, String path, int branchId){
        // Create model
        this.username = username;
        this.path = path;
        this.branchId = branchId;
        try{
        model = new AddWorldandBranchProductModel(username, path, branchId);
        f = new JFrame();
        JPanel jp1 = new JPanel();
        JPanel jp2 = new JPanel();
        JPanel jp3 = new JPanel();
        JPanel jp4 = new JPanel();
        JPanel jp5 = new JPanel();
        JPanel jp6 = new JPanel();
        JPanel jp7 = new JPanel();
        jl1 = new JLabel(" id: ");
        jl2 = new JLabel(" name: ");
        jl3 = new JLabel(" manufacturer: ");
        jl4 = new JLabel(" price sold: ");
        jl5 = new JLabel(" demand: ");
        jl6 = new JLabel(" minimum: ");
        jl7 = new JLabel(" place in store: ");
        jl9 = new JLabel();
        jf1 = new JTextField(20);
        jf2 = new JTextField(20);
        jf3 = new JTextField(20);
        jf4 = new JTextField(20);
        jf5 = new JTextField(20);
        jf6 = new JTextField(20);
        jf7 = new JTextField(20);

        JButton jb1 = new JButton("add");
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
        jp6.add(jl6);
        jp6.add(jf6);
        jp7.add(jl7);
        jp7.add(jf7);
        jp1.setBounds(200, 100, 600, 30);
        jp2.setBounds(200, 150, 600, 30);
        jp3.setBounds(200, 200, 600, 30);
        jp4.setBounds(200, 250, 600, 30);
        jp5.setBounds(200, 300, 600, 30);
        jp6.setBounds(200, 350, 600, 30);
        jp7.setBounds(200, 400, 600, 30);
        jb1.setBounds(200, 600, 200, 30);
        jb2.setBounds(400, 600, 200, 30);
        jp1.setLayout(new FlowLayout(FlowLayout.LEFT));
        jp2.setLayout(new FlowLayout(FlowLayout.LEFT));
        jp3.setLayout(new FlowLayout(FlowLayout.LEFT));
        jp4.setLayout(new FlowLayout(FlowLayout.LEFT));
        jp5.setLayout(new FlowLayout(FlowLayout.LEFT));
        jp6.setLayout(new FlowLayout(FlowLayout.LEFT));
        jp7.setLayout(new FlowLayout(FlowLayout.LEFT));
        jl9.setBounds(300, 500, 600, 30);
        //frame

        f.add(jp1); f.add(jp2); f.add(jp3); f.add(jp4); f.add(jp5); f.add(jp6); f.add(jp7);
        f.add(jb1); f.add(jb2); f.add(jl9);

        f.setSize(800, 800);
        f.setLayout(null);
        f.setVisible(true);

        // Create controller
        AddWorldandBranchProductController controller = new AddWorldandBranchProductController(Arrays.asList(jf1,jf2,jf3,jf4,jf5,jf6,jf7), Arrays.asList(jb1,jb2), model, this);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        }catch (Exception exception){
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void refresh() {
        jf1.setText("");
        jf2.setText("");
        jf3.setText("");
        jf4.setText("");
        jf5.setText("");
        jf6.setText("");
        jf7.setText("");
        jl9.setText(model.getError());
    }

    public void close() {
        new ProductExplorerView(path,branchId, null, true);
        f.dispose();
    }
    public String getUsername() {
        return username;
    }

    public String getPath() {
        return path;
    }

    public int getBranchId() {
        return branchId;
    }
}
