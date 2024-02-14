package suppliers.dal;

import shared.Database;
import stock.business.Product;
import suppliers.business.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DealProductsDAO {
    private Connection conn;
    private String belongsToSupplierId;

    public DealProductsDAO(String belongsToSupplierId){
        this.belongsToSupplierId = belongsToSupplierId;
    }

    public Map<Integer,DealProductInformation> getAllProducts() throws SQLException {
        try
        {
            conn = Database.connect();

            Map<Integer,DealProductInformation> products = new HashMap<>();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM DealProducts WHERE supplierId = ?");
            stmt.setString(1, belongsToSupplierId);
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
            {
                DealProductInformation dpi = new DealProductInformation(rs.getInt("catalougeNum"), rs.getInt("productId"), rs.getDouble("price"), rs.getInt("amount"), belongsToSupplierId);
                products.put(dpi.getCatalogueNum(), dpi);
            }
            return products;
        }
        catch(SQLException e)
        {
            throw e;
        }
        finally {
            conn.close();
        }
    }

    public DealProductInformation getProduct(int catalogueNum) throws SQLException {
        try
        {
            conn = Database.connect();

            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM DealProducts WHERE supplierId = ? AND catalougeNum = ?");
            stmt.setString(1, belongsToSupplierId);
            stmt.setInt(2, catalogueNum);
            ResultSet rs = stmt.executeQuery();

            DealProductInformation p = null;
            if(rs.next())
                p = new DealProductInformation(rs.getInt("catalougeNum"), rs.getInt("productId"), rs.getDouble("price"), rs.getInt("amount"), belongsToSupplierId);
            if(rs.next())
                throw new IllegalArgumentException("Supplier with id " + belongsToSupplierId + " has two products with catalouge number " + catalogueNum);
            if(p == null)
                throw new IllegalArgumentException("Supplier with id " + belongsToSupplierId + " does not have products with catalouge number " + catalogueNum);
            return p;
        }
        catch(SQLException e)
        {
            throw e;
        }
        finally {
            conn.close();
        }
    }

    public void addProduct(int catalogue, int productId, double price, int amount) throws SQLException {
        try
        {
            conn = Database.connect();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO DealProducts (supplierId, productId, catalougeNum, amount, price) VALUES (?, ?, ?, ?, ?)");
            stmt.setString(1, belongsToSupplierId);
            stmt.setInt(2, productId);
            stmt.setInt(3, catalogue);
            stmt.setInt(4, amount);
            stmt.setDouble(5, price);
            stmt.execute();
        }
        catch(SQLException e)
        {
            throw e;
        }
        finally {
            conn.close();
        }
    }

    public void updateProduct(int catalogue, double price, int amount) throws SQLException {
        try
        {
            conn = Database.connect();
            PreparedStatement stmt = conn.prepareStatement("UPDATE DealProducts SET amount = ?, price = ? WHERE supplierId = ? AND catalougeNum = ?");
            stmt.setInt(1, amount);
            stmt.setDouble(2, price);
            stmt.setString(3, belongsToSupplierId);
            stmt.setInt(4, catalogue);
            stmt.execute();
        }
        catch(SQLException e)
        {
            throw e;
        }
        finally {
            conn.close();
        }
    }

    public void removeProduct(int catalogue) throws SQLException {
        try
        {
            int productId = getProduct(catalogue).getProductId();

            conn = Database.connect();

            List<Integer> discountIds = new ArrayList<>();
            PreparedStatement discountsStmt = conn.prepareStatement("SELECT * FROM DealProductDiscounts WHERE productId = ?");
            discountsStmt.setInt(1, productId);
            ResultSet rs = discountsStmt.executeQuery();
            while(rs.next())
                discountIds.add(rs.getInt("discountId"));

            PreparedStatement stmt1 = conn.prepareStatement("DELETE FROM DealProducts WHERE catalougeNum = ?");
            stmt1.setInt(1, catalogue);
            stmt1.execute();

            PreparedStatement stmt2 = conn.prepareStatement("DELETE FROM DealProductDiscounts WHERE productId = ?");
            stmt2.setInt(1, productId);
            stmt2.execute();

            for(int discountId: discountIds)
            {
                PreparedStatement stmt3 = conn.prepareStatement("DELETE FROM ProductsAmountDiscounts WHERE discountId = ?");
                stmt3.setInt(1, discountId);
                stmt3.execute();
            }

            // remove from periodic orders
        }
        catch(SQLException e)
        {
            throw e;
        }
        finally {
            int x = 100;
            conn.close();
        }
    }


}
