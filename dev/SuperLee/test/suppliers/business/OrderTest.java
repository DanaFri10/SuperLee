package suppliers.business;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import shared.Database;
import stock.business.Branch;
import stock.business.Product;
import stock.business.StockController;
import stock.dal.BranchDAO;
import stock.dal.ProductDAO;
import suppliers.dal.ContactsDAO;
import suppliers.dal.OrdersDAO;
import suppliers.dal.SuppliersDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {
    private static Order o;
    private static ProductDAO productsDAO;
    private static BranchDAO branchDAO;
    private static SuppliersDAO suppliersDAO;
    private static OrdersDAO ordersDAO;

    @BeforeAll
    static void setUp() {
        try {
            Database.setPath("../SuperLeeTestDB.db");
            Database.clearDatabase();

            productsDAO = new ProductDAO();
            branchDAO = new BranchDAO();
            suppliersDAO = new SuppliersDAO(new ContactsDAO());
            ordersDAO = new OrdersDAO(suppliersDAO);

            productsDAO.insertProduct(new Product(823, "Product1", "Manu1"));
            productsDAO.insertProduct(new Product(101, "Product2", "Manu2"));


            suppliersDAO.addSupplier("872090123", "TestSupplier1", "TestLocation1", "346-00998/12", new ArrayList<>(), PaymentMethod.ongoing, true, 5, new boolean[]{true, true, true ,false, false, false, false});
            suppliersDAO.getSupplier("872090123").getDeal().addProduct(912, 823, 100, 100);
            suppliersDAO.getSupplier("872090123").getDeal().addTotalProductsDiscount(new TotalProductsDiscount(0.5, 50));

            suppliersDAO.addSupplier("980980976", "TestSupplier2", "TestLocation2", "123-56423/12", new ArrayList<>(), PaymentMethod.net30EOM, false, 12, new boolean[]{false, false, false ,false, false, false, false});
            suppliersDAO.getSupplier("980980976").getDeal().addProduct(176, 101, 100, 100);

            Map<String, Map<Integer, Integer>> orderProducts = new HashMap<>();
            orderProducts.put("872090123", new HashMap<>());
            orderProducts.get("872090123").put(912, 100);
            orderProducts.put("980980976", new HashMap<>());
            orderProducts.get("980980976").put(176, 100);

            o = ordersDAO.addOrder(orderProducts, 165);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
    @AfterAll
    static void cleanUp() {
        try {

            suppliersDAO.removeSupplier("872090123");
            suppliersDAO.removeSupplier("980980976");

            productsDAO.removeProduct(823);
            productsDAO.removeProduct(101);

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }*/

    @Test
    void getPriceTotal() {
        assertEquals(15000, o.getPriceTotal());
    }

    @Test
    void getPriceTotalBeforeDiscount() {
        assertEquals(20000, o.getPriceTotalBeforeDiscount());
    }

    @Test
    void getPriceForSupplier() {
        assertEquals(5000, o.getPriceForSupplier("872090123"));
        assertEquals(10000, o.getPriceForSupplier("980980976"));
    }

    @Test
    void ordersToSupplier() {
        assertTrue(o.ordersToSupplier("872090123"));
        assertTrue(o.ordersToSupplier("980980976"));
        assertFalse(o.ordersToSupplier("090787845"));
    }
}