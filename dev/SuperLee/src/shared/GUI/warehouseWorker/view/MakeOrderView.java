package shared.GUI.warehouseWorker.view;

import shared.GUI.warehouseWorker.controller.MakeOrderController;
import shared.GUI.warehouseWorker.model.ProductMapModel;
import shared.GUI.warehouseWorker.model.ProductModel;
import shared.GUI.warehouseWorker.model.UniversalProductModel;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MakeOrderView {
    private int branchId;
    private ProductMapModel model;
    private HashMap<Integer,JTextField> textFields;
    private JFrame frame;

    public MakeOrderView(int branchId){
        this(branchId, null);
    }

    public MakeOrderView(int branchId, ProductMapModel _model){
        try{
        if(_model == null){
            this.model = new ProductMapModel(branchId);
        }else{
            this.model = _model;
        }
        MakeOrderController controller = new MakeOrderController(this,model);
        textFields = new HashMap<>();
        this.branchId = branchId;
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setSize(new Dimension(500,500));
        Map<Integer, Integer> amounts = model.getProductAmountMap();
        Map<Integer, UniversalProductModel> models = model.getProductModelMap();
        JPanel products = new JPanel();
        for(int productId : amounts.keySet()){
            JLabel product = new JLabel(models.get(productId).toString());
            JTextField amount = new JTextField(""+amounts.get(productId));
            amount.setColumns(4);
            amount.getDocument().addDocumentListener(controller);
            amount.addFocusListener(controller);
            textFields.put(productId,amount);
            JButton increase = new JButton("+");
            JButton decrease = new JButton("-");
            JButton remove = new JButton("X");
            increase.setActionCommand("+" + productId);
            decrease.setActionCommand("-" + productId);
            remove.setActionCommand("*" + productId);
            increase.addActionListener(controller);
            decrease.addActionListener(controller);
            remove.addActionListener(controller);
            JPanel row = new JPanel();
            row.add(product);
            row.add(amount);
            row.add(increase);
            row.add(decrease);
            row.add(remove);
            products.add(row);
        }
        panel.add(products);
        JButton addProduct = new JButton("Add Product");
        addProduct.setActionCommand("add");
        addProduct.addActionListener(controller);
        addProduct.setAlignmentX(JButton.CENTER_ALIGNMENT);
        panel.add(addProduct);
        JButton orderOpt = new JButton("Order products in the cheapest configuration");
        orderOpt.setActionCommand("orderOpt");
        orderOpt.addActionListener(controller);
        orderOpt.setAlignmentX(JButton.CENTER_ALIGNMENT);
        orderOpt.setEnabled(model.orderable());
        panel.add(orderOpt);
        JButton orderFas = new JButton("Order products in the fastest configuration");
        orderFas.setActionCommand("orderFas");
        orderFas.addActionListener(controller);
        orderFas.setAlignmentX(JButton.CENTER_ALIGNMENT);
        orderFas.setEnabled(model.orderable());
        panel.add(orderFas);
        JButton choose = new JButton("Choose supplier for each product");
        choose.setActionCommand("orderChoose");
        choose.addActionListener(controller);
        choose.setAlignmentX(JButton.CENTER_ALIGNMENT);
        choose.setEnabled(model.orderable());
        JButton back = new JButton("Go Back");
        back.setActionCommand("back");
        back.addActionListener(controller);
        back.setAlignmentX(JButton.CENTER_ALIGNMENT);
        panel.add(choose);
        panel.add(back);
        frame = new JFrame();
        frame.add(panel);
        frame.setSize(500,500);
        frame.setTitle("Choose products for order");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        }catch (Exception exception){
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
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

    public void goAddProduct(){
        new ProductExplorerView("",branchId,model, false);
        frame.dispose();
    }

    public void chooseSuppliers(){
        new ChooseDivisionView(model);
        frame.dispose();
    }

    public void rerender(){
        new MakeOrderView(branchId, model);
        frame.dispose();
    }

    public void finalizeOrder(String orderJson){
        new OrderFinalizationView(orderJson, branchId);
        frame.dispose();
    }

    public void goBack(){
        new MainMenuView(branchId);
        frame.dispose();
    }
}
