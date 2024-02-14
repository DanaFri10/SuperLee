package shared.GUI.warehouseWorker.model;

import com.google.gson.*;
import shared.LocalDateAdapter;
import shared.service.Response;
import shared.service.WarehouseWorkerService;

import java.time.LocalDate;

public class AddInstanceModel {
    String error;
    private Gson gson;
    String username;
    WarehouseWorkerService wws;
    int branchId;


    public AddInstanceModel(String username, int branchId) throws Exception {
        this.wws = WarehouseWorkerService.getService();
        this.gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter().nullSafe()).create();
        error = "";
        this.branchId =branchId;
        this.username =username;
    }


    public void addInstance(String instanceId, String productId,String boughtPrice, String expireDate) throws Exception{
        Response response = gson.fromJson(wws.addInstance(username, branchId, instanceId, productId, expireDate, boughtPrice), Response.class);
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
