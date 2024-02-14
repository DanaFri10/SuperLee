package shared.GUI.suppliersManager.controller;

import shared.GUI.suppliersManager.model.ContactModel;
import shared.GUI.suppliersManager.model.DiscountsListModel;
import shared.GUI.suppliersManager.view.*;
import shared.service.Response;
import shared.service.SuppliersManagerService;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DiscountsListController implements ActionListener {
    private DiscountsListModel model;
    private DiscountsListView view;
    private String username;
    private String supplierId;
    private int catNum;

    public DiscountsListController(String supplierId, int catNum, DiscountsListModel model, DiscountsListView view, String username)
    {
        this.username = username;
        this.supplierId = supplierId;
        this.catNum = catNum;
        this.model = model;
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Clear"))
        {
            if(view.areYouSurePopUp()) {
                if (catNum < 0) {
                    Response res = model.clearDealDiscounts(supplierId);
                    if (!res.isError()) {
                        view.clearedPopUpMessage("Discounts cleared successfully!");
                        view.dispose();
                        DealView dealView = new DealView(supplierId, username);
                        dealView.setVisible(true);
                        view.dispose();
                    } else view.clearedPopUpMessage(res.getErrorMessage());
                } else {
                    Response res = model.clearProductDiscounts(supplierId, catNum);
                    if (!res.isError()) {
                        view.clearedPopUpMessage("Discounts cleared successfully!");
                        view.dispose();
                        DealProductView dealProductView = new DealProductView(supplierId, catNum, username);
                        dealProductView.setVisible(true);
                        view.dispose();
                    } else view.clearedPopUpMessage(res.getErrorMessage());
                }
            }
        }
        else if(e.getActionCommand().equals("Back")){
            if(catNum < 0)
            {
                DealView dealView = new DealView(supplierId, username);
                dealView.setVisible(true);
            }
            else
            {
                DealProductView dealProductView = new DealProductView(supplierId, catNum, username);
                dealProductView.setVisible(true);
            }
            view.dispose();
        }
        else {
            int type = 2;
            if (catNum < 0) {
                if (e.getActionCommand().equals("Products")) type = 1;
                else type = 0;
            }
            AddDiscountView addDiscountView = new AddDiscountView(supplierId, catNum, type, username);
            addDiscountView.setVisible(true);
            view.dispose();
        }

    }
}
