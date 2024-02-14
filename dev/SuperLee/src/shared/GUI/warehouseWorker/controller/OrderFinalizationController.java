package shared.GUI.warehouseWorker.controller;

import shared.GUI.warehouseWorker.model.OrderModel;
import shared.GUI.warehouseWorker.model.SupplierModel;
import shared.GUI.warehouseWorker.view.OrderFinalizationView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OrderFinalizationController implements ActionListener {
    private OrderModel model;
    private OrderFinalizationView view;
    public OrderFinalizationController(OrderFinalizationView view, OrderModel model){
        this.model = model;
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("final")){
            for(String supplierId : model.getSuppliers().keySet()){
                if(model.supplierHasContacts(supplierId)){
                    try{
                    model.assignContact(supplierId,view.getSelectedContact(supplierId).getId());
                    if(view.getIsEmail(supplierId)){
                        model.updateEmail(supplierId);
                    }
                    }catch (Exception exception){
                        JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            JOptionPane.showMessageDialog(null, "Order deployed successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            view.close();
        }
    }
}
