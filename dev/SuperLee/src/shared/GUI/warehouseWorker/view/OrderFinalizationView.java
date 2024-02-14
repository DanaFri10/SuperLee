package shared.GUI.warehouseWorker.view;

import shared.GUI.warehouseWorker.controller.OrderFinalizationController;
import shared.GUI.warehouseWorker.model.ContactModel;
import shared.GUI.warehouseWorker.model.OrderModel;
import shared.GUI.warehouseWorker.model.SupplierModel;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class OrderFinalizationView {
    private JFrame frame;
    private Map<String, JComboBox> selectionMap;
    private Map<String, JCheckBox> emailMap;
    private int branchId;

    public OrderFinalizationView(String orderJson, int branchId){
        try{
        this.branchId =branchId;
        OrderModel model = new OrderModel(orderJson);
        OrderFinalizationController controller = new OrderFinalizationController(this, model);
        selectionMap = new HashMap<>();
        emailMap = new HashMap<>();
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        for(SupplierModel supplier : model.getSuppliers().values()){
            JLabel supplierLabel = new JLabel("From supplier " + supplier.getName() + " (" + supplier.getId() + "):");
            JTextArea description = new JTextArea(model.describeOrderForSupplier(supplier.getId()));
            description.setEditable(false);
            description.setAlignmentX(JTextArea.LEFT_ALIGNMENT);
            description.setFont(new Font("Courier New", Font.PLAIN, 12));
            description.setMaximumSize(description.getPreferredSize());
            description.setOpaque(false);
            panel.add(supplierLabel);
            panel.add(description);
            if(model.supplierHasContacts(supplier.getId())){
                JPanel contactRow = new JPanel();
                JLabel please = new JLabel("Please select a contact for this supplier: ");
                JComboBox<ContactModel> selection = new JComboBox<>(supplier.getContacts().toArray(new ContactModel[0]));
                contactRow.setLayout(new BoxLayout(contactRow, BoxLayout.X_AXIS));
                contactRow.add(please);
                contactRow.add(selection);
                contactRow.setMaximumSize(new Dimension(contactRow.getPreferredSize().width+20,contactRow.getPreferredSize().height+20));
                contactRow.setAlignmentX(JPanel.LEFT_ALIGNMENT);
                panel.add(contactRow);
                selectionMap.put(supplier.getId(),selection);
                JPanel emailRow = new JPanel();
                JLabel checkThis = new JLabel("Check this box if you would like to inform the contact about the order via email");
                JCheckBox sendEmail = new JCheckBox();
                emailRow.setLayout(new BoxLayout(emailRow, BoxLayout.X_AXIS));
                emailRow.add(checkThis);
                emailRow.add(sendEmail);
                emailRow.setMaximumSize(new Dimension(emailRow.getPreferredSize().width+20,emailRow.getPreferredSize().height+20));
                emailRow.setAlignmentX(JPanel.LEFT_ALIGNMENT);
                panel.add(emailRow);
                emailMap.put(supplier.getId(),sendEmail);
            }
        }
        JButton finalize = new JButton("Complete order");
        finalize.setActionCommand("final");
        finalize.addActionListener(controller);
        panel.add(finalize);
        JScrollPane scrollable = new JScrollPane(panel);
        scrollable.setSize(700,500);
        frame = new JFrame("Finalize Order Creation");
        frame.setSize(700,500);
        frame.add(scrollable);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        }catch (Exception exception){
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public ContactModel getSelectedContact(String supplierId){
        return (ContactModel) selectionMap.get(supplierId).getSelectedItem();
    }

    public boolean getIsEmail(String supplierId){
        return emailMap.get(supplierId).isSelected();
    }

    public void close(){
        new MainMenuView(branchId);
        frame.dispose();
    }
}
