package shared.GUI.warehouseWorker.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import shared.service.Response;
import shared.service.WarehouseWorkerService;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SupplierModel {
    private String id;
    private String name;
    private List<ContactModel> contacts;
    private WarehouseWorkerService service;
    private Gson gson;

    public SupplierModel(String id, String name) throws Exception {
        this.id = id;
        this.name = name;
        this.gson = new Gson();
        this.contacts = new LinkedList<>();
        service = WarehouseWorkerService.getService();
        Response response = gson.fromJson(service.getContactsForSupplier(UsernameStorage.USERNAME, id), Response.class);
        if(!response.isError()){
            Type objectList = new TypeToken<List<JsonObject>>() {}.getType();
            List<JsonObject> jsonContacts = gson.fromJson(response.getData(), objectList);
            for(JsonObject contact : jsonContacts){
                contacts.add(new ContactModel(contact.get("contactId").getAsString(),contact.get("name").getAsString(),contact.get("email").getAsString()));
            }
        }else{
            throw new Exception(response.getErrorMessage());
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<ContactModel> getContacts() {
        return contacts;
    }

    @Override
    public String toString() {
        return name + " (" + id + ")";
    }
}
