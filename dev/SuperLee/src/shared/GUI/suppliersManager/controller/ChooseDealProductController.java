package shared.GUI.suppliersManager.controller;

import shared.GUI.suppliersManager.model.PeriodicOrderModel;
import shared.GUI.suppliersManager.view.ChooseDealProductView;
import shared.GUI.suppliersManager.view.EditPeriodicOrderView;
import shared.service.SuppliersManagerService;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChooseDealProductController implements ActionListener {
    private ButtonGroup products;
    private ChooseDealProductView chooseProductView;
    private EditPeriodicOrderView editOrderView;
    private boolean newOrder;

    public ChooseDealProductController(ButtonGroup products, ChooseDealProductView view, EditPeriodicOrderView editOrderView)
    {
        this.products = products;
        this.chooseProductView = view;
        this.editOrderView = editOrderView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Back")) {
        chooseProductView.dispose();
        }
        else if(!e.getActionCommand().equals("Ok")) {
            chooseProductView.enableOkButton();
        }
        else {
            int catNum = Integer.parseInt(products.getSelection().getActionCommand());
            int amount = chooseProductView.getAmount();
            editOrderView.addProductToList(catNum, amount);
            //editOrderView.setVisible(true);
            //editOrderView.displayProductsList(model.getPeriodicOrder(supplierId, editOrderView.getBranchId()).getProductAmounts());
            chooseProductView.dispose();
        }
    }


}
