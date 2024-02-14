package shared.GUI.warehouseWorker.controller;

import shared.GUI.warehouseWorker.view.MakeShortageOrderView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MakeShortageOrderController implements ActionListener {
    private MakeShortageOrderView view;

    public MakeShortageOrderController(MakeShortageOrderView view){
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("order")){
            view.goChooseExtras();
        }else if(e.getActionCommand().equals("back")){
            view.goBack();
        }
    }
}
