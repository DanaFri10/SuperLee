package shared.GUI.suppliersManager.controller;

import shared.GUI.suppliersManager.model.DealModel;
import shared.GUI.suppliersManager.view.*;
import shared.service.SuppliersManagerService;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DealController implements ActionListener{
    private String supplier;
    private DealModel model;
    private DealView view;
    private String username;

    public DealController(String supplier,DealModel model, DealView view, String username)
    {
        this.supplier = supplier;
        this.model = model;
        this.view = view;
        this.username = username;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("DealProducts"))
        {
            DealProductsListView dealProductsListView = new DealProductsListView(supplier, username);
            dealProductsListView.setVisible(true);
            view.dispose();
        }
        else if(e.getActionCommand().equals("PeriodicOrders"))
        {
            PeriodicOrdersListView periodicOrdersListView = new PeriodicOrdersListView(supplier, username);
            periodicOrdersListView.setVisible(true);
            view.dispose();
        }
        else if(e.getActionCommand().equals("DealDiscounts"))
        {
            DiscountsListView discountsListView = new DiscountsListView(supplier, -1, username);
            discountsListView.setVisible(true);
            view.dispose();
        }
        else if(e.getActionCommand().equals("Back"))
        {
            SupplierView supplierView = new SupplierView(supplier, username);
            supplierView.setVisible(true);
            view.dispose();
        }
    }
}
