package shared.GUI.warehouseWorker.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import shared.service.Response;
import shared.service.WarehouseWorkerService;

public class UpdateInstanceLocationModel {
    WarehouseWorkerService wws;
    JsonObject instance;
    String error;
    private Gson gson;
    String username;
    int instanceId;
    int branchId;

    public UpdateInstanceLocationModel(String username, int branchId, int instanceId) throws Exception {
        this.wws = WarehouseWorkerService.getService();
        this.gson = new Gson();
        this.username =username;
        this.instanceId = instanceId;
        this.branchId = branchId;
        instance = getInstance(username, branchId, instanceId);
        error = "";

    }

    public void updateInstanceLocation(String location) throws Exception{
        Response response = gson.fromJson(wws.updateInstanceLocation(username, branchId, instanceId + "", location), Response.class);
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



    public String getLocation(){
        if(instance.get("placeOfProduct").getAsString().equals("InStock")){
            return "instance is in stock";
        }
        return "instance is in store";
    }


    public String getError() { return error;}
}
