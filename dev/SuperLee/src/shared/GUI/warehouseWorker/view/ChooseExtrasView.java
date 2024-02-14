package shared.GUI.warehouseWorker.view;

import shared.GUI.warehouseWorker.controller.ChooseExtrasController;
import shared.GUI.warehouseWorker.controller.MakeOrderController;
import shared.GUI.warehouseWorker.model.ProductMapModel;
import shared.GUI.warehouseWorker.model.ProductModel;
import shared.GUI.warehouseWorker.model.ShortageModel;
import shared.GUI.warehouseWorker.model.UniversalProductModel;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ChooseExtrasView {
    private int branchId;
    private ShortageModel model;
    private HashMap<Integer,JTextField> textFields;
    private JFrame frame;

    public ChooseExtrasView(int branchId, ShortageModel model){
        ChooseExtrasController controller = new ChooseExtrasController(this,model);
        textFields = new HashMap<>();
        this.branchId = branchId;
        this.model = model;
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setSize(new Dimension(500,500));
        Map<Integer, Integer> amounts = model.getExtrasMap();
        Map<Integer, UniversalProductModel> models = model.getProductModelMap();
        JPanel products = new JPanel();
        for(int productId : amounts.keySet()){
            JLabel product = new JLabel(models.get(productId).toString());
            JTextField amount = new JTextField(""+amounts.get(productId));
            JLabel originalAmount = new JLabel("+" + model.getShortageMap().get(productId));
            amount.setColumns(4);
            amount.getDocument().addDocumentListener(controller);
            amount.addFocusListener(controller);
            textFields.put(productId,amount);
            JButton increase = new JButton("+");
            JButton decrease = new JButton("-");
            increase.setActionCommand("+" + productId);
            decrease.setActionCommand("-" + productId);
            increase.addActionListener(controller);
            decrease.addActionListener(controller);
            JPanel row = new JPanel();
            row.add(product);
            row.add(amount);
            row.add(originalAmount);
            row.add(increase);
            row.add(decrease);
            products.add(row);
        }
        panel.add(products);
        JButton order = new JButton("Order");
        JButton back = new JButton("Back");
        order.setActionCommand("order");
        order.addActionListener(controller);
        order.setAlignmentX(JButton.CENTER_ALIGNMENT);
        panel.add(order);
        back.setActionCommand("back");
        back.addActionListener(controller);
        back.setAlignmentX(JButton.CENTER_ALIGNMENT);
        panel.add(back);
        frame = new JFrame();
        frame.add(panel);
        frame.setSize(500,500);
        frame.setTitle("Choose extra amount for each product");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void setTextField(int productId, int amount){
        textFields.get(productId).setText(""+amount);
    }

    public String getTextField(int productId){
        return textFields.get(productId).getText();
    }

    public void makeRed(int productId){
        textFields.get(productId).setBackground(Color.RED);
    }

    public void makeNotRed(int productId){
        textFields.get(productId).setBackground(Color.WHITE);
    }

    public void finalizeOrder(String orderJson){
        new OrderFinalizationView(orderJson, branchId);
        frame.dispose();
    }
    public void goBack(){
        new MakeShortageOrderView(branchId);
        frame.dispose();
    }
}
