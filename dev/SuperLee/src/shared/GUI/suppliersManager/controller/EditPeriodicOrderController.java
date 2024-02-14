package shared.GUI.suppliersManager.controller;

import shared.GUI.suppliersManager.model.PeriodicOrderModel;
import shared.GUI.suppliersManager.model.SupplierModel;
import shared.GUI.suppliersManager.view.*;
import shared.service.Response;
import shared.service.SuppliersManagerService;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class EditPeriodicOrderController implements ActionListener {
    private JList<String> products;
    private String supplierId;
    private PeriodicOrderModel model;
    private EditPeriodicOrderView view;
    private String username;
    private String from;

    public EditPeriodicOrderController(JList<String> products, String supplierId, PeriodicOrderModel model, EditPeriodicOrderView view, String username, String from)
    {
        this.products = products;
        this.supplierId = supplierId;
        this.model = model;
        this.view = view;
        this.username = username;
        this.from = from;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        boolean newOrder = from.equals("OrderListView");

        if (e.getActionCommand().equals("AddProduct")) {
            List<Integer> used = new ArrayList<>();
            ListModel<String> productStrings = products.getModel();
            for(int i = 0 ; i<productStrings.getSize(); i++)
            {
                int catNum = Integer.parseInt(productStrings.getElementAt(i).substring(0, productStrings.getElementAt(i).indexOf('-')).trim());
                used.add(catNum);
            }

            ChooseDealProductView chooseProductView = new ChooseDealProductView(supplierId, view, username, newOrder, used);
            chooseProductView.setVisible(true);
            //view.dispose();
        }
        if (e.getActionCommand().equals("RemoveProduct")) {
            if(products.getSelectedIndex() != -1) {
                int catNum = Integer.parseInt(products.getSelectedValue().substring(0, products.getSelectedValue().indexOf('-')).trim());
                view.removeProductFromList(catNum);
                //model.removeProduct(supplierId, branchId, catNum, newOrder);
                //view.displayProductsList(model.getPeriodicOrder(supplierId, branchId).getProductAmounts());
            }
        }
        if (e.getActionCommand().equals("Done")) {
            Response productsRes = model.setPeriodicOrderProducts(supplierId, view.getBranchId(), view.getProductAmounts(), newOrder);
            if(!productsRes.isError())
            {
                Response contactsRes = model.setPeriodicOrderContact(supplierId, view.getBranchId(), view.getContactId());
                if(!contactsRes.isError()){
                    if(view.needsSetDays()) {
                        Response daysRes = model.setPeriodicOrderDays(supplierId, view.getBranchId(), view.getOrderDays());
                        if (!daysRes.isError()) {
                            view.editedPopUpMessage("Order edited successfully!");
                            goBack();
                        } else {
                            view.editedPopUpMessage(daysRes.getErrorMessage());
                        }
                    }else{
                        view.editedPopUpMessage("Order edited successfully!");
                        goBack();
                    }
                }else{
                    view.editedPopUpMessage(contactsRes.getErrorMessage());
                }
            }
            else
            {
                view.editedPopUpMessage(productsRes.getErrorMessage());
            }
        }
        if (e.getActionCommand().equals("Back")) {
            goBack();
        }

    }

    private void goBack()
    {
        if(from.equals("PeriodicOrderView"))
        {
            PeriodicOrderView orderView = new PeriodicOrderView(supplierId, view.getBranchId(), username);
            orderView.setVisible(true);
            view.dispose();
        }
        else
        {
            PeriodicOrdersListView listView = new PeriodicOrdersListView(supplierId, username);
            listView.setVisible(true);
            view.dispose();
        }
    }

}
