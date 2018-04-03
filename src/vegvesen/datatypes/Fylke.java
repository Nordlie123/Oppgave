package vegvesen.datatypes;

import java.sql.SQLException;
import java.util.NoSuchElementException;

/**
 * Represents a County
 * @see Kategori
 */
public class Fylke extends Kategori {
    /**
     * Empty county
     * @throws SQLException
     * @throws NoSuchElementException
     */
    public Fylke() throws SQLException, NoSuchElementException {

    }

    /**
     * @param name
     * @throws SQLException
     */
    public Fylke(String name) throws SQLException {
        super(name);
        this.getData();

    }

    /**
     * @param id
     * @throws SQLException
     */
    public Fylke(Integer id) throws SQLException {
        super(id);
        this.getData();
    }

    /**
     * Table where counties are stored
     * @return
     */
    @Override
    public String getType() {
        return "fylke";
    }
}
