package shared.GUI.warehouseWorker.controller;

import shared.GUI.warehouseWorker.model.CurrentCategoryModel;
import shared.GUI.warehouseWorker.model.ProductMapModel;
import shared.GUI.warehouseWorker.model.ProductModel;
import shared.GUI.warehouseWorker.model.UniversalProductModel;
import shared.GUI.warehouseWorker.view.ProductExplorerView;
import shared.GUI.warehouseWorker.view.ProductInfoView;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ProductExplorerController implements MouseListener, ActionListener {
    private ProductExplorerView view;
    private CurrentCategoryModel model;
    private UniversalProductModel currentSelectedProduct;
    private ProductMapModel productMapModel;

    public ProductExplorerController(ProductExplorerView view, int branchId, DefaultListModel<Object> children, ProductMapModel mapModel, String initPath){
        this.view = view;
        this.currentSelectedProduct = null;
        this.productMapModel = mapModel;
        try {
            this.model = new CurrentCategoryModel(branchId, children, initPath);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        JList<String> sub = (JList) e.getSource();
        if (e.getClickCount() == 2) {
            int index = sub.locationToIndex(e.getPoint());
            Object o = sub.getModel().getElementAt(index);
            if(o instanceof String){
                try{
                model.extendPath((String)o);
                view.setDiscountEnabled(model.canAddDiscount());
                }catch (Exception exception){
                    JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }else{
                view.popupProductInfo((UniversalProductModel) o);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public void setCurrentSelectedProduct(UniversalProductModel model){
        this.currentSelectedProduct = model;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("up")){
            try{
            model.reducePath();
            }catch (Exception exception){
                JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            view.setDiscountEnabled(model.canAddDiscount());
        }else if(e.getActionCommand().equals("back")){
            view.goBack();
        }else if(e.getActionCommand().equals("addproduct")){
            if(currentSelectedProduct != null) {
                productMapModel.addProduct(currentSelectedProduct);
                currentSelectedProduct = null;
                ((SwingUtilities.getWindowAncestor((JButton) e.getSource()))).dispose();
                view.goBack();
            }
        } else if (e.getActionCommand().equals("add")) {
            view.popupChooseAdd();
        } else if(e.getActionCommand().equals("addP")){
            ((SwingUtilities.getWindowAncestor((JButton) e.getSource()))).dispose();
            view.goAddProduct(model.getCurrPath());
        }else if(e.getActionCommand().equals("addB")){
            ((SwingUtilities.getWindowAncestor((JButton) e.getSource()))).dispose();
            view.goAddBranchProduct(model.getCurrPath());
        }else if(e.getActionCommand().equals("goto")){
            try{
            model.extendPath(view.getGoTo());
            view.setDiscountEnabled(model.canAddDiscount());
            view.setGoTo("");
            }catch (Exception exception){
                JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getActionCommand().equals("disc")) {
            view.goAddCategoryDiscount(model.getCurrPath());
        }
    }
}
