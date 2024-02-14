package shared.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import shared.ControllerFactory;
import com.google.gson.GsonBuilder;
import shared.LocalDateAdapter;
import stock.business.StockController;
import stock.business.UserRole;
import suppliers.business.SuppliersController;

import java.lang.reflect.Type;
import java.util.Map;
import java.time.LocalDate;

public class WarehouseWorkerService extends AuthenticatedService {
    private StockController stc;
    private SuppliersController spc;
    private Gson gson;
    static WarehouseWorkerService service = null;
    private WarehouseWorkerService(StockController stc, SuppliersController spc) {
        super(stc);
        this.stc = stc;
        this.spc = spc;
        this.gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter().nullSafe()).create();
    }

    public static WarehouseWorkerService getService() throws Exception {
        if(service == null){
            service = new WarehouseWorkerService(ControllerFactory.getStc(),ControllerFactory.getSpc());
        }
        return service;
    }

    //USE THIS FUNCTION AT THE START OF EVERY FUNCTION (THIS IS VERY IMPORTANT)
    @Override
    protected void authenticate(String username) throws Exception {
        if (stc.getRole(username) != UserRole.WareHoushWorker && stc.getRole(username) != UserRole.StoreManager) {
            throw new UnsupportedOperationException("Only Warehouse Workers and Store Managers are allowed to use these functions");
        }
    }

    public String getSuppliersForProduct(String operatorUsername, int productId, int amount){
        try{
            authenticate(operatorUsername);
            String data = spc.getSuppliersThatSupplyProduct(productId, amount);
            return gson.toJson(new Response(data, false, ""));
        }
        catch (Exception e){
            return gson.toJson(new Response("", true,e.getMessage()));
        }
    }

    public String getContactsForSupplier(String operatorUsername, String supplierId){
        try{
            authenticate(operatorUsername);
            String data = spc.getSupplierContacts(supplierId);
            return gson.toJson(new Response(data, false, ""));
        }
        catch (Exception e){
            return gson.toJson(new Response("", true,e.getMessage()));
        }
    }

    public String getSuppliersInOrder(String operatorUsername, int orderId){
        try{
            authenticate(operatorUsername);
            String data = spc.getSupplierIdsInOrder(orderId);
            return gson.toJson(new Response(data, false, ""));
        }
        catch (Exception e){
            return gson.toJson(new Response("", true,e.getMessage()));
        }
    }

    public String getSupplier(String operatorUsername, String supplierId){
        try{
            authenticate(operatorUsername);
            String data = spc.getSupplier(supplierId);
            return gson.toJson(new Response(data, false, ""));
        }
        catch (Exception e){
            return gson.toJson(new Response("", true,e.getMessage()));
        }
    }

    public String assignContactToOrder(String operatorUsername, int orderId, String supplierId, String contactId){
        try{
            authenticate(operatorUsername);
            spc.addContactToOrder(orderId,supplierId,contactId);
            return gson.toJson(new Response("OK", false, ""));
        }
        catch (Exception e){
            return gson.toJson(new Response("", true,e.getMessage()));
        }
    }

    public String updateContactOrderMade(String operatorUsername, int orderId, String supplierId, String contactId){
        try{
            authenticate(operatorUsername);
            spc.updateContactOrderMade(supplierId,contactId,orderId);
            return gson.toJson(new Response("OK", false, ""));
        }
        catch (Exception e){
            return gson.toJson(new Response("", true,e.getMessage()));
        }
    }

    public String describeOrderForSupplier(String operatorUsername, int orderId, String supplierId){
        try{
            authenticate(operatorUsername);
            String data = spc.describeOrderSupplier(supplierId,orderId);
            return gson.toJson(new Response(data, false, ""));
        }
        catch (Exception e){
            return gson.toJson(new Response("", true,e.getMessage()));
        }
    }

    public String createOptimalOrder(String operatorUsername, int branchId, String mapJson){
        try{
            authenticate(operatorUsername);
            Type intToint = new TypeToken<Map<Integer, Integer>>() {}.getType();
            Map<Integer, Integer> productMap= gson.fromJson(mapJson, intToint);
            String data = spc.createOptimalOrderJson(productMap, branchId);
            return gson.toJson(new Response(data, false, ""));
        }
        catch (Exception e){
            return gson.toJson(new Response("", true,e.getMessage()));
        }
    }

    public String createFastestOrder(String operatorUsername, int branchId, String mapJson){
        try{
            authenticate(operatorUsername);
            Type intToint = new TypeToken<Map<Integer, Integer>>() {}.getType();
            Map<Integer, Integer> productMap= gson.fromJson(mapJson, intToint);
            String data = spc.createFastestOrder(productMap, branchId);
            return gson.toJson(new Response(data, false, ""));
        }
        catch (Exception e){
            return gson.toJson(new Response("", true,e.getMessage()));
        }
    }

    public String createCustomOrder(String operatorUsername, int branchId, String mapJson){
        try{
            authenticate(operatorUsername);
            Type stringToIntToInt = new TypeToken<Map<String,Map<Integer, Integer>>>() {}.getType();
            Map<String,Map<Integer, Integer>> supplierMap = gson.fromJson(mapJson, stringToIntToInt);
            String data = spc.createCustomOrder(supplierMap, branchId);
            return gson.toJson(new Response(data, false, ""));
        }
        catch (Exception e){
            return gson.toJson(new Response("", true,e.getMessage()));
        }
    }

    public String getShortageReport(String operatorUsername, int branchId){
        try{
            authenticate(operatorUsername);
            String data = stc.getShortage(branchId);
            return gson.toJson(new Response(data, false, ""));
        }
        catch (Exception e){
            return gson.toJson(new Response("", true,e.getMessage()));
        }
    }

    public String describeShortageReport(String operatorUsername, int branchId, String mapJson){
        try{
            authenticate(operatorUsername);
            Type intToint = new TypeToken<Map<Integer, Integer>>() {}.getType();
            Map<Integer, Integer> shortageMap = gson.fromJson(mapJson, intToint);
            String data = stc.createShortageReport(branchId, shortageMap);
            return gson.toJson(new Response(data, false, ""));
        }
        catch (Exception e){
            return gson.toJson(new Response("", true,e.getMessage()));
        }
    }

    public String makeShortageOrder(String operatorUsername, int branchId, String mapJson, String extrasMapJson){
        try{
            authenticate(operatorUsername);
            Type intToint = new TypeToken<Map<Integer, Integer>>() {}.getType();
            Map<Integer, Integer> shortageMap = gson.fromJson(mapJson, intToint);
            Map<Integer, Integer> extrasMap = gson.fromJson(extrasMapJson, intToint);
            String data = spc.makeShortageOrder(shortageMap, extrasMap, branchId);
            return gson.toJson(new Response(data, false, ""));
        }
        catch (Exception e){
            return gson.toJson(new Response("", true,e.getMessage()));
        }
    }
    public String addDiscountProduct(String username, int branchId, String percentage, String sDate, String eDate, String productId){
        try {
            authenticate(username);
            stc.createSellingDiscount(branchId, percentage, "2", sDate, eDate, productId);
            return gson.toJson(new Response("product discount added successfully", false, null));

        }
         catch (Exception e) {
            return gson.toJson(new Response(null, true,  e.getMessage() ));
        }
    }

    public String addDiscountCategory(String username, int branchId, String percentage, String sDate, String eDate, String path){
        try {
            authenticate(username);
            stc.createSellingDiscount(branchId, percentage, "1", sDate, eDate, path);
            return gson.toJson(new Response("category discount added successfully", false, null));

        }
        catch (Exception e) {
            return gson.toJson(new Response(null, true,  e.getMessage() ));
        }
    }

    public String addBranchProduct(String username, int branchId, String productId, String sellingPrice, String path, String demand, String placeInStore, String minimum){
        try {
            authenticate(username);
            stc.submitBranchProduct(branchId, productId, sellingPrice, path, demand, placeInStore, minimum);
            return gson.toJson(new Response("branch product added successfully", false, null));

        }
        catch (Exception e) {
            return gson.toJson(new Response(null, true,  e.getMessage() ));
        }
    }

    public String addWorldProduct(String username, String productId, String name, String newManufacturer){
        try {
            authenticate(username);
            stc.addProduct(productId, name, newManufacturer);
            return gson.toJson(new Response("branch product added successfully", false, null));

        }
        catch (Exception e) {
            return gson.toJson(new Response(null, true,  e.getMessage() ));
        }
    }

    public String addWorldAndBranchProduct(String username, int branchId, String productId, String name, String manufacturer, String sellingPrice, String demand, String minimum, String placeInStore, String path){
        try {
            authenticate(username);
            stc.addProduct(productId, name, manufacturer);
            stc.submitBranchProduct(branchId, productId, sellingPrice, path, demand, placeInStore, minimum);
            return gson.toJson(new Response("branch and world product were added successfully", false, null));

        }
        catch (Exception e) {
            return gson.toJson(new Response(null, true,  e.getMessage() ));
        }
    }

    public String updateBranchProduct(String username, int branchId, String productId, String newSellingPrice, String newPath, String newDemand, String newPlaceInStore, String newMinimum){
        try {
            authenticate(username);
            stc.updateProductInfo(branchId, productId, newSellingPrice, newPath, newDemand, newPlaceInStore, newMinimum);
            return gson.toJson(new Response("branch product updated successfully", false, null));

        }
        catch (Exception e) {
            return gson.toJson(new Response(null, true,  e.getMessage() ));
        }
    }

    public String updateWorldProduct(String username, String productId, String newName, String newManufacturer){
        try {
            authenticate(username);
            stc.UpdateProduct(productId, newName, newManufacturer);
            return gson.toJson(new Response("world's product updated successfully", false, null));

        }
        catch (Exception e) {
            return gson.toJson(new Response(null, true,  e.getMessage() ));
        }
    }

    public String removeWorldProduct(String username, String productId){
        try {
            authenticate(username);
            stc.removeProduct(productId);
            return gson.toJson(new Response("congratulation you have committed war crimes", false, null));

        }
        catch (Exception e) {
            return gson.toJson(new Response(null, true, e.getMessage()));
        }
    }

    public String removeBranchProduct(String username, int branchId, int productId){
        try {
            authenticate(username);
            stc.removeBranchProduct(branchId, productId);
            return gson.toJson(new Response("Branch product removed successfully", false, null));

        }
        catch (Exception e) {
            return gson.toJson(new Response(null, true,  e.getMessage() ));
        }
    }

    public String getBranchProduct(String username, int branchId, int productId){
        try {
            authenticate(username);
            return gson.toJson(new Response(gson.toJson(stc.getBranchProduct(branchId, productId)), false, null));

        }
        catch (Exception e) {
            return gson.toJson(new Response(null, true,  e.getMessage() ));
        }
    }

    public String getWorldProduct(String username, int productId){
        try {
            authenticate(username);
            return gson.toJson(new Response(stc.getProduct(productId), false, null));

        }
        catch (Exception e) {
            return gson.toJson(new Response(null, true,  e.getMessage() ));
        }
    }
    public String getBranchDiscountOnProduct(String username, int branchId, int productId){
        try {
            authenticate(username);
            return gson.toJson(new Response(stc.getBranchProductDiscount(branchId, productId), false, null));

        }
        catch (Exception e) {
            return gson.toJson(new Response(null, true,  e.getMessage() ));
        }
    }

    public String updateExpired(String username, int branchId, String instanceId, String expireDate){
        try {
            authenticate(username);
            stc.updateInstanceExpiredDate(branchId, instanceId, expireDate);
            return gson.toJson(new Response("success", false, null));

        }
        catch (Exception e) {
            return gson.toJson(new Response(null, true,  e.getMessage() ));
        }
    }


    public String updateInstanceLocation(String username, int branchId, String instanceId, String location){
        try {
            authenticate(username);
            stc.changeLocationOfInstance(branchId, instanceId, location);
            return gson.toJson(new Response("success", false, null));

        }
        catch (Exception e) {
            return gson.toJson(new Response(null, true,  e.getMessage() ));
        }
    }

    public String updateInstanceDefect(String username, int branchId, String instanceId, String defectReason){
        try {
            authenticate(username);
            stc.reportDefective(branchId, instanceId, defectReason);
            return gson.toJson(new Response("success", false, null));
        }
        catch (Exception e) {
            return gson.toJson(new Response(null, true,  e.getMessage() ));
        }
    }

    public String getInstance(String username, int branchId, String instanceId){
        try {
            authenticate(username);
            return gson.toJson(new Response(gson.toJson(stc.getBranchInstanceString(branchId, instanceId)), false, null));
        }
        catch (Exception e) {
            return gson.toJson(new Response(null, true,  e.getMessage() ));
        }
    }

    public String addInstance(String username, int branchId, String instanceId, String ProductId, String expireDate, String priceBought){
        try {
            authenticate(username);
            stc.submitInstances(branchId, instanceId, priceBought,ProductId,expireDate);
            return gson.toJson(new Response("success", false, null));
        }
        catch (Exception e) {
            return gson.toJson(new Response(null, true,  e.getMessage() ));
        }
    }

    public String deleteInstance(String username, int branchId, String instanceId){
        try {
            authenticate(username);
            stc.submitMissingInstance(branchId, instanceId);
            return gson.toJson(new Response("success", false, null));
        }
        catch (Exception e) {
            return gson.toJson(new Response(null, true,  e.getMessage() ));
        }
    }

    public String getBranchProductReport(String username, int branchId, String productId){
        try {
            authenticate(username);
            String data = stc.getBranchProductReport(branchId, Integer.parseInt(productId));
            return gson.toJson(new Response(data, false, null));
        }
        catch (Exception e) {
            return gson.toJson(new Response(null, true,  e.getMessage() ));
        }
    }
}
