package shared.GUI.storeManager.controller;

import shared.GUI.storeManager.model.StoreManagerHomeModel;
import shared.GUI.storeManager.view.StoreManagerHome;
import shared.GUI.storeManager.view.StoreManagerTimeStep;
import shared.GUI.warehouseWorker.view.LoginView;
import shared.service.Response;
import shared.service.StoreManagerService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

public class StoreManagerController implements ActionListener, StoreManagerTimeStep.TimeStepUpdateListener {

    private JButton shortageReportButton;
    private JButton deliveryHistoryButton;
    private JButton defectiveTimeStampButton;
    private JButton defectiveReportButton;
    private StoreManagerHomeModel model;

    private String colorList = "b8d8d8-7a9e9f-4f6367-eef5db-eb7f78";
    private JTextField username;
    private JTextField password;
    private JTextField branchId;


    public StoreManagerController(JButton shortageReportButton, JButton deliveryHistoryButton,
                                  JButton defectiveTimeStampButton, JButton defectiveReportButton,
                                   StoreManagerHomeModel model,
                                  JTextField username, JTextField password, JTextField branchId) {
        this.shortageReportButton = shortageReportButton;
        this.deliveryHistoryButton = deliveryHistoryButton;
        this.defectiveTimeStampButton = defectiveTimeStampButton;
        this.defectiveReportButton = defectiveReportButton;
        this.model = model;
        this.username = username;
        this.password = password;
        this.branchId = branchId;

    }

    public void logout(){
        model.logout(username.getText());
    }

    private void openStoreManagerTimeStepWindow() {
        StoreManagerTimeStep timeStepWindow = new StoreManagerTimeStep(this);
        timeStepWindow.setVisible(true);
    }
    @Override
    public void onTimeStepUpdate(String timeStep) {
        Response res = model.updateTimeStep(username.getText(), Integer.parseInt(branchId.getText()), timeStep);
        if(res.isError()){
            JOptionPane.showMessageDialog(null, res.getErrorMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }else{
            JOptionPane.showMessageDialog(null, res.getData(), "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == shortageReportButton) {
            String report = model.createShortageReport(username.getText(), Integer.parseInt(branchId.getText()));
            showCustomMessageDialog(null, report, "Shortage Report", JOptionPane.INFORMATION_MESSAGE);
        } else if (e.getSource() == deliveryHistoryButton) {
            String report = model.getOrdersHistory(username.getText(), branchId.getText());
            showCustomMessageDialog(null, report, "Order History", JOptionPane.INFORMATION_MESSAGE);
        } else if (e.getSource() == defectiveTimeStampButton) {
            openStoreManagerTimeStepWindow();
        } else if (e.getSource() == defectiveReportButton) {
            String report = model.createDefectivesReportOnDemand(username.getText(), Integer.parseInt(branchId.getText()));
            showCustomMessageDialog(null, report, "Defective Report", JOptionPane.INFORMATION_MESSAGE);
        }
    }



    private void showCustomMessageDialog(Component parentComponent, Object message, String title, int messageType) {
        String[] colors = colorList.split("-");

        Color originalOptionPaneBackground = UIManager.getColor("OptionPane.background");
        Color originalPanelBackground = UIManager.getColor("Panel.background");
        Color originalMessageForeground = UIManager.getColor("OptionPane.messageForeground");
        Color originalButtonBackground = UIManager.getColor("Button.background");
        Color originalButtonForeground = UIManager.getColor("Button.foreground");
        Color originalButtonBorder = UIManager.getColor("Button.border");


        UIManager.put("OptionPane.background", Color.decode("#" + colors[2]));
        UIManager.put("Panel.background", Color.decode("#" + colors[2]));
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
        UIManager.put("Button.background", Color.decode("#" + colors[2]));
        UIManager.put("Button.foreground", Color.decode("#" + colors[3]));
        UIManager.put("Button.border", BorderFactory.createLineBorder(Color.decode("#" + colors[4]), 1));

        JOptionPane.showMessageDialog(parentComponent, message, title, messageType);

        // Reset the background colors to their original values
        UIManager.put("OptionPane.background", originalOptionPaneBackground);
        UIManager.put("Panel.background", originalPanelBackground);
        UIManager.put("OptionPane.messageForeground", originalMessageForeground);
        UIManager.put("Button.background", originalButtonBackground);
        UIManager.put("Button.foreground", originalButtonForeground);
        UIManager.put("Button.border", originalButtonBorder);
    }
    public void executeAfterOpen() {
        String report = model.createDefectivesReportOnDemand(username.getText(), Integer.parseInt(branchId.getText()));
        showCustomMessageDialog(null, report, "Defective Report", JOptionPane.INFORMATION_MESSAGE);
    }
    // test



}
