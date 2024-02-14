package shared.GUI.storeManager.view;

import shared.ControllerFactory;
import shared.Database;
import shared.GUI.storeManager.controller.StoreManagerController;
import shared.GUI.storeManager.model.StoreManagerHomeModel;
import shared.GUI.suppliersManager.view.SuppliersListView;
import shared.GUI.warehouseWorker.model.UsernameStorage;
import shared.GUI.warehouseWorker.view.LoginView;
import shared.GUI.warehouseWorker.view.MainMenuView;
import shared.service.Response;
import shared.service.StoreManagerService;
import stock.business.StockController;
import suppliers.business.SuppliersController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class StoreManagerHome extends JFrame {
    private JPanel panel;
    private JPanel buttonPanel;
    private JLabel titleLabel;
    private JButton shortageReportButton;
    private JButton deliveryHistoryButton;
    private JButton defectiveTimeStepButton;
    private JButton defectiveReportButton;
    private JButton backButton;
    private JButton supplierManagerButton;
    private JButton warehouseWorkerButton;

    private String colorList = "b8d8d8-7a9e9f-4f6367-eef5db-eb7f78";


    private JTextField username;
    private JTextField password;
    private JTextField branchId;
    public StoreManagerController smc = null;


    public StoreManagerHome() {
        try {
            initializeComponents();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        setupListeners();
    }
    public StoreManagerHome(JTextField username, JTextField password, JTextField branchId, Response response) {
        this.username = username;
        this.password = password;
        this.branchId = branchId;
        try {
            initializeComponents();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        setupListeners();
//        smc.executeAfterOpen();
    }

    private void initializeComponents() throws Exception {
        panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        titleLabel = new JLabel("Store Manager HomePage");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel mainButtonPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        mainButtonPanel.setBackground(Color.WHITE);
        mainButtonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] colors = colorList.split("-");

        shortageReportButton = new JButton("Shortage Report");
        shortageReportButton.setHorizontalAlignment(SwingConstants.LEFT);
        shortageReportButton.setPreferredSize(new Dimension(200, 30));
        Color buttonColor = Color.decode("#" + colors[0]);
        shortageReportButton.setBackground(buttonColor);
        shortageReportButton.setForeground(Color.WHITE);
        mainButtonPanel.add(shortageReportButton);

        deliveryHistoryButton = new JButton("Orders History");
        deliveryHistoryButton.setHorizontalAlignment(SwingConstants.LEFT);
        deliveryHistoryButton.setPreferredSize(new Dimension(200, 30));
        Color deliveryButtonColor = Color.decode("#" + colors[1]);
        deliveryHistoryButton.setBackground(deliveryButtonColor);
        deliveryHistoryButton.setForeground(Color.WHITE);
        mainButtonPanel.add(deliveryHistoryButton);

        defectiveTimeStepButton = new JButton("Set Time Step");
        defectiveTimeStepButton.setHorizontalAlignment(SwingConstants.LEFT);
        defectiveTimeStepButton.setPreferredSize(new Dimension(200, 30));
        Color defectiveButtonColor = Color.decode("#" + colors[2]);
        defectiveTimeStepButton.setBackground(defectiveButtonColor);
        defectiveTimeStepButton.setForeground(Color.WHITE);
        mainButtonPanel.add(defectiveTimeStepButton);

        defectiveReportButton = new JButton("Defective Report");
        defectiveReportButton.setHorizontalAlignment(SwingConstants.LEFT);
        defectiveReportButton.setPreferredSize(new Dimension(200, 30));
        Color reportButtonColor = Color.decode("#FF0000"); // Set custom color (red)
        defectiveReportButton.setBackground(reportButtonColor);
        defectiveReportButton.setForeground(Color.WHITE);
        mainButtonPanel.add(defectiveReportButton);

        panel.add(mainButtonPanel, BorderLayout.WEST);

        JPanel sideButtonPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        sideButtonPanel.setBackground(Color.WHITE);
        sideButtonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        supplierManagerButton = new JButton("Supplier Manager");
        supplierManagerButton.setHorizontalAlignment(SwingConstants.LEFT);
        supplierManagerButton.setPreferredSize(new Dimension(200, 30));
        Color supplierButtonColor = Color.decode("#" + colors[2]);
        supplierManagerButton.setBackground(supplierButtonColor);
        supplierManagerButton.setForeground(Color.WHITE);
        sideButtonPanel.add(supplierManagerButton);

        warehouseWorkerButton = new JButton("Warehouse Worker");
        warehouseWorkerButton.setHorizontalAlignment(SwingConstants.LEFT);
        warehouseWorkerButton.setPreferredSize(new Dimension(200, 30));
        Color workerButtonColor = Color.decode("#" + colors[4]);
        warehouseWorkerButton.setBackground(workerButtonColor);
        warehouseWorkerButton.setForeground(Color.WHITE);
        sideButtonPanel.add(warehouseWorkerButton);

        panel.add(sideButtonPanel, BorderLayout.EAST);

        backButton = new JButton("Back");
        backButton.setHorizontalAlignment(SwingConstants.CENTER);
        backButton.setPreferredSize(new Dimension(120, 30));
        Color backButtonColor = Color.decode("#" + colors[0]);
        backButton.setBackground(backButtonColor);
        backButton.setForeground(Color.WHITE);
        panel.add(backButton, BorderLayout.SOUTH);

        StoreManagerHomeModel model = new StoreManagerHomeModel();

        smc = new StoreManagerController(shortageReportButton, deliveryHistoryButton,
                defectiveTimeStepButton, defectiveReportButton, model,
                username, password, branchId);

        shortageReportButton.addActionListener(smc);
        deliveryHistoryButton.addActionListener(smc);
        defectiveTimeStepButton.addActionListener(smc);
        defectiveReportButton.addActionListener(smc);

        add(panel);

        setTitle("StoreManager Home");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame on the screen
    }



    private void setupListeners() {
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                smc.logout();
                UsernameStorage.logout();
                dispose(); // Close the current frame
                StoreManagerLogin loginScreen = new StoreManagerLogin();
                loginScreen.setVisible(true);
            }
        });
        warehouseWorkerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UsernameStorage.login(username.getText(), null);
                UsernameStorage.setManager();
                new MainMenuView(Integer.parseInt(branchId.getText()));
            }
        });

        supplierManagerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UsernameStorage.login(username.getText(), null);
                UsernameStorage.setManager();
                SuppliersListView slw = new SuppliersListView(username.getText());
                slw.setVisible(true);
            }
        });
    }
}
