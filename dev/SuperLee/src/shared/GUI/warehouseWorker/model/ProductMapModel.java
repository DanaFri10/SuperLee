package shared.GUI.warehouseWorker.model;

import com.google.gson.Gson;
import shared.service.Response;
import shared.service.WarehouseWorkerService;

import java.util.HashMap;
import java.util.Map;

public class ProductMapModel {
    private int branchId;
    private Map<Integer, Integer> productAmountMap;
    private Map<Integer, UniversalProductModel> productModelMap;
    private WarehouseWorkerService service;
    private Gson gson;

    public ProductMapModel(int branchId) throws Exception {
        this.branchId = branchId;
        productAmountMap = new HashMap<>();
        productModelMap = new HashMap<>();
        this.gson = new Gson();
        this.service = WarehouseWorkerService.getService();
    }

    public void addProduct(UniversalProductModel model){
        productAmountMap.put(model.getId(),1);
        productModelMap.put(model.getId(),model);
    }

    public void setProductAmount(int productId, int amount){
        productAmountMap.put(productId,amount);
    }

    public int getProductAmount(int productId){
        return productAmountMap.get(productId);
    }

    public void increaseProductAmount(int productId){
        productAmountMap.put(productId,productAmountMap.get(productId)+1);
    }

    public void decreaseProductAmount(int productId){
        productAmountMap.put(productId,productAmountMap.get(productId)-1);
    }

    public Map<Integer, Integer> getProductAmountMap() {
        return productAmountMap;
    }

    public Map<Integer, UniversalProductModel> getProductModelMap() {
        return productModelMap;
    }

    public String orderOptimally() throws Exception{
        Response response = gson.fromJson(service.createOptimalOrder(UsernameStorage.USERNAME, branchId, gson.toJson(productAmountMap)), Response.class);
        if(response.isError()){
            throw new Exception(response.getErrorMessage());
        }
        return response.getData();
    }
    public String orderFastest() throws Exception{
        Response response = gson.fromJson(service.createFastestOrder(UsernameStorage.USERNAME, branchId, gson.toJson(productAmountMap)), Response.class);
        if(response.isError()){
            throw new Exception(response.getErrorMessage());
        }
        return response.getData();
    }

    public void remove(int productId) {
        if(productAmountMap.containsKey(productId)){
            productAmountMap.remove(productId);
            productModelMap.remove(productId);
        }
    }

    public boolean orderable(){
        return !productAmountMap.isEmpty();
    }

    public int getBranchId() {
        return branchId;
    }
}
