package vegvesen.filehandler;

import javafx.scene.shape.Path;
import org.junit.jupiter.api.Test;

import javax.naming.InvalidNameException;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class DataSetTest {
    @Test
    void getValueByTextKey() {
        DataSet ds = new DataSet(Paths.get("./test_fixtures/"), Pattern.compile("data_file_parser_input\\d?.txt"));
        String[] first = ds.parsedLines().findFirst().orElse(new String[]{});

        try {
            assertEquals("835320765", ds.getValueByTextKey("vegobjektid", first));
            assertThrows(InvalidNameException.class, () -> ds.getValueByTextKey("kakekekeke", first));
        } catch (InvalidNameException e) {
            e.printStackTrace();
        }


    }

    @Test
    void RegexWorks() {
        DataSet ds = new DataSet(Paths.get("./test_fixtures/"), Pattern.compile("regex_test\\d+\\.csv"));
        String[] filenames = Arrays.stream(ds.getFiles()).map(x -> x.getFileName().toString()).toArray(String[]::new);
        assertEquals(2, filenames.length);
        assertEquals(true, Arrays.stream(filenames).anyMatch( x -> x.equals("regex_test02.csv")));
        assertEquals(true, Arrays.stream(filenames).anyMatch( x -> x.equals("regex_test01.csv")));
    }

    static Integer i = 0;
    private static void increaseI() {
        i++;
    }
    @Test
    void parsedLinesNoHeadersInResults() {
        DataSet ds = new DataSet(Paths.get("./test_fixtures/"), Pattern.compile("data_file_parser_input\\d?.txt"));
        boolean contains_headers = ds.parsedLines().anyMatch(x -> Arrays.stream(x).anyMatch(y -> y.equals("vegobjektid")));
        assertEquals(false, contains_headers);
    }
}