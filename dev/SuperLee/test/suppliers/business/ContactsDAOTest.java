package suppliers.business;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shared.Database;
import suppliers.dal.ContactsDAO;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ContactsDAOTest {
    private ContactsDAO contactsDAO;

    @BeforeEach
    void setUpBeforeAll()
    {
        try
        {
            Database.setPath("../SuperLeeTestDB.db");
            Database.clearDatabase();

            contactsDAO = new ContactsDAO();
            contactsDAO.addContact("948271000", "Emmy", "050-1046298", "emmy@gmail.com", "Emmy's house");
            contactsDAO.addContact("581098243", "John", "051-5427612", "john@gmail.com", "John's house");
            contactsDAO.addContact("209879109", "Alice", "050-0987143", "alice@gmail.com", "Alice's house");
            contactsDAO.addContact("427651987", "Bob", "052-1769385", "bob@gmail.com", "Bob's house");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    /*
    @AfterEach
    void cleanUp()
    {
        try {
            contactsDAO.removeContact("948271000");
            contactsDAO.removeContact("581098243");
            contactsDAO.removeContact("209879109");
            contactsDAO.removeContact("427651987");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }*/

    @Test
    void contactExists() {
        boolean exp = false;
        try
        {
            assertTrue(contactsDAO.contactExists("948271000"));
            assertTrue(contactsDAO.contactExists("581098243"));
            assertTrue(contactsDAO.contactExists("209879109"));
            assertTrue(contactsDAO.contactExists("427651987"));

            assertFalse(contactsDAO.contactExists("091847263"));
            assertFalse(contactsDAO.contactExists("174920987"));
            assertFalse(contactsDAO.contactExists("451837465"));
            assertFalse(contactsDAO.contactExists("109820663"));
        }
        catch (SQLException e) {
            exp = true;
        }
        assertFalse(exp);
    }

    @Test
    void removeContactShouldWork() {
        boolean exp = false;
        try
        {
            contactsDAO.removeContact("948271000");
            assertFalse(contactsDAO.contactExists("948271000"));
            assertTrue(contactsDAO.contactExists("581098243"));
            assertTrue(contactsDAO.contactExists("209879109"));
            assertTrue(contactsDAO.contactExists("427651987"));

            contactsDAO.removeContact("581098243");
            assertFalse(contactsDAO.contactExists("948271000"));
            assertFalse(contactsDAO.contactExists("581098243"));
            assertTrue(contactsDAO.contactExists("209879109"));
            assertTrue(contactsDAO.contactExists("427651987"));

            contactsDAO.removeContact("209879109");
            assertFalse(contactsDAO.contactExists("948271000"));
            assertFalse(contactsDAO.contactExists("581098243"));
            assertFalse(contactsDAO.contactExists("209879109"));
            assertTrue(contactsDAO.contactExists("427651987"));

            contactsDAO.removeContact("427651987");
            assertFalse(contactsDAO.contactExists("948271000"));
            assertFalse(contactsDAO.contactExists("581098243"));
            assertFalse(contactsDAO.contactExists("209879109"));
            assertFalse(contactsDAO.contactExists("427651987"));
        }
        catch (SQLException e) {
            exp = true;
        }
        assertFalse(exp);
    }


}