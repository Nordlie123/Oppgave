package geo;

/**
 * Generic GeoLocation object
 */
public abstract class GeoLocation {

    private String lat;
    private String lng;
    private String hgt;


    /**
     * Construct new GeoLocation element.
     * Used by subclasses
     * @param lat
     * @param lng
     * @param hgt
     */
    public GeoLocation(String lat, String lng, String hgt) {
        this.lat = lat;
        this.lng = lng;
        this.hgt = hgt;
    }

    /**
     * Construct new GeoLocation with 0 height
     * @param lat Latitude
     * @param lng Longitude
     */
    public GeoLocation(String lat, String lng) {
        this.lat = lat;
        this.lng = lng;
        this.hgt = "0";
    }

    public String getHgt() { return hgt; }
    public String getLat() { return lat; }
    public String getLng() { return lng; }

    public void setHgt(String hgt) {
        this.hgt = hgt;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    /**
     * Calculate distance between two locations
     * @param g
     * @return
     */
    public abstract float distanceTo(GeoLocation g);
    /**
     * This function has to exist in all subclasses and must be able to conform
     * @param type
     * @param <T>
     * @return
     */
    public abstract <T extends GeoLocation> GeoLocation convertToType(Class<T> type);

    /**
     * Convert from one GeoLocation type to another
     * @param type New type
     * @param from Old Object
     * @return New Object of type
     */
    public static <T extends  GeoLocation> GeoLocation convertTo(Class<T> type, GeoLocation from) {
        return type.cast(from.convertToType(type));
    }

}
