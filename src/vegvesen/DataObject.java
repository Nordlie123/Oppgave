package vegvesen;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;

import static java.util.regex.Pattern.compile;

public class DataObject {
    public static HashMap<String, Integer> headers;

    public static void addHeader(String name) {
        if(DataObject.headers == null)
            DataObject.headers = new HashMap<>();

        int i = -1;
        if(DataObject.headers.values().stream().mapToInt( x -> x.intValue() ).max().isPresent())
            i = DataObject.headers.values().stream().mapToInt( x -> x.intValue() ).max().getAsInt();
        DataObject.headers.put(name,  i +1 );
    }

    private String[] data;
    public DataObject(String[] data) {
        this.data = data;
    }

    public String get(int i) {
        return this.data[i];
    }
    public String get(String i) {
        try {
            return this.data[DataObject.headers.get(i)];
        } catch(ArrayIndexOutOfBoundsException ex) {
            System.out.println("Det er noe rart med " + String.join("; ", this.data));
            System.out.println(this);
            System.exit(-1);
            return "";
        }
    }
    public String toString() {
        StringBuffer sb = new StringBuffer();
        DataObject.headers.entrySet().forEach(es -> {
            sb.append(es.getKey() + " = " + (this.data.length > es.getValue() ? this.data[es.getValue()] : "OUTOFBOUNDS") + "\r\n");
        });
        return sb.toString();
    }
    public String combinedRoadID() {
        return this.get("vegstatus") + this.get("vegnummer");
    }
    public Float[][] getCoords() {
        String geo = this.get("geometri");

        Float[] tall = null;
        ArrayList<String> l = new ArrayList<>();
        Matcher m = compile("(\\d+(?:\\.\\d+)?)").matcher(geo);

        while(m.find()) {
            l.add(m.group());
        }

        tall = l.stream().map(x -> Float.parseFloat(x)).toArray(Float[]::new);

        ArrayList<Float[]> ok = new ArrayList<>();


        //System.out.println("tl:" + tall.length);
        for(int i = 0; i+3 < tall.length; i = i + 3) {
            try {
                ok.add(new Float[]{tall[i], tall[i + 1], tall[i + 2]});
            } catch(java.lang.ArrayIndexOutOfBoundsException ex) {
                System.out.println(this.get("vegobjektid") + " har noe fukk med geometrien");
            }
        }
        Float[][] ret = new Float[ok.size()][3];
        for(int i = 0; i < ok.size(); i++) {
            for(int x = 0; x < 3; x++) {
                ret[i][x] = ok.get(i)[x];
            }
        }

        return ret;

    }
    public static DataObject MakeNextDataObject(DataSetRunner dsr) {
        String tmp = dsr.next();
        if (tmp == null) {
            return null;
        }
        while(tmp.split(";").length < DataObject.headers.size()) {
            String tmp2 = dsr.next();
            if(tmp2 == null) {
                return null;
            }
            tmp = tmp + tmp2;
        }

        return new DataObject(Arrays.stream(tmp.split(";"))
                .map(str -> str.replace("\"", ""))
                .toArray(String[]::new));
    }
    public static DataObject Search(String field, String searchParam) {
        try {
            DataSetRunner dsr = new DataSetRunner("ds/");

            String[] headers = dsr.next().split(";");
            if(DataObject.headers == null) {
                Arrays.stream(headers).map(str -> str.replace("\"", "")).forEach(str -> DataObject.addHeader(str));
            }

            DataObject data = DataObject.MakeNextDataObject(dsr);
            while(data != null)
            {
                if(data.get(field).equals(searchParam))
                    return data;
                data = DataObject.MakeNextDataObject(dsr);
            }

            return null;
        } catch(IOException ex) {
            System.out.println(ex.getMessage());
            System.exit(-1);
        }
        return null;
    }
    public static DataObject[] SearchForConnectingRoads(Float[] searchParam) {
        try {
            DataSetRunner dsr = new DataSetRunner("ds/");

            ArrayList<DataObject> results = new ArrayList<>();
            String[] headers = dsr.next().split(";");
            if(DataObject.headers == null) {
                Arrays.stream(headers).map(str -> str.replace("\"", "")).forEach(str -> DataObject.addHeader(str));
            }

            DataObject data = DataObject.MakeNextDataObject(dsr);
            while(data != null)
            {
                if(Arrays.stream(data.getCoords()).map(x -> Arrays.equals(x, searchParam)).filter(x -> x == true).count() > 0)
                    results.add(data);
                data = DataObject.MakeNextDataObject(dsr);
            }
            if(results.size() == 0)
                return null;
            DataObject[] tmp = new DataObject[results.size()];
            for(int i = 0; i < results.size(); i++)
                tmp[i] = results.get(i);
            return tmp;
        } catch(IOException ex) {
            System.out.println(ex.getMessage());
            System.exit(-1);
        }
        return null;
    }
}
