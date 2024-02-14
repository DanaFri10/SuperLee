package shared.GUI.warehouseWorker.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import shared.GUI.warehouseWorker.view.ProductExplorerView;
import shared.service.ProductGetterService;
import shared.service.Response;

import javax.swing.*;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class CurrentCategoryModel {
    private int branchId;
    private String currPath;
    private DefaultListModel<Object> children;
    private Gson gson;
    private ProductGetterService service;

    public CurrentCategoryModel(int branchId, DefaultListModel<Object> children, String initPath) throws Exception {
        this.branchId = branchId;
        this.children = children;
        this.gson = new Gson();
        this.service = ProductGetterService.getService();
        currPath = initPath;
        loadPath();
    }

    public void extendPath(String newPath) throws Exception {
        if(!currPath.isEmpty()){
            currPath += "/";
        }
        currPath += newPath;
        loadPath();
    }

    public void reducePath() throws Exception {
        if(!currPath.contains("/")){
            currPath = "";
        }else {
            currPath = currPath.substring(0, currPath.lastIndexOf('/'));
        }
        loadPath();
    }

    private void loadPath() throws Exception{
        Response resSub = gson.fromJson(service.getImmediateSubCategories(branchId, currPath), Response.class);
        if(resSub.isError()){
            throw new Exception(resSub.getErrorMessage());
        }
        Response resPrd = gson.fromJson(service.getProductsOnlyIn(branchId, currPath), Response.class);
        if(resPrd.isError()){
            throw new Exception(resSub.getErrorMessage());
        }
        Type listType = new TypeToken<LinkedList<String>>(){}.getType();
        List<String> categoryList = gson.fromJson(resSub.getData(), listType);
        children.clear();
        for(String cat : categoryList){
            children.addElement(cat);
        }

        Type objectListType = new TypeToken<LinkedList<JsonObject>>(){}.getType();
        List<JsonObject> branchProductList = gson.fromJson(resPrd.getData(), objectListType);
        for(JsonObject bProduct : branchProductList){
            Response productDataResponse = gson.fromJson(service.getProduct(bProduct.get("id").getAsInt()), Response.class);
            if(productDataResponse.isError()){
                throw new Exception(productDataResponse.getErrorMessage());
            }
            JsonObject product = gson.fromJson(productDataResponse.getData(), JsonObject.class);
            children.addElement(new UniversalProductModel(product.get("productId").getAsInt(),product.get("name").getAsString(),product.get("manufacturer").getAsString()));
        }
    }

    public String getCurrPath() {
        return currPath;
    }

    public boolean canAddDiscount(){
        return !children.isEmpty();
    }
}
