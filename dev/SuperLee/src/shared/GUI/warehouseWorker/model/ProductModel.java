package shared.GUI.warehouseWorker.model;

import com.google.gson.*;
import shared.LocalDateAdapter;
import shared.service.Response;
import shared.service.WarehouseWorkerService;

import java.time.LocalDate;

public class ProductModel {
    UniversalProductModel worldProduct;
    JsonObject branchProduct;
    JsonObject productDiscount;
    String error;
    private int branchId;
    private Gson gson;
    String username;
    WarehouseWorkerService wws;

    public ProductModel(String username, int productId, int branchId) throws Exception {
        this.wws = WarehouseWorkerService.getService();
        this.gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter().nullSafe()).create();
        worldProduct = getWorldProduct(username, productId);
        branchProduct = getBranchProduct(username, productId, branchId);
        productDiscount = getProductDiscount(username, productId, branchId);
        error = "";
        this.username =username;
        this.branchId = branchId;
    }
    public UniversalProductModel getWorldProduct(String username,int ProductId) throws Exception{
        Response response = gson.fromJson(wws.getWorldProduct(username, ProductId), Response.class);
        if(response.isError()){
            throw new Exception("operation failed: " + response.getErrorMessage());
        }
        else{
            JsonObject product = gson.fromJson(response.getData(), JsonObject.class);
            return new UniversalProductModel(product.get("productId").getAsInt(),product.get("name").getAsString(),product.get("manufacturer").getAsString());
        }
    }

    public JsonObject getBranchProduct(String username,int ProductId, int branchId) throws Exception{
        Response response = gson.fromJson(wws.getBranchProduct(username, branchId, ProductId), Response.class);
        if(response.isError()){
            throw new Exception("operation failed: " + response.getErrorMessage());
        }
        else{
            return gson.fromJson(response.getData(), JsonObject.class);
        }
    }

    public JsonObject getProductDiscount(String username,int ProductId, int branchId) throws Exception{
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

    public String getReport() throws Exception{
        Response response = gson.fromJson(wws.getBranchProductReport(username, branchId, getId()), Response.class);
        if(response.isError()){
            throw new Exception("operation failed: " + response.getErrorMessage());
        }
        else{
            return response.getData();
        }
    }

    public String getId(){return ""+worldProduct.getId();}
    public String getName(){
        return worldProduct.getName();
    }
    public String getManufacturer(){return worldProduct.getManufacturer();}
    public String getOriginalSold(){return branchProduct.get("priceSold").getAsString();}
    public String getDemand(){return branchProduct.get("demand").getAsString();}
    public String getMinimum(){
        return branchProduct.get("minimum").getAsString();
    }
    public String getPlaceInStore(){return branchProduct.get("placeInStore").getAsString();}
    public String getPath(){
        return branchProduct.get("path").getAsString();
    }
    public String getDiscount(){
        try {
            return "has discount from " + productDiscount.get("beginningDate").getAsString() +
                    " to " + productDiscount.get("expiredDate").getAsString()+
                    " with value " + productDiscount.get("discountNumericValue").getAsString();
        }
        catch(Exception e){
            return  "no discount";
        }
    }


    public String getError() { return error;}
}
