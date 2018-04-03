package vegvesen.datatypes;

import vegvesen.DatabaseController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class VegObjekt {
    public int id;
    public int type_id;
    public int versjonid;
    public String strekningsbeskrivelse;
    public int kommune_id;
    private Kommune kommune;
    public int fylke_id;
    private Fylke fylke;
    public int region_id;
    private Region region;
    public String vegkategori;
    public int vegnummer;
    public VegObjekt() {

    }
    public VegObjekt(ResultSet rs) throws SQLException {
        this.id = rs.getInt("vegobjekt");
        this.type_id = rs.getInt("type_id");
        this.versjonid = rs.getInt("versjonid");
        this.strekningsbeskrivelse = rs.getString("strekningsbeskrivelse");
        this.kommune_id = rs.getInt("kommune_id");
        this.fylke_id = rs.getInt("fylke_id");
        this.region_id = rs.getInt("region_id");
        this.vegkategori = rs.getString("vegkategori");
        this.vegnummer = rs.getInt("vegnummer");
    }

    /**
     * Get the Kommune object
     * @return
     */
    public Kommune getKommune() {
        if(this.kommune == null ){
            try {
                this.kommune = new Kommune(this.kommune_id);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return this.kommune;
    }

    /**
     * Get the Fylke object
     * @return
     */
    public Fylke getFylke() {
        if(this.fylke == null ){
            try {
                this.fylke = new Fylke(this.fylke_id);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return this.fylke;
    }

    /**
     * Get the Region object
     * @return
     */
    public Region getRegion() {
        if(this.region == null ){
            try {
                this.region = new Region(this.region_id);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return this.region;
    }

    public static DatabaseController controller;
    public static VegObjekt[] GetAll() throws SQLException {
        ArrayList<VegObjekt> arvo = new ArrayList<>();
        ResultSet rs = controller.GetAlleVegObjekter();
        while(rs.next()) {
            arvo.add(new VegObjekt(rs));
        }
        return arvo.stream().toArray(VegObjekt[]::new);
    }
}
