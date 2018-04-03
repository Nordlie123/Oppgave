package geo;

public class WgsLocation extends GeoLocation {
    public WgsLocation(String lat, String lng) {
        super(lat, lng, "0");
    }
    @Override
    public <T extends GeoLocation> GeoLocation convertToType(Class<T> type) {
        return null;
    }
}
