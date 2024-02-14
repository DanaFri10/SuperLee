package shared.GUI.warehouseWorker.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import shared.service.Response;
import shared.service.WarehouseWorkerService;

public class DeleteBranchProductModel {
    String error;
    private Gson gson;
    String username;
    WarehouseWorkerService wws;
    int productId;
    int branchId;
    String success;

    public DeleteBranchProductModel(String username, int productId, int branchId) throws Exception {
        this.wws = WarehouseWorkerService.getService();
        this.gson = new Gson();
        error = "";
        success = "";
        this.username =username;
        this.productId = productId;
        this.branchId = branchId;
    }
    public void deleteBranchProduct() throws Exception{
        Response response = gson.fromJson(wws.removeBranchProduct(username, branchId, productId), Response.class);
        if(response.isError()){
            throw new Exception("operation failed: " + response.getErrorMessage());
        }
        else{
            //no need to worry the parse will fail because if it isn't int, the response will be error.
            error = "";
            success = response.getData();
        }

    }
    public String getAnswer(){
        if(success.equals("")){
            return error;
        }
        return success;
    }

}
