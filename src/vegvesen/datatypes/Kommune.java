package vegvesen.datatypes;

import java.sql.SQLException;
import java.util.NoSuchElementException;

/**
 * Represents a municipality.
 * @see Kategori
 */
public class Kommune extends Kategori {

    public Kommune() throws SQLException, NoSuchElementException { }

    /**
     * @param name
     * @throws SQLException
     * @throws NoSuchElementException
     */
    public Kommune(String name) throws SQLException, NoSuchElementException{
        super(name);
        this.getData();
    }

    /**
     * @param id
     * @throws SQLException
     * @throws NoSuchElementException
     */
    public Kommune(Integer id) throws SQLException, NoSuchElementException{
        super(id);
        this.getData();
    }

    /**
     * Returns the table where municipalities are stored
     * @return
     */
    @Override
    public String getType() {
        return "kommune";
    }
}
