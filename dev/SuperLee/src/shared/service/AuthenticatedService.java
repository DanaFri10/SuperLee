package shared.service;

import com.google.gson.Gson;
import stock.business.StockController;

import javax.naming.AuthenticationException;

public abstract class AuthenticatedService {
    private StockController stc;
    private Gson gson;
    public AuthenticatedService(StockController stc){
        this.stc = stc;
        this.gson = new Gson();
    }


    protected abstract void authenticate(String username) throws Exception;

    public String login(String username, String password){
        try{
            authenticate(username);
            stc.login(username, password);
            return gson.toJson(new Response("Successfully logged in!", false, ""));
        }
        catch (Exception e){
            return gson.toJson(new Response("",true,e.getMessage()));
        }
    }

    public String logout(String username){
        try{
            stc.logout(username);
            return gson.toJson(new Response("Successfully logged out!", false, ""));
        }
        catch (Exception e){
            return gson.toJson(new Response("",true,e.getMessage()));
        }
    }
}
