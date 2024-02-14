package shared.GUI.warehouseWorker.controller;

import shared.GUI.warehouseWorker.model.CustomDivisionModel;
import shared.GUI.warehouseWorker.view.ChooseDivisionView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChooseDivisionController implements ActionListener {
    private ChooseDivisionView view;
    private CustomDivisionModel model;

    public ChooseDivisionController(ChooseDivisionView view, CustomDivisionModel model){
        this.view = view;
        this.model = model;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("back")){
            view.goBack();
        }else if(e.getActionCommand().equals("order")){
            for(int productId : model.getProductsMap().getProductAmountMap().keySet()){
                model.addCustomDivision(productId, view.getSupplierForProduct(productId).getId());
            }
            try{
            view.finalizeOrder(model.order());
            }catch (Exception exception){
                JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
