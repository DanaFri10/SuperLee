package shared.GUI.warehouseWorker.controller;

import shared.GUI.warehouseWorker.model.DeleteWorldProductModel;
import shared.GUI.warehouseWorker.view.DeleteWorldProductView;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class DeleteWorldProductController {
    private DeleteWorldProductView view;
    private DeleteWorldProductModel model;


    public DeleteWorldProductController(List<JButton> buttons, DeleteWorldProductModel model, DeleteWorldProductView view) {
        this.model = model;
        this.view = view;
        //0 is yes 1 is no 2 is ok
        buttons.get(0).addActionListener(new DeleteWorldProductController.ButtonYesClickListener());
        buttons.get(1).addActionListener(new DeleteWorldProductController.ButtonNoClickListener());
        buttons.get(2).addActionListener(new DeleteWorldProductController.ButtonOkClickListener());

    }

    private class ButtonYesClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                model.deleteWorldProduct();

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
