package shared.GUI.warehouseWorker.controller;

import shared.GUI.warehouseWorker.model.DeleteBranchProductModel;
import shared.GUI.warehouseWorker.view.DeleteBranchProductView;
import shared.GUI.warehouseWorker.view.ProductView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class DeleteBranchProductController {
    private DeleteBranchProductView view;
    private DeleteBranchProductModel model;


    public DeleteBranchProductController(List<JButton> buttons, DeleteBranchProductModel model, DeleteBranchProductView view) {
        this.model = model;
        this.view = view;
        //0 is yes 1 is no
        buttons.get(0).addActionListener(new DeleteBranchProductController.ButtonYesClickListener());
        buttons.get(1).addActionListener(new DeleteBranchProductController.ButtonNoClickListener());
        buttons.get(2).addActionListener(new DeleteBranchProductController.ButtonOkClickListener());


    }

    private class ButtonYesClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try{
            model.deleteBranchProduct();

            //confirm delete view
            view.confirm();
            }catch (Exception exception){
                JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private class ButtonNoClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.close();
        }
    }

    private class ButtonOkClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.closeDueDelete();
            //new ProductView(view.getUsername(), view.getProductId(), view.getBranchId());
        }
    }
}
