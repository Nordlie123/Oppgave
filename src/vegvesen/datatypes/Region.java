package vegvesen.datatypes;

import java.sql.SQLException;
import java.util.NoSuchElementException;

/**
 * Represents a region
 * @see Kategori
 */
public class Region extends Kategori {
    /**
     * Empty Region
     * @throws SQLException
     * @throws NoSuchElementException
     */
    public Region() throws SQLException, NoSuchElementException {

    }

    /**
     * @param name
     * @throws SQLException
     */
    public Region(String name) throws SQLException {
        super(name);
        this.getData();
    }

    /**
     * @param id
     * @throws SQLException
     */
    public Region(Integer id) throws SQLException {
        super(id);
        this.getData();
    }

    /**
     * table where regions are stored
     * @return
     */
    @Override
    public String getType() {
        return "region";
    }
}
