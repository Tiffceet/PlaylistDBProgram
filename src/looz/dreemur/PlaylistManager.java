package looz.dreemur;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class PlaylistManager {
    
    MainFrame mf;
    DBManager db;
    String defaultPath;
    ArrayList<String> playlist, filepaths; // used to keep playlist and filepaths detected in database, is updating here neccessary after updating database ? 
    int status;
    
    public PlaylistManager(MainFrame mf, DBManager db) {
        
        this.mf = mf;
        this.db = db;

        // current status of this class or db
        // 0 = totally fine
        // 1 = this thing wont even work
        // 2 = minor corruptions/data intergrity loss
        this.status = 0;
        
        getPlaylist();
        getFilePaths();
    }

    // obtain playlist from database
    private void getPlaylist() {
        playlist = new ArrayList<String>();
        
        String query = "SELECT name FROM music_playlist";
        try {
            ResultSet result = db.getData(query);
            while (result.next()) {
                String str = result.getString("name");
                if (playlist.indexOf(str) == -1) {
                    playlist.add(str);
                }
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(mf, "An error occured during query execution", "Error", JOptionPane.ERROR_MESSAGE);
            status = 1;
        }
    }

    // get all filepaths in database
    public void getFilePaths() {
        filepaths = new ArrayList<String>();
        
        String query = "SELECT path FROM music_playlist";
        try {
            ResultSet result = db.getData(query);
            while (result.next()) {
                // if empty playlist exists, that means null path exist, skip null path
                String str = result.getString("path");
                if (str != null) {
                    str = mf.getPath(str);
                    if (filepaths.indexOf(str) == -1) {
                        filepaths.add(str);
                    }
                }
                
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(mf, "An error occured during query execution", "Error", JOptionPane.ERROR_MESSAGE);
            status = 1;
        }
    }

    // get all songs in a playlist
    public ArrayList<String> getSongs(String playlist) {
        ArrayList<String> songs = new ArrayList<String>();
        
        String query = "SELECT path FROM music_playlist WHERE name=?";
        // String query = "SELECT path FROM music_playlist WHERE name='" + playlist + "'";
        try {
            PreparedStatement st = db.conn.prepareStatement(query);
            st.setString(1, playlist);
            ResultSet result = st.executeQuery();
            while (result.next()) {
                String str = result.getString("path");
                if (str != null) {
                    str = str.substring(str.lastIndexOf("/") + 1);
                    songs.add(str);
                }
            }
            st.close();
            result.close();
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(mf, "An error occured during query execution", "Error", JOptionPane.ERROR_MESSAGE);
            status = 1;
        }
        
        return songs;
    }
    
    public void insertSong(String playlist, String songpath) {
        try {
            // song must not alrdy exist
            String q2 = "SELECT * FROM music_playlist WHERE name=?";
            PreparedStatement st = db.conn.prepareStatement(q2);
            st.setString(1, playlist);
            ResultSet rs = st.executeQuery();
            
            String songName = songpath.substring(songpath.lastIndexOf("/") + 1);
            while (rs.next()) {
                String str = rs.getString("path");

                // if str is null means empty playlist exist, hence do not even check for it
                if (str != null) {
                    
                    str = str.substring(str.lastIndexOf("/") + 1);

                    // stop adding song if already exist, error msg is not required because i might implement batch insert
                    if (songName.equals(str)) {
                        return;
                    }
                    
                }
            }
            st.close();
            rs.close();
            
            boolean nullPlaylistUpdated = false;

            // If empty playlist exist(null path), update that entry instead of adding a new entry
            String q3 = "SELECT * FROM music_playlist WHERE name='" + playlist + "'";
            rs = db.getData(q3);
            while (rs.next()) {
                String filepathInCurrentEntry = rs.getString("path");
                if (filepathInCurrentEntry == null) {
                    String sql = "UPDATE music_playlist SET path=? WHERE _id=?";
                    st = db.conn.prepareStatement(sql);
                    st.setString(1, songpath);
                    st.setInt(2, rs.getInt("_id"));
                    st.executeUpdate();
                    st.close();
                    // db.execQuery("UPDATE music_playlist SET path='" + songpath + "' WHERE _id='" + rs.getInt("_id") + "'");
                    nullPlaylistUpdated = true;
                    break;
                }
            }
            
            st.close();
            rs.close();

            // we dont want duplicate entry right? :3
            if (!nullPlaylistUpdated) {
                // Add new entry into database
                String q = "INSERT INTO music_playlist(name, path) VALUES(?, ?)";
                // String old_q = "INSERT INTO music_playlist(name, path) VALUES('" + playlist + "', '" + songpath + "')";

                // System.out.println("Query: " + q);
                // db.execQuery(q);
                st = db.conn.prepareStatement(q);
                st.setString(1, playlist);
                st.setString(2, songpath);
                st.executeUpdate();
                st.close();
            }
            // resets it, just in case
            nullPlaylistUpdated = false;
            
            mf.songFileName.addElement(songName);
            status = 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage() + " Song: " + songpath);
            String err_msg = "An error occured during query execution\n " + songpath.substring(songpath.lastIndexOf("/") + 1) + "\n will not be added.\n\nThis is usually due to illegal character in fileName or lousy programmer";
            JOptionPane.showMessageDialog(mf, err_msg, "Error", JOptionPane.ERROR_MESSAGE);
            status = 2;
        }
    }

    // NOTE TO MYSELF: YOU FKING DUMBASS IF ITS THE LAST SONG IN PLAYLIST THE LIST ITSELF GETS DELETED
    // NOTE: PLEASE RECREATE NULL PLAYLIST (empty playlist)
    public void removeSong(String playlist, int idx) {
        String song = mf.songFileName.getElementAt(idx).toString();
        
        try {
            // String q = "DELETE FROM music_playlist WHERE path LIKE '%" + song + "%' AND name='" + playlist + "'";

            // resetup connection < ME: kono jdbc BAKA >///< >
            String q = "DELETE FROM music_playlist WHERE path LIKE ? AND name=?";
            PreparedStatement st = db.conn.prepareStatement(q);
            st.setString(1, "%" + song + "%");
            st.setString(2, playlist);
            st.executeUpdate();
            st.close();

            // checks if playlist -> prevents all songs from being removed causing playlist itself is gone
            q = "SELECT name FROM music_playlist WHERE name=?";
            st = db.conn.prepareStatement(q);
            st.setString(1, playlist);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                // if playlist still exist
            } else {
                // if not, insert a null playlist
                q = "INSERT INTO music_playlist(name) VALUES(?)";
                st.close();
                st = db.conn.prepareStatement(q);
                st.setString(1, playlist);
                st.executeUpdate();
            }
            
            st.close();
            rs.close();
            
            mf.songFileName.remove(idx);
            status = 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(mf, "Something went wrong, song will not be deleted", "Error", JOptionPane.ERROR_MESSAGE);
            status = 2;
        }
    }
    
    public void batchRemoveSong(String playlist, int[] idxs) {
        String _WHERE_STATEMENT_songslist = "";
        for (int idx : idxs) {
            _WHERE_STATEMENT_songslist = _WHERE_STATEMENT_songslist + "path LIKE ? OR ";
        }
        _WHERE_STATEMENT_songslist = _WHERE_STATEMENT_songslist.substring(0, _WHERE_STATEMENT_songslist.length() - 4);
        try {
            String q = "DELETE FROM music_playlist WHERE name=? AND " + _WHERE_STATEMENT_songslist;

            // resetup connection < ME: kono jdbc BAKA >///< >
            PreparedStatement st = db.conn.prepareStatement(q);
            st.setString(1, playlist);
            int val_of_st_count = 2;
            for (int idx : idxs) {
                st.setString(val_of_st_count, "%" + mf.songFileName.get(idx) + "%");
                val_of_st_count++;
            }
            st.executeUpdate();

            // checks if playlist -> prevents all songs from being removed causing playlist itself is gone
            q = "SELECT name FROM music_playlist WHERE name=?";
            st = db.conn.prepareStatement(q);
            st.setString(1, playlist);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                // if playlist still exist
            } else {
                // if not, insert a null playlist
                q = "INSERT INTO music_playlist(name) VALUES(?)";
                st.close();
                st = db.conn.prepareStatement(q);
                st.setString(1, playlist);
                st.executeUpdate();
            }
            
            st.close();
            rs.close();

            // Remove song from interface's list
            for (int a = idxs.length - 1; a >= 0; a--) {
                mf.songFileName.remove(idxs[a]);
            }
            status = 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(mf, "Something went wrong, Songs didnt get deleted properly", "Error", JOptionPane.ERROR_MESSAGE);
            status = 2;
        }
    }
    
    public void renameSong(String playlist, int idx, String newPath) {
        String song = mf.songFileName.getElementAt(idx).toString();
        
        try {
            String q = "UPDATE music_playlist SET path=? WHERE name=? AND path LIKE ?";
            PreparedStatement st = db.conn.prepareStatement(q);
            st.setString(1, newPath);
            st.setString(2, playlist);
            st.setString(3, "%" + song + "%");
            st.executeUpdate();
            st.close();
            // String q = "UPDATE music_playlist SET path='" + newPath + "' WHERE name='" + playlist + "' AND path='" + oldPath + "'";
            mf.songFileName.set(idx, mf.getSong(newPath));
            mf.TEXT_SongFilePath.setText(mf.getPath(newPath));
            status = 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(mf, "Something went wrong, song will not be renamed", "Error", JOptionPane.ERROR_MESSAGE);
            status = 2;
        }
    }
    
    public void insertPlaylist(String playlist) {
        try {
            // playlist must not alrdy exist
            String q2 = "SELECT * FROM music_playlist WHERE name='" + playlist + "'";
            ResultSet rs = db.getData(q2);
            if (rs.next()) {
                JOptionPane.showMessageDialog(mf, "Playlist already exist.\n Playlist will not be added.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String q = "INSERT INTO music_playlist(name) VALUES('" + playlist + "')";
            db.execQuery(q);
            mf.playlist.addElement(playlist);
            // is this really needed ?
            this.playlist.add(playlist);
            status = 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(mf, "An error occured during query execution\n Playlist will not be added.", "Error", JOptionPane.ERROR_MESSAGE);
            status = 2;
        }
    }
    
    public void removePlaylist(int idx) {
        String playlistName = mf.playlist.get(idx).toString();
        
        try {
            String q = "DELETE FROM music_playlist WHERE name='" + playlistName + "'";
            db.execQuery(q);
            mf.playlist.remove(idx);
            status = 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(mf, "Something went wrong, Playlist is probably corrupted now, sorry.", "Error", JOptionPane.ERROR_MESSAGE);
            status = 2;
        }
        
    }
    
    public void batchRemovePlaylist(int idxs[]) {
        String _WHERE_STATEMENT_playlistName = "";
        for (int idx : idxs) {
            _WHERE_STATEMENT_playlistName = _WHERE_STATEMENT_playlistName + "name='" + mf.playlist.get(idx).toString() + "' OR ";
        }
        _WHERE_STATEMENT_playlistName = _WHERE_STATEMENT_playlistName.substring(0, _WHERE_STATEMENT_playlistName.length() - 4);
        try {
            String q = "DELETE FROM music_playlist WHERE " + _WHERE_STATEMENT_playlistName;
            db.execQuery(q);
            for (int a = idxs.length - 1; a >= 0; a--) {
                mf.playlist.remove(idxs[a]);
            }
            status = 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(mf, "Something went wrong, Playlist is probably corrupted now, sorry.", "Error", JOptionPane.ERROR_MESSAGE);
            status = 2;
        }
    }
    
    public void renamePlaylist(int idx, String newName) {
        String playlistName = mf.playlist.get(idx).toString();
        try {
            String q = "UPDATE music_playlist SET name='" + newName + "' WHERE name='" + playlistName + "'";
            db.execQuery(q);
            mf.playlist.set(idx, newName);
            status = 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(mf, "Something went wrong, Playlist is not renamed", "Error", JOptionPane.ERROR_MESSAGE);
            status = 2;
        }
    }

    // updatePath globally
    public void updatePath(String oldpath, String newpath) {
        try {
            String q = "SELECT _id, path FROM music_playlist WHERE path LIKE '%" + oldpath + "%'";
            ResultSet rs = db.getData(q);
            while (rs.next()) {
                int _id = rs.getInt("_id");
                String oldPath_withName = rs.getString("path");
                String newPath_withName = newpath + mf.getSong(oldPath_withName);
                String q2 = "UPDATE music_playlist SET path='" + newPath_withName + "' WHERE _id='" + _id + "'";

                // The reason i didnt use db.execQuery() is because if theres alot of records need to be updated, memory leak might happen as multiple statements are left unclosed
                Statement st = db.conn.createStatement();
                st.executeUpdate(q2);
                st.close();
                
            }

            // THIS WILL CAUSE TROUBLE PLS CHECK IN FUTURE
            filepaths.set(filepaths.indexOf(oldpath), newpath);
            if (mf.TEXT_SongFilePath.getText().equals(oldpath)) {
                mf.TEXT_SongFilePath.setText(newpath);
            }
            
            status = 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Something went wrong, Path Edit failed", "Error", JOptionPane.ERROR_MESSAGE);
            status = 2;
        }
    }
    
    public String pathOf(String playlist, String song) {
        try {
            // System.out.println("playlist: " + playlist + " Song: " + song);
            String sql = "SELECT * FROM music_playlist WHERE name=? AND path LIKE ?";
            // System.out.println(sql);
            PreparedStatement st = db.conn.prepareStatement(sql);
            st.setString(1, playlist);
            st.setString(2, "%" + song + "%");
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                String str = rs.getString("path");
                rs.close();
                st.close();
                return mf.getPath(str);
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Something went wrong during query Execution", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return "";
    }
    
}
