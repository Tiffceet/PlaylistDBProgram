import javax.swing.JOptionPane;

import looz.dreemur.DTO.Playlist;
import looz.dreemur.generic.DBManager;
import looz.dreemur.generic.Pair;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;

public class MobileV5BackupParser {
    // obtain playlist from database
    public ArrayList<Playlist> getPlaylist(String dbPath, String dbName) {
        ArrayList<Playlist> playlists = new ArrayList<Playlist>();
        DBManager db = new DBManager(dbPath, dbName);
        String query = "SELECT DISTINCT name FROM music_playlist";
        try {
            Pair<Connection, ResultSet> result = db.resultQuery(query);
            ResultSet rs = result.getRight();
            while (rs.next()) {
                String str = rs.getString("name");
                Playlist p = new Playlist(str);
                playlists.add(new Playlist(str));

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
    public ArrayList<String> getFilePaths(String dbPath, String dbName) {
        ArrayList<String> filepaths = new ArrayList<String>();

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