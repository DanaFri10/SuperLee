package shared.GUI.storeManager.model;

import com.google.gson.Gson;
import shared.service.Response;
import shared.service.StoreManagerService;

public class StoreManagerHomeModel {
    private StoreManagerService service;
    private Gson gson;

    public StoreManagerHomeModel() throws Exception {
        this.service = StoreManagerService.getService();
        gson = new Gson();

    }

    public String createShortageReport(String username, int branchId) {
        Response response;
        response = gson.fromJson(service.createShortageReport(username, branchId), Response.class);
        if(response.isError()){
            return response.getErrorMessage();
        }
        return response.getData();
    }

    public String getOrdersHistory(String username, String branchId) {
        Response response;
        response = gson.fromJson(service.getOrdersHistory(username, Integer.parseInt(branchId)), Response.class);
        if(response.isError()){
            return response.getErrorMessage();
        }
        return response.getData();
    }

    public String createDefectivesReportOnDemand(String username, int branchId) {
        Response response;
        response = gson.fromJson(service.createDefectivesReportOnDemand(username, branchId), Response.class);
        if(response.isError()){
            return response.getErrorMessage();
        }
        return response.getData();
    }

    public Response updateTimeStep(String username, int branchId, String timeStep) {
        Response response;
        response = gson.fromJson(service.updateTimeStep(username, branchId, timeStep), Response.class);

        return response;
    }

    public Response logout(String username){
        Response response = gson.fromJson(this.service.logout(username), Response.class);
        return response;
    }

}
