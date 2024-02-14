package shared.GUI.warehouseWorker.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import shared.service.Response;
import shared.service.WarehouseWorkerService;

public class UpdateBranchProductModel {
    WarehouseWorkerService wws;
    JsonObject branchProduct;
    String error;
    private Gson gson;
    String username;
    int productId;
    int branchId;

    public UpdateBranchProductModel(String username, int productId, int branchId) throws Exception {
        this.wws = WarehouseWorkerService.getService();
        this.gson = new Gson();
        error = "";
        this.username =username;
        this.productId = productId;
        this.branchId = branchId;
        branchProduct = getBranchProduct();

    }
    public void updateBranchProduct(String priceSold, String demand, String minimum, String placeInStore,String path) throws Exception{
        Response response = gson.fromJson(wws.updateBranchProduct(username,branchId , productId + "", priceSold, path, demand, placeInStore, minimum), Response.class);
        if(response.isError()){
            throw new Exception("operation failed: " + response.getErrorMessage());
        }
        else{
            branchProduct = getBranchProduct();
            error ="";
        }
    }

    public JsonObject getBranchProduct() throws Exception{
        Response response = gson.fromJson(wws.getBranchProduct(username, branchId, productId), Response.class);
        if(response.isError()){
            throw new Exception("operation failed: " + response.getErrorMessage());
        }
        else{
            return gson.fromJson(response.getData(), JsonObject.class);
        }
    }










    public String getOriginalSold(){return branchProduct.get("priceSold").getAsString();}
    public String getDemand(){return branchProduct.get("demand").getAsString();}
    public String getMinimum(){
        return branchProduct.get("minimum").getAsString();
    }
    public String getPlaceInStore(){return branchProduct.get("placeInStore").getAsString();}
    public String getPath(){
        return branchProduct.get("path").getAsString();
    }
    public String getError() { return error;}

}
