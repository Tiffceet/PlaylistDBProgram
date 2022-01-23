package looz.dreemur.parser;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import looz.dreemur.DTO.Playlist;
import looz.dreemur.DTO.Song;
import looz.dreemur.generic.DBManager;
import looz.dreemur.generic.Pair;

public class MobileV5BackupParser {

    /**
     * Reads the entries in each playlist and store it in the object
     */
    private static void populateSongs(Playlist pl, String dbPath, String dbName) {
        String plName = pl.getPlaylistName();
        DBManager db = new DBManager(dbPath, dbName);
        String query = "SELECT * FROM music_playlist WHERE name = '" + plName + "'";
        try {
            Pair<Connection, ResultSet> result = db.resultQuery(query);
            ResultSet rs = result.getRight();
            while (rs.next()) {
                String str = rs.getString("path");
                pl.addSong(new Song(str));
            }
            result.getLeft().close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "An error occured during query execution", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // obtain playlist from database
    public static ArrayList<Playlist> getPlaylist(String dbPath, String dbName) {
        ArrayList<Playlist> playlists = new ArrayList<Playlist>();
        DBManager db = new DBManager(dbPath, dbName);
        String query = "SELECT DISTINCT name FROM music_playlist";
        try {
            Pair<Connection, ResultSet> result = db.resultQuery(query);
            ResultSet rs = result.getRight();
            while (rs.next()) {
                String str = rs.getString("name");
                Playlist p = new Playlist(str);
                MobileV5BackupParser.populateSongs(p, dbPath, dbName);
                playlists.add(p);
                
            }
            result.getLeft().close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "An error occured during query execution", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        return playlists;
    }

    // get all filepaths in database
    public static ArrayList<String> getFilePaths(String dbPath, String dbName) {
        ArrayList<String> filepaths = new ArrayList<String>();
        DBManager db = new DBManager(dbPath, dbName);
        String query = "SELECT DISTINCT path FROM music_playlist";
        try {
            Pair<Connection, ResultSet> result = db.resultQuery(query);
            ResultSet rs = result.getRight();
            while (rs.next()) {
                // if empty playlist exists, that means null path exist, skip null path
                String str = rs.getString("path");
                if (str != null) {
                    filepaths.add(str);
                    
                }
                
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "An error occured during query execution", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        return filepaths;
    }
}
