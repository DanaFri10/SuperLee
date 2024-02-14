package stock.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import shared.Database;
import stock.business.BranchDiscount;

public class BranchCategoryDiscountDAO {
    private Map<String, ArrayList<BranchDiscount>> branchCategoryDiscountsMap;
    private ArrayList<String> pathsWeGotAll;
    private int branchId;

    public BranchCategoryDiscountDAO(int _branchId) throws SQLException{
        Connection conn = Database.connect();
        branchCategoryDiscountsMap =  new HashMap<String, ArrayList<BranchDiscount>>();
        pathsWeGotAll =  new ArrayList<String>();
        branchId = _branchId;
        conn.close();
    }
    public void insertDiscount(String path, BranchDiscount d) throws SQLException{
        ArrayList<BranchDiscount> discountList = branchCategoryDiscountsMap.get(path);
        if(discountList == null){
                discountList = new ArrayList<>();
        }
        Connection conn = Database.connect();
        discountList.add(d);
        branchCategoryDiscountsMap.put(path, discountList);

        PreparedStatement stmt = conn.prepareStatement("INSERT INTO CategoryDiscounts (branchId, path, begginingDate, expiredDate, percentage) VALUES (?, ?, ?, ?, ?)");
        stmt.setInt(1, branchId);
        stmt.setString(2, path);
        stmt.setString(3, d.getBeginningDate().toString());
        stmt.setString(4, d.getExpiredDate().toString());
        stmt.setDouble(5, d.getDiscountNumericValue());
        stmt.executeUpdate();
        stmt.close();
        conn.close();

    }

    public ArrayList<BranchDiscount> getAllPath(String path) throws SQLException{
        ArrayList<BranchDiscount> discountList = new ArrayList<>();

        if(path.contains("/")){
            ArrayList<BranchDiscount> parentsDiscountsList = getAllPath(path.substring(0, path.lastIndexOf('/')));
            discountList.addAll(parentsDiscountsList);
        }
        if(pathsWeGotAll.contains(path)){
            discountList.addAll(branchCategoryDiscountsMap.get(path));
        }
        else {
            Connection conn = Database.connect();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM CategoryDiscounts WHERE branchId = ? AND path = ?");
            stmt.setInt(1, branchId);
            stmt.setString(2, path);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                BranchDiscount discount = new BranchDiscount(LocalDate.parse(rs.getString(3)), LocalDate.parse(rs.getString(4)), rs.getDouble(5));

                discountList.add(discount);
            }
            branchCategoryDiscountsMap.put(path, discountList);
            pathsWeGotAll.add(path);
            stmt.close();
            conn.close();
        }

        return discountList;
    }
    public void DeleteAll() throws SQLException {
        branchCategoryDiscountsMap= new HashMap<>();
        pathsWeGotAll = new ArrayList<>();
        Connection conn = Database.connect();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM CategoryDiscounts");
        stmt.executeUpdate();
        stmt.close();
        conn.close();
    }
}
