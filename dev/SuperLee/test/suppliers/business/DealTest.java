package suppliers.business;

import org.junit.jupiter.api.*;
import shared.Database;
import stock.business.Product;
import stock.dal.ProductDAO;
import suppliers.dal.ContactsDAO;
import suppliers.dal.SuppliersDAO;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DealTest {
    private static Deal d1;
    private static Deal d2;
    private static ProductDAO productDAO;

    @BeforeAll
    static void setUp() {
        try
        {
            Database.setPath("../SuperLeeTestDB.db");
            Database.clearDatabase();

            productDAO = new ProductDAO();
            //suppliersDAO = new SuppliersDAO(productDAO, new ContactsDAO());

            Supplier s1 = new Supplier("872090123", "TestSupplier1", "TestLocation1", "346-00998/12", new ArrayList<>());
            d1 = new Deal(true, 5, new boolean[]{true, true, true ,false, false, false, false}, PaymentMethod.ongoing, s1);

            Supplier s2 = new Supplier("980980976", "TestSupplier2", "TestLocation2", "123-56423/12", new ArrayList<>());
            d2 = new Deal(false, 12, new boolean[]{false, false, false ,false, false, false, false}, PaymentMethod.net30EOM, s2);

            Product p1 = new Product(8361, "Product1", "Manufacturer1");
            productDAO.insertProduct(p1);
            Product p2 = new Product(1008, "Product2", "Manufacturer2");
            productDAO.insertProduct(p2);
            Product p3 = new Product(4476, "Product3", "Manufacturer3");
            productDAO.insertProduct(p3);
            Product p4 = new Product(9223, "Product4", "Manufacturer4");
            productDAO.insertProduct(p4);
            Product p5 = new Product(5610, "Product5", "Manufacturer5");
            productDAO.insertProduct(p5);
            d1.addProduct(1, 8361, 10.50, 300);
            d1.addProduct(2, 1008, 12, 400);
            d1.addProduct(3, 4476, 15.32, 150);
            d1.addProduct(4, 9223, 5.6, 900);
            d1.addProduct(5, 5610, 40, 5);

            d1.addTotalProductsDiscount(new TotalProductsDiscount(0.2, 100));
            d1.addTotalPriceDiscount(new TotalPriceDiscount(0.1, 500));

            d1.addDiscountToProduct(new ProductAmountDiscount(0.1, 30), 1);
            d1.addDiscountToProduct(new ProductAmountDiscount(0.2, 40), 1);

            d1.addDiscountToProduct(new ProductAmountDiscount(0.5, 10), 2);
            d1.addDiscountToProduct(new ProductAmountDiscount(0.2, 20), 2);

            d1.addDiscountToProduct(new ProductAmountDiscount(0.13, 100), 4);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
    @AfterAll
    static void cleanUp()
    {
        try {
            d1.removeProduct(1);
            d1.removeProduct(2);
            d1.removeProduct(3);
            d1.removeProduct(4);
            d1.removeProduct(5);
            productDAO.removeProduct(8361);
            productDAO.removeProduct(1008);
            productDAO.removeProduct(4476);
            productDAO.removeProduct(9223);
            productDAO.removeProduct(5610);
            d1.clearDealDiscounts();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }*/

    @Test
    void calculatePriceShouldWork() {
        Map<Integer, Integer> productAmounts1 = new HashMap<>();
        productAmounts1.put(1, 50);
        productAmounts1.put(2, 30);
        productAmounts1.put(4, 200);
        productAmounts1.put(5, 1);

        Map<Integer, Integer> productAmounts2 = new HashMap<>();
        productAmounts2.put(1, 10);
        productAmounts2.put(2, 6);
        productAmounts2.put(3, 3);
        productAmounts2.put(4, 9);
        productAmounts2.put(5, 2);
        boolean exp = false;
        try {
            assertEquals(1162.368, Math.round(d1.calculatePrice(productAmounts1) * 10000000d) / 10000000d);
            assertEquals(353.36, d1.calculatePrice(productAmounts2));
        }
        catch (SQLException e) {
            exp = true;
        }
        assertFalse(exp);
    }

    @Test
    void calculatePriceShouldNotWork() {
        Map<Integer, Integer> productAmounts3 = new HashMap<>();
        productAmounts3.put(1, 50);
        productAmounts3.put(2, 30);
        productAmounts3.put(4, 200);
        productAmounts3.put(5, 50);

        boolean exception = false;
        try
        {
            d1.calculatePrice(productAmounts3);
        }
        catch(Exception e)
        {
            exception = true;
        }
        assertTrue(exception);
    }

    @Test
    void hasEnoughOfProduct() {
        boolean exp = false;
        try {
            assertTrue(d1.hasEnoughOfProduct(8361, 200));
            assertTrue(d1.hasEnoughOfProduct(1008, 399));
            assertTrue(d1.hasEnoughOfProduct(4476, 150));
            assertTrue(d1.hasEnoughOfProduct(9223, 317));

            assertFalse(d1.hasEnoughOfProduct(8361, 301));
            assertFalse(d1.hasEnoughOfProduct(1008, 401));
            assertFalse(d1.hasEnoughOfProduct(4476, 151));
            assertFalse(d1.hasEnoughOfProduct(9223, 901));
            assertFalse(d1.hasEnoughOfProduct(4, 6));
        }
        catch (SQLException e) {
            exp = true;
        }
        assertFalse(exp);
    }

    @Test
    void getArrivalDate() {
        boolean exp = false;
        try {
            Date testDate = new SimpleDateFormat("dd/MM/yyyy").parse("11/5/2023");
            Date expected1 = new SimpleDateFormat("dd/MM/yyyy").parse("14/5/2023");
            Date expected2 = new SimpleDateFormat("dd/MM/yyyy").parse("23/5/2023");

            assertEquals(expected1, d1.getArrivalDate(testDate));
            assertEquals(expected2, d2.getArrivalDate(testDate));
        }
        catch (ParseException e) {
            exp = true;
        }
        assertFalse(exp);
    }
}