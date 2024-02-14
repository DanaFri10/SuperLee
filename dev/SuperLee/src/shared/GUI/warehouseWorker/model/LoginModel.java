package shared.GUI.warehouseWorker.model;

import com.google.gson.Gson;
import shared.service.Response;
import shared.service.WarehouseWorkerService;

public class LoginModel {
    private WarehouseWorkerService service;
    private Gson gson;

    public LoginModel() throws Exception {
        this.service = WarehouseWorkerService.getService();
        this.gson = new Gson();
    }

    public void login(String username, String password) throws Exception{
        Response response = gson.fromJson(service.login(username,password), Response.class);
        if(response.isError()){
            throw new Exception(response.getErrorMessage());
        }
        UsernameStorage.login(username, this);
    }

    public void logout() throws Exception{
        Response response = gson.fromJson(service.logout(UsernameStorage.USERNAME), Response.class);
        if(response.isError()){
            throw new Exception(response.getErrorMessage());
        }
        UsernameStorage.logout();
    }
}
