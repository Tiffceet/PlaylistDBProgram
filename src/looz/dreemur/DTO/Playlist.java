package looz.dreemur.DTO;

import java.util.ArrayList;

/**
 *
 * @author Looz
 */
public class Playlist {
    private String playlistName;
    /**
     * Songs are stored as filepaths
     */
    private ArrayList<Song> songs;

    public Playlist() {
        this.playlistName = "";
        this.songs = new ArrayList<>();
    }

    public Playlist(String playlistName) {
        this();
        this.playlistName = playlistName;
    }

    public String getPlaylistName() {
        return this.playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public void addSong(Song song) {
        songs.add(song);
    }

    public boolean contains(Song song) {
        for(int i = 0; i < songs.size(); i++) {
            if(songs.get(i).equals(song)) {
                return true;
            }
        }
        return false;
    }
}
