package shared.GUI.warehouseWorker.model;

import com.google.gson.*;
import shared.LocalDateAdapter;
import shared.service.Response;
import shared.service.WarehouseWorkerService;

import java.time.LocalDate;

public class AddCategoryDiscountModel {

    String error;
    private Gson gson;
    String username;
    WarehouseWorkerService wws;
    int branchId;
    String path;

    public AddCategoryDiscountModel(String username, String path, int branchId) throws Exception {
        this.wws = WarehouseWorkerService.getService();
        this.gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter().nullSafe()).create();
        error = "";
        this.branchId =branchId;
        this.path = path;
        this.username =username;
    }


    public void addDiscount(String from, String to, String value) throws Exception{
        Response response = gson.fromJson(wws.addDiscountCategory(username, branchId, value, from, to, path), Response.class);
        if(response.isError()){
            throw new Exception("operation failed: " + response.getErrorMessage());
        }
        else{
            //no need to worry the parse will fail because if it isn't int, the response will be error.
            error = "operation succeeded";
        }
    }



    public String getError() { return error;}
}
