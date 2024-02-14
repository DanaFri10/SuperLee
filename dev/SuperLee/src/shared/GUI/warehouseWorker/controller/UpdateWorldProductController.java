package shared.GUI.warehouseWorker.controller;


import shared.GUI.warehouseWorker.model.UpdateWorldProductModel;
import shared.GUI.warehouseWorker.view.ProductView;
import shared.GUI.warehouseWorker.view.UpdateWorldProductView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;


public class UpdateWorldProductController {
    private UpdateWorldProductModel model;
    private UpdateWorldProductView view;
    List<JTextField> fields;

    public UpdateWorldProductController(List<JTextField> fields, List<JButton> buttons, UpdateWorldProductModel model, UpdateWorldProductView view) {
        this.model = model;
        this.view = view;
        this.fields = fields;
        //0 is update 1 is return
        buttons.get(0).addActionListener(new ButtonUpdateClickListener());
        buttons.get(1).addActionListener(new ButtonReturnClickListener());


    }

    private class ButtonUpdateClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try{
            model.updateProduct(fields.get(0).getText(), fields.get(1).getText());

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
