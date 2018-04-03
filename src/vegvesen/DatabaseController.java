package vegvesen;

import org.sqlite.SQLiteConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;

public class DatabaseController {
    Connection connection;

    private DatabaseController() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:dataset.db");
        } catch(SQLException ex) {
            System.out.println(ex.getErrorCode() + ":" + ex.getMessage());
            System.exit(-1);
        }

    }


    public ArrayList<VegObjekt> AlleVegObjeker() throws SQLException {
        Statement s = connection.createStatement();
        s.execute("SELECT * FROM vegobjekter");
        ArrayList<VegObjekt> alle = new ArrayList<>();
        ResultSet rs = s.getResultSet();
        while(rs.next()) {
            VegObjekt v = new VegObjekt();
            v.id = rs.getInt("vegobjekt");
            alle.add(v);
        }
        return alle;
    }


    public boolean addDataObject(DataObject data) throws SQLException{

        PreparedStatement s = connection.prepareStatement("INSERT OR REPLACE INTO vegobjekter (vegobjekt, type_id, versjonid, vegliste_versjon, strekningsbeskrivelse, region_id, fylke_id, kommune_id) VALUES (?,?,?,?,?,?,?,?)");
        s.setInt(1, Integer.parseInt(data.get("vegobjektid")));
        s.setInt(2, Integer.parseInt(data.get("type_id")));
        s.setInt(3, Integer.parseInt(data.get("versjonid")));
        s.setString(5, data.get("Strekningsbeskrivelse"));
        s.setInt(6, this.getIDForRegion(data.get("region")));
        s.setInt(7, this.getIDForFylke(data.get("fylke")));
        s.setInt(8, this.getIDForKommune(data.get("kommune")));
        return s.execute();
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
                ResultSet maxrs = connection.createStatement().executeQuery("SELECT MAX(id) as MAX from " + domain);
                Integer maxID = maxrs.isClosed() ? 1 : maxrs.getInt("MAX") + 1;
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
    private static DatabaseController instance;
    public static DatabaseController GetInstance() {
        if(instance == null) {
            instance = new DatabaseController();
        }
        return instance;
    }


    public static void main(String[] args) {
        DatabaseController dc = DatabaseController.GetInstance();
        try {
            dc.AlleVegObjeker().stream().forEach(x -> System.out.println(x.id));
        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode());
        }
    }
}
