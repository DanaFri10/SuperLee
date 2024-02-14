package shared.GUI.warehouseWorker.view;

import shared.GUI.warehouseWorker.controller.ProductExplorerController;

import javax.swing.*;
import java.awt.*;

public class AddChooseView {
    private int branchId;
    private String path;
    private JFrame frame;
    public AddChooseView(ProductExplorerController controller){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        JButton addB = new JButton("Add branch product for existing universal product");
        JButton addP = new JButton("Add branch product for a new universal product");
        addP.setMaximumSize(new Dimension(400,100));
        addB.setMaximumSize(new Dimension(400,100));
        addB.setActionCommand("addB");
        addP.setActionCommand("addP");
        addB.addActionListener(controller);
        addP.addActionListener(controller);
        panel.add(addB);
        panel.add(addP);
        panel.setSize(400,200);
        frame = new JFrame("Select how to add a product");
        frame.add(panel);
        frame.setSize(400,200);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
