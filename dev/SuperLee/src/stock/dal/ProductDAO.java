package stock.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import shared.Database;
import stock.business.Product;

public class ProductDAO {
    private Map<Integer, Product> productsMap;
    private boolean gotAll;

    public ProductDAO() throws SQLException{
        productsMap = new HashMap<>();
        gotAll = false;
    }

    public void insertProduct(Product p) throws SQLException{
        Integer key = p.getProductId();
        productsMap.put(key, p);
        Connection conn = Database.connect();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Products (id, name, manufacturer) VALUES (?, ?, ?)");
        stmt.setInt(1, p.getProductId());
        stmt.setString(2, p.getName());
        stmt.setString(3, p.getManufacturer());
        stmt.executeUpdate();
        stmt.close();
        conn.close();
    }

    public void updateProduct(Product p) throws SQLException{
        productsMap.put(p.getProductId(), p);
        Connection conn = Database.connect();
        PreparedStatement stmt = conn.prepareStatement("UPDATE Products SET name = ?, manufacturer = ? WHERE id = ?");
        stmt.setString(1, p.getName());
        stmt.setString(2, p.getManufacturer());
        stmt.setInt(3, p.getProductId());
        stmt.executeUpdate();
        stmt.close();
        conn.close();
    }

    public void removeProduct(int productId) throws SQLException{
        Integer key = productId;
        productsMap.remove(key);
        Connection conn = Database.connect();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Products WHERE id = ?");
        stmt.setInt(1, key);
        stmt.executeUpdate();
        stmt.close();
        conn.close();
    }

    public Product getProduct(int productId) throws SQLException{
        Integer key = productId;
        if(productsMap.containsKey(key)){
            return productsMap.get(key);
        }
        Connection conn = Database.connect();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Products WHERE id = ?");
        stmt.setInt(1, key);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            Product product =  new Product(rs.getInt(1), rs.getString(2), rs.getString(3));
            productsMap.put(key, product);
            stmt.close();
            conn.close();
            return product;
        } else {
            stmt.close();
            conn.close();
            return null;
        }    
    }
    public ArrayList<Product> getAll() throws SQLException{
        ArrayList<Product> products = new ArrayList<>();
        if(gotAll){
            products.addAll(productsMap.values());
            return products;
        }
        Connection conn = Database.connect();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Products");
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Product product =  new Product(rs.getInt(1), rs.getString(2), rs.getString(3));
            products.add(product);
            productsMap.put(product.getProductId(), product);
        }
        stmt.close();
        conn.close();
        gotAll = true;
        return products;
    }
}

