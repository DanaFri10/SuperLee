package suppliers.business;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shared.Database;
import stock.business.Product;
import stock.dal.ProductDAO;
import suppliers.dal.SuppliersDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SupplierTest {

    private static SuppliersDAO sc;
    private static List<String> supplyFields1;
    private static List<String> supplyFields2;
    private static Supplier s1;
    private static Supplier s2;

    @BeforeAll
    static void prepareBeforeAll(){
        try {
            Database.setPath("../SuperLeeTestDB.db");
            Database.clearDatabase();

            supplyFields1 = new ArrayList<>();
            supplyFields2 = new ArrayList<>();
            supplyFields1.add("Cleaning supplies");
            supplyFields1.add("Dairy");
            supplyFields2.add("");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @BeforeEach
    void prepareBeforeEach()
    {
        try {
            s1 = new Supplier("878245463", "ShapiroLtd", "Rishon Lezion", "452-87034/33", supplyFields1);
            s1.addDeal(false, 5, new boolean[]{false, false, false, false, false, false, false}, PaymentMethod.ongoing);
            s2 = new Supplier("765091323", "AharonySupplies", "Rehovot", "143-82749/29", supplyFields1);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void hasDeal()
    {
        assertTrue(s1.hasDeal());
        assertFalse(s2.hasDeal());
    }

    @Test
    void getDealShouldWork() {
        boolean exp = false;
        try {
            Deal testDeal = new Deal(false, 5, new boolean[]{false, false, false, false, false, false, false}, PaymentMethod.ongoing, s1);
            assertEquals(s1.getDeal(), testDeal);
        }
        catch (SQLException e)
        {
            exp = true;
        }
        assertFalse(exp);
    }

    @Test
    void getDealShouldNotWork(){
        boolean exception = false;

        try
        {
            s2.getDeal();
        }
        catch(Exception e)
        {
            exception = true;
        }
        assertTrue(exception);
    }

    @Test
    void addDealShouldWork(){
        boolean exp = false;
        try {
            s2.addDeal(true,  8, new boolean[]{false, true, false, false, true, false, false},PaymentMethod.net30EOM);
        }
        catch (SQLException e)
        {
            exp = true;
        }
        assertFalse(exp);
    }

    @Test
    void addDealShouldNotWork(){
        boolean exception = false;

        try
        {
            s1.addDeal(true,  8, new boolean[]{false, true, false, false, true, false, false}, PaymentMethod.ongoing);
        }
        catch(Exception e)
        {
            exception = true;
        }
        assertTrue(exception);
    }

    @Test
    void isSupplierContact() {
        s1.addSupplierContact("762910243");
        s1.addSupplierContact("132093544");
        s1.addSupplierContact("187454209");

        assertTrue(s1.isContact("762910243"));
        assertTrue(s1.isContact("132093544"));
        assertTrue(s1.isContact("187454209"));
        assertFalse(s1.isContact("451234576"));
        assertFalse(s1.isContact("109234212"));
        assertFalse(s1.isContact("987109265"));

    }

    @Test
    void addSupplierContact()
    {
        s1.addSupplierContact("762910243");
        s1.addSupplierContact("132093544");
        s1.addSupplierContact("187454209");

        List<String> expectedContacts = new ArrayList<>();
        expectedContacts.add("762910243");
        expectedContacts.add("132093544");
        expectedContacts.add("187454209");

        assertEquals(expectedContacts, s1.getSupplierContacts());
    }

    @Test
    void removeSupplierContact()
    {
        s1.addSupplierContact("762910243");
        s1.addSupplierContact("132093544");
        s1.addSupplierContact("187454209");
        s2.addSupplierContact("762910243");
        s2.addSupplierContact("132093544");
        s2.addSupplierContact("187454209");

        List<String> expectedContacts1 = new ArrayList<>();
        expectedContacts1.add("132093544");
        expectedContacts1.add("187454209");

        List<String> expectedContacts2 = new ArrayList<>();
        expectedContacts2.add("187454209");

        List<String> expectedContacts3 = new ArrayList<>();

        List<String> expectedContacts4 = new ArrayList<>();
        expectedContacts4.add("762910243");
        expectedContacts4.add("132093544");
        expectedContacts4.add("187454209");

        s1.removeSupplierContact("762910243");
        assertEquals(expectedContacts1, s1.getSupplierContacts());

        s1.removeSupplierContact("132093544");
        assertEquals(expectedContacts2, s1.getSupplierContacts());

        s1.removeSupplierContact("187454209");
        assertEquals(expectedContacts3, s1.getSupplierContacts());

        s2.removeSupplierContact("501298733");
        s2.removeSupplierContact("902314234");
        assertEquals(expectedContacts4, s2.getSupplierContacts());
    }

    @Test
    void supplyAreaExists()
    {
        assertTrue(s1.supplyAreaExists("Cleaning supplies"));
        assertTrue(s1.supplyAreaExists("Dairy"));
        assertTrue(s2.supplyAreaExists("Cleaning supplies"));
        assertTrue(s2.supplyAreaExists("Dairy"));

        assertFalse(s1.supplyAreaExists("Electronics"));
        assertFalse(s1.supplyAreaExists("Clothing"));
        assertFalse(s2.supplyAreaExists("Desserts"));
        assertFalse(s2.supplyAreaExists("Sauces"));
    }

    @Test
    void getCompanyId() {
        assertEquals("878245463", s1.getCompanyId());
        assertEquals("765091323", s2.getCompanyId());
    }

    @Test
    void getCompanyName() {
        assertEquals("ShapiroLtd", s1.getCompanyName());
        assertEquals("AharonySupplies", s2.getCompanyName());
    }

    @Test
    void getLocation() {
        assertEquals("Rishon Lezion", s1.getLocation());
        assertEquals("Rehovot", s2.getLocation());
    }

    @Test
    void getBankAccount() {
        assertEquals("452-87034/33", s1.getBankAccount());
        assertEquals("143-82749/29", s2.getBankAccount());
    }



    @Test
    void getSupplierContacts() {
        s1.addSupplierContact("345287654");
        s1.addSupplierContact("109876254");
        s2.addSupplierContact("345287654");
        s2.addSupplierContact("109876254");
        List<String> expected = new ArrayList<>();
        expected.add("345287654");
        expected.add("109876254");

        assertEquals(expected, s1.getSupplierContacts());
        assertEquals(expected, s2.getSupplierContacts());
    }

    @Test
    void getSupplyAreas() {
        assertEquals(supplyFields1, s1.getSupplyAreas());
        assertEquals(supplyFields1, s2.getSupplyAreas());
    }

}