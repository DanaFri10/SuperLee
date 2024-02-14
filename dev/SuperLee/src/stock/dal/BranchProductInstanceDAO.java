package stock.dal;

import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.Date;

import shared.Database;
import stock.business.Branch;
import stock.business.BranchInstance;
import stock.business.PlaceOfProduct;

public class BranchProductInstanceDAO {
    private Map<Integer, BranchInstance> branchProductInstancesMap;
    private int branchId;
    private boolean gotAll;
    public BranchProductInstanceDAO(int _branchId) throws SQLException{
        branchProductInstancesMap =  new HashMap<>();
        branchId = _branchId;
        gotAll = false;
    }
    public void insertInstance(BranchInstance bpi) throws SQLException{
        Integer key = bpi.getInstanceId();
        branchProductInstancesMap.put(key, bpi);
        Connection conn = Database.connect();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO BranchProductInstances (branchId, instanceId, productId, boughtPrice, expireDate, placeOfProduct, arriveDate) VALUES (?, ?, ?, ?, ?, ?, ?)");
        stmt.setInt(1, branchId);
        stmt.setInt(2, bpi.getInstanceId());
        stmt.setInt(3, bpi.getProductId());
        stmt.setDouble(4, bpi.getBoughtPrice());
        if(bpi.getExpireDate() == null){
            stmt.setNull(5, Types.VARCHAR);
        }else{
            stmt.setString(5, bpi.getExpireDate().toString());
        }
        stmt.setInt(6, bpi.getPlaceOfProduct().getValue());
        stmt.setString(7, bpi.getArriveDate().toString());
        stmt.executeUpdate();
        stmt.close();
        conn.close();
    }

    public void updateInstance(BranchInstance bpi) throws SQLException{
        Integer key = bpi.getInstanceId();
        branchProductInstancesMap.put(key, bpi);
        Connection conn = Database.connect();
        PreparedStatement stmt = conn.prepareStatement("UPDATE BranchProductInstances SET placeOfProduct = ?, defected_description = ?, expireDate = ? WHERE branchId = ? AND instanceId = ?");
        stmt.setInt(1, bpi.getPlaceOfProduct().getValue());
        stmt.setString(2, bpi.getdescription());
        if(bpi.getExpireDate() == null){
            stmt.setNull(3, Types.VARCHAR);
        }else{
            stmt.setString(3, bpi.getExpireDate().toString());
        }
        stmt.setInt(4, branchId);
        stmt.setInt(5, bpi.getInstanceId());
        stmt.executeUpdate();
        stmt.close();
        conn.close();

    }


    public void removeInstance(int instanceId) throws SQLException{
        Integer key = instanceId;
        branchProductInstancesMap.remove(key);
        Connection conn = Database.connect();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM BranchProductInstances WHERE branchId = ? AND instanceId = ?");
        stmt.setInt(1, branchId);
        stmt.setInt(2, instanceId);
        stmt.executeUpdate();
        stmt.close();
        conn.close();
     
    }
    
    public BranchInstance getInstance(int instanceId) throws SQLException{
        Integer key = instanceId;
        if(branchProductInstancesMap.containsKey(key)){
            return branchProductInstancesMap.get(key);
        }
        Connection conn = Database.connect();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM BranchProductInstances WHERE branchId = ? AND instanceId = ?");
        stmt.setInt(1, branchId);
        stmt.setInt(2, key);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            String expStr = rs.getString(5);
            BranchInstance branchInsatnce;
            if(expStr != null) {
                branchInsatnce = new BranchInstance(rs.getInt(2), rs.getInt(3), rs.getDouble(4), LocalDate.parse(expStr), PlaceOfProduct.fromInteger(rs.getInt(6)), LocalDate.parse(rs.getString(7)));
            }else{
                branchInsatnce = new BranchInstance(rs.getInt(2), rs.getInt(3), rs.getDouble(4), null, PlaceOfProduct.fromInteger(rs.getInt(6)), LocalDate.parse(rs.getString(7)));
            }
            if (rs.getString(8) != null)
                branchInsatnce.setDefected(rs.getString(8));
            branchProductInstancesMap.put(key, branchInsatnce);
            stmt.close();
            conn.close();
            return branchInsatnce;
        } else {
            stmt.close();
            conn.close();
            return null;
        }    
    }

    public ArrayList<BranchInstance> getAll() throws SQLException{
        ArrayList<BranchInstance> products = new ArrayList<>();
        if(gotAll){
            products.addAll(branchProductInstancesMap.values());
            return products;
        }
        Connection conn = Database.connect();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM BranchProductInstances WHERE branchId = ?");
        stmt.setInt(1, branchId);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            String expStr = rs.getString(5);
            BranchInstance branchInsatnce;
            if(expStr != null) {
                branchInsatnce = new BranchInstance(rs.getInt(2), rs.getInt(3), rs.getDouble(4), LocalDate.parse(expStr), PlaceOfProduct.fromInteger(rs.getInt(6)), LocalDate.parse(rs.getString(7)));
            }else{
                branchInsatnce = new BranchInstance(rs.getInt(2), rs.getInt(3), rs.getDouble(4), null, PlaceOfProduct.fromInteger(rs.getInt(6)), LocalDate.parse(rs.getString(7)));
            }
            if (rs.getString(8) != null)
                branchInsatnce.setDefected(rs.getString(8));
            products.add(branchInsatnce);
            branchProductInstancesMap.put(branchInsatnce.getInstanceId(), branchInsatnce);
        }
        stmt.close();
        conn.close();
        gotAll = true;
        return products;
    }

    public void removeInstanceOfProduct(int productId) throws SQLException {
        for(BranchInstance bi : getAll()){
            if(bi.getProductId() == productId){
                branchProductInstancesMap.remove(bi.getInstanceId());
            }
        }
        Connection conn = Database.connect();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM BranchProductInstances WHERE branchId = ? AND productId = ?");
        stmt.setInt(1, branchId);
        stmt.setInt(2, productId);
        stmt.executeUpdate();
        stmt.close();
        conn.close();
    }
}
