package stock.presentation;



import java.time.LocalDate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import shared.ControllerFactory;
import shared.LocalDateAdapter;
import stock.business.Branch;
import stock.business.StockController;
import stock.business.UserRole;
import suppliers.business.SuppliersController;

public class MainView {
    public String username;
    public StockController sc;
    private SuppliersController spc;
    private UserRole desiredRole;
    public boolean end;
    public int branchId;
    private static Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter().nullSafe()).create();

    public MainView(UserRole desiredRole){
        try {
            username = null;
            sc = ControllerFactory.getStc();
            spc = ControllerFactory.getSpc();
            this.desiredRole = desiredRole;
            end = false;
        } catch (Exception e) {
            Writer.print("couldn't create Stock Controller\n");
        }

    }

 

    public void start(){
        while(!end){
            username = null;
            Writer.print("Hello, there fella\n" );
            Writer.print("Please login");
            Writer.print("\n");
            boolean flag = false;
            outer: while(!flag){
                flag = startLogin();
                if (flag)
                    break outer;
            }
            //selecting a branch
            Writer.print("Plz write your current branch id\n");
            Writer.print("\n");
            Branch branch = null; 
            while(branch == null){
                String branchId = Reader.read("branch id: ");
                try{
                    branch = gson.fromJson(sc.getBranchGson(branchId), Branch.class);
                }
                catch (Exception e){
                    Writer.print(e.getMessage());
                }
                if(branch == null){
                    Writer.print("branch does not exist\n");
                }
            }
            try{
                switch(desiredRole){
                    case StoreManager:
                        StockManagerView stockManagerView = new StockManagerView(branch.getBranchId(), this);
                        stockManagerView.start(sc,spc);
                        break;
                    case WareHoushWorker :
                        WarehouseWorkerView wareHouseWorkerView = new WarehouseWorkerView(branch.getBranchId(), this);
                        wareHouseWorkerView.start(sc,spc);
                        break;
                    case SupplierManager:
                        suppliers.presentation.MainView mw = new suppliers.presentation.MainView(spc);
                        mw.start();
                        break;
                    default:
                        break;

                }
                //automatically logout
                sc.logout(username);
            }
            catch (Exception e){
                Writer.print(e.getMessage());
            }

        }
    }

    public boolean startRegistertion(){
        boolean flag = false;
        Writer.print("To register plz enter your desired username and password.\n");
        Writer.print("And finally write 1 if you are a StockManager, 3 if you are a WareHoushWorker and 4 if you are a SupplierManager \n" );
        Writer.print("(Note after you are registered you are automatically logged)\n" );
        Writer.print("\n");
        String username2;
        while(true){
            username2 = Reader.read("username: ");
            String passward = Reader.read("password: ");
            String MOW = Reader.read("role: ");
            int manuallyReg = 0;
            try{
                flag = sc.registration(username2, passward, MOW, manuallyReg);
                break;
            } 
            catch (Exception e) {
                Writer.print(e.getMessage());        
            }
        }
        if (flag)
            this.username = username2;
        return flag;
    }

    public boolean startLogin(){
        Writer.print("To login please enter your username and password.\n");
        Writer.print("\n");
        String password;
        this.username = Reader.read("username: ");
        password = Reader.read("password: ");
        boolean flag = false;
        try{
            flag = sc.login(username, password);
            if(desiredRole != sc.getRole(username)){
                flag = false;
                throw new Exception("Incorrect role. You must be: " + desiredRole.name() + "\n");
            }
        }
        catch (Exception e) {
            Writer.print(e.getMessage());
        }
        return flag;

    }

    public void end(){
        end=true;
        spc.stopTimer();
    }
    
}
