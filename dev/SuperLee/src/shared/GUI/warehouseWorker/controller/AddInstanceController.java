package shared.GUI.warehouseWorker.controller;

import shared.GUI.warehouseWorker.model.AddDiscountProductModel;
import shared.GUI.warehouseWorker.model.AddInstanceModel;
import shared.GUI.warehouseWorker.view.AddDiscountProductView;
import shared.GUI.warehouseWorker.view.AddInstanceView;
import shared.GUI.warehouseWorker.view.ProductView;
import shared.GUI.warehouseWorker.view.SearchInstanceView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AddInstanceController {
    private AddInstanceModel model;
    private AddInstanceView view;
    List<JTextField> fields;

    public AddInstanceController(List<JTextField> fields, List<JButton> buttons, AddInstanceModel model, AddInstanceView view) {
        this.model = model;
        this.view = view;
        this.fields = fields;
        //0 is update 1 is return
        buttons.get(0).addActionListener(new AddInstanceController.ButtonAddInstanceClickListener());
        buttons.get(1).addActionListener(new AddInstanceController.ButtonReturnClickListener());


    }

    private class ButtonAddInstanceClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try{
            model.addInstance(fields.get(0).getText(), fields.get(1).getText(), fields.get(2).getText(), fields.get(3).getText());

            //Update view
            view.refresh();
                JOptionPane.showMessageDialog(null, "Product Instance added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            }catch (Exception exception){
                JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class ButtonReturnClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.close();
            new SearchInstanceView(view.getUsername(),view.getBranchId());
        }
    }
}
