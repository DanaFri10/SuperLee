package shared.GUI.suppliersManager.controller;

import shared.GUI.suppliersManager.model.ContactModel;
import shared.GUI.suppliersManager.model.DealProductModel;
import shared.GUI.suppliersManager.view.*;
import shared.service.Response;
import shared.service.SuppliersManagerService;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditDealProductController implements ActionListener {
    private DealProductModel model;
    private EditDealProductView view;
    private String username;
    private String from;
    private String supplierId;

    public EditDealProductController(String supplierId, DealProductModel model, EditDealProductView view, String username, String from)
    {
        this.model = model;
        this.view = view;
        this.username = username;
        this.from = from;
        this.supplierId = supplierId;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        /*
        if (e.getActionCommand().equals("ChooseProduct")) {
            ChooseProductView chooseProductView = new ChooseProductView(view, supplierId, model.getProductComplement(supplierId), service, username);
            chooseProductView.setVisible(true);
            view.dispose();
        }*/
        if (e.getActionCommand().equals("Done")) {
            if (from.equals("DealProductsList")) {
                Response res = model.addDealProduct(supplierId, view.getCatNum(), view.getProductId(), view.getPrice(), view.getAmount());
                if (!res.isError()) {
                    view.editedPopUpMessage("Deal product added successfully!");
                    view.dispose();
                    DealProductsListView listView = new DealProductsListView(supplierId, username);
                    listView.setVisible(true);
                    view.dispose();
                } else {
                    view.editedPopUpMessage(res.getErrorMessage());
                }
            }
            if (from.equals("DealProductView")) {
                Response res = model.editDealProduct(supplierId, view.getCatNum(), view.getPrice(), view.getAmount());
                if (!res.isError()) {
                    view.editedPopUpMessage("Deal product edited successfully!");
                    view.dispose();
                    DealProductsListView listView = new DealProductsListView(supplierId, username);
                    listView.setVisible(true);
                    view.dispose();
                } else {
                    view.editedPopUpMessage(res.getErrorMessage());
                }
            }
        }
        if (e.getActionCommand().equals("Back")) {
            if (from.equals("DealProductsList")) {
                DealProductsListView listView = new DealProductsListView(supplierId, username);
                listView.setVisible(true);
                view.dispose();
            }
            if (from.equals("DealProductView")) {
                DealProductView productView = new DealProductView(supplierId, view.getCatNum(), username);
                productView.setVisible(true);
                view.dispose();
            }
        }
    }

}
