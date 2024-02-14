package stock.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import shared.Database;
import stock.business.BranchDiscount;
import stock.business.BranchProduct;

import javax.xml.crypto.Data;

public class BranchProductDAO {
    private Map<Integer, BranchProduct> branchProductsMap;
    private int branchId;
    private boolean gotAll;

    public BranchProductDAO(int _branchId) throws SQLException{
        branchId = _branchId;
        branchProductsMap = new HashMap<>();
        gotAll = false;
    }

    public void insertProduct(BranchProduct bp) throws SQLException{
        Integer key = bp.getId();
        branchProductsMap.put(key, bp);
        Connection conn = Database.connect();
        BranchDiscount discount = bp.getDiscount();
        if(discount != null){
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO BranchProducts (branchId, productId, price_sold, demand, place_in_store, minimum, path, beginningDate, expiredDate, percentage) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            stmt.setInt(1, branchId);
            stmt.setInt(2, bp.getId());
            stmt.setDouble(3, bp.getPriceSold());
            stmt.setInt(4, bp.getDemand());
            stmt.setString(5, bp.getplaceInStore());
            stmt.setInt(6, bp.getMinimum());
            stmt.setString(7, bp.getPath());
            stmt.setString(8, discount.getBeginningDate().toString());
            stmt.setString(9, discount.getExpiredDate().toString());
            stmt.setDouble(10, discount.getDiscountNumericValue());
            stmt.executeUpdate();
            stmt.close();
            conn.close();
        }
        else{
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO BranchProducts (branchId, productId, price_sold, demand, place_in_store, minimum, path) VALUES (?, ?, ?, ?, ?, ?, ?)");
            stmt.setInt(1, branchId);
            stmt.setInt(2, bp.getId());
            stmt.setDouble(3, bp.getPriceSold());
            stmt.setInt(4, bp.getDemand());
            stmt.setString(5, bp.getplaceInStore());
            stmt.setInt(6, bp.getMinimum());
            stmt.setString(7, bp.getPath());
            stmt.executeUpdate();
            stmt.close();
            conn.close();
        }
    }

    public void updateProduct(BranchProduct bp)  throws SQLException {
        Integer key = bp.getId();
        branchProductsMap.put(key, bp);
        BranchDiscount discount = bp.getDiscount();
        Connection conn = Database.connect();
        PreparedStatement stmt = conn.prepareStatement("UPDATE BranchProducts SET price_sold = ?, demand = ?, place_in_store = ?, minimum = ?, path = ?, beginning_date = ?, expired_date = ?, percentage = ? WHERE branchId = ? AND productId = ?");
        stmt.setInt(9, branchId);
        stmt.setInt(10, bp.getId());
        stmt.setDouble(1, bp.getPriceSold());
        stmt.setInt(2, bp.getDemand());
        stmt.setString(3, bp.getplaceInStore());
        stmt.setInt(4, bp.getMinimum());
        stmt.setString(5, bp.getPath());
        if (discount != null) {
            stmt.setString(6, discount.getBeginningDate().toString());
            stmt.setString(7, discount.getExpiredDate().toString());
            stmt.setDouble(8, discount.getDiscountNumericValue());

        } else {
            stmt.setNull(6, java.sql.Types.VARCHAR);
            stmt.setNull(7, java.sql.Types.VARCHAR);
            stmt.setNull(8, java.sql.Types.DOUBLE);
        }

        stmt.executeUpdate();
        stmt.close();
        conn.close();
    }

    public ArrayList<BranchProduct> getShortage(int branchId){
        return null;
    }

    public void removeProduct(int productId) throws SQLException {
        Integer key = productId;
        branchProductsMap.remove(key);
        Connection conn = Database.connect();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM BranchProducts WHERE branchId = ? AND productId = ?");
        stmt.setInt(1, branchId);
        stmt.setInt(2, productId);
        stmt.executeUpdate();
        stmt.close();
        conn.close();
    }

    public BranchProduct getProduct(int productId) throws SQLException {
        if(branchProductsMap.containsKey(productId)){
            return branchProductsMap.get(productId);
        }
        Connection conn = Database.connect();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM BranchProducts WHERE branchId = ? AND productId = ?");
        stmt.setInt(1, branchId);
        stmt.setInt(2, productId);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            BranchProduct branchProduct =  new BranchProduct(rs.getInt(1), rs.getDouble(6), rs. getInt(4), rs.getString(7), rs.getInt(5), rs.getString(3));
            if(rs.getString(8) != null && rs.getString(8).length() != 0){
                BranchDiscount discount = new BranchDiscount(LocalDate.parse(rs.getString(8)), LocalDate.parse(rs.getString(9)), rs.getDouble(10) );
                branchProduct.setMainDiscount(discount);
            }
            branchProductsMap.put(productId, branchProduct);
            stmt.close();
            conn.close();
            return branchProduct;
        } else {
            stmt.close();
            conn.close();
            return null;
        }    

    }

    public ArrayList<BranchProduct> getAll() throws SQLException{
        ArrayList<BranchProduct> products = new ArrayList<>();
        if(gotAll){
            products.addAll(branchProductsMap.values());
            return products;
        }
        Connection conn = Database.connect();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM BranchProducts WHERE branchId = ?");
        stmt.setInt(1, branchId);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            BranchProduct branchProduct =  new BranchProduct(rs.getInt(1), rs.getDouble(6), rs. getInt(4), rs.getString(7), rs.getInt(5), rs.getString(3));
            String x = rs.getString(8);
            if(rs.getString(8) != null && rs.getString(8).length() != 0){
                BranchDiscount discount = new BranchDiscount(LocalDate.parse(rs.getString(8)), LocalDate.parse(rs.getString(9)), rs.getDouble(10) );
                branchProduct.setMainDiscount(discount);
            }

            products.add(branchProduct);
            branchProductsMap.put(branchProduct.getId(), branchProduct);

        }
        stmt.close();
        conn.close();
        gotAll = true;
        return products;
    }


}
