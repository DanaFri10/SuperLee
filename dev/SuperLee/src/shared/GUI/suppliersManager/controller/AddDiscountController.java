package shared.GUI.suppliersManager.controller;

import shared.GUI.suppliersManager.model.ContactModel;
import shared.GUI.suppliersManager.model.DiscountModel;
import shared.GUI.suppliersManager.model.DiscountsListModel;
import shared.GUI.suppliersManager.view.*;
import shared.service.Response;
import shared.service.SuppliersManagerService;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddDiscountController implements ActionListener {

    private DiscountModel model;
    private AddDiscountView view;
    private String username;
    private String supplierId;
    private int catNum;
    private int type;

    public AddDiscountController(String supplierId, int catNum, int type, DiscountModel model, AddDiscountView view, String username)
    {
        this.model = model;
        this.view = view;
        this.username = username;
        this.supplierId = supplierId;
        this.catNum = catNum;
        this.type = type;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Back"))
        {
            DiscountsListView listView = new DiscountsListView(supplierId, catNum, username);
            listView.setVisible(true);
            view.dispose();
        }
        else {
            Response res;
            double percentage = view.getPercentage();
            if (type == 0) //total price deal discount
            {
                double minPrice = view.getMinPrice();
                res = model.addTotalPriceDiscount(supplierId, percentage, minPrice);
            } else if (type == 1) {
                int minAmount = view.getMinAmount();
                res = model.addTotalProductDiscount(supplierId, percentage, minAmount);
            } else //product amount discount
            {
                int minAmount = view.getMinAmount();
                res = model.addProductAmountDiscount(supplierId, catNum, percentage, minAmount);
            }

            if (res.isError()) {
                view.addedPopUpMessage(res.getErrorMessage());
            } else {
                view.addedPopUpMessage("Discount added successfully!");
                DiscountsListView listView = new DiscountsListView(supplierId, catNum, username);
                listView.setVisible(true);
                view.dispose();
            }
        }
    }


}
