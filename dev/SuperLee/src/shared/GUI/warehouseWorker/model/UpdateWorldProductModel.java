package shared.GUI.warehouseWorker.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import shared.service.Response;
import shared.service.WarehouseWorkerService;
import stock.business.BranchProduct;
import stock.business.Product;

public class UpdateWorldProductModel {
    WarehouseWorkerService wws;
    JsonObject product;
    String error;
    private Gson gson;
    String username;
    int productId;

    public UpdateWorldProductModel(String username, int productId) throws Exception {
        this.wws = WarehouseWorkerService.getService();
        this.gson = new Gson();
        product = getWorldProduct(username, productId);
        error = "";
        this.username =username;
        this.productId = productId;
    }
    public void updateProduct(String name, String manufacturer) throws Exception{
        Response response = gson.fromJson(wws.updateWorldProduct(username, productId + "", name, manufacturer), Response.class);
        if(response.isError()){
            throw new Exception("operation failed: " + response.getErrorMessage());
        }
        else{
            //no need to worry the parse will fail because if it isn't int, the response will be error.
            product= getWorldProduct(username, productId);
            error = "";
        }
    }

    public JsonObject getWorldProduct(String username,int ProductId) throws Exception{
        Response response = gson.fromJson(wws.getWorldProduct(username, ProductId), Response.class);
        if(response.isError()){
            throw new Exception("operation failed: " + response.getErrorMessage());
        }
        else{
            return gson.fromJson(response.getData(), JsonObject.class);
        }
    }

    public String getId(){return product.get("productId").getAsString();}
    public String getName(){
        return product.get("name").getAsString();
    }
    public String getManufacturer(){return product.get("manufacturer").getAsString();}

    public String getError() { return error;}
}
