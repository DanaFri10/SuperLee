package shared.GUI.warehouseWorker.view;


import shared.GUI.warehouseWorker.controller.UpdateInstanceLocationController;
import shared.GUI.warehouseWorker.model.UpdateInstanceLocationModel;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class UpdateInstanceLocationView {
    JLabel jl1;
    JLabel jl2;
    JLabel jl3;
    UpdateInstanceLocationModel model;
    JFrame f;
    String username;
    int branchId;
    int instanceId;
    public UpdateInstanceLocationView(String username, int instanceId, int branchId){
        // Create model
        this.username = username;
        this.branchId = branchId;
        this.instanceId = instanceId;
        try{
        model = new UpdateInstanceLocationModel(username, branchId, instanceId);
        f = new JFrame();
        jl2 = new JLabel("current place: " + model.getLocation());
        jl3 = new JLabel();
        JButton jb1 = new JButton("Store");
        JButton jb2 = new JButton("stock");
        JButton jb3 = new JButton("return");


        jb1.setBounds(300, 500, 150, 30);
        jb2.setBounds(450, 500, 150, 30);
        jb3.setBounds(150, 500, 150, 30);

        jl2.setBounds(300, 300, 600, 30);
        jl3.setBounds(300, 400, 600, 30);
        //frame

        f.add(jb1); f.add(jb2); f.add(jb3); f.add(jl2); f.add(jl3); f.add(jl3);

        f.setSize(800, 800);
        f.setLayout(null);
        f.setVisible(true);

        // Create controller
        UpdateInstanceLocationController controller = new UpdateInstanceLocationController(Arrays.asList(jb1,jb2, jb3), model, this);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        }catch (Exception exception){
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void refresh() {
        jl2.setText("current place: " + model.getLocation());
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
