package shared.GUI.storeManager.model;

import com.google.gson.Gson;
import shared.service.Response;
import shared.service.StoreManagerService;

public class StoreManagerLoginModel {
    private StoreManagerService service;
    private Gson gson;
    public StoreManagerLoginModel() throws Exception {
        this.service = StoreManagerService.getService();
        gson = new Gson();
    }

    public boolean authenticateUser(String username) {
        return this.service.authenticateUser(username);
    }

    public Response login(String username, String password){
        Response response = gson.fromJson(this.service.login(username, password), Response.class);
        return response;
    }

}
