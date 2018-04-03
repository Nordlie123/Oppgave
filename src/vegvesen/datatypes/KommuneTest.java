package vegvesen.datatypes;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vegvesen.DatabaseController;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class KommuneTest {
    DatabaseController old;
    DatabaseController dc;
    @BeforeEach
    void setUp() {
        dc = DatabaseController.GetTestInstance();
        old = Kategori.controller;
        Kategori.controller = dc;

    }

    @Test
    void CreateNewKommuneByText() {
        try {
            Kommune f = new Kommune("Din mor");
            assertNotEquals(null, f.getId());
            assertEquals((int)f.getId(), 1);
        } catch (SQLException e) {
            e.printStackTrace();
            fail("SQL Error");
        }
    }
    @Test
    void GetKommuneByID() {
        try {
            Kommune f = new Kommune("Din mor");
            f = new Kommune(1);
            assertNotEquals(null, f.getId());
            assertEquals(f.toString(), new Kommune(f.getId()).toString());
            assertEquals((int)f.getId(), 1);
        } catch (SQLException e) {
            e.printStackTrace();
            fail("SQL Error");
        }
    }


    @Test
    void GetNextID() {
        try {
            Kommune f1 = new Kommune("abc");
            Kommune f2 = new Kommune("bcd");
            Kommune f3 = new Kommune("cde");
            assertEquals((int) f1.id, 1);
            assertEquals((int) f2.id, 2);
            assertEquals((int) f3.id, 3);
        } catch(SQLException e) {
            e.printStackTrace();
            fail("SQL Error");
        }
    }

    @Test
    void CanSave() {

        try {
            Kommune tmp = new Kommune("test-Kommune-save");
            assertNotEquals(null, tmp.getId());
            tmp.setName("test-Kommune-save2");
            tmp.Save();

            assertEquals(tmp.toString(), new Kommune(tmp.getId()).toString());
        } catch (SQLException e) {
            e.printStackTrace();
            fail("SQL Error");
        }
    }

    @AfterEach
    void tearDown() {
        try {
            dc.Close();
            Kategori.controller = old;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}