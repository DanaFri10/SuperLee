package shared.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import shared.ControllerFactory;
import stock.business.StockController;
import stock.business.UserRole;
import suppliers.business.SuppliersController;

import java.lang.reflect.Type;
import java.util.Map;

public class StoreManagerService extends AuthenticatedService {
    private StockController stockController;
    private SuppliersController suppliersController;
    private Gson gson;
    static StoreManagerService service = null;
    private StoreManagerService(StockController stockController, SuppliersController suppliersController){
        super(stockController);
        this.stockController = stockController;
        this.suppliersController = suppliersController;
        this.gson = new Gson();
    }

    public static StoreManagerService getService() throws Exception {
        if(service == null){
            service = new StoreManagerService(ControllerFactory.getStc(), ControllerFactory.getSpc());
        }
        return service;
    }

    //USE THIS FUNCTION AT THE START OF EVERY FUNCTION (THIS IS VERY IMPORTANT)
    @Override
    protected void authenticate(String username) throws Exception {
        if(stockController.getRole(username) != UserRole.StoreManager){
            throw new UnsupportedOperationException("Only Store Managers are allowed to use these functions");
        }
    }

    public String createShortageReport(String username, int branchId){
        try {
            authenticate(username);
            Type intToint = new TypeToken<Map<Integer, Integer>>() {}.getType();
            Map<Integer, Integer> mapShortage = gson.fromJson(stockController.getShortage(branchId), intToint);
            return gson.toJson(new Response(stockController.createShortageReport(branchId, mapShortage), false, null));
        } catch (Exception e) {
            return gson.toJson(new Response(null, true, e.getMessage()));
        }
    }

    public String getOrdersHistory(String username, int branchId) {
        try {
            authenticate(username);
            return gson.toJson(new Response(suppliersController.branchOrderHistory(branchId), false, null));
        } catch (Exception e) {
            return gson.toJson(new Response(null, true, e.getMessage()));
        }
    }

    public String updateTimeStep(String username, int branchId, String _timeStep) {
        try {
            authenticate(username);
            stockController.updateTimeStep(branchId, _timeStep);
            return gson.toJson(new Response("time step has been updated successfully", false, null));
        } catch (Exception e) {
            return gson.toJson(new Response(null, true, e.getMessage()));
        }
    }
    public String createDefectivesReportOnDemand(String username, int branchId) {
        try {
            authenticate(username);
            return gson.toJson(new Response(stockController.createDefectivesReportOnDemand(branchId), false, null));
        } catch (Exception e) {
            return gson.toJson(new Response(null, true, e.getMessage()));
        }
    }

    public boolean authenticateUser(String username) {
        try {
            authenticate(username);
            return true; // Authentication successful
        } catch (Exception e) {
            return false; // Authentication failed
        }
    }



    //TODO ask eyal about how to implement the product explorer





}
