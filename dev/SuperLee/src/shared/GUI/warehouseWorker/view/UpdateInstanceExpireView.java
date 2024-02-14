package shared.GUI.warehouseWorker.view;

import shared.GUI.warehouseWorker.controller.UpdateInstanceExpireController;
import shared.GUI.warehouseWorker.controller.UpdateInstanceLocationController;
import shared.GUI.warehouseWorker.model.UpdateInstanceExpireModel;
import shared.GUI.warehouseWorker.model.UpdateInstanceLocationModel;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class UpdateInstanceExpireView {
    JLabel jl1;
    JLabel jl3;
    UpdateInstanceExpireModel model;
    JFrame f;
    String username;
    int branchId;
    int instanceId;
    public UpdateInstanceExpireView(String username, int instanceId, int branchId){
        // Create model
        this.username = username;
        this.branchId = branchId;
        this.instanceId = instanceId;
        try{
        model = new UpdateInstanceExpireModel(username, branchId, instanceId);
        f = new JFrame();
        jl1 = new JLabel("current expire date: " + model.getExpireDate() + "; new expire date: ");
        jl3 = new JLabel();
        JButton jb1 = new JButton("update");
        JButton jb3 = new JButton("return");
        JTextField jf1 = new JTextField(20);
        JPanel jp1 = new JPanel();
        JLabel jl6 = new JLabel("write date in this format: yyyy-MM-dd, or write \"-\" for no expire date ;");

        jp1.add(jl1);
        jp1.add(jf1);
        jb1.setBounds(300, 500, 150, 30);
        jb3.setBounds(150, 500, 150, 30);
        jp1.setBounds(100, 100, 600, 30);
        jp1.setLayout(new FlowLayout(FlowLayout.LEFT));

        jl6.setBounds(100, 100, 700, 30);

        //frame

        f.add(jb1);  f.add(jb3); f.add(jp1); f.add(jl3); f.add(jl3); f.add(jl6);

        f.setSize(800, 800);
        f.setLayout(null);
        f.setVisible(true);

        // Create controller
        UpdateInstanceExpireController controller = new UpdateInstanceExpireController(Arrays.asList(jf1), Arrays.asList(jb1, jb3), model, this);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        }catch (Exception exception){
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void refresh() {
        jl1.setText("current expire date: " + model.getExpireDate() + "new expire date: ");
        jl3.setText(model.getError());
    }

    public void close() {
        f.dispose();
    }
    public String getUsername() {
        return username;
    }


    public int getBranchId() {return branchId;}
    public int getInstanceId() {
        return instanceId;
    }
}
