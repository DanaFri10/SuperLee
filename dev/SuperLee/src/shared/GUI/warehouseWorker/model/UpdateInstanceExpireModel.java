package shared.GUI.warehouseWorker.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import shared.LocalDateAdapter;
import shared.service.Response;
import shared.service.WarehouseWorkerService;

import java.time.LocalDate;

public class UpdateInstanceExpireModel {
    WarehouseWorkerService wws;
    JsonObject instance;
    String error;
    private Gson gson;
    String username;
    int instanceId;
    int branchId;

    public UpdateInstanceExpireModel(String username, int branchId, int instanceId) throws Exception {
        this.wws = WarehouseWorkerService.getService();
        this.gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter().nullSafe()).create();
        this.username =username;
        this.instanceId = instanceId;
        this.branchId = branchId;
        instance = getInstance(username, branchId, instanceId);
        error = "";

    }

    public void updateInstanceExpire(String expireDate) throws Exception{
        Response response = gson.fromJson(wws.updateExpired(username, branchId, instanceId + "", expireDate), Response.class);
        if(response.isError()){
            throw new Exception("operation failed: " + response.getErrorMessage());
        }
        else{
            //no need to worry the parse will fail because if it isn't int, the response will be error.
            instance= getInstance(username, branchId, instanceId);
            error = "";
        }
    }

    public JsonObject getInstance(String username, int branchId, int instanceId) throws Exception{
        Response response = gson.fromJson(wws.getInstance(username, branchId, instanceId + ""), Response.class);
        if(response.isError()){
            throw new Exception("operation failed: " + response.getErrorMessage());
        }
        else{
            return gson.fromJson(response.getData(), JsonObject.class);
        }
    }



    public String getExpireDate(){
        if(!instance.has("expireDate")){
            return "-";
        }
        return instance.get("expireDate").getAsString();
    }


    public String getError() { return error;}
}
