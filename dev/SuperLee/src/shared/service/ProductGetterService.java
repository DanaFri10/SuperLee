package shared.service;

import com.google.gson.Gson;
import shared.ControllerFactory;
import stock.business.StockController;

public class ProductGetterService {
    private StockController stc;
    private Gson gson;
    static ProductGetterService service = null;

    private ProductGetterService(StockController stc){
        this.stc = stc;
        gson = new Gson();
    }

    public static ProductGetterService getService() throws Exception {
        if(service == null){
            service = new ProductGetterService(ControllerFactory.getStc());
        }
        return service;
    }

    public String getAllProducts(){
        try{
            String data = stc.getAllProducts();
            return gson.toJson(new Response(data, false, ""));
        }
        catch (Exception e){
            return gson.toJson(new Response("", true,e.getMessage()));
        }
    }

    public String getProduct(int productId){
        try{
            String data = stc.getProduct(productId);
            return gson.toJson(new Response(data, false, ""));
        }
        catch (Exception e){
            return gson.toJson(new Response("", true,e.getMessage()));
        }
    }

    public String getImmediateSubCategories(int branchId, String path){
        try{
            String data = stc.getImmidiateSubCategories(branchId, path);
            return gson.toJson(new Response(data, false, ""));
        }
        catch (Exception e){
            return gson.toJson(new Response("", true,e.getMessage()));
        }
    }

    public String getProductsOnlyIn(int branchId, String path){
        try{
            String data = stc.getProductOnlyIn(branchId, path);
            return gson.toJson(new Response(data, false, ""));
        }
        catch (Exception e){
            return gson.toJson(new Response("", true,e.getMessage()));
        }
    }
}
