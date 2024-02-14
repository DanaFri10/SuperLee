package shared.GUI.warehouseWorker.controller;

import shared.GUI.warehouseWorker.model.ProductMapModel;
import shared.GUI.warehouseWorker.model.ShortageModel;
import shared.GUI.warehouseWorker.view.ChooseExtrasView;
import shared.GUI.warehouseWorker.view.MakeOrderView;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class ChooseExtrasController implements ActionListener, DocumentListener, FocusListener {
    private ShortageModel shortageModel;
    private ChooseExtrasView view;

    public ChooseExtrasController(ChooseExtrasView view, ShortageModel shortageModel){
        this.shortageModel = shortageModel;
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().startsWith("-")){
            int productId = Integer.parseInt(e.getActionCommand().substring(1));
            if(shortageModel.getProductExtra(productId) > 0) {
                shortageModel.decreaseProductExtra(productId);
                view.setTextField(productId, shortageModel.getProductExtra(productId));
            }
        }else if(e.getActionCommand().startsWith("+")){
            int productId = Integer.parseInt(e.getActionCommand().substring(1));
            shortageModel.increaseProductExtra(productId);
            view.setTextField(productId,shortageModel.getProductExtra(productId));
        }else if(e.getActionCommand().equals("order")){
            try{
            view.finalizeOrder(shortageModel.order());
            }catch (Exception exception){
                JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        else if(e.getActionCommand().equals("back")){
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
        for(int productId : shortageModel.getProductModelMap().keySet()){
            String amount = view.getTextField(productId);
            try{
                shortageModel.setProductExtra(productId, Integer.parseInt(amount));
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
        for(int productId : shortageModel.getExtrasMap().keySet()){
            view.setTextField(productId,shortageModel.getProductExtra(productId));
            view.makeNotRed(productId);
        }
    }
}
