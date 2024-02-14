package suppliers.business;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import shared.ControllerFactory;
import shared.Database;
import stock.business.Branch;
import stock.business.StockController;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class IntegrationTests {
    private StockController stc;
    private SuppliersController spc;
    private Gson gson = new Gson();
    @Before
    public void setUp() throws Exception {
        Database.setPath("../SuperLeeTestDB.db");
        ControllerFactory.resetControllers();
        stc = ControllerFactory.getStc();
        spc = ControllerFactory.getSpc();
        spc.addSupplier("000000000","Test","Test","111-11111/11",PaymentMethod.net30EOM,new ArrayList<>(),true,0,new boolean[7]);
        spc.addSupplier("111111111","Slower","Test","121-11111/11",PaymentMethod.net30EOM,new ArrayList<>(),true,10,new boolean[7]);
        stc.addProduct("0", "Test", "Test");
        spc.addProductToDeal("000000000",0,0,10.0,100);
        spc.addProductToDeal("111111111",0,0,10.0,100);
        stc.submitBranchProduct(0, "0", "10.0", "", "10", "", "5");
        stc.submitInstances(0,"0 1 2","5.0","0",LocalDate.now().toString());
    }

    @Test
    public void shortageToOrderTest() throws SQLException {
        Map<Integer, Integer> extras = new HashMap<>();
        extras.put(0,5);
        String mapJson = stc.getShortage(0);
        Type mapType = new TypeToken<Map<Integer,Integer>>(){}.getType();
        Map<Integer, Integer> shortageMap = gson.fromJson(mapJson, mapType);
        String orderJson = spc.makeShortageOrder(shortageMap,extras,0);
        Order order = gson.fromJson(orderJson, Order.class);
        String orderMapJson = spc.getOrderProducts(order.getOrderId());
        Type orderMapType = new TypeToken<Map<String,Map<Integer,Integer>>>(){}.getType();
        Map<String,Map<Integer, Integer>> orderMap = gson.fromJson(orderMapJson, orderMapType);
        assertTrue(orderMap.containsKey("000000000"));
        assertNotNull(orderMap.get("000000000"));
        assertEquals(7,orderMap.get("000000000").get(0));
    }

    @Test
    public void orderNotYetArrivedTest() throws Exception {
        Map<String, Map<Integer, Integer>> orderMap = new HashMap<>();
        orderMap.put("111111111", new HashMap<>()); //supplier 111111111 does not have same day delivery
        orderMap.get("111111111").put(0,5);
        spc.createCustomOrder(orderMap,0);
        int amountBefore = stc.getAmountOfProduct(0,0);
        spc.refillStock(stc);
        assertEquals(amountBefore,stc.getAmountOfProduct(0,0)); //the order did not yet arrive so the amount shouldn't have changed
    }

    @Test
    public void orderArrivalTest() throws Exception {
        Map<String, Map<Integer, Integer>> orderMap = new HashMap<>();
        orderMap.put("000000000", new HashMap<>()); //supplier 000000000 has same day delivery
        orderMap.get("000000000").put(0,5);
        spc.createCustomOrder(orderMap,0);
        int amountBefore = stc.getAmountOfProduct(0,0);
        spc.refillStock(stc);
        assertEquals(amountBefore+5,stc.getAmountOfProduct(0,0));
    }

    @Test
    public void orderAlreadyArrivedTest() throws Exception {
        Map<String, Map<Integer, Integer>> orderMap = new HashMap<>();
        orderMap.put("000000000", new HashMap<>()); //supplier 000000000 has same day delivery
        orderMap.get("000000000").put(0,5);
        spc.createCustomOrder(orderMap,0);
        int amountBefore = stc.getAmountOfProduct(0,0);
        spc.refillStock(stc);
        assertEquals(amountBefore+5,stc.getAmountOfProduct(0,0));
        spc.refillStock(stc);
        assertEquals(amountBefore+5,stc.getAmountOfProduct(0,0)); //order shouldn't be added twice
    }

    @After
    public void cleanUp() throws Exception {
        Database.clearDatabase();
    }

}
