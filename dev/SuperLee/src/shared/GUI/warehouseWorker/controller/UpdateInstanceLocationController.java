package shared.GUI.warehouseWorker.controller;

import shared.GUI.warehouseWorker.model.UpdateInstanceLocationModel;
import shared.GUI.warehouseWorker.view.ProductView;
import shared.GUI.warehouseWorker.view.ShowInstanceView;
import shared.GUI.warehouseWorker.view.UpdateInstanceLocationView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class UpdateInstanceLocationController {
    private UpdateInstanceLocationModel model;
    private UpdateInstanceLocationView view;

    public UpdateInstanceLocationController(List<JButton> buttons, UpdateInstanceLocationModel model, UpdateInstanceLocationView view) {
        this.model = model;
        this.view = view;
        //0 is update 1 is return
        buttons.get(0).addActionListener(new UpdateInstanceLocationController.ButtonStockClickListener());
        buttons.get(1).addActionListener(new UpdateInstanceLocationController.ButtonStoreClickListener());
        buttons.get(2).addActionListener(new UpdateInstanceLocationController.ButtonReturnClickListener());


    }

    private class ButtonStockClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try{
            model.updateInstanceLocation("store");

            //Update view
            view.refresh();
                JOptionPane.showMessageDialog(null, "Product location updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            }catch (Exception exception){
                JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class ButtonStoreClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try{
            model.updateInstanceLocation("stock");

            //Update view
            view.refresh();
                JOptionPane.showMessageDialog(null, "Product location updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            }catch (Exception exception){
                JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class ButtonReturnClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.close();
            new ShowInstanceView(view.getUsername(), view.getBranchId(), view.getInstanceId());
        }
    }
}
