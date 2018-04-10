package vegvesen.filehandler;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class DataFileParserTest {

    /**
     * Tests that the DataFileParser is able to get the correct headers.
     */
    @Test
    void testHeaders() {
        try {
            assertArrayEquals((new DataFileParser("test_fixtures/data_file_parser_input.txt")).getHeaders(),
                    new String[]{
                            "vegobjektid",
                            "type_id",
                            "versjonid",
                            "startdato",
                            "sistmodifisert",
                            "Bruksklasse",
                            "Bruksklasse vinter",
                            "Maks vogntoglengde",
                            "Strekningsbeskrivelse",
                            "Maks totalvekt kj�ret�y, skiltet",
                            "Maks totalvekt vogntog, skiltet",
                            "Merknad",
                            "Veglisteversjon",
                            "kommune",
                            "fylke",
                            "region",
                            "vegavdeling"
                            ,"riksvegrute",
                            "kontraktsomr�de",
                            "vegreferanse",
                            "fylkesnummer",
                            "kommunenummer",
                            "vegkategori",
                            "vegstatus",
                            "vegnummer",
                            "fra hp",
                            "til hp",
                            "fra meter",
                            "til meter",
                            "stedfesting",
                            "geometri",
                            "srid"
                    });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tests that the Lines function skips headers
     */
    @Test
    void testHeadersSkippedInLines() {
        try {
            String s = (new DataFileParser("test_fixtures/data_file_parser_input.txt")).lines().limit(1).findFirst().get();
            assertEquals("\"835320765\";\"904\";\"1\";\"1980-01-01\";\"2018-02-15T10:29:43\";\"Bk10 - 50 tonn\";;\"12,40\";\"Oddahagen\";;;;\"20171001\";\"Stavanger\";\"Rogaland\";\"VEST\";\"Rogaland\";\"\";\"\";\"1103 Kv1746 hp2 m0-59\";\"11\";\"1103\";\"K\";\"V\";\"1746\";\"2\";\"2\";\"0\";\"59\";\"284179 0.0 1.0 WITH N/A \";\"LINESTRING Z (-32212.7 6564838.95 7.13, -32209.8 6564840.5 7.1, -32206.8 6564842.4 7.1, -32198.6 6564848.5 6.7, -32191.1 6564855.9 6.1, -32190.7 6564856.4 6.1, -32185.7 6564864.1 5.8, -32185.3 6564865 5.8, -32182.1 6564874.1 5.5, -32181.3 6564876.6 5.4, -32179.5 6564881.3 5.2)\";\"32633\"", s);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}