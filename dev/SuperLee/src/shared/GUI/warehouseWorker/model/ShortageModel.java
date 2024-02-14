package shared.GUI.warehouseWorker.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import shared.service.ProductGetterService;
import shared.service.Response;
import shared.service.WarehouseWorkerService;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ShortageModel {
    private Map<Integer, Integer> shortageMap;
    private Map<Integer, Integer> extrasMap;
    private Map<Integer, UniversalProductModel> productModelMap;
    private int branchId;
    private Gson gson;
    private WarehouseWorkerService service;
    private ProductGetterService pService;

    public ShortageModel(int branchId) throws Exception {
        this.branchId = branchId;
        this.productModelMap = new HashMap<>();
        this.extrasMap = new HashMap<>();
        gson = new Gson();
        service = WarehouseWorkerService.getService();
        pService = ProductGetterService.getService();
        Response response = gson.fromJson(service.getShortageReport(UsernameStorage.USERNAME, branchId), Response.class);
        if(!response.isError()){
            Type intToint = new TypeToken<Map<Integer, Integer>>() {}.getType();
            shortageMap = gson.fromJson(response.getData(), intToint);
            for(int productId : shortageMap.keySet()){
                extrasMap.put(productId,0);
                Response productDataResponse = gson.fromJson(pService.getProduct(productId), Response.class);
                if(productDataResponse.isError()){
                    throw new Exception(productDataResponse.getErrorMessage());
                }
                JsonObject product = gson.fromJson(productDataResponse.getData(), JsonObject.class);
                productModelMap.put(productId, new UniversalProductModel(product.get("productId").getAsInt(),product.get("name").getAsString(),product.get("manufacturer").getAsString()));
            }
        }
    }

    public String getShortageOrderDescription() throws Exception{
        Response response = gson.fromJson(service.describeShortageReport(UsernameStorage.USERNAME, branchId, gson.toJson(shortageMap)), Response.class);
        if(response.isError()){
            throw new Exception(response.getErrorMessage());
        }
        return response.getData();
    }

    public String order() throws Exception{
        Response response = gson.fromJson(service.makeShortageOrder(UsernameStorage.USERNAME, branchId, gson.toJson(shortageMap),gson.toJson(extrasMap)), Response.class);
        if(response.isError()){
            throw new Exception(response.getErrorMessage());
        }
        return response.getData();
    }

    public Map<Integer, Integer> getShortageMap() {
        return shortageMap;
    }

    public void setProductExtra(int productId, int amount){
        extrasMap.put(productId,amount);
    }

    public int getProductExtra(int productId){
        return extrasMap.get(productId);
    }

    public void increaseProductExtra(int productId){
        extrasMap.put(productId,extrasMap.get(productId)+1);
    }

    public void decreaseProductExtra(int productId){
        extrasMap.put(productId,extrasMap.get(productId)-1);
    }

    public Map<Integer, Integer> getExtrasMap() {
        return extrasMap;
    }

    public Map<Integer, UniversalProductModel> getProductModelMap() {
        return productModelMap;
    }
}
