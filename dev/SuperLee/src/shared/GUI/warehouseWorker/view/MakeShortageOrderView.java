package shared.GUI.warehouseWorker.view;

import shared.GUI.warehouseWorker.controller.MakeShortageOrderController;
import shared.GUI.warehouseWorker.model.ShortageModel;

import javax.swing.*;

public class MakeShortageOrderView {
    private JFrame frame;
    private ShortageModel model;
    private int branchId;
    public MakeShortageOrderView(int branchId){
        try{
        model = new ShortageModel(branchId);
        MakeShortageOrderController controller = new MakeShortageOrderController(this);
        this.branchId = branchId;
        JTextArea description = new JTextArea(model.getShortageOrderDescription());
        description.setSize(description.getPreferredSize());
        description.setEditable(false);
        JButton order = new JButton("Order shortage products");
        JButton back = new JButton("Back");
        back.setActionCommand("back");
        back.addActionListener(controller);
        order.setActionCommand("order");
        order.addActionListener(controller);
        JPanel panel = new JPanel();
        panel.add(description);
        panel.add(back);
        panel.add(order);
        frame = new JFrame("Shortage Report");
        frame.add(panel);
        frame.setSize(500,300);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        }catch (Exception exception){
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void goChooseExtras(){
        new ChooseExtrasView(branchId, model);
        frame.dispose();
    }

    public void goBack(){
        new MainMenuView(branchId);
        frame.dispose();
    }
}
