package stock.dal;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.internal.bind.util.ISO8601Utils;
import shared.Database;
import stock.business.Branch;

public class BranchDAO {
    private Map<Integer, Branch> branchMap;
    private boolean gotAll;
    public BranchDAO() throws SQLException{
        branchMap = new HashMap<>();
        gotAll = false;
    }


    public void insertBranch(Branch b) throws SQLException{
        Connection conn = Database.connect();
        Integer key = b.getBranchId();
        branchMap.put(key, b);
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Branches (id, startTime, timeStep) VALUES (?, ?, ?)");
        stmt.setInt(1, b.getBranchId());
        stmt.setString(2, b.getStartTime().toString());
        stmt.setInt(3, b.getTimeStep());
        stmt.executeUpdate();
        stmt.close();
        conn.close();
    }


    public Branch getBranch(int branchId) throws SQLException{
        int key = branchId;
        if(branchMap.containsKey(key)){
            return branchMap.get(key);
        }
        Connection conn = Database.connect();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Branches WHERE id = ?");
        stmt.setInt(1, key);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            Branch branch =  new Branch(rs.getInt(1), LocalDate.parse(rs.getString(3)), rs.getInt(2));
            branchMap.put(key, branch);
            stmt.close();
            conn.close();
            return branch;
        } else {
            stmt.close();
            conn.close();
            return null;
        }    
    }

    public void updateBranch(Branch b) throws SQLException{
        Integer key = b.getBranchId();
        branchMap.put(key, b);
        Connection conn = Database.connect();
        PreparedStatement stmt = conn.prepareStatement("UPDATE Branches SET startTime = ?, timeStep = ? WHERE id = ?");
        stmt.setString(1, b.getStartTime().toString());
        stmt.setInt(2, b.getTimeStep());
        stmt.setInt(3, b.getBranchId());
        stmt.executeUpdate();
        stmt.close();
        conn.close();
    }

    public ArrayList<Branch> getAll() throws SQLException{
        ArrayList<Branch> branches = new ArrayList<>();
        if(gotAll){
            branches.addAll(branchMap.values());
            return branches;
        }
        Connection conn = Database.connect();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Branches");
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Branch branch = new Branch(rs.getInt(1), LocalDate.parse(rs.getString(3)), rs.getInt(2));
            if(!branchMap.containsKey(branch.getBranchId())) {
                branches.add(branch);
                branchMap.put(branch.getBranchId(), branch);
            }else{
                branches.add(branchMap.get(branch.getBranchId()));
            }
        }
        stmt.close();
        conn.close();
        gotAll = true;
        return branches;
    }
}
