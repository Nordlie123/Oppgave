package vegvesen.filehandler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Stream;

/**
 *  En filhåndterer for CSV filene vi har generert fra datasettet vårt.
 *  Den vil ta første linje og bruke dem som headers
 *
 *  @see DataSet
 *  @author Tommy Stigen Olsen
 */
public class DataFileParser {
    String[] headers;
    String[][] data;
    BufferedReader br;

    /**
     * Creates new DAtaFileParser
     * @param String filename in string
     * @throws FileNotFoundException
     */
    public DataFileParser(String file) throws FileNotFoundException {
        br = new BufferedReader(new FileReader(file));
        try {
            this.headers = SplitAndTrim(";", br.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returnerer alle linjer som enda ikke er lest som string
     * @return
     */
    public Stream<String> lines() {
        return this.br.lines();
    }

    public Stream<String[]> parsedLines() throws IOException {
        return this.lines().map(x -> this.SplitAndTrim(";", x));
    }

    public String[] getNextLine(){
        String s = null;
        try {
            s = br.readLine();
        } catch (IOException e) {
        }
        if(s == null) {
            return null;
        }
        return SplitAndTrim(";", s);
    }

    public String[] getHeaders() { return this.headers; }
    private String[] SplitAndTrim(String separator, String text) {
        return Arrays.stream(text.split(separator))
                .map( str -> str.toString().trim().replace("\"", ""))
                .toArray(String[]::new);
    }
}
