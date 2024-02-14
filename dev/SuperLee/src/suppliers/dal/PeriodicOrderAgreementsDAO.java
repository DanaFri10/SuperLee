package suppliers.dal;

import shared.Database;
import suppliers.business.DealDiscount;
import suppliers.business.PeriodicOrderAgreement;
import suppliers.business.ProductDiscount;

import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PeriodicOrderAgreementsDAO {
    private String supplierId;
    private boolean gotAll;

    public PeriodicOrderAgreementsDAO(String belongsToSupplierId) throws SQLException {
        this.supplierId = belongsToSupplierId;
        gotAll = false;
    }

    public Map<Integer, PeriodicOrderAgreement> getAllPeriodicOrders() throws SQLException {
        Connection conn = Database.connect();
        Map<Integer, PeriodicOrderAgreement> temp = new HashMap<>();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM PeriodicOrders WHERE supplierId=?");
        stmt.setString(1,supplierId);
        ResultSet rs = stmt.executeQuery();
        while(rs.next()){
            int branchId = rs.getInt("branchId");
            PreparedStatement products = conn.prepareStatement("SELECT * FROM PeriodicOrderProducts WHERE supplierId=? AND branchId=?");
            products.setString(1, supplierId);
            products.setInt(2, branchId);
            ResultSet productsSet = products.executeQuery();
            Map<Integer,Integer> productsMap = new HashMap<>();
            while(productsSet.next()){
                productsMap.put(productsSet.getInt(3),productsSet.getInt(4));
            }
            products.close();
            boolean[] days = new boolean[7];
            for(int i = 3; i < 10; i++){
                days[i-3] = rs.getInt(i) == 1;
            }
            PeriodicOrderAgreement poa = new PeriodicOrderAgreement(productsMap);
            poa.setOrderDays(days);
            poa.setAssignedContactId(rs.getString("contactId"));
            temp.put(branchId,poa);
        }
        stmt.close();
        conn.close();
        gotAll = true;
        return temp;
    }

    public void addPeriodicOrder(PeriodicOrderAgreement po, int branchId) throws SQLException {
        Connection conn = Database.connect();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO PeriodicOrders VALUES(?,?,?,?,?,?,?,?,?,?)");
        boolean[] days = po.getOrderDays();
        stmt.setString(1, supplierId);
        stmt.setInt(2, branchId);
        for(int i = 0; i < 7; i++){
            stmt.setInt(i+3,days[i] ? 1 : 0);
        }
        stmt.setString(10,po.getAssignedContactId());
        stmt.executeUpdate();
        stmt.close();
        Map<Integer,Integer> productsMap = po.getProductAmounts();
        for(int catNum : productsMap.keySet()){
            PreparedStatement product = conn.prepareStatement("INSERT INTO PeriodicOrderProducts VALUES(?,?,?,?)");
            product.setString(1,supplierId);
            product.setInt(2,branchId);
            product.setInt(3,catNum);
            product.setInt(4, productsMap.get(catNum));
            product.executeUpdate();
            product.close();
        }
        conn.close();
    }

    public void removeProduct(int catalogueNum, int branchId) throws SQLException {
        Connection conn = Database.connect();
        PreparedStatement remove = conn.prepareStatement("DELETE FROM PeriodicOrderProducts WHERE supplierId=? AND branchId = ? AND catalogueNum = ?");
        remove.setString(1, supplierId);
        remove.setInt(2,branchId);
        remove.setInt(3, catalogueNum);
        remove.executeUpdate();
        remove.close();
        conn.close();
    }

    public void setPeriodicOrderProducts(int branchId, Map<Integer,Integer> productsMap) throws SQLException {
        Connection conn = Database.connect();
        PreparedStatement removeFormer = conn.prepareStatement("DELETE FROM PeriodicOrderProducts WHERE supplierId=? AND branchId = ?");
        removeFormer.setString(1, supplierId);
        removeFormer.setInt(2,branchId);
        removeFormer.executeUpdate();
        removeFormer.close();
        for(int catNum : productsMap.keySet()){
            PreparedStatement product = conn.prepareStatement("INSERT INTO PeriodicOrderProducts VALUES(?,?,?,?)");
            product.setString(1,supplierId);
            product.setInt(2,branchId);
            product.setInt(3,catNum);
            product.setInt(4, productsMap.get(catNum));
            product.executeUpdate();
            product.close();
        }
        conn.close();
    }

    public void setPeriodicOrderDays(int branchId, boolean[] days) throws SQLException {
        Connection conn = Database.connect();
        PreparedStatement updateDays = conn.prepareStatement("UPDATE PeriodicOrders SET sunday=?,monday=?,tuesday=?,wednesday=?,thursday=?,friday=?,saturday=? WHERE supplierId=? AND branchId=?");
        for(int i = 0; i < 7; i++){
            updateDays.setInt(i+1,days[i] ? 1 : 0);
        }
        updateDays.setString(8, supplierId);
        updateDays.setInt(9, branchId);
        updateDays.executeUpdate();
        updateDays.close();
        conn.close();
    }

    public void setPeriodicOrderContact(int branchId, String contactId) throws SQLException {
        Connection conn = Database.connect();
        PreparedStatement updateDays = conn.prepareStatement("UPDATE PeriodicOrders SET contactId=? WHERE supplierId=? AND branchId=?");
        updateDays.setString(1,contactId);
        updateDays.setString(2, supplierId);
        updateDays.setInt(3, branchId);
        updateDays.executeUpdate();
        updateDays.close();
        conn.close();
    }

    public void removePeriodicOrder(int branchId) throws SQLException {
        Connection conn = Database.connect();
        PreparedStatement removeFromMain = conn.prepareStatement("DELETE FROM PeriodicOrders WHERE supplierId = ? AND branchId = ?");
        removeFromMain.setString(1,supplierId);
        removeFromMain.setInt(2,branchId);
        PreparedStatement removeProducts = conn.prepareStatement("DELETE FROM PeriodicOrderProducts WHERE supplierId = ?  AND branchId = ?");
        removeProducts.setString(1,supplierId);
        removeProducts.setInt(2,branchId);
        removeProducts.executeUpdate();
        removeProducts.close();
        removeFromMain.executeUpdate();
        removeFromMain.close();
        conn.close();
    }

    public void removePeriodicOrders() throws SQLException {
        Connection conn = Database.connect();
        PreparedStatement removeFromMain = conn.prepareStatement("DELETE FROM PeriodicOrders WHERE supplierId = ?");
        removeFromMain.setString(1,supplierId);
        PreparedStatement removeProducts = conn.prepareStatement("DELETE FROM PeriodicOrderProducts WHERE supplierId = ?");
        removeProducts.setString(1,supplierId);
        removeProducts.executeUpdate();
        removeProducts.close();
        removeFromMain.executeUpdate();
        removeFromMain.close();
        conn.close();
    }

    public boolean isGotAll(){
        return gotAll;
    }
}
