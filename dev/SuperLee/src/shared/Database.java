package shared;

import java.sql.*;
public class Database {
    private static String DB_URL = "jdbc:sqlite:";

    public static void setPath(String path){
        DB_URL = "jdbc:sqlite:" + path;
    }

    public static Connection connect() throws SQLException
    {
        return DriverManager.getConnection(DB_URL);
    }

    public static void clearDatabase() throws SQLException {
        Connection conn = connect();

        String [] tables = new String[] {"BranchProductInstances", "BranchProducts", "CategoryDiscounts", "Contacts", "DealDiscounts", "DealProductDiscounts", "DealProducts", "OrderContacts", "OrderProducts", "Orders", "PeriodicOrderProducts", "PeriodicOrders", "Products", "ProductsAmountDiscounts", "Suppliers", "SuppliersContacts", "SupplyAreas", "TotalPriceDiscounts", "TotalProductsDiscount", "Users"};
        for(String tblName : tables)
        {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM " + tblName);
            stmt.execute();
        }
    }
}