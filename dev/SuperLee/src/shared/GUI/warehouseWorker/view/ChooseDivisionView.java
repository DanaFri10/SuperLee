package shared.GUI.warehouseWorker.view;

import shared.GUI.warehouseWorker.controller.ChooseDivisionController;
import shared.GUI.warehouseWorker.model.CustomDivisionModel;
import shared.GUI.warehouseWorker.model.ProductMapModel;
import shared.GUI.warehouseWorker.model.SupplierModel;

import javax.swing.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ChooseDivisionView {
    private JFrame frame;
    private ProductMapModel mapModel;
    private Map<Integer, JComboBox> selections;

    public ChooseDivisionView(ProductMapModel mapModel){
        this.mapModel = mapModel;
        CustomDivisionModel model = null;
        frame = new JFrame("Select division among the suppliers");
        try{
         model = new CustomDivisionModel(mapModel);
        }catch (Exception exception){
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            goBack();
            return;
        }
        ChooseDivisionController controller = new ChooseDivisionController(this,model);
        selections = new HashMap<>();
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        for(int productId : mapModel.getProductModelMap().keySet()){
            JPanel productRow = new JPanel();
            productRow.setLayout(new BoxLayout(productRow, BoxLayout.X_AXIS));
            JLabel productName = new JLabel(mapModel.getProductAmount(productId) + "x" + mapModel.getProductModelMap().get(productId).toString());
            List<SupplierModel> suppliers = new LinkedList<>();
            try {
                suppliers = model.getSuppliersForProduct(productId);
            }catch (Exception exception){
                JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                goBack();
                return;
            }
            if(suppliers.isEmpty()){
                JOptionPane.showMessageDialog(null, "Product " + mapModel.getProductModelMap().get(productId) + " cannot currently be supplied", "Error", JOptionPane.ERROR_MESSAGE);
                goBack();
                return;
            }
            JComboBox<SupplierModel> selection = new JComboBox<>(suppliers.toArray(new SupplierModel[0]));
            selections.put(productId, selection);
            productRow.add(productName);
            productRow.add(selection);
            productRow.setMaximumSize(productRow.getPreferredSize());
            productRow.setAlignmentX(JPanel.LEFT_ALIGNMENT);
            panel.add(productRow);
        }
        JButton button = new JButton("Order");
        button.setActionCommand("order");
        JButton back = new JButton("Back");
        back.setActionCommand("back");
        button.addActionListener(controller);
        back.addActionListener(controller);
        panel.add(button);
        panel.add(back);
        JScrollPane scrollable = new JScrollPane(panel);
        scrollable.setSize(500,500);
        frame.add(scrollable);
        frame.setSize(500,500);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public SupplierModel getSupplierForProduct(int productId){
        return (SupplierModel) selections.get(productId).getSelectedItem();
    }

    public void goBack(){
        new MakeOrderView(mapModel.getBranchId(), mapModel);
        frame.dispose();
    }

    public void finalizeOrder(String orderJson){
        new OrderFinalizationView(orderJson, mapModel.getBranchId());
        frame.dispose();
    }
}
