package shared.GUI.warehouseWorker.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import shared.service.Response;
import shared.service.WarehouseWorkerService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderModel {
    private int orderId;
    private Map<String,SupplierModel> suppliers;
    private Gson gson;
    private WarehouseWorkerService service;
    private Map<String, String> assignedContacts;

    public OrderModel(String orderJson) throws Exception {
        gson = new Gson();
        JsonObject order = gson.fromJson(orderJson, JsonObject.class);
        this.orderId = order.get("orderId").getAsInt();
        this.assignedContacts = new HashMap<>();
        this.suppliers = new HashMap<>();
        service = WarehouseWorkerService.getService();
        Response response = gson.fromJson(service.getSuppliersInOrder(UsernameStorage.USERNAME, orderId), Response.class);
        if(!response.isError()){
            List<String> jsonSupplierIds = gson.fromJson(response.getData(), List.class);
            for(String supplierId : jsonSupplierIds){
                Response supplierResponse = gson.fromJson(service.getSupplier(UsernameStorage.USERNAME,supplierId), Response.class);
                if(!supplierResponse.isError()) {
                    JsonObject supplier = gson.fromJson(supplierResponse.getData(), JsonObject.class);
                    suppliers.put(supplierId,new SupplierModel(supplier.get("companyId").getAsString(), supplier.get("companyName").getAsString()));
                }
            }
        }else{
            throw new Exception("operation failed: " + response.getErrorMessage());
        }
    }

    public List<ContactModel> getContactsOfSupplier(String supplierId){
        return suppliers.get(supplierId).getContacts();
    }

    public Map<String, SupplierModel> getSuppliers() {
        return suppliers;
    }

    public void assignContact(String supplierId, String contactId) throws Exception{
        Response response = gson.fromJson(service.assignContactToOrder(UsernameStorage.USERNAME,orderId,supplierId,contactId), Response.class);
        if(response.isError()){
            throw new Exception(response.getErrorMessage());
        }
        assignedContacts.put(supplierId, contactId);
    }

    public void updateEmail(String supplierId) throws Exception{
        Response response = gson.fromJson(service.updateContactOrderMade(UsernameStorage.USERNAME,orderId,supplierId,assignedContacts.get(supplierId)), Response.class);
        if(response.isError()){
            throw new Exception(response.getErrorMessage());
        }
    }

    public String describeOrderForSupplier(String supplierId) throws Exception{
        Response response = gson.fromJson(service.describeOrderForSupplier(UsernameStorage.USERNAME,orderId,supplierId), Response.class);
        if(response.isError()){
            throw new Exception(response.getErrorMessage());
        }
        return response.getData();
    }

    public boolean supplierHasContacts(String supplierId){
        return !suppliers.get(supplierId).getContacts().isEmpty();
    }
}
