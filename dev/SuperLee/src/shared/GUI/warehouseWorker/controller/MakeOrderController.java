package shared.GUI.warehouseWorker.controller;

import shared.GUI.warehouseWorker.model.ProductMapModel;
import shared.GUI.warehouseWorker.view.MakeOrderView;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class MakeOrderController implements ActionListener, DocumentListener, FocusListener {
    private ProductMapModel productMapModel;
    private MakeOrderView view;

    public MakeOrderController(MakeOrderView view,ProductMapModel productMapModel){
        this.productMapModel = productMapModel;
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().startsWith("-")){
            int productId = Integer.parseInt(e.getActionCommand().substring(1));
            if(productMapModel.getProductAmount(productId) > 0) {
                productMapModel.decreaseProductAmount(productId);
                view.setTextField(productId, productMapModel.getProductAmountMap().get(productId));
            }
        }else if(e.getActionCommand().startsWith("+")){
            int productId = Integer.parseInt(e.getActionCommand().substring(1));
            productMapModel.increaseProductAmount(productId);
            view.setTextField(productId,productMapModel.getProductAmountMap().get(productId));
        }else if(e.getActionCommand().startsWith("*")){
            int productId = Integer.parseInt(e.getActionCommand().substring(1));
            productMapModel.remove(productId);
            view.rerender();
        }else if(e.getActionCommand().equals("add")){
            view.goAddProduct();
        }else if(e.getActionCommand().startsWith("order")){
            if(e.getActionCommand().endsWith("Opt")){
                try{
                view.finalizeOrder(productMapModel.orderOptimally());
                }catch (Exception exception){
                    JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }else if(e.getActionCommand().endsWith("Fas")){
                try{
                view.finalizeOrder(productMapModel.orderFastest());
                    JOptionPane.showMessageDialog(null, "Branch Product added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                }catch (Exception exception){
                    JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }else if(e.getActionCommand().endsWith("Choose")){
                view.chooseSuppliers();
            }
        }else if(e.getActionCommand().equals("back")){
            view.goBack();
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        changedUpdate(e);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        changedUpdate(e);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        for(int productId : productMapModel.getProductModelMap().keySet()){
            String amount = view.getTextField(productId);
            try{
                productMapModel.setProductAmount(productId, Integer.parseInt(amount));
                view.makeNotRed(productId);
            }catch (NumberFormatException nfe){
                view.makeRed(productId);
            }
        }
    }

    @Override
    public void focusGained(FocusEvent e) {

    }

    @Override
    public void focusLost(FocusEvent e) {
        for(int productId : productMapModel.getProductAmountMap().keySet()){
            view.setTextField(productId,productMapModel.getProductAmount(productId));
            view.makeNotRed(productId);
        }
    }
}
