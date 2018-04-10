package vegvesen.filehandler;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataObjectTest {
    @Test
    void get() {
        DataObject d = new DataObject(new String[]{"a", "b"}, new String[]{"123", "234"});
        assertEquals(d.get("a"), "123");
        assertEquals(d.get("bb"), null);
    }

}