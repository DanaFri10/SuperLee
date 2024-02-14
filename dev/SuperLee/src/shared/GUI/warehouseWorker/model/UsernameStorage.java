package shared.GUI.warehouseWorker.model;

public class UsernameStorage {
    public static String USERNAME = null;
    public static LoginModel logoutHandler = null;
    public static boolean isManager = false;
    public static void login(String operatorUsername, LoginModel loginModel){
        if(USERNAME == null){
            USERNAME = operatorUsername;
            logoutHandler = loginModel;
        }
    }

    public static void setManager(){
        isManager = true;
    }

    public static void logout(){
        USERNAME = null;
        logoutHandler = null;
    }
}
