package vegvesen.datatypes;

import java.sql.SQLException;

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

    public VegObjekt() {

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
}
