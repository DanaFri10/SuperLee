package suppliers.dal;

import jdk.jshell.spi.ExecutionControl;
import shared.Database;
import stock.dal.ProductDAO;
import suppliers.business.*;
import stock.business.Product;
//import suppliers.service.Response;

import java.sql.*;
import java.util.*;

import static suppliers.business.Utils.*;


public class SuppliersDAO {
    private Connection conn;
    private Map<String, Supplier> suppliers;
    private ContactsDAO contactsDAO;
    private boolean gotAll;

    public SuppliersDAO(ContactsDAO contactsDAO) throws SQLException
    {
        this.contactsDAO = contactsDAO;
        suppliers = new HashMap<>();
        gotAll = false;

        conn = Database.connect();
    }

    private boolean supplierExists(String companyId) throws SQLException {
        try
        {
            conn = Database.connect();

            if(suppliers.containsKey(companyId)) return true;

            Statement stmt = conn.createStatement();
            ResultSet suppliersRecords = stmt.executeQuery(String.format("SELECT * FROM Suppliers WHERE id = \"%s\"", companyId));
            if(suppliersRecords.next()) return true;
            return false;
        }
        catch(SQLException e)
        {
            throw e;
        }
        finally {
            conn.close();;
        }
    }

    public Supplier addSupplier(String companyId, String companyName, String location, String bankAccount, List<String> supplyAreas, PaymentMethod paymentMethod, boolean delivers, int daysToDeliver, boolean[] deliveryDays) throws SQLException {
        if(supplierExists(companyId))
            throw new IllegalArgumentException("A supplier with ID " + companyId + " already exists.");

        Supplier supplier = new Supplier(companyId, companyName, location, bankAccount, supplyAreas);
        supplier.addDeal(delivers, daysToDeliver, deliveryDays, paymentMethod);

        try
        {
            conn = Database.connect();

            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Suppliers (id, name, location, bank_account, payment_method, delivers, days_to_deliver, sunday, monday, tuesday, wednesday, thursday, friday, saturday) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            stmt.setString(1, companyId);
            stmt.setString(2, companyName);
            stmt.setString(3, location);
            stmt.setString(4, bankAccount);
            stmt.setInt(5, (paymentMethod == PaymentMethod.net30EOM) ? 0 : ((paymentMethod == PaymentMethod.net60EOM) ? 1 : 2));
            stmt.setInt(6, delivers ? 1 : 0);
            stmt.setInt(7, daysToDeliver);
            for(int i = 0 ; i <= 6; i++) stmt.setInt(i + 8, deliveryDays[i] ? 1 : 0);
            stmt.execute();

            for(String supplyArea : supplyAreas) {
                if(supplyArea != "")
                {
                    PreparedStatement supplyAreaStmt = conn.prepareStatement("INSERT INTO SupplyAreas (supplierId, supplyArea) VALUES (?, ?)");
                    supplyAreaStmt.setString(1, companyId);
                    supplyAreaStmt.setString(2, supplyArea);
                    supplyAreaStmt.execute();
                }
            }

            suppliers.put(companyId, supplier);
            return supplier;
        }
        catch(SQLException e)
        {
            throw e;
        }
        finally {
            conn.close();
        }
    }

    public void removeSupplier(String companyId) throws SQLException {
        try {
            getSupplier(companyId).getDeal().clearDealDiscounts();

            if(getSupplier(companyId).getDeal().getProducts() != null)
                for(int catNum : getSupplier(companyId).getDeal().getProducts().keySet())
                    getSupplier(companyId).getDeal().removeProduct(catNum);

            conn = Database.connect();

            String[] tables = new String[]{"SuppliersContacts", "SupplyAreas", "PeriodicOrderProducts", "PeriodicOrders"};

            for(String tbl : tables)
            {
                PreparedStatement stmt1 = conn.prepareStatement("DELETE FROM " + tbl + " WHERE supplierId = ?");
                stmt1.setString(1, companyId);
                stmt1.execute();
            }

            PreparedStatement stmt2 = conn.prepareStatement("DELETE FROM Suppliers WHERE id = ?");
            stmt2.setString(1, companyId);
            stmt2.execute();

            if (suppliers.containsKey(companyId))
                suppliers.remove(companyId);
        }
        catch(SQLException e)
        {
            throw e;
        }
        finally {
            conn.close();
        }
    }

    public Supplier getSupplier(String companyId) throws SQLException
    {
        try
        {
            conn = Database.connect();

            if(!suppliers.containsKey(companyId)){
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Suppliers WHERE id = ?");
                stmt.setString(1, companyId);
                ResultSet suppliersRecords = stmt.executeQuery();

                if(!suppliersRecords.isBeforeFirst() && suppliersRecords.getRow() == 0)
                    throw new IllegalArgumentException("This supplier does not exist.");
                addQueryResultToMap(suppliersRecords);
            }
            return suppliers.get(companyId);
        }
        catch(SQLException e)
        {
            throw e;
        }
        finally {
            conn.close();
        }
    }

    public List<Supplier> getAllSuppliers() throws SQLException
    {
        try
        {
            conn = Database.connect();
            if(!gotAll) {
                Statement stmt = conn.createStatement();
                ResultSet suppliersRecords = stmt.executeQuery("SELECT * FROM Suppliers");
                addQueryResultToMap(suppliersRecords);
                gotAll = true;
            }
            return suppliers.values().stream().toList();
        }
        catch(SQLException e)
        {
            throw e;
        }
        finally {
            conn.close();
        }
    }

    private void addQueryResultToMap(ResultSet suppliersRecords) throws SQLException
    {
        try {
            while (suppliersRecords.next()) {
                String supplierId = suppliersRecords.getString("id");

                if (!suppliers.containsKey(supplierId)) {
                    conn = Database.connect();

                    List<String> supplyAreas = new ArrayList<>();
                    PreparedStatement supplyAreasStmt = conn.prepareStatement("SELECT * FROM SupplyAreas WHERE supplierId = ?");
                    supplyAreasStmt.setString(1, supplierId);
                    ResultSet supplyAreasRecords = supplyAreasStmt.executeQuery();
                    while (supplyAreasRecords.next())
                        supplyAreas.add(supplyAreasRecords.getString("supplyArea"));

                    boolean delivers = suppliersRecords.getInt("delivers") == 0 ? false : true;

                    String[] days = new String[]{"sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday"};
                    boolean[] deliveryDays = new boolean[7];
                    for (int i = 0; i < 7; i++) deliveryDays[i] = suppliersRecords.getInt(days[i]) == 0 ? false : true;

                    PaymentMethod pm = (suppliersRecords.getInt("payment_method") == 0) ? PaymentMethod.net30EOM : ((suppliersRecords.getInt("payment_method") == 1) ? PaymentMethod.net60EOM : PaymentMethod.ongoing);

                    Supplier supplier = new Supplier(supplierId, suppliersRecords.getString("name"), suppliersRecords.getString("location"), suppliersRecords.getString("bank_account"), supplyAreas);
                    supplier.addDeal(delivers, suppliersRecords.getInt("days_to_deliver"), deliveryDays, pm);

                    PreparedStatement contactsStmt = conn.prepareStatement("SELECT * FROM SuppliersContacts WHERE supplierId = ?");
                    contactsStmt.setString(1, supplierId);
                    ResultSet contactsRecords = contactsStmt.executeQuery();
                    while(contactsRecords.next())
                        supplier.addSupplierContact(contactsRecords.getString("contactId"));

                    suppliers.put(supplierId, supplier);
                }
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

    public List<String> filterBySupplyArea(String supplyArea) throws SQLException
    {
        try {
            conn = Database.connect();
            if (!gotAll) {
                Statement stmt = conn.createStatement();
                ResultSet suppliersRecords = stmt.executeQuery(String.format("SELECT * FROM Suppliers JOIN SupplyAreas ON Suppliers.id = SupplyAreas.supplierId WHERE SupplyAreas.supplyArea = \"%s\"", supplyArea));
                addQueryResultToMap(suppliersRecords);
            }
        }
        catch(SQLException e)
        {
            throw e;
        }
        finally {
            conn.close();
        }

        List<String> filtered = new ArrayList<>();
        for(Supplier s : suppliers.values())
            if(s.supplyAreaExists(supplyArea.toLowerCase()))
                filtered.add(s.getCompanyId() + ": " + s.getCompanyName());
        return filtered;
    }


    public void clearData() throws SQLException {
        suppliers.clear();
        try {
            conn = Database.connect();

            String[] tables = new String[]{"Suppliers", "SuppliersContacts", "SupplyAreas", "DealDiscounts", "DealProductDiscounts", "DealProducts", "PeriodicOrderProducts", "PeriodicOrders"};

            for(String tbl : tables)
            {
                PreparedStatement stmt1 = conn.prepareStatement("DELETE FROM " + tbl);
                stmt1.execute();
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

    /*
    public void removeProduct(int productId) throws SQLException {
        for(Supplier supplier : suppliers.values()){
            if(supplier.hasDeal() && supplier.getDeal().hasEnoughOfProduct(productId,0)){
                DealProductInformation dpm = supplier.getDeal().getDealProductByProductId(productId);
                supplier.getDeal().removeProduct(dpm.getCatalogueNum());
                // remove from DB
            }
        }
    }*/

    public void updateSupplierInfo(String companyId, String companyName, String location, String bankAccount, List<String> supplyAreas) throws SQLException{
        try
        {
            conn = Database.connect();

            PreparedStatement stmt1 = conn.prepareStatement("UPDATE Suppliers SET name = ?, location = ?, bank_account = ? WHERE id = ?");
            stmt1.setString(1, companyName);
            stmt1.setString(2, location);
            stmt1.setString(3, bankAccount);
            stmt1.setString(4, companyId);
            stmt1.execute();

            PreparedStatement stmt2 = conn.prepareStatement("DELETE FROM SupplyAreas WHERE supplierId = ?");
            stmt2.setString(1, companyId);
            stmt2.execute();

            for(String supplyArea : supplyAreas) {
                if(supplyArea != "")
                {
                    PreparedStatement supplyAreaStmt = conn.prepareStatement("INSERT INTO SupplyAreas (supplierId, supplyArea) VALUES (?, ?)");
                    supplyAreaStmt.setString(1, companyId);
                    supplyAreaStmt.setString(2, supplyArea);
                    supplyAreaStmt.execute();
                }
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

    public void addNewSupplierContact(String companyId, String contactId, String name, String phoneNumber, String email, String address) throws SQLException{
        contactsDAO.addContact(contactId, name, phoneNumber, email, address);
        addExistingSupplierContact(companyId, contactId);
    }

    public void addExistingSupplierContact(String companyId, String contactId) throws SQLException{
        if(!contactsDAO.contactExists(contactId))
            throw new IllegalArgumentException("A contact with ID " + contactId + " doesn't exist.");

        try
        {
            conn = Database.connect();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO SuppliersContacts (supplierId, contactId) VALUES (?, ?)");
            stmt.setString(1, companyId);
            stmt.setString(2, contactId);
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

    public void removeSupplierContact(String companyId, String contactId) throws SQLException{
        try
        {
            conn = Database.connect();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM SuppliersContacts WHERE supplierId = ? AND contactId = ?");
            stmt.setString(1, companyId);
            stmt.setString(2, contactId);
            stmt.execute();

            PreparedStatement stmt2 = conn.prepareStatement("UPDATE PeriodicOrders SET contactId = NULL WHERE contactId = ?");
            stmt2.setString(1, contactId);
            stmt2.execute();

            PreparedStatement stmt3 = conn.prepareStatement("UPDATE OrderContacts SET contactId = NULL WHERE contactId = ?");
            stmt3.setString(1, contactId);
            stmt3.execute();
        }
        catch(SQLException e)
        {
            throw e;
        }
        finally {
            conn.close();
        }
    }

    public void updateDeal(String companyId, boolean delivers, int daysToDeliver, PaymentMethod paymentMethod, boolean[] deliveryDays) throws SQLException{
        try
        {
            conn = Database.connect();
            PreparedStatement stmt = conn.prepareStatement("UPDATE Suppliers SET delivers = ?, days_to_deliver = ?, payment_method = ?, sunday = ?, monday = ?, tuesday = ?, wednesday = ?, thursday = ?, friday = ?, saturday = ? WHERE id = ?");
            stmt.setInt(1, delivers ? 1 : 0);
            stmt.setInt(2, daysToDeliver);
            stmt.setInt(3, (paymentMethod == PaymentMethod.net30EOM) ? 0 : ((paymentMethod == PaymentMethod.net60EOM) ? 1 : 2));
            for(int i = 0 ; i <= 6; i++) stmt.setInt(i + 4, deliveryDays[i] ? 1 : 0);
            stmt.setString(11,companyId);
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

    /*
    public void addProductToDeal(String companyId, int catalogue, int productId, double price, int amount) throws SQLException{
        Deal deal = getSupplier(companyId).getDeal(); // Checks if supplier and deal exist.
        deal.addProduct(catalogue, productId, price, amount);
    }

    public void updateDealProduct(String companyId, int catalogue, double price, int amount) throws SQLException{
        Deal deal = getSupplier(companyId).getDeal(); // Checks if supplier and deal exist.
        deal.updateProductInfo(catalogue, price, amount);
    }

    public void removeProductFromDeal(String companyId, int catalogue) throws SQLException{
        getSupplier(companyId).getDeal().removeProduct(catalogue);
    }

    public List<String> getAllProductSuppliers(int productId) throws SQLException {
        List<String> productSuppliers = new ArrayList<>();
        for(Supplier s : suppliers.values())
            if(s.getDeal().getDealProductByProductId(productId) != null)
                productSuppliers.add(s.getCompanyId() + ": " + s.getCompanyName());
        return productSuppliers;
    }

    public List<String> getAllSuppliedProducts() throws SQLException {
        Set<Product> suppliedProducts = new HashSet<>();
        for(Supplier s : suppliers.values())
            for(DealProductInformation product : s.getDeal().getProducts().values())
                suppliedProducts.add(productDAO.getProduct(product.getProductId()));
        List<Product> sortedSuppliedProducts = new ArrayList<>(suppliedProducts);
        Collections.sort(sortedSuppliedProducts, (Product p1, Product p2)->p1.getProductId()-p2.getProductId());
        List<String> res = new ArrayList<>();
        for(Product p : sortedSuppliedProducts)
            res.add(p.getProductId() + ": " + p.getName() + " supplied by " + getAllProductSuppliers(p.getProductId()).toString());
        return res;
    }*/

}
