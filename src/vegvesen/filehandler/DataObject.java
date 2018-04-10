package vegvesen.filehandler;

import com.sun.xml.internal.ws.util.StreamUtils;

import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Abstraction of the String[] data
 */
public class DataObject {
    HashMap<String, Object> keyValues;
    public DataObject(String[] headers, String[] values) {
        keyValues = new HashMap<>();
        for(int i = 0; i < headers.length; i++) {
            keyValues.put(headers[i], values[i]);
        }
    }

    public Object get(String key) {
        return this.keyValues.get(key);
    }
}
