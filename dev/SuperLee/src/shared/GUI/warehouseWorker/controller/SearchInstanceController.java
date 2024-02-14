package shared.GUI.warehouseWorker.controller;

import shared.GUI.warehouseWorker.model.SearchInstanceModel;
import shared.GUI.warehouseWorker.model.UpdateWorldProductModel;
import shared.GUI.warehouseWorker.view.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class SearchInstanceController {
    private SearchInstanceModel model;
    private SearchInstanceView view;
    List<JTextField> fields;

    public SearchInstanceController(List<JTextField> fields, List<JButton> buttons, SearchInstanceModel model, SearchInstanceView view) {
        this.model = model;
        this.view = view;
        this.fields = fields;
        buttons.get(0).addActionListener(new SearchInstanceController.ButtonShowClickListener());
        buttons.get(1).addActionListener(new SearchInstanceController.ButtonAddClickListener());
        buttons.get(2).addActionListener(new SearchInstanceController.ButtonReturnClickListener());


    }

    private class ButtonShowClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try{
            boolean flag = model.checkIfExist(fields.get(0).getText());
            if(flag){
                view.close();
                new ShowInstanceView(view.getUsername(), view.getBranchId(), model.getInstanceId());
            }
            else{
                view.refresh();
            }
            }catch (Exception exception){
                JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class ButtonAddClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.close();
            new AddInstanceView(view.getUsername(), view.getBranchId());
        }
    }

    private class ButtonReturnClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.back();
            //new ProductView(view.getUsername() view.getBranchId());
        }
    }
}
