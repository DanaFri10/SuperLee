package suppliers.dal;

import shared.Database;
import suppliers.business.ProductAmountDiscount;
import suppliers.business.ProductDiscount;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ProductDiscountsDAO {
    private Connection conn;
    private String belongsToSupplierId;
    private int belongsToProductId;
    static int nextId;

    public ProductDiscountsDAO(String belongsToSupplierId, int belongsToProductId) throws SQLException {
        this.belongsToSupplierId = belongsToSupplierId;
        this.belongsToProductId = belongsToProductId;

        try
        {
            conn = Database.connect();
            PreparedStatement stmt = conn.prepareStatement("SELECT MAX(discountId) AS max FROM DealProductDiscounts");
            ResultSet rs = stmt.executeQuery();
            nextId = rs.getInt("max") + 1;
        }
        catch(SQLException e)
        {
            throw e;
        }
        finally {
            conn.close();
        }
    }

    public List<ProductDiscount> getAllDiscounts() throws SQLException {
        List<ProductDiscount> discounts = new ArrayList<>();

        try
        {
            conn = Database.connect();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM ProductsAmountDiscounts JOIN DealProductDiscounts USING(discountId) WHERE supplierId = ? AND productId = ?");
            stmt.setString(1, belongsToSupplierId);
            stmt.setInt(2, belongsToProductId);
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
                discounts.add(new ProductAmountDiscount(rs.getDouble("percentage"), rs.getInt("amount")));

            return discounts;
        }
        catch(SQLException e)
        {
            throw e;
        }
        finally {
            conn.close();
        }
    }

    public void addProductAmountDiscount(ProductAmountDiscount discount) throws SQLException {
        try
        {
            conn = Database.connect();
            PreparedStatement stmt1 = conn.prepareStatement("INSERT INTO ProductsAmountDiscounts (discountId, amount, percentage) VALUES (?, ?, ?)");
            stmt1.setInt(1, nextId);
            stmt1.setInt(2, discount.getAmountOfProducts());
            stmt1.setDouble(3, discount.getPercentage());
            stmt1.execute();

            PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO DealProductDiscounts (supplierId, productId, discountId, type) VALUES (?, ?, ?, ?)");
            stmt2.setString(1, belongsToSupplierId);
            stmt2.setInt(2, belongsToProductId);
            stmt2.setInt(3, nextId);
            stmt2.setInt(4, discount.getType());
            stmt2.execute();

            nextId++;
        }
        catch(SQLException e)
        {
            throw e;
        }
        finally {
            conn.close();
        }
    }

    public void removeAllDiscounts() throws SQLException {
        try
        {
            conn = Database.connect();

            List<Integer> discountIds = new LinkedList<>();

            PreparedStatement stmt1 = conn.prepareStatement("SELECT * FROM DealProductDiscounts WHERE productId = ?");
            stmt1.setInt(1, belongsToProductId);
            ResultSet idsRs = stmt1.executeQuery();
            while(idsRs.next())
                discountIds.add(idsRs.getInt("discountId"));

            PreparedStatement stmt2 = conn.prepareStatement("DELETE FROM DealProductDiscounts WHERE productId = ?");
            stmt2.setInt(1, belongsToProductId);
            stmt2.execute();

            for(int id : discountIds)
            {
                PreparedStatement stmt3 = conn.prepareStatement("DELETE FROM ProductsAmountDiscounts WHERE discountId = ?");
                stmt3.setInt(1, id);
                stmt3.execute();
            }
        }
        catch(SQLException e)
        {
            throw e;
        }
        finally {
            conn.close();
        }
    }

}
