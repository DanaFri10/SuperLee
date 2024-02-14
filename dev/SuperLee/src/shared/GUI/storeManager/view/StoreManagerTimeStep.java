package shared.GUI.storeManager.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StoreManagerTimeStep extends JFrame {

    private JPanel panel;
    private JLabel promptLabel;
    private JTextField timeStepField;
    private JButton updateButton;

    private TimeStepUpdateListener updateListener;

    public StoreManagerTimeStep(TimeStepUpdateListener listener) {
        this.updateListener = listener;
        initializeComponents();
        setupListeners();
    }

    private void initializeComponents() {
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);

        promptLabel = new JLabel("Enter your Time Step:");
        promptLabel.setForeground(Color.WHITE);
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(promptLabel, constraints);

        timeStepField = new JTextField(10);
        constraints.gridx = 1;
        panel.add(timeStepField, constraints);

        updateButton = new JButton("Update");
        constraints.gridy = 1;
        panel.add(updateButton, constraints);

        add(panel, BorderLayout.CENTER);

        // Apply custom colors
        String[] colors = "b8d8d8-7a9e9f-4f6367-eef5db-eb7f78".split("-");
        panel.setBackground(Color.decode("#" + colors[2]));
        updateButton.setBackground(Color.decode("#" + colors[2]));
        updateButton.setForeground(Color.WHITE);
        updateButton.setBorder(BorderFactory.createLineBorder(Color.decode("#" + colors[4]), 1));

        setTitle("StoreManager Time Step");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame on the screen
    }

    private void setupListeners() {
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String timeStep = timeStepField.getText();
                if (updateListener != null) {
                    updateListener.onTimeStepUpdate(timeStep);
                }
                dispose(); // Close the time step window after updating
            }
        });
    }

    public interface TimeStepUpdateListener {
        void onTimeStepUpdate(String timeStep);
    }
}
