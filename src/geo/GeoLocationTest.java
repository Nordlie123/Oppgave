package geo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GeoLocationTest {
    @Test
    void ConvertToTypeTest() {
        assertTrue(GeoLocation.convertTo(WgsLocation.class, new EurefLocation("123", "234", "345")) != null);
    }

}