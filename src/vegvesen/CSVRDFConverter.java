package vegvesen;

import com.sun.xml.internal.ws.util.StreamUtils;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CSVRDFConverter {
    BufferedReader br;
    String[] headers;
    int objectKey;

    public CSVRDFConverter(Reader fis, int objectKey) {
        br = new BufferedReader(fis);
        objectKey = objectKey;
        try {
            headers = Arrays.stream(br.readLine().split(";"))
                    .map(x -> x.replace(" ", "_"))
                    .map(x -> x.replace("\"", ""))
                    .map(x -> {
                        try {
                            return new String(new String(x.getBytes(), "UTF-8").getBytes("UTF-8"));
                        }
                        catch(UnsupportedEncodingException ex) { return ""; }

                    })
                    .map(x -> x.toLowerCase())
                    .toArray(String[]::new);

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return;
        }
    }


    public String[] toRDF(String prefix) {
        return br.lines()
                .map(x -> x.split(";"))
                .map(x -> String.join("\r\n", createPredicates(x, objectKey, headers, prefix)))
                .toArray(String[]::new);
    }

    public void toRDFFile(String filename, String prefix) throws IOException{
            FileWriter fw = new FileWriter(filename);
            BufferedWriter bw = new BufferedWriter(fw);
            String data = String.join("\r\n", this.toRDF(prefix));
            bw.write(data, 0, data.length());
            bw.close();
            fw.close();
    }
    public static String[] createPredicates(String[] values, int keyId, String[] headers, String prefix) {
        String s = "";
        String c_prefix = prefix + ":" + values[keyId];

        List<String> tmpHeaders = Arrays.stream(headers).collect(Collectors.toList());
        String[] filteredHeaders = IntStream.range(0, headers.length)
                .filter(x -> x != keyId)
                .mapToObj(tmpHeaders::get)
                .toArray(String[]::new);
        List<String> tmpValues = Arrays.stream(values).collect(Collectors.toList());
        String[] filteredValues = IntStream.range(0, values.length)
                .filter(x -> x != keyId)
                .mapToObj(tmpValues::get)
                .map(x -> Pattern.compile("^[0-9\\.\"]+$").matcher(x).matches() ? x.replace("\"", ""): x)
                .toArray(String[]::new);

        return IntStream.range(0, Math.min(filteredHeaders.length, filteredValues.length))
                .mapToObj(x -> c_prefix + " " + prefix + ":" + filteredHeaders[x] + " " + filteredValues[x])
                .toArray(String[]::new);
    }

    public String[] getHeaders() { return this.headers; }

    public static void main(String[] args) {
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream("ds/datasplit0.csv"), "ISO-8859-1");
            CSVRDFConverter c = new CSVRDFConverter(isr, 0);
            Arrays.stream(c.toRDF("veg")).forEach(x -> System.out.println(x));
            //Arrays.stream(c.getHeaders()).forEach(x -> System.out.println(x));
            //c.toRDFFile("ds/datasplit0.rdf", "veg");
        } catch(FileNotFoundException fnf) {
            System.out.println(fnf.getMessage());
        } catch(UnsupportedEncodingException fnf) {
            System.out.println(fnf.getMessage());
        } catch(IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
