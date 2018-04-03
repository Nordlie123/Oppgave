package vegvesen;

import java.util.HashMap;

public class VegObjekt {
    public int id;
    public int type_id;
    public int versjonid;
    public int vegliste_versjon;
    public String strekningsbeskrivelse;
    public int kommune_id;
    public int fylke_id;
    public int region_id;

    public VegObjekt() {

    }

    public VegObjekt(HashMap<String, Object> data) {

    }
}
