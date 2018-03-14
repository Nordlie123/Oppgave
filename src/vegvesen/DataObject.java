package vegvesen;

import javax.xml.crypto.Data;
import java.util.HashMap;

public class DataObject {
    public static HashMap<String, Integer> headers;

    public static void addHeader(String name) {
        if(DataObject.headers == null)
            DataObject.headers = new HashMap<>();
        DataObject.headers.put(name, DataObject.headers.values().stream().mapToInt( x -> x.intValue() ).max().getAsInt() +1 );
    }

    private String[] data;
    public DataObject(String[] data) {
        this.data = data;
    }

    public String get(int i) {
        return this.data[i];
    }
    public String get(String i) {
        return this.data[DataObject.headers.get(i)];
    }
}
