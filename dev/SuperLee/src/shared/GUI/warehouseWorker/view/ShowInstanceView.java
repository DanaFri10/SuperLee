package shared.GUI.warehouseWorker.view;

import shared.GUI.warehouseWorker.controller.ProductController;
import shared.GUI.warehouseWorker.controller.ShowInstanceController;
import shared.GUI.warehouseWorker.model.ProductModel;
import shared.GUI.warehouseWorker.model.SearchInstanceModel;
import shared.GUI.warehouseWorker.model.ShowInstanceModel;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class ShowInstanceView {
    JFrame f;
    String username;
    int branchId;
    int instanceId;
    JButton jb1new;
    ShowInstanceModel model;
    JFrame newf;
    public ShowInstanceView(String username, int branchId, int instanceId) {
        // Create model

        this.username = username;
        this.branchId = branchId;
        this.instanceId = instanceId;
        try{
        model = new ShowInstanceModel(username, instanceId, branchId);

        f = new JFrame();
        JPanel jp1 = new JPanel();
        JPanel jp2 = new JPanel();
        JPanel jp3 = new JPanel();
        JLabel jl1 = new JLabel("instance id: " + model.getInstanceId());
        JLabel jl2 = new JLabel("product id: " + model.getProductId());
        JLabel jl3 = new JLabel("arrive date: "+ model.getArriveDate());
        JLabel jl4 = new JLabel("bought Price: " + model.getBoughtPrice());
        JLabel jl5 = new JLabel("place in store: " + model.getPlaceOfProduct());
        JLabel jl6 = new JLabel("expire: " + model.getExpireDate());
        JLabel jl7 = new JLabel(model.getDefectedDescription());
        JButton jb1 = new JButton("change expire date");
        JButton jb2 = new JButton("change location");
        JButton jb3 = new JButton("report defect");
        JButton jb4 = new JButton("delete instance");
        JButton jb7 = new JButton("return");

        //labels bounds
        jl1.setBounds(30, 20, 600, 30);
        jl2.setBounds(30, 40, 600, 30);
        jl3.setBounds(30, 60, 600, 30);
        jl4.setBounds(30, 80, 600, 30);
        jl5.setBounds(30, 100, 600, 30);
        jl6.setBounds(30, 120, 600, 30);
        jl7.setBounds(30, 140, 600, 30);

        jl1.setLayout(new FlowLayout(FlowLayout.LEFT));
        jl2.setLayout(new FlowLayout(FlowLayout.LEFT));
        jl3.setLayout(new FlowLayout(FlowLayout.LEFT));
        jl4.setLayout(new FlowLayout(FlowLayout.LEFT));
        jl5.setLayout(new FlowLayout(FlowLayout.LEFT));
        jl6.setLayout(new FlowLayout(FlowLayout.LEFT));
        jl7.setLayout(new FlowLayout(FlowLayout.LEFT));

        //buttons bound
        jb1.setBounds(200, 600, 180, 30);
        jb2.setBounds(400, 600, 180, 30);
        jb3.setBounds(200, 500, 180, 30);
        jb4.setBounds(400, 500, 180, 30);
        jb7.setBounds(600, 600, 180, 30);
        //frame
        f.add(jl1);
        f.add(jl2);
        f.add(jl3);
        f.add(jl4);
        f.add(jl5);
        f.add(jl6);
        f.add(jl7);

        f.add(jb1);
        f.add(jb2);
        f.add(jb3);
        f.add(jb4);

        f.add(jb7);


        f.setSize(800, 800);
        f.setLayout(null);
        f.setVisible(true);

        // Create controller
        jb1new = new JButton("whatever");

        ShowInstanceController controller = new ShowInstanceController(Arrays.asList(jb1,jb2,jb3,jb4,jb7,jb1new), model, this);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        }catch (Exception exception){
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void confirm(){
        JLabel jl1new = new JLabel(model.getError());//add
        newf = new JFrame();
        jl1new.setBounds(10, 100, 300, 20);
        jb1new.setBounds(110, 200, 100, 20);
        newf.add(jl1new);
        newf.add(jb1new);
        newf.setSize(400, 400);
        newf.setLayout(null);
        newf.setVisible(true);
    }
    public void closeDueDelete() {
        f.dispose();
        newf.dispose();
    }
    public void close() {
        f.dispose();
    }

    public String getUsername() {
        return username;
    }

    public int getInstanceId() {
        return instanceId;
    }

    public int getBranchId() {
        return branchId;
    }
}
