package vegvesen.datatypes;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vegvesen.DatabaseController;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class FylkeTest {
    DatabaseController old;
    DatabaseController dc;
    @BeforeEach
    void setUp() {
        dc = DatabaseController.GetTestInstance();
        old = Kategori.controller;
        Kategori.controller = dc;

    }

    @Test
    void CreateNewFylkeByText() {
        try {
            Fylke f = new Fylke("Din mor");
            assertNotEquals(null, f.getId());
            assertEquals((int)f.getId(), 1);
        } catch (SQLException e) {
            e.printStackTrace();
            fail("SQL Error");
        }
    }
    @Test
    void GetFylkeByID() {
        try {
            Fylke f = new Fylke("Din mor");
            f = new Fylke(1);
            assertNotEquals(null, f.getId());
            assertEquals(f.toString(), new Fylke(f.getId()).toString());
            assertEquals((int)f.getId(), 1);
        } catch (SQLException e) {
            e.printStackTrace();
            fail("SQL Error");
        }
    }


    @Test
    void GetNextID() {
        try {
            Fylke f1 = new Fylke("abc");
            Fylke f2 = new Fylke("bcd");
            Fylke f3 = new Fylke("cde");
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
            Fylke tmp = new Fylke("test-fylke-save");
            assertNotEquals(null, tmp.getId());
            tmp.setName("test-fylke-save2");
            tmp.Save();

            assertEquals(tmp.toString(), new Fylke(tmp.getId()).toString());
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