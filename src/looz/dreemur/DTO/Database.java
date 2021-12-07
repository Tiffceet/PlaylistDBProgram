package looz.dreemur.DTO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import looz.dreemur.generic.DBManager;
import looz.dreemur.generic.Pair;

/**
 * Instance for storing database of playlists
 * @author Looz
 */
public class Database {
    /**
     * To store a list of playlists in DB
     */
    private ArrayList<Playlist> playlists;
    /**
     * To store existing filepaths
     */
    private ArrayList<String> filepaths;

    public Database() {
    }

    

    private boolean containsPlaylist(Playlist playlist) {
        for (int i = 0; i < this.playlists.size(); i++) {
            if (this.playlists.get(i).getPlaylistName().equals(playlist.getPlaylistName())) {
                return true;
            }
        }
        return false;
    }

}
