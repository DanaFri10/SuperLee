package suppliers.dal;

import shared.Database;
import suppliers.business.Deal;
import suppliers.business.DealProductInformation;
import suppliers.business.Order;
import suppliers.business.Supplier;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class OrdersDAO {
    private Connection conn;
    private Map<Integer, Order> orders;
    private SuppliersDAO suppliersDAO;
    static int availableId;
    private boolean gotAll;

    public OrdersDAO(SuppliersDAO suppliersDAO) throws SQLException {
        this.suppliersDAO = suppliersDAO;
        orders = new HashMap<>();
        gotAll = false;
        try
        {
            this.conn = Database.connect();

            PreparedStatement stmt = conn.prepareStatement("SELECT MAX(id) AS max FROM Orders");
            ResultSet rs = stmt.executeQuery();
            availableId = rs.getInt("max") + 1;
        }
        catch (SQLException e)
        {
            throw  e;
        }
        finally {
            if(conn != null) {
                conn.close();
            }
        }
    }

    public Order addOrder(Map<String,Map<Integer,Integer>> supplierToCatalogueToAmount, int branchId) throws SQLException
    {
        Map<String, Map<Integer, Double>> priceAfterDiscountMap = new HashMap<>();
        Map<String, Map<Integer, Double>> priceBeforeDiscountMap = new HashMap<>();

        for(String companyId : supplierToCatalogueToAmount.keySet()){
            Supplier s = suppliersDAO.getSupplier(companyId);
            priceAfterDiscountMap.put(companyId,s.getDeal().calculatePriceProductMap(supplierToCatalogueToAmount.get(companyId)));
            priceBeforeDiscountMap.put(companyId,new HashMap<>());
            for(int catNum : supplierToCatalogueToAmount.get(companyId).keySet()){
                DealProductInformation dpi = s.getDeal().getDealProductInfo(catNum);
                priceBeforeDiscountMap.get(companyId).put(catNum,dpi.calculatePriceBeforeDiscount(supplierToCatalogueToAmount.get(companyId).get(catNum)));
            }
        }

        Order o = new Order(availableId, supplierToCatalogueToAmount, priceAfterDiscountMap, priceBeforeDiscountMap, branchId);

        try {
            conn = Database.connect();

            PreparedStatement stmt1 = conn.prepareStatement("INSERT INTO Orders (id, order_date, branchId) VALUES (?, ?, ?)");
            stmt1.setInt(1, o.getOrderId());
            stmt1.setLong(2, o.getOrderDate().getTime());
            stmt1.setInt(3, branchId);
            stmt1.execute();

            for (String supplierId : supplierToCatalogueToAmount.keySet()) {
                PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO OrderProducts (orderId, supplierId, catalougeNum, amount, priceBeforeDiscount, priceAfterDiscount) VALUES (?, ?, ?, ?, ?, ?)");
                stmt2.setInt(1, o.getOrderId());
                stmt2.setString(2, supplierId);
                for (int catNum : supplierToCatalogueToAmount.get(supplierId).keySet()) {
                    stmt2.setInt(3, catNum);
                    stmt2.setInt(4, supplierToCatalogueToAmount.get(supplierId).get(catNum));
                    stmt2.setDouble(5, o.getPriceForSupplierMapBeforeDiscount(supplierId).get(catNum));
                    stmt2.setDouble(6, o.getPriceMapForSupplier(supplierId).get(catNum));
                    stmt2.execute();
                }
                PreparedStatement insertContact = conn.prepareStatement("INSERT INTO OrderContacts (orderId, contactId, supplierId, isArrived) VALUES (?, ?, ?, ?)");
                insertContact.setInt(1, o.getOrderId());
                insertContact.setNull(2, Types.VARCHAR);
                insertContact.setString(3, supplierId);
                insertContact.setInt(4, 0);
                insertContact.execute();
            }

            orders.put(availableId, o);
            availableId++;
        }
        catch(SQLException e)
        {
            throw e;
        }
        finally {
            conn.close();
        }
        return o;
    }

    public Order addOrder(Map<String,Map<Integer,Integer>> supplierToCatalogueToAmount, int branchId, Date orderDate) throws SQLException
    {
        Map<String, Map<Integer, Double>> priceAfterDiscountMap = new HashMap<>();
        Map<String, Map<Integer, Double>> priceBeforeDiscountMap = new HashMap<>();
        for(String companyId : supplierToCatalogueToAmount.keySet()){
            Supplier s = suppliersDAO.getSupplier(companyId);
            priceAfterDiscountMap.put(companyId,s.getDeal().calculatePriceProductMap(supplierToCatalogueToAmount.get(companyId)));
            priceBeforeDiscountMap.put(companyId,new HashMap<>());
            for(int catNum : supplierToCatalogueToAmount.get(companyId).keySet()){
                DealProductInformation dpi = s.getDeal().getDealProductInfo(catNum);
                priceBeforeDiscountMap.get(companyId).put(catNum,dpi.calculatePriceBeforeDiscount(supplierToCatalogueToAmount.get(companyId).get(catNum)));
            }
        }

        Order o = new Order(availableId, supplierToCatalogueToAmount, orderDate, priceAfterDiscountMap, priceBeforeDiscountMap, branchId);

        try {
            conn = Database.connect();

            PreparedStatement stmt1 = conn.prepareStatement("INSERT INTO Orders (id, order_date, branchId) VALUES (?, ?, ?)");
            stmt1.setInt(1, o.getOrderId());
            stmt1.setLong(2, o.getOrderDate().getTime());
            stmt1.setInt(3, branchId);
            stmt1.execute();

            for (String supplierId : supplierToCatalogueToAmount.keySet()) {
                PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO OrderProducts (orderId, supplierId, catalougeNum, amount, priceBeforeDiscount, priceAfterDiscount) VALUES (?, ?, ?, ?, ?, ?)");
                stmt2.setInt(1, o.getOrderId());
                stmt2.setString(2, supplierId);
                for (int catNum : supplierToCatalogueToAmount.get(supplierId).keySet()) {

                    stmt2.setInt(3, catNum);
                    stmt2.setInt(4, supplierToCatalogueToAmount.get(supplierId).get(catNum));
                    stmt2.setDouble(5, o.getPriceForSupplierMapBeforeDiscount(supplierId).get(catNum));
                    stmt2.setDouble(6, o.getPriceMapForSupplier(supplierId).get(catNum));
                    stmt2.execute();
                }

                PreparedStatement insertContact = conn.prepareStatement("INSERT INTO OrderContacts (orderId, contactId, supplierId, isArrived) VALUES (?, ?, ?, ?)");
                insertContact.setInt(1, o.getOrderId());
                insertContact.setNull(2, Types.VARCHAR);
                insertContact.setString(3, supplierId);
                insertContact.setInt(4, 0);
                insertContact.execute();
            }

            orders.put(availableId, o);
            availableId++;
        }
        catch(SQLException e)
        {
            throw e;
        }
        finally {
            conn.close();
        }
        return o;
    }

    /*
    public void cancelOrder(int orderId){
        if(!orderExists(orderId))
            throw new IllegalArgumentException(String.format("An order with ID %d doesn't exist.", orderId));
        if(orders.get(orderId).orderStartedToArrive())
            throw new IllegalArgumentException(String.format("You can not cancel order %d since some products arrived.", orderId));

        orders.remove(orderId);
    }

    public void removeOrderFromHistory(int orderId){
        if(!orderExists(orderId))
            throw new IllegalArgumentException(String.format("An order with ID %d doesn't exist.", orderId));
        if(!orders.get(orderId).orderArrivedCompletely())
            throw new IllegalArgumentException(String.format("Order number %d is not complete.", orderId));
        orders.remove(orderId);
    }

    public void updateOrder(int orderId, Map<Deal,Map<Integer,Integer>> newDivision){
        Order order = getOrder(orderId);
        cancelOrder(orderId);
        orders.put(order.getOrderId(), new Order(order.getOrderId(), newDivision, order.getOrderDate()));
    }

    public List<Order> getCompleteOrders()
    {
        List<Order> completeOrders = new LinkedList<>();
        for(Order o : orders.values())
            if(o.orderArrivedCompletely())
                completeOrders.add(o);
        return completeOrders;
    }

    public List<Order> getArrivingOrders()
    {
        List<Order> arrivingOrders = new LinkedList<>();
        for(Order o : orders.values())
            if(!o.orderArrivedCompletely())
                arrivingOrders.add(o);
        return arrivingOrders;
    }*/

    private void addQueryResToMap(ResultSet rs) throws SQLException {
        while(rs.next()) {
            int orderId = rs.getInt("orderId");

            if (!orders.containsKey(orderId)) {
                Map<String, Map<Integer, Integer>> supplierTocatalogueNumToAmount = new HashMap<>();
                Map<String, Map<Integer, Double>> suppliersProductPriceMap = new HashMap<>();
                Map<String, Map<Integer, Double>> suppliersProductPriceBeforeDiscountMap = new HashMap<>();

                try {
                    conn = Database.connect();

                    PreparedStatement orderProductStmt = conn.prepareStatement("SELECT * FROM OrderProducts WHERE orderId = ?");
                    orderProductStmt.setInt(1, orderId);
                    ResultSet orderProductStmtRs = orderProductStmt.executeQuery();
                    while (orderProductStmtRs.next()) {
                        String supplierId = orderProductStmtRs.getString("supplierId");
                        int catalougeNum = orderProductStmtRs.getInt("catalougeNum");

                        if (!supplierTocatalogueNumToAmount.containsKey(supplierId))
                            supplierTocatalogueNumToAmount.put(supplierId, new HashMap<>());
                        supplierTocatalogueNumToAmount.get(supplierId).put(catalougeNum, orderProductStmtRs.getInt("amount"));

                        if (!suppliersProductPriceMap.containsKey(supplierId))
                            suppliersProductPriceMap.put(supplierId, new HashMap<>());
                        suppliersProductPriceMap.get(supplierId).put(catalougeNum, orderProductStmtRs.getDouble("priceAfterDiscount"));

                        if (!suppliersProductPriceBeforeDiscountMap.containsKey(supplierId))
                            suppliersProductPriceBeforeDiscountMap.put(supplierId, new HashMap<>());
                        suppliersProductPriceBeforeDiscountMap.get(supplierId).put(catalougeNum, orderProductStmtRs.getDouble("priceBeforeDiscount"));
                    }
                    Order o = new Order(orderId, supplierTocatalogueNumToAmount, new Date(rs.getLong("order_date")), suppliersProductPriceMap, suppliersProductPriceBeforeDiscountMap, rs.getInt("branchId"));
                    orders.put(orderId, o);
                    PreparedStatement orderContactStmt = conn.prepareStatement("SELECT * FROM OrderContacts WHERE orderId = ?");
                    orderContactStmt.setInt(1, orderId);
                    ResultSet orderContactStmtRs = orderContactStmt.executeQuery();
                    while (orderContactStmtRs.next()) {
                        String supplierId = orderContactStmtRs.getString("supplierId");
                        String contactId = orderContactStmtRs.getString("contactId");
                        boolean isArrived = orderContactStmtRs.getInt("isArrived") == 1;
                        if(contactId != null){
                            o.setContactForSupplier(supplierId,contactId);
                        }
                        if(isArrived){
                            o.makeArrive(supplierId);
                        }
                    }
                }
                catch(SQLException e)
                {
                    throw e;
                }
                finally {
                    conn.close();;
                }
            }
        }
    }

    public Order getOrder(int orderId) throws SQLException {
        if(!orders.containsKey(orderId)){
            try {
                conn = Database.connect();

                PreparedStatement orderStmt = conn.prepareStatement("SELECT FROM Orders WHERE id = ?");
                orderStmt.setInt(1, orderId);
                ResultSet orderRs = orderStmt.executeQuery();

                if (!orderRs.isBeforeFirst())
                    throw new IllegalArgumentException("An order with ID " + orderId + " does not exist.");

                addQueryResToMap(orderRs);
            }
            catch (SQLException e) {
                throw e;
            }
            finally {
                conn.close();
            }
        }
        return orders.get(orderId);
    }

    public void assignContactToOrder(int orderId, String supplierId, String contactId) throws SQLException {
        try {
            conn = Database.connect();
            orders.get(orderId).setContactForSupplier(supplierId,contactId);
            PreparedStatement updateContact = conn.prepareStatement("UPDATE OrderContacts SET contactId = ? WHERE orderId = ? AND supplierId = ?");
            updateContact.setString(1, contactId);
            updateContact.setInt(2, orderId);
            updateContact.setString(3, supplierId);
            updateContact.execute();
        }
        catch (SQLException e) {
            throw e;
        }
        finally {
            conn.close();
        }
    }

    public void setArrived(int orderId, String supplierId) throws SQLException {
        try {
            conn = Database.connect();

            PreparedStatement updateContact = conn.prepareStatement("UPDATE OrderContacts SET isArrived = ? WHERE orderId = ? AND supplierId = ?");
            updateContact.setInt(1, 1);
            updateContact.setInt(2, orderId);
            updateContact.setString(3, supplierId);
            updateContact.execute();
            orders.get(orderId).makeArrive(supplierId);
        }
        catch (SQLException e) {
            throw e;
        }
        finally {
            conn.close();
        }
    }

    public List<Order> getOrderHistory() throws SQLException {
        if(!gotAll){
            try {
                conn = Database.connect();

                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Orders JOIN OrderProducts ON Orders.id = OrderProducts.orderId");
                ResultSet rs = stmt.executeQuery();
                addQueryResToMap(rs);
                gotAll = true;
            }
            catch (SQLException e) {
                throw e;
            }
            finally {
                conn.close();
            }
        }
        List<Order> orderHistory = (new ArrayList<>(orders.values()));
        orderHistory.sort(Comparator.comparing(Order::getOrderDate));
        Collections.reverse(orderHistory);
        return orderHistory;
    }

    public void clearData() throws SQLException {
        orders.clear();

        try {
            conn = Database.connect();
            PreparedStatement stmt1 = conn.prepareStatement("DELETE FROM Orders");
            stmt1.execute();

            PreparedStatement stmt2 = conn.prepareStatement("DELETE FROM OrderContacts");
            stmt2.execute();

            PreparedStatement stmt3 = conn.prepareStatement("DELETE FROM OrderProducts");
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
}
