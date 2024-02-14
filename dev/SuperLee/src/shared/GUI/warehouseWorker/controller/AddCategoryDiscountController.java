package shared.GUI.warehouseWorker.controller;

import shared.GUI.warehouseWorker.model.AddCategoryDiscountModel;
import shared.GUI.warehouseWorker.model.AddDiscountProductModel;
import shared.GUI.warehouseWorker.view.AddCategoryDiscountView;
import shared.GUI.warehouseWorker.view.AddDiscountProductView;
import shared.GUI.warehouseWorker.view.ProductView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AddCategoryDiscountController {
    private AddCategoryDiscountModel model;
    private AddCategoryDiscountView view;
    List<JTextField> fields;

    public AddCategoryDiscountController(List<JTextField> fields, List<JButton> buttons, AddCategoryDiscountModel model, AddCategoryDiscountView view) {
        this.model = model;
        this.view = view;
        this.fields = fields;
        //0 is update 1 is return
        buttons.get(0).addActionListener(new AddCategoryDiscountController.ButtonUpdateClickListener());
        buttons.get(1).addActionListener(new AddCategoryDiscountController.ButtonReturnClickListener());


    }

    private class ButtonUpdateClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try{
                model.addDiscount(fields.get(0).getText(), fields.get(1).getText(), fields.get(2).getText());
                view.refresh();
                view.close();
                JOptionPane.showMessageDialog(null, "Discount added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            }catch (Exception exception){
                JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            //Update view
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
