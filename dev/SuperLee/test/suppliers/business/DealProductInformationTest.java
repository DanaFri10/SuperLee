package suppliers.business;

import org.junit.jupiter.api.*;
import shared.Database;
import stock.business.Product;
import stock.dal.ProductDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DealProductInformationTest {
    private DealProductInformation dpi;
    private ProductDAO productsDAO;

    @BeforeEach
    void setUp() {
        try {
            Database.setPath("../SuperLeeTestDB.db");
            Database.clearDatabase();

            productsDAO = new ProductDAO();
            productsDAO.insertProduct(new Product(9173, "Ketchup", "Heinz"));

            dpi = new DealProductInformation(621, 9173, 10, 300, "999999999");
            dpi.addProductAmountDiscount(new ProductAmountDiscount(0.1, 10));
            dpi.addProductAmountDiscount(new ProductAmountDiscount(0.2, 20));
            dpi.addProductAmountDiscount(new ProductAmountDiscount(0.15, 30));
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
    @AfterEach
    void cleanUp()
    {
        try {
            dpi.clearDiscounts();
            productsDAO.removeProduct(9173);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }*/

    @Test
    void calculatePriceShouldWork() {
        boolean exp = false;
        try {
            assertEquals(50, dpi.calculatePrice(5));
            assertEquals(90, dpi.calculatePrice(10));
            assertEquals(135, dpi.calculatePrice(15));
            assertEquals(160, dpi.calculatePrice(20));
            assertEquals(200, dpi.calculatePrice(25));
            assertEquals(240, dpi.calculatePrice(30));
            assertEquals(280, dpi.calculatePrice(35));
            assertEquals(2400, dpi.calculatePrice(300));
        }
        catch(SQLException e)
        {
            exp = true;
        }
        assertFalse(exp);
    }

    @Test
    void calculatePriceShouldNotWork() {
        boolean exception = false;
        try
        {
            dpi.calculatePrice(301);
        }
        catch (Exception e)
        {
            exception = true;
            assertEquals("The supplier can not supply this amount.", e.getMessage());
        }
        assertTrue(exception);
    }

    @Test
    void discountExists() {
        boolean exp = false;
        try {
            assertTrue(dpi.discountExists(new ProductAmountDiscount(0.1, 10)));
            assertTrue(dpi.discountExists(new ProductAmountDiscount(0.2, 20)));
            assertTrue(dpi.discountExists(new ProductAmountDiscount(0.15, 30)));

            assertFalse(dpi.discountExists(new ProductAmountDiscount(0.09, 10)));
            assertFalse(dpi.discountExists(new ProductAmountDiscount(0.1, 11)));
            assertFalse(dpi.discountExists(new ProductAmountDiscount(0.2, 7)));
        }
        catch(SQLException e)
        {
            exp = true;
        }
        assertFalse(exp);
    }

    @Test
    void addDiscountShouldWork() {
        List<ProductDiscount> expected = new ArrayList<>();
        expected.add(new ProductAmountDiscount(0.1, 10));
        expected.add(new ProductAmountDiscount(0.2, 20));
        expected.add(new ProductAmountDiscount(0.15, 30));
        expected.add(new ProductAmountDiscount(0.07, 20));
        expected.add(new ProductAmountDiscount(0.12, 30));

        boolean exp = false;
        try {
            dpi.addProductAmountDiscount(new ProductAmountDiscount(0.07,20));
            dpi.addProductAmountDiscount(new ProductAmountDiscount(0.12, 30));
            assertEquals(expected, dpi.getDiscounts());
        }
        catch(SQLException e)
        {
            exp = true;
        }
        assertFalse(exp);
    }

    @Test
    void addDiscountShouldNotWork() {
        boolean exception = false;
        try {
            dpi.addProductAmountDiscount(new ProductAmountDiscount(0.1, 10));
        }
        catch (Exception e)
        {
            exception = true;
        }
        assertTrue(exception);

    }
}