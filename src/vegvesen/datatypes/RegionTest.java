package vegvesen.datatypes;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vegvesen.DatabaseController;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class RegionTest {
    DatabaseController old;
    DatabaseController dc;
    @BeforeEach
    void setUp() {
        dc = DatabaseController.GetTestInstance();
        old = Kategori.controller;
        Kategori.controller = dc;

    }

    @Test
    void CreateNewRegionByText() {
        try {
            Region f = new Region("Din mor");
            assertNotEquals(null, f.getId());
            assertEquals((int)f.getId(), 1);
        } catch (SQLException e) {
            e.printStackTrace();
            fail("SQL Error");
        }
    }
    @Test
    void GetRegionByID() {
        try {
            Region f = new Region("Din mor");
            f = new Region(1);
            assertNotEquals(null, f.getId());
            assertEquals(f.toString(), new Region(f.getId()).toString());
            assertEquals((int)f.getId(), 1);
        } catch (SQLException e) {
            e.printStackTrace();
            fail("SQL Error");
        }
    }


    @Test
    void GetNextID() {
        try {
            Region f1 = new Region("abc");
            Region f2 = new Region("bcd");
            Region f3 = new Region("cde");
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
            Region tmp = new Region("test-Region-save");
            assertNotEquals(null, tmp.getId());
            tmp.setName("test-Region-save2");
            tmp.Save();

            assertEquals(tmp.toString(), new Region(tmp.getId()).toString());
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