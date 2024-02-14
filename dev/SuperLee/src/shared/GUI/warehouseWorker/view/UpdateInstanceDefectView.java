package shared.GUI.warehouseWorker.view;

import shared.GUI.warehouseWorker.controller.UpdateInstanceDefectController;
import shared.GUI.warehouseWorker.controller.UpdateInstanceLocationController;
import shared.GUI.warehouseWorker.model.UpdateInstanceDefectModel;
import shared.GUI.warehouseWorker.model.UpdateInstanceLocationModel;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class UpdateInstanceDefectView {
    JLabel jl1;
    JLabel jl3;
    UpdateInstanceDefectModel model;
    JFrame f;
    String username;
    int branchId;
    int instanceId;
    public UpdateInstanceDefectView(String username, int instanceId, int branchId){
        // Create model
        this.username = username;
        this.branchId = branchId;
        this.instanceId = instanceId;
        try{
        model = new UpdateInstanceDefectModel(username, branchId, instanceId);
        f = new JFrame();
        jl1 = new JLabel( model.getDefectiveDescription() + " ;   new defective description: ");
        jl3 = new JLabel();
        JButton jb1 = new JButton("set defective");
        JButton jb3 = new JButton("return");
        JTextField jf1 = new JTextField(20);
        JPanel jp1 = new JPanel();

        jp1.add(jl1);
        jp1.add(jf1);
        jb1.setBounds(300, 500, 150, 30);
        jb3.setBounds(150, 500, 150, 30);
        jp1.setBounds(100, 100, 750, 30);
        jp1.setLayout(new FlowLayout(FlowLayout.LEFT));


        //frame

        f.add(jb1);  f.add(jb3); f.add(jp1); f.add(jl3); f.add(jl3);

        f.setSize(800, 800);
        f.setLayout(null);
        f.setVisible(true);

        // Create controller
        UpdateInstanceDefectController controller = new UpdateInstanceDefectController(Arrays.asList(jf1), Arrays.asList(jb1, jb3), model, this);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        }catch (Exception exception){
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void refresh() {
        jl1.setText(model.getDefectiveDescription() + " ;   new defective description: ");
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
