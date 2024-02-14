package shared.GUI.suppliersManager.controller;

import shared.GUI.suppliersManager.view.*;
import shared.service.SuppliersManagerService;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PeriodicOrderController implements ActionListener {
    private String supplierId;
    private int branchId;
    private PeriodicOrderView view;
    private String username;

    public PeriodicOrderController(String supplierId, int branchId, PeriodicOrderView view, String username)
    {
        this.supplierId = supplierId;
        this.branchId = branchId;
        this.view = view;
        this.username = username;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Back"))
        {
            PeriodicOrdersListView listView = new PeriodicOrdersListView(supplierId, username);
            listView.setVisible(true);
            view.dispose();
        }
        if(e.getActionCommand().equals("Edit"))
        {
            EditPeriodicOrderView editView = new EditPeriodicOrderView(supplierId, branchId, username, "PeriodicOrderView");
            editView.setVisible(true);
            view.dispose();
        }
    }
}
