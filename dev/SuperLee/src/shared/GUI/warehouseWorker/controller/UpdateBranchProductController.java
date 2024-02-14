package shared.GUI.warehouseWorker.controller;

import shared.GUI.warehouseWorker.model.UpdateBranchProductModel;
import shared.GUI.warehouseWorker.view.ProductView;
import shared.GUI.warehouseWorker.view.UpdateBranchProductView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class UpdateBranchProductController {

    private UpdateBranchProductModel model;
    private UpdateBranchProductView view;
    List<JTextField> fields;

    public UpdateBranchProductController(List<JTextField> fields, List<JButton> buttons, UpdateBranchProductModel model, UpdateBranchProductView view) {
        this.model = model;
        this.view = view;
        this.fields = fields;
        //0 is update 1 is return
        buttons.get(0).addActionListener(new UpdateBranchProductController.ButtonUpdateClickListener());
        buttons.get(1).addActionListener(new UpdateBranchProductController.ButtonReturnClickListener());


    }

    private class ButtonUpdateClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try{
            model.updateBranchProduct(fields.get(0).getText(), fields.get(1).getText(), fields.get(2).getText(), fields.get(3).getText(), fields.get(4).getText());

            //Update view
            view.refresh();
                JOptionPane.showMessageDialog(null, "Product updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            }catch (Exception exception){
                JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private class ButtonReturnClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.close();
            new ProductView(view.getUsername(), view.getProductId(), view.getBranchId());
        }
    }
}
