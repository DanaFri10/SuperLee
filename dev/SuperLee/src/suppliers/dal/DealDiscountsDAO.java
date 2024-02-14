package suppliers.dal;

import shared.Database;
import suppliers.business.DealDiscount;
import suppliers.business.ProductDiscount;
import suppliers.business.TotalPriceDiscount;
import suppliers.business.TotalProductsDiscount;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DealDiscountsDAO {
    private Connection conn;
    private String belongsToSupplierId;
    static int nextId;

    public DealDiscountsDAO(String belongsToSupplierId) throws SQLException {
        try
        {
            this.conn = Database.connect();
            PreparedStatement stmt = conn.prepareStatement("SELECT Max(discountId) AS max FROM DealDiscounts");
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

        this.belongsToSupplierId = belongsToSupplierId;
    }

    public List<DealDiscount> getAllDiscounts() throws SQLException {
        List<DealDiscount> discounts = new ArrayList<>();

        try
        {
            conn = Database.connect();

            PreparedStatement priceDiscounts = conn.prepareStatement("SELECT * FROM DealDiscounts JOIN TotalPriceDiscounts USING(discountId) WHERE supplierId = ?");
            priceDiscounts.setString(1, belongsToSupplierId);
            ResultSet priceDiscountsRs = priceDiscounts.executeQuery();
            while(priceDiscountsRs.next())
                discounts.add(new TotalPriceDiscount(priceDiscountsRs.getDouble("percentage"), priceDiscountsRs.getDouble("minPrice")));

            PreparedStatement productsDiscounts = conn.prepareStatement("SELECT * FROM DealDiscounts JOIN TotalProductsDiscount USING(discountId) WHERE supplierId = ?");
            productsDiscounts.setString(1, belongsToSupplierId);
            ResultSet productsDiscountsRs = productsDiscounts.executeQuery();
            while(productsDiscountsRs.next())
                discounts.add(new TotalProductsDiscount(productsDiscountsRs.getDouble("percentage"), productsDiscountsRs.getInt("amount")));

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

    public void addTotalPriceDiscount(TotalPriceDiscount discount) throws SQLException {
        try
        {
            conn = Database.connect();

            PreparedStatement stmt1 = conn.prepareStatement("INSERT INTO DealDiscounts (supplierId, discountId, type) VALUES (?, ?, ?)");
            stmt1.setString(1, belongsToSupplierId);
            stmt1.setInt(2, nextId);
            stmt1.setInt(3, discount.getType());
            stmt1.execute();

            PreparedStatement stmt = conn.prepareStatement("INSERT INTO TotalPriceDiscounts (discountId, minPrice, percentage) VALUES (?, ?, ?)");
            stmt.setInt(1, nextId);
            stmt.setDouble(2, discount.getMinPrice());
            stmt.setDouble(3, discount.getPercentage());
            stmt.execute();
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

    public void addTotalProductsDiscount(TotalProductsDiscount discount) throws SQLException {
        try
        {
            conn = Database.connect();

            PreparedStatement stmt1 = conn.prepareStatement("INSERT INTO DealDiscounts (supplierId, discountId, type) VALUES (?, ?, ?)");
            stmt1.setString(1, belongsToSupplierId);
            stmt1.setInt(2, nextId);
            stmt1.setInt(3, discount.getType());
            stmt1.execute();

            PreparedStatement stmt = conn.prepareStatement("INSERT INTO TotalProductsDiscount (discountId, amount, percentage) VALUES (?, ?, ?)");
            stmt.setInt(1, nextId);
            stmt.setDouble(2, discount.getAmountOfProducts());
            stmt.setDouble(3, discount.getPercentage());
            stmt.execute();
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

            PreparedStatement stmt1 = conn.prepareStatement("SELECT * FROM DealDiscounts WHERE supplierId = ?");
            stmt1.setString(1, belongsToSupplierId);
            ResultSet idsRs = stmt1.executeQuery();
            while(idsRs.next())
                discountIds.add(idsRs.getInt("discountId"));

            PreparedStatement stmt2 = conn.prepareStatement("DELETE FROM DealDiscounts WHERE supplierId = ?");
            stmt2.setString(1, belongsToSupplierId);
            stmt2.execute();

            for(int id : discountIds)
            {
                PreparedStatement stmt3 = conn.prepareStatement("DELETE FROM TotalPriceDiscounts WHERE discountId = ?");
                PreparedStatement stmt4 = conn.prepareStatement("DELETE FROM TotalProductsDiscount WHERE discountId = ?");
                stmt3.setInt(1, id);
                stmt4.setInt(1, id);
                stmt3.execute();
                stmt4.execute();
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
