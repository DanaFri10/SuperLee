package stocks;

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import shared.Database;
import stock.business.*;
import suppliers.business.SuppliersController;

import java.sql.SQLException;
import java.time.LocalDate;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StockTests {

    private StockController sc;
    private static Gson gson = new Gson();


    @BeforeAll
    void setUp() throws Exception {
        Database.setPath("../SuperLeeTestDB.db");
    }

    @BeforeEach
    void setEach() throws Exception {
        Database.clearDatabase();
        sc = new StockController();
        sc.setSuppliersController(new SuppliersController(sc,sc.getpDao(),sc.getbDAO()));
        sc.addProduct("6","Milk3%", "Tnuva" );
        sc.addProduct("7","Milk5%", "Tnuva" );
        sc.addProduct("8","Meat", "Red Butcher" );

        String product1CategoryPath = "Dairy products/Milk/3%";
        sc.submitBranchProduct(1, "6", "5", product1CategoryPath, "15", "fridge", "5");
        sc.submitInstances(1, "1 2 3 4", "5", "6","2003-05-20");

        String product1CategoryPath2 = "Dairy products/Milk/5%";
        sc.submitBranchProduct(1, "7", "5", product1CategoryPath2, "15", "fridge", "5");
        sc.submitInstances(1, "5 6 7", "5", "7","2003-05-20");

        String product1CategoryPath3 = "Meat";
        sc.submitBranchProduct(1, "8", "45", product1CategoryPath3, "25", "fridge", "10");


        //expiered
        //sc.submitInstances(1, "1 2 3", 4.0, "6", "2003-04-9");
        //defective
        //sc.reportDefective(1,"3", "too ugly for humans to consume");

    }

    @AfterEach
    void cleanUp() throws Exception {
        Database.clearDatabase();
    }

    @Test
    void test350mlg_Shampoo() throws Exception {
        sc.addProduct("10","Water", "Mey Eden" );
        String product4CategoryPath = "Water/350mlg";
        sc.submitBranchProduct(1, "10", "6.2", product4CategoryPath, "15", "stock", "5");
        sc.submitInstances(1, "8 9 10 11", "4.5", "10","2003-05-20");

        sc.addProduct("11","ShampooFun", "Hawaii" );
        String product5CategoryPath = "Shampoo/350mlg";
        sc.submitBranchProduct(1, "11", "6.3", product5CategoryPath, "16", "stock", "5");
        sc.submitInstances(1, "12 13 14 15", "4.5", "11","2003-05-20");



        sc.removeProduct(""+10);
        sc.removeProduct(""+11);
    }

    @Test
    void updateInfoProduct() throws Exception {
        Branch branch = sc.getBranch("1");
        String product1CategoryPath = "Dairy products/Milk/3%";
        Assert.assertEquals("fridge",sc.getBranchProduct(1, 6).getplaceInStore());
        sc.updateProductInfo(1, "6", "6.2", product1CategoryPath, "20", "area 5", "5");
        Assert.assertEquals("area 5",sc.getBranchProduct(1, 6).getplaceInStore());
        sc.updateProductInfo(1, "6", "6.2", product1CategoryPath, "20", "fridge", "5");
    }

    @Test
    void deleteBranchInstance() throws Exception {
        sc.submitInstances(1, "16", "4.5", "8","2003-05-20");

        sc.submitMissingInstance(1, "16");
        Assert.assertEquals(null, sc.getBranch("1").getInstance(16));
    }
    @Test
    void insertAndGetBranchProduct() throws Exception {
        String product1CategoryPath = "Dairy products/Cheese";
        sc.addProduct("12","Cheese", "YellowIndustries" );
        sc.submitBranchProduct(1, "12", "8", product1CategoryPath, "15", "stock", "5");
        BranchProduct cheese = sc.getBranch("1").getProduct(12);
        Assert.assertNotNull(cheese);
        sc.submitInstances(1, "17 18 19", "5", "12","2003-05-30");
        sc.changeLocationOfInstance(1, "17", "store");
        Assert.assertEquals(2, sc.getBranch("1").getAmountInStock(12));
        Assert.assertEquals(1, sc.getBranch("1").getAmountInStore(12));

        sc.removeBranchInstance(1, 17);
        sc.removeBranchInstance(1, 18);
        sc.removeBranchInstance(1, 19);

        sc.removeBranchProduct(1, 12);
        sc.removeProduct("12");

    }
    @Test
    void discountNewPrice() throws Exception {
        sc.getBranch("1").deleteAllDiscounts();
        Assert.assertEquals(5.0, sc.getBranch("1").getFinalSellingPrice(7), 0);
        Assert.assertEquals(5.0, sc.getBranch("1").getFinalSellingPrice(6), 0);
        LocalDate startDiscount1 = LocalDate.of(2023,5,5);
        LocalDate endDiscount1 = LocalDate.of(2023,6,30);
        BranchDiscount discount1 = new BranchDiscount(startDiscount1, endDiscount1, 0.1);
        sc.createSellingDiscount(1, String.valueOf(discount1.getDiscountNumericValue()),"1",startDiscount1.toString(),endDiscount1.toString(),"Dairy products/Milk");
        LocalDate startDiscount2 = LocalDate.of(2023,5,5);
        LocalDate endDiscount2 = LocalDate.of(2023,6,30);
        BranchDiscount discount2 = new BranchDiscount(startDiscount2, endDiscount2, 0.2);
        sc.createSellingDiscount(1, String.valueOf(discount2.getDiscountNumericValue()),"2",startDiscount2.toString(),endDiscount2.toString(),"7");

        Assert.assertEquals(4.0, sc.getBranch("1").getFinalSellingPrice(7), 0);
        Assert.assertEquals(4.5, sc.getBranch("1").getFinalSellingPrice(6), 0);

        sc.getBranch("1").deleteAllDiscounts();

    }

    @Test
    void defectiveTest() throws Exception {
        sc.submitInstances(1, "18 19 20", "1", "6","2023-05-30");
        sc.reportDefective(1, "19", "this milk is no good, it was made by the devil himself");
        Assert.assertEquals(19, sc.getBranch("1").getDefectives().get(0).getInstanceId());

        sc.removeBranchInstance(1, 18);
        sc.removeBranchInstance(1, 19);
        sc.removeBranchInstance(1, 20);
    }

    @Test
    void checkAbleChangeExpireDate() throws Exception {
        sc.submitInstances(1, "18 19 20", "1", "6","2023-05-30");
        sc.updateInstanceExpiredDate(1,"18","2023-02-02");
        Assert.assertEquals("2023-02-02", sc.getBranch("1").getInstance(18).getExpireDate().toString());
    }
}
