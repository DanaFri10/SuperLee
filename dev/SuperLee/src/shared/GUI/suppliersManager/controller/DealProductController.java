package shared.GUI.suppliersManager.controller;

import shared.GUI.suppliersManager.model.ContactModel;
import shared.GUI.suppliersManager.model.DealProductModel;
import shared.GUI.suppliersManager.view.*;
import shared.service.Response;
import shared.service.SuppliersManagerService;
import suppliers.business.Contact;
import suppliers.business.DealProductInformation;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DealProductController implements ActionListener {
    private DealProductModel model;
    private DealProductView view;
    private String supplierId;
    private int catNum;
    private String username;

    public DealProductController(DealProductModel model, DealProductView view, String supplierId, int catNum, String username) {
        this.model = model;
        this.view = view;
        this.supplierId = supplierId;
        this.catNum = catNum;
        this.username = username;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Edit"))
        {
            EditDealProductView editView = new EditDealProductView(supplierId, catNum, username, "DealProductView");
            editView.setVisible(true);
            view.dispose();
        }
        if(e.getActionCommand().equals("Delete"))
        {
            if(view.areYouSurePopUp()) {
                Response res = model.removeProductFromDeal(supplierId, catNum);
                if (res.isError()) {
                    view.deletedPopUpMessage(res.getErrorMessage());
                } else {
                    view.deletedPopUpMessage("Deal product was removed successfully!");
                    view.dispose();
                    DealProductsListView listView = new DealProductsListView(supplierId, username);
                    listView.setVisible(true);
                    view.dispose();
                }
            }
        }
        if(e.getActionCommand().equals("Discounts"))
        {
            DiscountsListView discountsListView = new DiscountsListView(supplierId, catNum, username);
            discountsListView.setVisible(true);
            view.dispose();
        }
        if(e.getActionCommand().equals("Back"))
        {
            DealProductsListView listView = new DealProductsListView(supplierId, username);
            listView.setVisible(true);
            view.dispose();
        }
    }

}
