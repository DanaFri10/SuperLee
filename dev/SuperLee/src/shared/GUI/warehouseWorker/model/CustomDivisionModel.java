package shared.GUI.warehouseWorker.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import shared.service.Response;
import shared.service.WarehouseWorkerService;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CustomDivisionModel {
    private ProductMapModel productsMap;
    private Map<Integer, String> customDivision;
    private Gson gson;
    private WarehouseWorkerService service;

    public CustomDivisionModel(ProductMapModel productsMap) throws Exception {
        this.productsMap = productsMap;
        this.customDivision = new HashMap<>();
        gson = new Gson();
        service = WarehouseWorkerService.getService();
    }

    public void addCustomDivision(int productId, String supplierId){
        customDivision.put(productId, supplierId);
    }
    public List<SupplierModel> getSuppliersForProduct(int productId) throws Exception{
        List<SupplierModel> suppliers = new LinkedList<>();
        Response response = gson.fromJson(service.getSuppliersForProduct(UsernameStorage.USERNAME, productId, productsMap.getProductAmount(productId)), Response.class);
        if(!response.isError()){
            Type objectList = new TypeToken<List<JsonObject>>() {}.getType();
            List<JsonObject> jsonSuppliers = gson.fromJson(response.getData(), objectList);
            for(JsonObject supplier : jsonSuppliers){
                suppliers.add(new SupplierModel(supplier.get("companyId").getAsString(), supplier.get("companyName").getAsString()));
            }
        }else{
            throw new Exception(response.getErrorMessage());
        }
        return suppliers;
    }

    public String order() throws Exception{
        Map<String, Map<Integer, Integer>> divisionMap = new HashMap<>();
        for(int productId : customDivision.keySet()){
            if(!divisionMap.containsKey(customDivision.get(productId))){
                divisionMap.put(customDivision.get(productId), new HashMap<>());
            }
            divisionMap.get(customDivision.get(productId)).put(productId,productsMap.getProductAmount(productId));
        }
        Response response = gson.fromJson(service.createCustomOrder(UsernameStorage.USERNAME, productsMap.getBranchId(), gson.toJson(divisionMap)), Response.class);
        if(response.isError()){
            throw new Exception(response.getErrorMessage());
        }
        return response.getData();
    }

    public ProductMapModel getProductsMap() {
        return productsMap;
    }
}
