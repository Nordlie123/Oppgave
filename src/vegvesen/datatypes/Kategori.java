package vegvesen.datatypes;

import vegvesen.DatabaseController;
import java.sql.SQLException;
import java.util.NoSuchElementException;

/**
 * Kategori er en abstakt klasse som håndterer import og eksport av kategorityper
 *
 * It is a super class for
 *
 *  * Fylke
 *  * Kommune
 *  * Region
 */
public abstract class Kategori{

    // A connection to the database
    public static DatabaseController controller;

    protected Integer id;
    protected String name;

    /**
     * Creates an empty item, it will not trigger database searches
     * @throws SQLException
     * @throws NoSuchElementException
     */
    public Kategori() throws SQLException, NoSuchElementException {
    }

    /**
     * Creates or gets an item with the text in name.
     *
     * Triggers db search for the item. If it is not found, it is created
     * @param name Name of the object
     * @throws SQLException If the database lookup fails
     * @throws NoSuchElementException
     */
    public Kategori(String name) throws SQLException, NoSuchElementException {
        this.name = name;
    }

    /**
     * Gets a item based on the ID
     *
     * Will not create item and fail if the ID is invalid
     * @param id id of the item searched for
     * @throws SQLException If the database lookup fails
     * @throws NoSuchElementException If the ID is invalid
     */
    public Kategori(Integer id) throws SQLException, NoSuchElementException {
        this.id = id;
    }

    /**
     * Triggers a search from the Database Controller to get all relevant data
     * or creates the object if it can not find it.
     * @throws SQLException
     * @throws NoSuchElementException
     */
    protected void getData() throws SQLException, NoSuchElementException {
        Kategori k = controller.getObject(this);
        if(k != null) {
            this.id = k.id;
            this.name = k.name;
            return;
        }
        if(this.id != null)
            throw new NoSuchElementException("An element of that ID does not exist");

        this.id = controller.getNextIdFor(this.getClass());
        controller.setObject(this);
    }

    /**
     * Triggers a save of the item-
     * @throws SQLException
     */
    public void Save() throws SQLException {
        controller.setObject(this);
    }

    /**
     * Set the name of this item
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set the ID of this item
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Get the name of this item
     * @return name of the item
     */
    public String getName() { return name; }

    /**
     * Get the ID of this item
     * @return id of the item
     */
    public Integer getId() {
        return id;
    }

    /**
     * Return a text representation of the item in the following syntax
     *
     * #<class_name:id:name>
     * @return A string representation of the item
     */
    public String toString() {
        return "#<" + String.join(":", new String[] {
                this.getClass().getSimpleName(),
                this.id != null ? this.id.toString() : "null",
                this.getName()
        }) + ">";
    }

    /**
     * Should return a text representation of only the name of the table we are looking up in
     * @return
     */
    public abstract String getType();
}
