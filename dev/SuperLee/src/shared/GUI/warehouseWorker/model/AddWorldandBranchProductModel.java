package shared.GUI.warehouseWorker.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import shared.LocalDateAdapter;
import shared.service.Response;
import shared.service.WarehouseWorkerService;

import java.time.LocalDate;

public class AddWorldandBranchProductModel {
    String error;
    private Gson gson;
    String username;
    WarehouseWorkerService wws;
    int branchId;
    String path;

    public AddWorldandBranchProductModel(String username, String path, int branchId) throws Exception {
        this.wws = WarehouseWorkerService.getService();
        this.gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter().nullSafe()).create();
        error = "";
        this.branchId =branchId;
        this.path = path;
        this.username =username;
    }


    public void addProductWorldAndBranch(String id, String name, String manufacturer, String priceSold, String demand, String minimum, String placeInStore) throws Exception{
        Response response = gson.fromJson(wws.addWorldAndBranchProduct(username, branchId, id, name, manufacturer, priceSold, demand, minimum, placeInStore, path), Response.class);
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
