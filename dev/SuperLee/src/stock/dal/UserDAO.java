package stock.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import shared.Database;
import stock.business.User;
import stock.business.UserRole;

public class UserDAO {
    private Map<String, User> usersMap;

    public UserDAO() throws SQLException{
        usersMap = new HashMap<>();
    }
    
    public void insertNewUser(User user) throws SQLException{
        Connection conn = Database.connect();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Users (username, password, role) VALUES (?, ?, ?)");
        stmt.setString(1, user.getUserName());
        stmt.setString(2, user.getPassword());
        stmt.setInt(3, user.getRole().getValue());
        stmt.executeUpdate();
        String key = user.getUserName();
        usersMap.put(key, user);
        stmt.close();
        conn.close();
        
    }

    public User getUser(String username) throws SQLException{
        String key = username;    
        if(usersMap.containsKey(key)){
            return usersMap.get(key);
        }
        Connection conn = Database.connect();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Users WHERE username = ?");
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            User user = new User(rs.getString(1), rs.getString(2), UserRole.fromInteger(rs.getInt(3)));
            usersMap.put(key, user);
            stmt.close();
            conn.close();
            return user;
        } else {
            stmt.close();
            conn.close();
            return null;
        }
    }
}
