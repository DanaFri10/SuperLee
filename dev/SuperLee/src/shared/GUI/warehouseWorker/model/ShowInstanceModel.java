package shared.GUI.warehouseWorker.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import shared.LocalDateAdapter;
import shared.service.Response;
import shared.service.WarehouseWorkerService;

import java.time.LocalDate;

public class ShowInstanceModel {
    WarehouseWorkerService wws;
    String error;
    private Gson gson;
    String username;
    int branchId;
    JsonObject instance;

    public ShowInstanceModel(String username, int instanceId, int branchId) throws Exception {
        this.wws = WarehouseWorkerService.getService();
        this.gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter().nullSafe()).create();
        error = "";
        this.username = username;
        this.branchId = branchId;
        instance = getInstance(instanceId + "");
    }

    public JsonObject getInstance(String instanceId) throws Exception{
        Response response = gson.fromJson(wws.getInstance(username, branchId, instanceId), Response.class);
        if(response.isError()){
            throw new Exception("operation failed: " + response.getErrorMessage());
        }
        else{
            return gson.fromJson(response.getData(), JsonObject.class);
        }
    }

    public void deleteInstance() throws Exception{
        Response response = gson.fromJson(wws.deleteInstance(username, branchId, instance.get("instanceId").getAsString()), Response.class);
        if(response.isError()){
            throw new Exception("operation failed: " + response.getErrorMessage());
        }
        else{
            error = "success";
        }
    }

    public String getInstanceId(){return instance.get("instanceId").getAsString();}
    public String getProductId(){return instance.get("productId").getAsString();}
    public String getBoughtPrice(){
        return instance.get("boughtPrice").getAsString();
    }
    public String getExpireDate(){
        if(!instance.has("expireDate")){
            return "-";
        }
        return instance.get("expireDate").getAsString();
    }
    public String getDefectedDescription(){
        if(!instance.has("defectedDescription")){
            return "the instance isn't defective";
        }
        return "the item is defected, description: \"" +instance.get("defectedDescription").getAsString()+ "\"";
    }
    public String getPlaceOfProduct(){
        if(instance.get("placeOfProduct").getAsString().equals("InStock")){
            return "instance is in stock";
        }
        return "instance is in store";
    }
    public String getArriveDate(){
        return instance.get("arriveDate").getAsString();
    }
    public String getError() { return error;}
}
