package geo;

import org.junit.jupiter.api.Test;

/**
 * To be added
 */
public class EurefLocation extends GeoLocation {

    /**
     * TODO
     * @param lat
     * @param lng
     */
    public EurefLocation(String lat, String lng) {
        super(lat,lng, "0");
    }

    @Override
    public float distanceTo(GeoLocation g) {
        return 0;
    }

    public EurefLocation(String lat, String lng, String hgt) {
        super(lat, lng, hgt);
    }

    /**
     * TODO
     * @param type
     * @param <T>
     * @return
     */
    @Override
    public <T extends GeoLocation> GeoLocation convertToType(Class<T> type) {
        return null;
    }

}
