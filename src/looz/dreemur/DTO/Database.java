package looz.dreemur.DTO;
import java.util.ArrayList;

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
        playlists = new ArrayList<>();
        filepaths = new ArrayList<>();
    }

    public void addPlaylist(Playlist newPlaylist) {
        this.playlists.add(newPlaylist);
    }

    public Playlist getPlaylistByName(String playlistName) throws RuntimeException {
        for (int i = 0; i < this.playlists.size(); i++) {
            Playlist pl = this.playlists.get(i);
            if (pl.getPlaylistName().equals(playlistName)) {
                return pl;
            }
        }
        throw new RuntimeException("Playlist with name " + playlistName + " not found.");
    }

    public void addFilepath(String filepath) {
        this.filepaths.add(filepath);
    }

    public ArrayList<String> getFilepaths() {
        return this.filepaths;
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
