package vegvesen;

import vegvesen.datatypes.*;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Databasekontroller for applikasjonen. Dette er eneste tilkoblingspunktet for
 * databasen i appen.
 *
 * Om du skal bruke den. Velg singleton metoden GetInstance()
 */
public class DatabaseController {
    Connection connection;

    /**
     * Private Constructor for the database controller. Must be singleton instantiated.
     */
    private DatabaseController() {
        try {
            Boolean dbExists = Files.exists(Paths.get("dataset.db"));
            connection = DriverManager.getConnection("jdbc:sqlite:dataset.db");
            if(!dbExists) {
                this.createDatabase();
            }
        } catch(SQLException ex) {
            System.out.println(ex.getErrorCode() + ":" + ex.getMessage());
            System.exit(-1);
        }

    }

    /**
     * Used for test databases. Will destroy previous databases if it can
     * @param path
     * @throws IOException
     * @throws SQLException
     */
    private DatabaseController(String path) throws IOException, SQLException {
        Files.deleteIfExists(Paths.get(path));
        connection = DriverManager.getConnection("jdbc:sqlite:" + path);
        this.createDatabase();
    }

    /**
     * Reads database_init.sql and performs setup of new database
     */
    public void createDatabase() {
        try {
            String[] sql = String.join("", Files.readAllLines(Paths.get("database_init.sql")).stream().filter(x -> !x.startsWith("--")).toArray(String[]::new)).split("NEWQUERY");
            Statement s = connection.createStatement();
            Arrays.stream(sql).forEach(x -> {
                try {
                    s.addBatch(x);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            System.out.println("Creating database... " + (Arrays.stream(s.executeBatch()).filter(x -> x > 0).count() == 0 ? "Success" : "FAILED"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (AssertionError e) {
            e.printStackTrace();
        }
    }

    /**
     * Finds the next valid ID that can be used for new objects
     * @param domain The class that inherits from Kategori
     * @param <T> The class that inherits from Kategori
     * @return Integer number one higher than current maximum
     * @throws SQLException
     */
    public <T extends Kategori> Integer getNextIdFor(Class<T> domain) throws SQLException {
        ResultSet maxrs = connection
                .createStatement()
                .executeQuery("SELECT MAX(id) as MAX from " + domain.getSimpleName().toLowerCase());
        return maxrs.isClosed() ? 1 : maxrs.getInt("MAX") + 1;
    }

    /**
     * Returns a fully filled object based on a partially filled object of the same class
     * @param obj
     * @param <T>
     * @return object or null when it cant be found
     * @throws SQLException
     */
    public <T extends Kategori> T getObject(T obj) throws SQLException {
        ResultSet results;
        if(obj.getName() == null) {
            PreparedStatement p = connection
                    .prepareStatement("SELECT * FROM " + obj.getType() + " WHERE id = ? LIMIT 1");
            p.setInt(1, obj.getId());
            results = p.executeQuery();
        }
        else {
            PreparedStatement p = connection
                    .prepareStatement("SELECT * FROM " + obj.getType() + " WHERE name = ? LIMIT 1");
            p.setString(1, obj.getName());
            results = p.executeQuery();
        }

        if(results.isClosed())
            return null;

        Kategori k;
        if(obj instanceof Fylke)
            k = new Fylke();
        else if(obj instanceof Kommune)
            k = new Kommune();
        else
            k = new Region();
        k.setId(results.getInt("id"));
        k.setName(results.getString("name"));
        return (T)k;
    }

    /**
     * Saves the object
     * @param k A Kategori inheriting object
     * @return execution-success
     * @throws SQLException
     */
    public boolean setObject(Kategori k) throws SQLException {
        PreparedStatement p = connection
                .prepareStatement("INSERT OR REPLACE INTO " + k.getType() + " (id, name) VALUES (?, ?)");
        p.setInt(1, k.getId());
        p.setString(2, k.getName());
        return p.execute();

    }

    /**
     * Creates VegObject from a DataObject
     * @param data
     * @return
     * @throws SQLException
     */
    public boolean addDataObject(DataObject data) throws SQLException{
        PreparedStatement s = connection
            .prepareStatement(
                "INSERT OR REPLACE INTO " +
                "vegobjekter " +
                "(" +
                "vegobjekt, " +
                "type_id, " +
                "versjonid, " +
                "strekningsbeskrivelse, " +
                "region_id, " +
                "fylke_id, " +
                "kommune_id," +
                "vegkategori,"+
                "vegnummer" +
                ") VALUES (?,?,?,?,?,?,?,?,?)"
            );
        s.setInt(1, Integer.parseInt(data.get("vegobjektid")));
        s.setInt(2, Integer.parseInt(data.get("type_id")));
        s.setInt(3, Integer.parseInt(data.get("versjonid")));
        s.setString(4, data.get("Strekningsbeskrivelse"));
        s.setInt(5, this.getIDForRegion(data.get("region")));
        s.setInt(6, this.getIDForFylke(data.get("fylke")));
        s.setInt(7, this.getIDForKommune(data.get("kommune")));
        s.setString(8, data.get("vegkategori") + data.get("vegstatus"));
        s.setInt(9, Integer.parseInt(data.get("vegnummer")));
        return s.execute();
    }

    public ResultSet GetAlleVegObjekter() throws SQLException {
        Statement s = connection.createStatement();
        return s.executeQuery("SELECT * FROM vegobjekter");

    }
    private Integer getIDForRegion(String r) throws SQLException{
        return getIDForObject("region", r);
    }

    private Integer getIDForFylke(String r) throws SQLException{
        return getIDForObject("fylke", r);
    }

    private Integer getIDForKommune(String r) throws SQLException{
        return getIDForObject("kommune", r);
    }
    private Integer getIDForObject(String domain, String region) throws SQLException{
        PreparedStatement s = connection.prepareStatement("SELECT id FROM " + domain+ " WHERE name = ? LIMIT 1");
        s.setString(1, region);
        if(s.execute()) {
            ResultSet rs = s.getResultSet();
            Integer id = rs.isClosed() ? 0 : rs.getInt("id");
            if (id == 0) {
                Integer maxID;
                switch(domain) {
                    case "kommune":
                        maxID = this.getNextIdFor(Kommune.class);
                        break;
                    case "fylke":
                        maxID = this.getNextIdFor(Fylke.class);
                        break;
                    case "region":
                    default:
                        maxID = this.getNextIdFor(Region.class);
                        break;
                }
                s = connection.prepareStatement("INSERT INTO " + domain + " (id, name) VALUES (?, ?)");
                s.setInt(1, maxID);
                s.setString(2, region);
                s.execute();
                return maxID;
            }
            return id;
        } else {
            System.out.println("SQL QUERY FAILED OR SOMETHING");
            System.exit(-1);
        }
        return null;
    }

    public void Close() throws SQLException {
        connection.close();
    }
    /**
     * The Instance
     */
    private static DatabaseController instance;

    /**
     * Instantiates and returns a new object
     * @return
     */
    public static DatabaseController GetInstance() {
        if(instance == null) {
            instance = new DatabaseController();
        }
        return instance;
    }

    /**
     * Returns an empty database
     * @return
     */
    public static DatabaseController GetTestInstance() {
        try {
            return new DatabaseController("testdb.db");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static void main(String[] args) {
        DatabaseController dc = DatabaseController.GetInstance();

        //System.out.println(DataObject.Search("vegobjektid", "834243277"));

        // Sett kontrolleren for Kategorisøket til å være DatabaseController singleton
        Kategori.controller = DatabaseController.GetInstance();
        VegObjekt.controller = DatabaseController.GetInstance();


            try {
                DataSetRunner dsr = new DataSetRunner("ds/");
                String[] headers = dsr.next().split(";");
                if(DataObject.headers == null) {
                    Arrays.stream(headers).map(str -> str.replace("\"", "")).forEach(str -> DataObject.addHeader(str));
                }
                String s = dsr.next();
                Integer added = 0;
                long start_time = System.nanoTime();
                long checkpointTime = start_time;
                while(s != null) {
                    if(added % 1000 == 0 && added >= 1000) {
                        System.out.println("We have added " + added + " items");
                        long now_time = System.nanoTime();
                        System.out.println("Time elapsed: \t" + (now_time - start_time) / 1000000000 + "s");
                        System.out.println("Checkpoint: \t" + (now_time - checkpointTime) /  1000000 + "ms");
                        checkpointTime = now_time;
                    }
                    added++;
                    String[] tmp = Arrays.stream(s.split(";"))
                            .map(x -> x.trim())
                            .map(str -> str.replace("\"", ""))
                            .toArray(String[]::new);
                    DatabaseController.GetInstance().addDataObject(new DataObject(tmp));
                    s = dsr.next();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }

    }

}
