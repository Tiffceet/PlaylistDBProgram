package looz.dreemur.generator;

import java.sql.SQLException;
import java.util.ArrayList;
import looz.dreemur.DTO.Database;
import looz.dreemur.DTO.Playlist;
import looz.dreemur.DTO.Song;
import looz.dreemur.generic.DBManager;

/**
 *
 * @author Looz
 */
public class MobileV5BackupGenerator {

    private static final String dbSchema = "DROP TABLE IF EXISTS android_metadata;"
            + "DROP TABLE IF EXISTS music_playlist;"
            + "CREATE TABLE android_metadata (locale TEXT);"
            + "CREATE TABLE music_playlist (_id INTEGER PRIMARY KEY autoincrement,name TEXT NOT NULL,path TEXT,album_pic TEXT);"
            + "INSERT INTO android_metadata VALUES('en_GB');";

    public static void generate(Database db) {
        String dbPath = db.getDbPath();
        String dbName = db.getDbName();
        DBManager dbm = new DBManager(dbPath, dbName);

        // Clean up DB
        try {
            dbm.execBatch(dbSchema);
        } catch (SQLException ex) {
            System.out.println("Failed to clean up DB");
            ex.printStackTrace();
        }

        String insertQuery = "";
        ArrayList<Playlist> pl = db.getPlaylists();
        for (Playlist p : pl) {
            ArrayList<Song> songs = p.getSongs();
            for (Song s : songs) {
                insertQuery += "INSERT INTO music_playlist(name, path) VALUES ('" + p.getPlaylistName() + "', '" + s.getFilepath() + "');";
            }
        }

        // Execute Insert Query
        try {
            dbm.execBatch(insertQuery);
        } catch (SQLException ex) {
            System.out.println("Failed to exec insert query");
            System.out.println(insertQuery);
            ex.printStackTrace();
        }
    }
}
