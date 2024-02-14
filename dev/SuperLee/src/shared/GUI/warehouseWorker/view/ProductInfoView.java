package shared.GUI.warehouseWorker.view;

import shared.GUI.warehouseWorker.controller.ProductExplorerController;
import shared.GUI.warehouseWorker.model.ProductMapModel;
import shared.GUI.warehouseWorker.model.ProductModel;
import shared.GUI.warehouseWorker.model.UniversalProductModel;

import javax.swing.*;

public class ProductInfoView {
    private JFrame frame;

    public ProductInfoView(ProductExplorerController controller, UniversalProductModel model){
        JLabel label1 = new JLabel("Product ID: " + model.getId());
        JLabel label2 = new JLabel("Product Name: " + model.getName());
        JLabel label3 = new JLabel("Product Manufacturer: " + model.getManufacturer());
        JButton button = new JButton("Add");
        button.setActionCommand("addproduct");
        button.addActionListener(controller);
        controller.setCurrentSelectedProduct(model);
        JPanel panel = new JPanel();
        panel.add(label1);
        panel.add(label2);
        panel.add(label3);
        panel.add(button);
        frame = new JFrame();
        frame.setSize(panel.getPreferredSize().width, 200);
        frame.add(panel);
        frame.setVisible(true);
    }

    public void exit(){
        frame.dispose();
    }
}
