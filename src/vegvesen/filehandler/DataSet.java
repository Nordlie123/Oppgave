package vegvesen.filehandler;



import sun.misc.Regexp;

import javax.naming.InvalidNameException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Tar en serie med filer i en mappe og parser dem med DataFileParseren
 * @see DataFileParser
 * @author Tommy Stigen Olsen
 */
public class DataSet {
    int current_file = 0;
    Path[] files;
    DataFileParser current_dfs;
    String[] headers = null;
    public DataSet(Path folder, Pattern fileMatcher) {
        try {
            files = Files.list(folder)
                    .filter( file -> fileMatcher.matcher(file.getFileName().toString()).matches() )
                    .toArray(Path[]::new);
            this.loadNextFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    public String getValueByTextKey(String key, String[] valueSet) throws InvalidNameException {
        if(Arrays.stream(this.headers).anyMatch(header -> header.equals(key)) == false)
            throw new InvalidNameException("The key you supplied is not in the headers");
        return valueSet[Arrays.asList(headers).indexOf(key)];
    }
    /**
     * Returns a String[] splitted Stream that returns all the lines of all the files
     * Do not
     * @return
     */
    public Stream<String[]> parsedLines() {
        return Arrays.stream(this.files).map(x -> {
            try {
                return new DataFileParser(x.toString()).parsedLines();
            } catch (Exception e) {
                return null;
            }
        }).filter( x -> x != null)
                .reduce(Stream::concat)
                .orElseGet(Stream::empty);

    }
    public Path[] getFiles() { return this.files; }


    private void loadNextFile() {
        try {
            current_dfs = new DataFileParser(files[current_file++].toAbsolutePath().toString());
            if(headers == null) {
                this.headers = current_dfs.headers;
            }
            if(this.headers != current_dfs.headers) {
                System.out.println("The headers of the files you loaded does not match");
                System.out.println(-127);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
