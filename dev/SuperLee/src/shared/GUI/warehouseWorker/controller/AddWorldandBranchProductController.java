package shared.GUI.warehouseWorker.controller;

import shared.GUI.warehouseWorker.model.AddCategoryDiscountModel;
import shared.GUI.warehouseWorker.model.AddWorldandBranchProductModel;
import shared.GUI.warehouseWorker.view.AddCategoryDiscountView;
import shared.GUI.warehouseWorker.view.AddWorldandBranchProductView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AddWorldandBranchProductController {
    private AddWorldandBranchProductModel model;
    private AddWorldandBranchProductView view;
    List<JTextField> fields;

    public AddWorldandBranchProductController(List<JTextField> fields, List<JButton> buttons, AddWorldandBranchProductModel model, AddWorldandBranchProductView view) {
        this.model = model;
        this.view = view;
        this.fields = fields;
        //0 is update 1 is return
        buttons.get(0).addActionListener(new AddWorldandBranchProductController.ButtonAddClickListener());
        buttons.get(1).addActionListener(new AddWorldandBranchProductController.ButtonReturnClickListener());


    }

    private class ButtonAddClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try{
            model.addProductWorldAndBranch(fields.get(0).getText(), fields.get(1).getText(), fields.get(2).getText(),fields.get(3).getText(), fields.get(4).getText(), fields.get(5).getText(), fields.get(6).getText());

            //Update view
            view.refresh();
            view.close();
                JOptionPane.showMessageDialog(null, "Product added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
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
