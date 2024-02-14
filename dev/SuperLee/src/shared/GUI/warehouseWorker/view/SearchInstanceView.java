package shared.GUI.warehouseWorker.view;

import shared.GUI.warehouseWorker.controller.SearchInstanceController;
import shared.GUI.warehouseWorker.model.SearchInstanceModel;


import javax.swing.*;
import java.util.Arrays;

public class SearchInstanceView {
    JLabel jl1;
    JLabel jl2;
    JLabel jl3;
    JFrame f;
    SearchInstanceModel model;
    String username;
    int branchId;

    public SearchInstanceView(String username, int branchId){
        // Create model
        this.username = username;
        this.branchId = branchId;
        try{
        model = new SearchInstanceModel(username, branchId);
        f = new JFrame();
        jl3 = new JLabel();
        JButton jb1 = new JButton("show");
        JButton jb2 = new JButton("add instance");
        JButton jb3 = new JButton("return");
        JPanel jp1 = new JPanel();
        jl1 = new JLabel("search instance (enter instance id): ");
        JTextField jf1 = new JTextField(20);

        //panel
        jp1.add(jl1);
        jp1.add(jf1);
        jp1.setBounds(50, 100, 600, 30);
        jl3.setBounds(300, 100, 150, 30);
        jb1.setBounds(450, 300, 150, 30);
        jb2.setBounds(300, 300, 150, 30);
        jb3.setBounds(150, 300, 150, 30);

        //frame

        f.add(jb1); f.add(jb2); f.add(jb3); f.add(jp1);
        f.add(jl3);

        f.setSize(800, 500);
        f.setLayout(null);
        f.setVisible(true);

        // Create controller
        SearchInstanceController controller = new SearchInstanceController(Arrays.asList(jf1), Arrays.asList(jb1,jb2,jb3), model, this);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        }catch (Exception exception){
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void refresh() {
        jl3.setText(model.getError());
    }

    public void back(){
        new MainMenuView(branchId);
        close();
    }

    public void close() {
        f.dispose();
    }
    public String getUsername() {
        return username;
    }


    public int getBranchId() {return branchId;}
}
