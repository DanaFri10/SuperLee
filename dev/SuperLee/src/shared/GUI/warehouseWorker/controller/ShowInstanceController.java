package shared.GUI.warehouseWorker.controller;

import shared.GUI.warehouseWorker.model.ProductModel;
import shared.GUI.warehouseWorker.model.SearchInstanceModel;
import shared.GUI.warehouseWorker.model.ShowInstanceModel;
import shared.GUI.warehouseWorker.view.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ShowInstanceController {
    private ShowInstanceModel model;
    private ShowInstanceView view;

    public ShowInstanceController(List<JButton> buttons, ShowInstanceModel model, ShowInstanceView view) {
        this.model = model;
        this.view = view;
//        JButton jb1 = new JButton("change expire date");
//        JButton jb2 = new JButton("change location");
//        JButton jb3 = new JButton("report defect");
//        JButton jb4 = new JButton("delete instance");
//        JButton jb7 = new JButton("return");

        buttons.get(0).addActionListener(new ShowInstanceController.ButtonChangeExpireClickListener());
        buttons.get(1).addActionListener(new ShowInstanceController.ButtonChangeLocationClickListener());
        buttons.get(2).addActionListener(new ShowInstanceController.ButtonReportDefectiveClickListener());
        buttons.get(3).addActionListener(new ShowInstanceController.ButtonDeleteInstanceClickListener());
        buttons.get(4).addActionListener(new ShowInstanceController.ButtonReturnClickListener());
        buttons.get(5).addActionListener(new ShowInstanceController.ButtonOkClickListener());

    }

    private class ButtonChangeExpireClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.close();
            new UpdateInstanceExpireView(view.getUsername(), view.getInstanceId(), view.getBranchId());
        }
    }

    private class ButtonChangeLocationClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.close();
            new UpdateInstanceLocationView(view.getUsername(), view.getInstanceId() ,view.getBranchId());
        }
    }

    private class ButtonReportDefectiveClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.close();
            new UpdateInstanceDefectView(view.getUsername(), view.getInstanceId(), view.getBranchId());
        }
    }

    private class ButtonDeleteInstanceClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try{
            model.deleteInstance();
            view.confirm();
            }catch (Exception exception){
                JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class ButtonReturnClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.close();
            new SearchInstanceView(view.getUsername(), view.getBranchId());
        }
    }
    private class ButtonOkClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.closeDueDelete();
            new SearchInstanceView(view.getUsername(), view.getBranchId());
        }
    }
}
