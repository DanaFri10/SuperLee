package suppliers.business;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shared.Database;
import stock.dal.BranchDAO;
import stock.dal.ProductDAO;
import suppliers.dal.ContactsDAO;
import suppliers.dal.SuppliersDAO;

import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class SuppliersDAOTest {
    private SuppliersDAO suppliersDAO;

    @BeforeEach
    void setUp()
    {
        List<String> supplyAreas1 = new ArrayList<>();
        supplyAreas1.add("SupplyArea1");
        supplyAreas1.add("SupplyArea2");
        supplyAreas1.add("SupplyArea3");

        List<String> supplyAreas2 = new ArrayList<>();
        supplyAreas2.add("SupplyArea1");
        supplyAreas2.add("SupplyArea4");

        List<String> supplyAreas3 = new ArrayList<>();
        supplyAreas3.add("SupplyArea2");
        supplyAreas3.add("SupplyArea3");
        supplyAreas3.add("SupplyArea5");

        try {
            Database.setPath("../SuperLeeTestDB.db");
            Database.clearDatabase();

            suppliersDAO = new SuppliersDAO(new ContactsDAO());
            
            suppliersDAO.addSupplier("967354201", "Supplier1", "Location1", "123-12345/11", supplyAreas1, PaymentMethod.ongoing, false, 0, new boolean[7]);
            suppliersDAO.addSupplier("183029846", "Supplier2", "Location2", "123-12345/22", supplyAreas2, PaymentMethod.ongoing, false, 0, new boolean[7]);
            suppliersDAO.addSupplier("562019867", "Supplier3", "Location3", "123-12345/33", supplyAreas3, PaymentMethod.ongoing, false, 0, new boolean[7]);
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
            suppliersDAO.removeSupplier("967354201");
            suppliersDAO.removeSupplier("183029846");
            suppliersDAO.removeSupplier("562019867");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }*/

    @Test
    void filterBySupplyArea() {
        List<String> expected1 = new ArrayList<>();
        expected1.add("183029846: Supplier2");
        expected1.add("967354201: Supplier1");

        List<String> expected2 = new ArrayList<>();
        expected2.add("562019867: Supplier3");
        expected2.add("967354201: Supplier1");

        List<String> expected3 = new ArrayList<>();
        expected3.add("183029846: Supplier2");

        List<String> expected4 = new ArrayList<>();
        expected4.add("562019867: Supplier3");

        List<String> expected5 = new ArrayList<>();

        boolean exp = false;
        try {
            List<String> res1 = suppliersDAO.filterBySupplyArea("SupplyArea1");
            List<String> res2 = suppliersDAO.filterBySupplyArea("SupplyArea2");
            List<String> res3 = suppliersDAO.filterBySupplyArea("SupplyArea3");
            List<String> res4 = suppliersDAO.filterBySupplyArea("SupplyArea4");
            List<String> res5 = suppliersDAO.filterBySupplyArea("SupplyArea5");
            List<String> res6 = suppliersDAO.filterBySupplyArea("SupplyArea6");
            List<String> res7 = suppliersDAO.filterBySupplyArea("SupplyArea7");

            Collections.sort(res1);
            Collections.sort(res2);
            Collections.sort(res3);
            Collections.sort(res4);
            Collections.sort(res5);
            Collections.sort(res6);
            Collections.sort(res7);

            assertEquals(expected1, res1);
            assertEquals(expected2, res2);
            assertEquals(expected2, res3);
            assertEquals(expected3, res4);
            assertEquals(expected4, res5);
            assertEquals(expected5, res6);
            assertEquals(expected5, res7);
        }
        catch (SQLException e) {
            exp = true;
        }
        assertFalse(exp);
    }

    /*
    @Test
    void minPriceSupplier() {
        boolean exp = false;
        try {
            Supplier s1 = suppliersDAO.getSupplier("967354201");
            Supplier s2 = suppliersDAO.getSupplier("183029846");
            Supplier s3 = suppliersDAO.getSupplier("562019867");

            s1.addDeal(true,  5, new boolean[]{false, false, false, true, false, false, false},PaymentMethod.ongoing);
            s2.addDeal(true,  5, new boolean[]{false, false, false, true, false, false, false},PaymentMethod.net30EOM);
            s3.addDeal(true,  5, new boolean[]{false, false, false, true, false, false, false},PaymentMethod.net60EOM);

            s1.getDeal().addProduct(13, 0, 1, 100);
            s1.getDeal().addProduct(53, 1, 1, 100);
            s1.getDeal().addProduct(9, 2, 1, 100);

            s2.getDeal().addProduct(44, 0, 2, 200);
            s2.getDeal().addProduct(81, 1, 2, 200);
            s2.getDeal().addProduct(2, 2, 2, 200);

            s3.getDeal().addProduct(0, 0, 3, 300);
            s3.getDeal().addProduct(22, 1, 3, 300);
            s3.getDeal().addProduct(1, 2, 3, 300);

            Map<Integer, Integer> productAmount1 = new HashMap<>();
            productAmount1.put(0, 50);
            productAmount1.put(1, 50);
            productAmount1.put(2, 50);

            Map<Integer, Integer> productAmount2 = new HashMap<>();
            productAmount2.put(0, 150);
            productAmount2.put(1, 150);
            productAmount2.put(2, 150);

            Map<Integer, Integer> productAmount3 = new HashMap<>();
            productAmount3.put(0, 250);
            productAmount3.put(1, 250);
            productAmount3.put(2, 250);

            Map<Integer, Integer> productAmount4 = new HashMap<>();
            productAmount4.put(0, 350);
            productAmount4.put(1, 350);
            productAmount4.put(2, 350);

            Map<Integer, Integer> productAmount5 = new HashMap<>();
            productAmount5.put(0, 50);
            productAmount5.put(1, 200);
            productAmount5.put(2, 200);

            assertEquals(s1, suppliersDAO.minPriceSupplier(productAmount1));
            assertEquals(s2, suppliersDAO.minPriceSupplier(productAmount2));
            assertEquals(s3, suppliersDAO.minPriceSupplier(productAmount3));
            assertEquals(null, suppliersDAO.minPriceSupplier(productAmount4));
            assertEquals(s2, suppliersDAO.minPriceSupplier(productAmount5));
        }
        catch (SQLException e) {
            exp = true;
        }
        assertFalse(exp);

    }

    @Test
    void getOptimalOrder() {
        boolean exp = false;
        try {
            Supplier s1 = suppliersDAO.getSupplier("967354201");
            Supplier s2 = suppliersDAO.getSupplier("183029846");
            Supplier s3 = suppliersDAO.getSupplier("562019867");

            s1.addDeal(true,  5, new boolean[]{false, false, false, true, false, false, false},PaymentMethod.ongoing);
            s2.addDeal(true,  5, new boolean[]{false, false, false, true, false, false, false},PaymentMethod.net30EOM);
            s3.addDeal(true,  5, new boolean[]{false, false, false, true, false, false, false},PaymentMethod.net60EOM);

            s1.getDeal().addProduct(13, 0, 10, 100);
            s1.getDeal().addProduct(53, 1, 25, 200);
            s1.getDeal().addProduct(9, 2, 17, 300);

            s2.getDeal().addProduct(44, 0, 20, 300);
            s2.getDeal().addProduct(81, 1, 23, 100);
            s2.getDeal().addProduct(2, 2, 20, 200);

            s3.getDeal().addProduct(0, 0, 15, 200);
            s3.getDeal().addProduct(22, 1, 19, 300);
            s3.getDeal().addProduct(1, 2, 24, 100);

            s1.getDeal().addDiscountToProduct(new ProductAmountDiscount(0.1, 70), 13);
            s1.getDeal().addDiscountToProduct(new ProductAmountDiscount(0.8, 140), 13);
            s1.getDeal().addDiscountToProduct(new ProductAmountDiscount(0.05, 50), 53);
            s1.getDeal().addDiscountToProduct(new ProductAmountDiscount(0.09, 60), 9);
            s1.getDeal().addTotalProductsDiscount(new TotalProductsDiscount(0.03, 100));

            s2.getDeal().addDisuppliersDAOountToProduct(new ProductAmountDisuppliersDAOount(0.40, 150), 44);
            s2.getDeal().addDisuppliersDAOountToProduct(new ProductAmountDisuppliersDAOount(0.20, 80), 81);
            s2.getDeal().addDisuppliersDAOountToProduct(new ProductAmountDisuppliersDAOount(0.10, 100), 2);
            s2.getDeal().addDisuppliersDAOountToProduct(new ProductAmountDisuppliersDAOount(0.12, 120), 2);
            s2.getDeal().addDealDisuppliersDAOount(new TotalPriceDisuppliersDAOount(0.17, 10000));

            s3.getDeal().addDisuppliersDAOountToProduct(new ProductAmountDisuppliersDAOount(0.14, 99), 0);
            s3.getDeal().addDisuppliersDAOountToProduct(new ProductAmountDisuppliersDAOount(0.25, 123), 1);
            s3.getDeal().addDealDisuppliersDAOount(new TotalProductsDisuppliersDAOount(0.15, 400));
            s3.getDeal().addDealDisuppliersDAOount(new TotalPriceDisuppliersDAOount(0.11, 9000));

            Map<Integer, Integer> productAmount1 = new HashMap<>();
            productAmount1.put(0, 50);
            productAmount1.put(1, 50);
            productAmount1.put(2, 50);
            Map<Deal, Map<Integer, Integer>> optimal1 = new HashMap<>();
            Map<Integer, Integer> optimal11 = new HashMap<>();
            optimal11.put(13, 50);
            optimal11.put(53, 50);
            optimal11.put(9, 50);
            optimal1.put(s1.getDeal(), optimal11);
            assertEquals(optimal1, suppliersDAO.getOptimalOrder(productAmount1));

            Map<Integer, Integer> productAmount2 = new HashMap<>();
            productAmount2.put(0, 90);
            productAmount2.put(1, 250);
            productAmount2.put(2, 150);
            Map<Deal, Map<Integer, Integer>> optimal2 = new HashMap<>();
            Map<Integer, Integer> optimal21 = new HashMap<>();
            optimal21.put(13, 90);
            optimal21.put(9, 150);
            Map<Integer, Integer> optimal22 = new HashMap<>();
            optimal22.put(22, 250);
            optimal2.put(s1.getDeal(), optimal21);
            optimal2.put(s3.getDeal(), optimal22);
            assertEquals(optimal2, suppliersDAO.getOptimalOrder(productAmount2));
        }
        catch (SQLException e) {
            exp = true;
        }
        assertFalse(exp);
    }*/
}
