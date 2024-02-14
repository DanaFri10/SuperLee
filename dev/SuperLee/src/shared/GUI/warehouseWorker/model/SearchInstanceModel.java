package shared.GUI.warehouseWorker.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import shared.LocalDateAdapter;
import shared.service.Response;
import shared.service.WarehouseWorkerService;

import java.time.LocalDate;

public class SearchInstanceModel {
    WarehouseWorkerService wws;
    String error;
    private Gson gson;
    String username;
    int instanceId;
    int branchId;

    public SearchInstanceModel(String username, int branchId) throws Exception {
        this.wws = WarehouseWorkerService.getService();
        this.gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter().nullSafe()).create();
        error = "";
        this.username =username;
        this.branchId =branchId;
    }

    public boolean checkIfExist(String instanceId) throws Exception{
        Response response = gson.fromJson(wws.getInstance(username, branchId, instanceId), Response.class);
        if(response.getData().equals("null")){
            throw new Exception("Instance doesn't exist");
        }
        else{
            //because we reached here we know it won't fail
            this.instanceId = Integer.parseInt(instanceId);
            return true;
        }
    }


    public String getError() { return error;}
    public int getInstanceId() { return instanceId;}
}
