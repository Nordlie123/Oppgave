package vegvesen.datatypes;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vegvesen.DataObject;
import vegvesen.DatabaseController;

import javax.xml.crypto.Data;

import java.sql.SQLException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class VegObjektTest {
    DatabaseController dc;
    DatabaseController old;

    VegObjekt vo;
    @BeforeEach
    void setUp() {
        dc = DatabaseController.GetTestInstance();
        old = Kategori.controller;
        Kategori.controller = dc;
        VegObjekt.controller = dc;
        try {
            Fylke f = new Fylke("ABC");
            Region r = new Region("District 9");
            Kommune k = new Kommune("Kommunen");

            String[] headers = "\"vegobjektid\";\"type_id\";\"versjonid\";\"startdato\";\"sistmodifisert\";\"Bruksklasse\";\"Bruksklasse vinter\";\"Maks vogntoglengde\";\"Strekningsbeskrivelse\";\"Maks totalvekt kj�ret�y, skiltet\";\"Maks totalvekt vogntog, skiltet\";\"Merknad\";\"Veglisteversjon\";\"kommune\";\"fylke\";\"region\";\"vegavdeling\";\"riksvegrute\";\"kontraktsomr�de\";\"vegreferanse\";\"fylkesnummer\";\"kommunenummer\";\"vegkategori\";\"vegstatus\";\"vegnummer\";\"fra hp\";\"til hp\";\"fra meter\";\"til meter\";\"stedfesting\";\"geometri\";\"srid\"\n".split(";");
            if(DataObject.headers == null) {
                Arrays.stream(headers).map(str -> str.replace("\"", "")).forEach(str -> DataObject.addHeader(str));
            }
            String[] vegObjektString = "\"834319854\";\"904\";\"4\";\"2014-01-14\";\"2017-10-01T17:03:58\";\"Bk10 - 50 tonn\";;\"19,50\";;;;;\"20171001\";\"Kommunen\";\"ABC\";\"District 9\";\"ABC\";\"\";\"\";\"0123 Kv1031 hp1 m0-200\";\"1\";\"123\";\"K\";\"V\";\"1031\";\"1\";\"1\";\"0\";\"200\";\"946251 0.0 1.0 WITH N/A \";\"LINESTRING Z (278324.1 6614999.2 117.9, 278326 6614998.1001 118, 278348.40002 6614985.2002 120.5, 278366.59998 6614970.8501 121.4, 278369.47778 6614969.0415 121.4, 278389.80005 6614957.69971 121.9, 278410 6614944.69971 121.8, 278430 6614930.30029 121.4, 278443.19995 6614919.3999 120.7, 278455.80005 6614902 120.6, 278459.80005 6614889.8999 120.1, 278463.21484 6614876.49854 120.1, 278465.25116 6614867.91992 119.41)\";\"32633\"\n".split(";");
            vegObjektString = Arrays.stream(vegObjektString)
                    .map(item -> item.trim())
                    .map(item -> item.replace("\"", ""))
                    .toArray(String[]::new);

            dc.addDataObject(new DataObject(vegObjektString));
            vo = VegObjekt.GetAll()[0];

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @AfterEach
    void tearDown() {
        try {
            dc.Close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Kategori.controller = old;
        VegObjekt.controller = old;
    }

    @Test
    void getKommune() {
        assertEquals(vo.getKommune().getName(), "Kommunen");
    }

    @Test
    void getFylke() {
        assertEquals(vo.getFylke().getName(), "ABC");
    }

    @Test
    void getRegion() {
        assertEquals(vo.getRegion().getName(), "District 9");
    }

}