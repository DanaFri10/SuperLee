package shared;

import shared.GUI.storeManager.view.StoreManagerLogin;
import shared.GUI.suppliersManager.view.LoginView;
import stock.business.UserRole;
import stock.presentation.MainView;

public class Main {
    public static void main(String[] args){
        Database.setPath("./SuperLee.db");
        if(args.length == 0){
            StoreManagerLogin view = new StoreManagerLogin();
            view.setVisible(true);
        }
        if(args.length == 1){
            if(args[0].equals("CLI")){
                MainView mw = new MainView(UserRole.StoreManager);
                mw.start();
            }else if(args[0].equals("GUI")){
                StoreManagerLogin view = new StoreManagerLogin();
                view.setVisible(true);
            }else{
                System.out.println("Unknown option: " + args[0]);
            }
        }
        if(args.length >= 2){
            if(args[0].equals("CLI")){
                if(args[1].equals("StoreManager")) {
                    MainView mw = new MainView(UserRole.StoreManager);
                    mw.start();
                }else if(args[1].equals("SuppliersManager")){
                    MainView mw = new MainView(UserRole.SupplierManager);
                    mw.start();
                }else if(args[1].equals("WarehouseWorker")){
                    MainView mw = new MainView(UserRole.WareHoushWorker);
                    mw.start();
                }else{
                    System.out.println("Unknown Role: " + args[1]);
                }
            }else if(args[0].equals("GUI")){
                if(args[1].equals("StoreManager")) {
                    StoreManagerLogin view = new StoreManagerLogin();
                    view.setVisible(true);
                }else if(args[1].equals("SuppliersManager")){
                    shared.GUI.suppliersManager.view.LoginView view = new shared.GUI.suppliersManager.view.LoginView();
                    view.setVisible(true);
                }else if(args[1].equals("WarehouseWorker")){
                    shared.GUI.warehouseWorker.view.LoginView view = new shared.GUI.warehouseWorker.view.LoginView();
                    view.setVisible(true);
                }else{
                    System.out.println("Unknown Role: " + args[1]);
                }
            }else{
                System.out.println("Unknown option: " + args[0]);
            }
        }
    }
}
