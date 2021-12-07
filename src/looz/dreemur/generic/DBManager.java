package looz.dreemur.generic;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;

/**
 * Class used to execute queries for database <br>
 *
 * <b>Change Log:</b> <br>
 * 1.0 <br>
 * - Initial <br>
 * 1.1 <br>
 * - Remove lastErrorMessage member from class <br>
 * - Connection will be closed upon completion of any DB operations <br>
 * - Removed disconnectDB() <br>
 * - Now expects the developer to handle SQLException on their own
 *
 * @author Loo Zi Kang
 * @version 1.1
 */
public class DBManager {

    private String dbPath;
    private String dbName;

    public DBManager(String dbPath, String dbName) { 
        this.dbPath = dbPath;
        this.dbName = dbName;
    }

    /**
     * Connect to Database
     *
     * @return Connection instance
     */
    private Connection connectDB() throws SQLException {
        // make sure directory exist
        new File(dbPath).mkdir();
        String url = "jdbc:sqlite:" + dbPath + dbName;

        Connection conn;

        // Connecting to the database
        conn = DriverManager.getConnection(url);
        if (conn == null) {
            throw new SQLException();
        }

        return conn;
    }

    /**
     * Set up database schema
     *
     * @throws SQLException
     * @throws IOException
     */
    public void prepareTable() throws SQLException, IOException {
        File f = new File(dbPath + dbName);
        if (f.exists() && !f.isDirectory()) {
            return;
        }
        Connection conn = connectDB();

        // Read SQL file from resource folder
        InputStream is = getClass().getClassLoader().getResourceAsStream("SQL/TABLE_SCHEMA.sql");
        BufferedReader buf = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        String line = buf.readLine();
        StringBuilder sb = new StringBuilder();
        while (line != null) {
            sb.append(line).append("\n");
            line = buf.readLine();
        }
        String schemaQuery = sb.toString();

        Statement stmt = conn.createStatement();
        String[] queries = schemaQuery.split(";");
        for (String query : queries) {
            if (query.trim().compareTo("") == 0) {
                continue;
            }
            stmt.addBatch(query);
        }
        stmt.executeBatch();
        conn.close();
    }

    /**
     * Execute update Query
     *
     * @param q SQLITE query
     * @return rows are affected
     * @throws SQLException
     */
    public int updateQuery(String q) throws SQLException {
        Connection conn = connectDB();
        int rowsAffected = -1;

        Statement stmt = conn.createStatement();
        rowsAffected = stmt.executeUpdate(q);

        // Close DB Connection
        conn.close();

        return rowsAffected;
    }

    /**
     * Execute query
     *
     * @param q SQLITE Query
     * @return true if the query returned a ResultSet
     * @throws SQLException
     */
    public boolean execQuery(String q) throws SQLException {
        Connection conn = connectDB();
        boolean status = false;

        Statement stmt = conn.createStatement();
        status = stmt.execute(q);

        // Close DB Connection
        conn.close();

        return status;
    }

    /**
     * Execute query that returns rows of data As ResultSet cant be read after
     * connection is closed, Connection instance will be returned
     *
     * Developer are expected to close the connection after usage
     *
     * @param q SQLITE query
     * @return Connection and ResultSet instance in a pair
     * @see Generic.Pair
     * @throws SQLException
     */
    public Pair<Connection, ResultSet> resultQuery(String q) throws SQLException {
        Connection conn = connectDB();
        ResultSet result = null;

        Statement stmt = conn.createStatement();
        result = stmt.executeQuery(q);

        return new Pair<>(conn, result);
    }
}
