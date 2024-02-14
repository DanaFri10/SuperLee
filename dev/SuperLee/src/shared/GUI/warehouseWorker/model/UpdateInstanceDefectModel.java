package shared.GUI.warehouseWorker.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import shared.LocalDateAdapter;
import shared.service.Response;
import shared.service.WarehouseWorkerService;

import java.time.LocalDate;

public class UpdateInstanceDefectModel {
    WarehouseWorkerService wws;
    JsonObject instance;
    String error;
    private Gson gson;
    String username;
    int instanceId;
    int branchId;

    public UpdateInstanceDefectModel(String username, int branchId, int instanceId) throws Exception{
        this.wws = WarehouseWorkerService.getService();
        this.gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter().nullSafe()).create();
        this.username =username;
        this.instanceId = instanceId;
        this.branchId = branchId;
        instance = getInstance(username, branchId, instanceId);
        error = "";

    }

    public void updateDefectiveDescription(String defectiveDescription) throws Exception{
        Response response = gson.fromJson(wws.updateInstanceDefect(username, branchId, instanceId + "", defectiveDescription), Response.class);
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



    public String getDefectiveDescription(){
        if(!instance.has("defectedDescription")){
            return "the item isn't defected";
        }
        return " the item is defected, description: \"" +instance.get("defectedDescription").getAsString()+ "\"" ;
    }


    public String getError() { return error;}
}
