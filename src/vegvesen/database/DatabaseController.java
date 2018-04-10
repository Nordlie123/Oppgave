package vegvesen.database;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseController {
    Connection connection;

    private DatabaseController() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:dataset.db");
        } catch(SQLException ex) {
            System.out.println(ex.getErrorCode() + ":" + ex.getMessage());
            System.exit(-1);
        }

    }



    public Connection getConnection() {
        return connection;
    }
    private static DatabaseController instance;
    public static DatabaseController GetInstance() {
        if(instance == null) {
            instance = new DatabaseController();
        }
        return instance;
    }
    
}
