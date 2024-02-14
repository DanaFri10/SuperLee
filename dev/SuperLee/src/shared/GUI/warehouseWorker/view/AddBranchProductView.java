package shared.GUI.warehouseWorker.view;

import shared.GUI.warehouseWorker.controller.AddBranchProductController;
import shared.GUI.warehouseWorker.controller.AddWorldandBranchProductController;
import shared.GUI.warehouseWorker.model.AddBranchProductModel;
import shared.GUI.warehouseWorker.model.AddWorldandBranchProductModel;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class AddBranchProductView {
    JLabel jl1;
    JLabel jl4;
    JLabel jl5;
    JLabel jl6;
    JLabel jl7;
    JLabel jl9;
    JTextField jf1;
    JTextField jf4;
    JTextField jf5;
    JTextField jf6;
    JTextField jf7;
    AddBranchProductModel model;
    JFrame f;
    String username;
    String path;
    int branchId;
    public AddBranchProductView(String username, String path, int branchId){
        // Create model
        this.username = username;
        this.path = path;
        this.branchId = branchId;
        f = new JFrame();
        try {
            model = new AddBranchProductModel(username, path, branchId);
            JPanel jp1 = new JPanel();
            JPanel jp4 = new JPanel();
            JPanel jp5 = new JPanel();
            JPanel jp6 = new JPanel();
            JPanel jp7 = new JPanel();
            jl1 = new JLabel(" id: ");
            jl4 = new JLabel(" price sold: ");
            jl5 = new JLabel(" demand: ");
            jl6 = new JLabel(" minimum: ");
            jl7 = new JLabel(" place in store: ");
            jl9 = new JLabel();
            jf1 = new JTextField(20);
            jf4 = new JTextField(20);
            jf5 = new JTextField(20);
            jf6 = new JTextField(20);
            jf7 = new JTextField(20);

            JButton jb1 = new JButton("add");
            JButton jb2 = new JButton("return");

        //panel
            jp1.add(jl1);
            jp1.add(jf1);
            jp4.add(jl4);
            jp4.add(jf4);
            jp5.add(jl5);
            jp5.add(jf5);
            jp6.add(jl6);
            jp6.add(jf6);
            jp7.add(jl7);
            jp7.add(jf7);
            jp1.setBounds(200, 100, 600, 30);
            jp4.setBounds(200, 150, 600, 30);
            jp5.setBounds(200, 200, 600, 30);
            jp6.setBounds(200, 250, 600, 30);
            jp7.setBounds(200, 300, 600, 30);
            jb1.setBounds(200, 600, 200, 30);
            jb2.setBounds(400, 600, 200, 30);
            jp1.setLayout(new FlowLayout(FlowLayout.LEFT));
            jp4.setLayout(new FlowLayout(FlowLayout.LEFT));
            jp5.setLayout(new FlowLayout(FlowLayout.LEFT));
            jp6.setLayout(new FlowLayout(FlowLayout.LEFT));
            jp7.setLayout(new FlowLayout(FlowLayout.LEFT));
            jl9.setBounds(300, 500, 600, 30);
            //frame

            f.add(jp1); f.add(jp4); f.add(jp5); f.add(jp6); f.add(jp7);
            f.add(jb1); f.add(jb2); f.add(jl9);

            f.setSize(800, 800);
            f.setLayout(null);
            f.setVisible(true);

            // Create controller
            AddBranchProductController controller = new AddBranchProductController(Arrays.asList(jf1,jf4,jf5,jf6,jf7), Arrays.asList(jb1,jb2), model, this);
            f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        }catch (Exception exception){
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void refresh() {
        jf1.setText("");

        jf4.setText("");
        jf5.setText("");
        jf6.setText("");
        jf7.setText("");
        jl9.setText(model.getError());
    }

    public void close() {
        new ProductExplorerView(path, branchId, null, true);
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
