package shared;

import stock.business.Branch;
import stock.business.StockController;
import stock.dal.BranchDAO;
import stock.dal.ProductDAO;
import suppliers.business.SuppliersController;

import java.sql.SQLException;
import java.time.LocalDate;

public class ControllerFactory {
    static StockController stc = null;
    static SuppliersController spc = null;

    public static SuppliersController getSpc() throws Exception {
        if(stc == null){
            stc = new StockController();
        }
        if(spc == null){
            spc = new SuppliersController(stc,stc.getpDao(),stc.getbDAO());
            stc.setSuppliersController(spc);
        }
        return spc;
    }

    public static StockController getStc() throws SQLException {
        if(stc == null){
            stc = new StockController();
        }
        return stc;
    }

    public static void resetControllers(){
        stc = null;
        spc = null;
    }
}