package geo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EurefLocationTest {
    /**
     * Set Height
     */
    @Test
    void getHgt() {
        assertEquals((new EurefLocation("123", "234", "345")).getHgt(), "345");
    }

    @Test
    void getLat() {
        assertEquals((new EurefLocation("123", "234", "345")).getLat(), "123");
    }

    @Test
    void getLng() {
        assertEquals((new EurefLocation("123", "234", "345")).getLng(), "234");
    }

    @Test
    void setHgt() {
        EurefLocation e = new EurefLocation("123", "234");
        e.setHgt("666");
        assertEquals(e.getHgt(), "666");
    }

    @Test
    void setLat() {
        EurefLocation e = new EurefLocation("123", "234");
        e.setLat("666");
        assertEquals(e.getLat(), "666");
    }

    @Test
    void setLng() {
        EurefLocation e = new EurefLocation("123", "234");
        e.setLng("666");
        assertEquals(e.getLng(), "666");
    }


}