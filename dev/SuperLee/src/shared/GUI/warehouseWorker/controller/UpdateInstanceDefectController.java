package shared.GUI.warehouseWorker.controller;

import shared.GUI.warehouseWorker.model.UpdateInstanceDefectModel;
import shared.GUI.warehouseWorker.model.UpdateInstanceExpireModel;
import shared.GUI.warehouseWorker.view.ShowInstanceView;
import shared.GUI.warehouseWorker.view.UpdateInstanceDefectView;
import shared.GUI.warehouseWorker.view.UpdateInstanceExpireView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class UpdateInstanceDefectController {
    private UpdateInstanceDefectModel model;
    private UpdateInstanceDefectView view;
    List<JTextField> fields;

    public UpdateInstanceDefectController(List<JTextField> fields, List<JButton> buttons, UpdateInstanceDefectModel model, UpdateInstanceDefectView view) {
        this.model = model;
        this.view = view;
        this.fields = fields;
        //0 is update 1 is return
        buttons.get(0).addActionListener(new UpdateInstanceDefectController.ButtonUpdateClickListener());
        buttons.get(1).addActionListener(new UpdateInstanceDefectController.ButtonReturnClickListener());


    }

    private class ButtonUpdateClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                model.updateDefectiveDescription(fields.get(0).getText());

                //Update view
                view.refresh();
                JOptionPane.showMessageDialog(null, "Product defect description updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
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
