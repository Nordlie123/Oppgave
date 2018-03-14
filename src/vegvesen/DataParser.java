package vegvesen;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class DataParser {
    String headers;
    String fp;
    BufferedReader br;
    Path p;


    public static void main(String[] args) {
        try {
            Path p = Paths.get("ds");
            if (!Files.exists(p)) {
                Files.createDirectory(p);
            }
            DataParser dp = new DataParser(new FileReader("904-eksport.csv"), "datasplit", p);
            dp.generateSubFiles();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace(System.out);
        }
    }

    public void generateSubFiles() {

        try {
            int file = 0;
            ArrayList<String> lines = new ArrayList<>();
            String tmp = this.br.readLine();
            while(tmp != null)
            {
                lines.add(tmp);
                if(lines.size() >= 1000)
                {
                    String path = String.join("/", new String[]{
                            this.p.toAbsolutePath().toString(),
                            this.fp + String.valueOf(file++) + ".csv"
                    });
                    System.out.println("Currently creating file " + path.toString() + " with " + lines.size() + " lines");
                    BufferedWriter bw = new BufferedWriter(new FileWriter(path));
                    bw.write(this.headers); bw.newLine();
                    lines.stream().forEach( line -> {
                        try {
                            bw.write(line);
                            bw.newLine();
                        } catch(IOException ex) {
                            System.out.println(ex.getMessage());
                        }
                    });
                    lines.clear();
                    bw.close();

                }

                tmp = this.br.readLine();
            }
        } catch(IOException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public DataParser(InputStreamReader inputFile, String filename_prefix, Path path) {
        this.fp = filename_prefix;
        this.p = path;
        if (!Files.isDirectory(path)) {
            System.out.println("Path is not dir");
            System.exit(1);
        }
        try {
            this.br = new BufferedReader(inputFile);
            this.headers = br.readLine();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            System.exit(-1);
        } catch (Exception ex) {
            System.exit(-1);
        }
    }

    public DataObject getEntry(Integer i) {
        return this.br.lines()
                .limit(i + 2)
                .skip(i + 1)
                .map(x -> new DataObject(x.split(";")))
                .findFirst().get();
    }
}
