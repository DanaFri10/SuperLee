package shared.GUI.warehouseWorker.controller;

import shared.GUI.warehouseWorker.model.ProductModel;
import shared.GUI.warehouseWorker.view.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ProductController {
    private ProductModel model;
    private ProductView view;

    public ProductController(List<JButton> buttons, ProductModel model, ProductView view) {
        this.model = model;
        this.view = view;
//        JButton jb1 = new JButton("updateBranchProduct");
//        JButton jb2 = new JButton("updateWorldProduct");
//        JButton jb3 = new JButton("deleteBranchProduct");
//        JButton jb4 = new JButton("deleteWorldProduct");
//        JButton jb5 = new JButton("addProductDiscount");
//        JButton jb6 = new JButton("showInstances");
//        JButton jb7 = new JButton("return");
        buttons.get(0).addActionListener(new ProductController.ButtonUpdateBranchProductClickListener());
        buttons.get(1).addActionListener(new ProductController.ButtonUpdateWorldProductClickListener());
        buttons.get(2).addActionListener(new ProductController.ButtonDeleteBranchProductClickListener());
        buttons.get(3).addActionListener(new ProductController.ButtonDeleteWorldProductClickListener());
        buttons.get(4).addActionListener(new ProductController.ButtonAddDiscountClickListener());
        buttons.get(5).addActionListener(new ProductController.ButtonReturnClickListener());

    }

    private class ButtonUpdateWorldProductClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.close();
            new UpdateWorldProductView(view.getUsername(), view.getProductId(), view.getBranchId());
        }
    }

    private class ButtonUpdateBranchProductClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.close();
            new UpdateBranchProductView(view.getUsername(), view.getProductId(), view.getBranchId());
        }
    }

    private class ButtonDeleteBranchProductClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new DeleteBranchProductView(view, view.getUsername(), view.getProductId(), view.getBranchId());
        }
    }

    private class ButtonDeleteWorldProductClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new DeleteWorldProductView(view, view.getUsername(), view.getProductId(), view.getBranchId());
        }
    }
    private class ButtonAddDiscountClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.close();
            new AddDiscountProductView(view.getUsername(), view.getProductId(), view.getBranchId());
        }
    }

    private class ButtonReturnClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.back();
        }
    }


}
