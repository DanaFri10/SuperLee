package shared.GUI.warehouseWorker.controller;

import shared.GUI.warehouseWorker.model.AddBranchProductModel;
import shared.GUI.warehouseWorker.model.AddWorldandBranchProductModel;
import shared.GUI.warehouseWorker.view.AddBranchProductView;
import shared.GUI.warehouseWorker.view.AddWorldandBranchProductView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AddBranchProductController {
    private AddBranchProductModel model;
    private AddBranchProductView view;
    List<JTextField> fields;

    public AddBranchProductController(List<JTextField> fields, List<JButton> buttons, AddBranchProductModel model, AddBranchProductView view) {
        this.model = model;
        this.view = view;
        this.fields = fields;
        //0 is update 1 is return
        buttons.get(0).addActionListener(new AddBranchProductController.ButtonAddClickListener());
        buttons.get(1).addActionListener(new AddBranchProductController.ButtonReturnClickListener());


    }

    private class ButtonAddClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                model.addProductBranch(fields.get(0).getText(), fields.get(1).getText(), fields.get(2).getText(), fields.get(3).getText(), fields.get(4).getText());
                view.refresh();
                view.close();
                JOptionPane.showMessageDialog(null, "Branch Product added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            }catch (Exception exception){
                JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private class ButtonReturnClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.close();
            //new (view.getUsername(), view.getPath(), view.getBranchId());
        }
    }
}
