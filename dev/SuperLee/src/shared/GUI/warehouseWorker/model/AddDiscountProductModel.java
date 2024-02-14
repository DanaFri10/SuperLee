package shared.GUI.warehouseWorker.model;

import com.google.gson.*;
import shared.LocalDateAdapter;
import shared.service.Response;
import shared.service.WarehouseWorkerService;

import java.time.LocalDate;

public class AddDiscountProductModel {

    JsonObject productDiscount;
    String error;
    private Gson gson;
    String username;
    WarehouseWorkerService wws;
    int branchId;
    int productId;

    public AddDiscountProductModel(String username, int productId, int branchId) throws Exception {
        this.wws = WarehouseWorkerService.getService();
        this.gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter().nullSafe()).create();
        productDiscount = getProductDiscount(username, productId, branchId);
        error = "";
        this.branchId =branchId;
        this.productId = productId;
        this.username =username;
    }


    public void addDiscount(String from, String to, String value) throws Exception{
        Response response = gson.fromJson(wws.addDiscountProduct(username, branchId, value, from, to, productId + ""), Response.class);
        if(response.isError()){
            throw new Exception("operation failed: " + response.getErrorMessage());
        }
        else{
            //no need to worry the parse will fail because if it isn't int, the response will be error.
            productDiscount = getProductDiscount(username, productId, branchId);
            error = "";
        }
    }


    public JsonObject getProductDiscount(String username, int ProductId, int branchId) throws Exception{
        Response response = gson.fromJson(wws.getBranchDiscountOnProduct(username, branchId, ProductId), Response.class);
        if(response.isError()){
            throw new Exception("operation failed: " + response.getErrorMessage());
        }
        else{
            if(gson.fromJson(response.getData(), JsonElement.class) instanceof JsonNull){
                return null;
            }
            return gson.fromJson(response.getData(), JsonObject.class);
        }
    }


    public String getDiscountFrom(){
        if(productDiscount == null){
            return "none";
        }
        return productDiscount.get("beginningDate").getAsString();
    }

    public String getDiscountTo(){
        if(productDiscount == null){
            return "none";
        }
        return productDiscount.get("expiredDate").getAsString();
    }

    public String getDiscountValue(){
        if(productDiscount == null){
            return "none";
        }
        return productDiscount.get("discountNumericValue").getAsString();
    }
    public String getError() { return error;}
}
