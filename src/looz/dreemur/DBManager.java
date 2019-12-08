package looz.dreemur;

import java.sql.*;
import java.io.*;
import javax.swing.JOptionPane;

public class DBManager {

    File src_DB, db;
    Connection conn;
    MainFrame mf;
    int status;
    String DB_schema, src_DB_path, db_path;

    public DBManager(MainFrame mf, File db) {

        // Defaults
        status = 0;
        DB_schema = "CREATE TABLE android_metadata (locale TEXT)\n"
                + "CREATE TABLE music_playlist (_id INTEGER PRIMARY KEY autoincrement,name TEXT NOT NULL,path TEXT,album_pic TEXT)\n"
                + "CREATE TABLE sqlite_sequence(name,seq)\n";
        conn = null;

        // references
        this.src_DB = db;
        src_DB_path = src_DB.getAbsolutePath();
        this.mf = mf;

        // DB startup operations
        connectDatabase(src_DB);
        verifyDB();

        // verify if db is OK before proceeding
        if (status == 1) {
            return;
        }

        // Close database connection to source DB
        try {
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        // Create a new DB file to be edited in process
        db = new File("ongoing.db");
        db_path = db.getAbsolutePath();

        // copy the source DB to ongoing.db
        try {
            this.copyFileUsingStream(src_DB, db);

            // connect to ongoing.db
            connectDatabase(db);
            // verify DB once again to check if file is copied correctly
            verifyDB();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // constructor for creating new database straight away at ongoing.db
    public DBManager(MainFrame mf) {
        // Defaults
        status = 0;
        DB_schema = "CREATE TABLE android_metadata (locale TEXT)\n"
                + "CREATE TABLE music_playlist (_id INTEGER PRIMARY KEY autoincrement,name TEXT NOT NULL,path TEXT,album_pic TEXT)\n"
                + "CREATE TABLE sqlite_sequence(name,seq)\n";
        conn = null;

        // references
        this.mf = mf;
        db = new File("ongoing.db");
        db_path = db.getAbsolutePath();
        this.src_DB = null;
        this.src_DB_path = null;
        
        createDatabase();
    }

    public void createDatabase() {        
        // Create new db in 'ongoing.db'
        // clean up the old data
        if (db.exists()) {
            if(!db.delete())
            {
                System.out.println("DBManager() -> createDatabase: Something is using ongoing.db ?");
            }
        }
        db = new File("ongoing.db");
        String url = "jdbc:sqlite:" + db.getAbsolutePath().replace("\\", "/");
        try {
            conn = DriverManager.getConnection(url);
            String sql = "CREATE TABLE android_metadata (locale TEXT);";
            String sql2 = "CREATE TABLE music_playlist (_id INTEGER PRIMARY KEY autoincrement,name TEXT NOT NULL,path TEXT,album_pic TEXT);";
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            stmt.execute(sql2);
        } catch (SQLException e) {
            System.out.println("DBManager() -> createDatabase: Something went wrong");
            System.out.println(e.getMessage());
        }
        
    }

    public void connectDatabase(File dbfileToBeConnected) {
        try {
            String url = "jdbc:sqlite:" + dbfileToBeConnected.getAbsolutePath().replace("\\", "/");
            // System.out.println("URL: " + url);
            conn = DriverManager.getConnection(url);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Connection to .db failed", "Error", JOptionPane.ERROR_MESSAGE);
            status = 1;
        }

    }

    // use this only when user tries to save
    // remember to make sure src_DB_path is defined
    public void overwriteSourceDatabase() throws IOException {
        try {
            conn.close();

            // redefine both of them as stated in copyFileUsingStream()
            db = new File(db_path);
            src_DB = new File(src_DB_path);

            copyFileUsingStream(db, src_DB);

            connectDatabase(db);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Something went really wrong, please check overwriteSourceDatabase()", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "File Not Found error, pls dont mess with the file when saving.", "Error", JOptionPane.ERROR_MESSAGE);

        }

    }
    // Why was this here ?
//
//    // use this only when user tries to save
//    public void overwriteSourceDatabase(String src_db_path) throws IOException {
//        try {
//            conn.close();
//
//            // redefine both of them as stated in copyFileUsingStream()
//            db = new File(db_path);
//            src_DB = new File(src_db_path);
//
//            copyFileUsingStream(db, src_DB);
//
//            connectDatabase(db);
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//            JOptionPane.showMessageDialog(null, "Something went really wrong, please check overwriteSourceDatabase()", "Error", JOptionPane.ERROR_MESSAGE);
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//            JOptionPane.showMessageDialog(null, "File Not Found error, pls dont mess with the file when saving.", "Error", JOptionPane.ERROR_MESSAGE);
//
//        }
//
//    }

    public void verifyDB() {
        try {

            // checks schema *(shit way)
            String q = "SELECT name, sql FROM sqlite_master "
                    + "WHERE type='table' "
                    + "ORDER BY name;";
            ResultSet rs = getData(q);

            String schema = "";

            while (rs.next()) {
                schema = schema + rs.getString("sql") + "\n";
            }

            if (!schema.equals(DB_schema)) {
                throw new Exception("Validation error: Database schema doesnt match");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "An error occured during database verification", "Error", JOptionPane.ERROR_MESSAGE);
            status = 1;
        }
    }

    public ResultSet getData(String sql) throws SQLException {
        return conn.createStatement().executeQuery(sql);
    }

    public void execQuery(String sql) throws SQLException {
        Statement st = conn.createStatement();
        st.executeUpdate(sql);
        st.close();
    }

    // will be used in future as statement in getData() is not handled properly, duh
    public void freeUpMemory() throws SQLException {
        if (conn != null) {
            conn.close();
        }
        db = new File(db_path);
        src_DB = new File(src_DB_path);

        connectDatabase(db);
    }

    // copy file
    private void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            // Assumption 1: file is closed when its stream is closed
            // Assumption 2: file is closed when connection is closed
            // NOTE: whenever "db" and "src_DB" needs to be reaccess,
            // NOTE: OR 
            // NOTE: whenever "conn" is closed using conn.close()
            // NOTE: redefine both of them using "new File(db_path)" and "new File(src_DB_path)"
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
        }
    }
}
