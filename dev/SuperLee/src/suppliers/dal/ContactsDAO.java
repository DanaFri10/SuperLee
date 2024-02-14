package suppliers.dal;
import jdk.jshell.spi.ExecutionControl;
import shared.Database;
import suppliers.business.Contact;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactsDAO {
    private Connection conn;
    private Map<String, Contact> contacts;
    private boolean gotAll;

    public ContactsDAO() throws SQLException {
        contacts = new HashMap<>();
        gotAll = false;

        conn = Database.connect();
    }

    public Contact getContact(String contactId) throws SQLException {
        if(!contacts.containsKey(contactId))
        {
            try
            {
                conn = Database.connect();
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Contacts WHERE id = ?");
                stmt.setString(1, contactId);
                ResultSet rs = stmt.executeQuery();
                if(!rs.isBeforeFirst() && rs.getRow() != 0)
                    throw new IllegalArgumentException("This contact does not exist.");
                contacts.put(contactId, new Contact(contactId, rs.getString("name"), rs.getString("phone_number"), rs.getString("email"), rs.getString("address")));
            }
            catch(SQLException e)
            {
                throw e;
            }
            finally {
                conn.close();
            }
        }
        return contacts.get(contactId);
    }

    public List<Contact> getAllContacts() throws SQLException {
        if(!gotAll) {
            try {
                conn = Database.connect();
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Contacts");
                ResultSet rs = stmt.executeQuery();
                while (rs.next())
                    if (!contacts.containsKey(rs.getString("id")))
                        contacts.put(rs.getString("id"), new Contact(rs.getString("id"), rs.getString("name"), rs.getString("phone_number"), rs.getString("email"), rs.getString("address")));
                gotAll = true;
            }
            catch(SQLException e)
            {
                throw e;
            }
            finally {
                conn.close();
            }
        }
        return contacts.values().stream().toList();
    }

    public void addContact(String contactId, String name, String phoneNumber, String email, String address) throws SQLException {
        if(contactExists(contactId))
            throw new IllegalArgumentException("A contact with ID " + contactId + " already exists.");

        Contact contact = new Contact(contactId, name, phoneNumber, email, address);

        try {
            conn = Database.connect();
            PreparedStatement stmt1 = conn.prepareStatement("INSERT INTO Contacts (id, name, phone_number, address, email) VALUES (?, ?, ?, ?, ?)");
            stmt1.setString(1, contactId);
            stmt1.setString(2, name);
            stmt1.setString(3, phoneNumber);
            stmt1.setString(4, address);
            stmt1.setString(5, email);
            stmt1.execute();

            contacts.put(contactId, contact);
        }
        catch(SQLException e)
        {
            throw e;
        }
        finally {
            conn.close();
        }
    }

    public void removeContact(String contactId) throws SQLException {
        if(contacts.containsKey(contactId)) {
            contacts.remove(contactId);
            try {
                conn = Database.connect();
                PreparedStatement stmt1 = conn.prepareStatement("DELETE FROM Contacts WHERE id = ?");
                stmt1.setString(1, contactId);
                stmt1.execute();

                PreparedStatement stmt2 = conn.prepareStatement("DELETE FROM SuppliersContacts WHERE contactId = ?");
                stmt2.setString(1, contactId);
                stmt2.execute();

                PreparedStatement stmt3 = conn.prepareStatement("DELETE FROM OrderContacts WHERE contactId = ?");
                stmt3.setString(1, contactId);
                stmt3.execute();

                PreparedStatement stmt4 = conn.prepareStatement("UPDATE PeriodicOrders SET contactId = NULL WHERE contactId = ?");
                stmt4.setString(1, contactId);
                stmt4.execute();
            }
            catch(SQLException e)
            {
                throw e;
            }
            finally {
                conn.close();
            }
        }
    }

    public void updateContactInfo(String contactId, String name, String phoneNumber, String email, String address) throws SQLException {
        Contact toUpdate = getContact(contactId);
        toUpdate.updateContactInfo(name, phoneNumber, email, address);
        try {
            conn = Database.connect();
            PreparedStatement stmt = conn.prepareStatement("UPDATE Contacts SET name = ?, phone_number = ?, email = ?, address = ? WHERE id = ?");
            stmt.setString(1, name);
            stmt.setString(2, phoneNumber);
            stmt.setString(3, email);
            stmt.setString(4, address);
            stmt.setString(5, contactId);

            stmt.execute();
        }
        catch(SQLException e)
        {
            throw e;
        }
        finally {
            conn.close();
        }
    }

    public void clearData() throws SQLException {
        contacts.clear();

        try {
            conn = Database.connect();
            PreparedStatement stmt1 = conn.prepareStatement("DELETE FROM Contacts");
            stmt1.execute();

            PreparedStatement stmt2 = conn.prepareStatement("DELETE FROM SuppliersContacts");
            stmt2.execute();

            PreparedStatement stmt3 = conn.prepareStatement("UPDATE OrderContacts SET contactId = NULL");
            stmt3.execute();

            PreparedStatement stmt4 = conn.prepareStatement("UPDATE PeriodicOrders SET contactId = NULL");
            stmt4.execute();
        }
        catch(SQLException e)
        {
            throw e;
        }
        finally {
            conn.close();
        }
    }

    public boolean contactExists(String contactId) throws SQLException {
        if(contacts.containsKey(contactId)) return true;

        try {
            conn = Database.connect();
            Statement contactExistsStmt = conn.createStatement();
            ResultSet contactsRecords = contactExistsStmt.executeQuery(String.format("SELECT * FROM Contacts WHERE id = \"%s\"", contactId));
            if (contactsRecords.next())
                return true;
            return false;
        }
        catch(SQLException e)
        {
            throw e;
        }
        finally {
            conn.close();
        }
    }

}
