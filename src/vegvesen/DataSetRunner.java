package vegvesen;

import jdk.nashorn.internal.runtime.regexp.joni.Regex;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.*;

public class DataSetRunner {
    String[] datafiles;
    Integer currentFileIndex = 0;
    BufferedReader currentFile;

    public DataSetRunner(String folder) throws IOException {
        this.datafiles = Files.list(Paths.get(folder)).map(x -> x.toString()).toArray(String[]::new);

        this.LoadNextFile(false);
    }

    public String next() {
        try {


            String line = this.currentFile.readLine();
            int attempts = 0;
            while (line == null) {
                this.LoadNextFile();
                line = this.currentFile.readLine();
                if (++attempts == 200) {
                    System.out.println("200 nulls");
                    System.exit(-1);
                }
            }
            if (this.IsAtEnd()) {
                return null;
            }

            return line;
        } catch(IOException ex) {
            System.out.println(ex.getMessage());
            System.out.println("......");
            System.exit(-1);
        }
        return "-------";
    }

    private boolean IsAtEnd() {
        return this.currentFileIndex >= this.datafiles.length;
    }
    private void LoadNextFile() {
        this.LoadNextFile(true);
    }
    private void LoadNextFile(boolean skipFirst) {
        try {
            if(this.currentFile != null) {
                this.currentFile.close();
            }
            this.currentFile = new BufferedReader(new FileReader(datafiles[currentFileIndex++]));
            if(skipFirst)
                this.currentFile.readLine();
        } catch(FileNotFoundException ex) {
            System.out.println(ex.getMessage());
            System.exit(-1);
        } catch(IOException ex) {
            System.out.println(ex.getMessage());
            System.exit(-1);
        }

    }

    public static String CoordString(Float[] f) {
        return "(x:" + f[0] + ", y:" + f[1] + ", z:" + f[2] + ")";
    }
    public static void main(String[] args) {


        DatabaseController db = DatabaseController.GetInstance();
        DataObject d = new DataObject(new String[]{});
        try {
            DataSetRunner dsr = new DataSetRunner("ds");

            String[] headers = dsr.next().split(";");
            if(DataObject.headers == null) {
                Arrays.stream(headers).map(str -> str.replace("\"", "")).forEach(str -> DataObject.addHeader(str));
            }
            String s = dsr.next();
            Integer objectsSearched = 0;
            long start_time = System.nanoTime();
            long checkpointTime = start_time;
            while(s != null) {
                if(objectsSearched % 1000 == 0) {
                    System.out.println("Added " + objectsSearched + " entries");
                    long now_time = System.nanoTime();
                    System.out.println("Time elapsed: \t" + (now_time - start_time) / 1000000000 + "s");
                    System.out.println("Checkpoint: \t" + (now_time - checkpointTime) /  1000000 + "ms");
                    checkpointTime = now_time;
                }
                objectsSearched++;
                d = DataObject.MakeNextDataObject(dsr);
                db.addDataObject(d);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println(d.toString());
            e.printStackTrace();

        }
    }
}
