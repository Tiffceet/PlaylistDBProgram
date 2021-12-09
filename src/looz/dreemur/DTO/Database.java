package looz.dreemur.DTO;

import java.util.ArrayList;

/**
 * Instance for storing database of playlists
 * 
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

    public ArrayList<String> getFilePaths() {
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

    public ArrayList<String> getSongs(String playlist) {
        Playlist pl = this.getPlaylistByName(playlist);
        ArrayList<Song> songs = pl.getSongs();
        ArrayList<String> retSong = new ArrayList<>();
        for (int i = 0; i < songs.size(); i++) {
            String str = songs.get(i).getFilepath();
            str = str.substring(str.lastIndexOf("/") + 1);
            retSong.add(str);
        }
        return retSong;
    }

    public void insertSong(String playlist, String songpath) {
        Playlist pl = this.getPlaylistByName(playlist);
        pl.addSong(new Song(songpath));
    }

    public void removeSong(String playlist, int idx) {
        Playlist pl = this.getPlaylistByName(playlist);
        pl.getSongs().remove(idx);
    }

    public void batchRemoveSong(String playlist, int[] idxs) {
        ArrayList<Song> songs = this.getPlaylistByName(playlist).getSongs();
        for (int i = 0; i < idxs.length; i++) {
            songs.remove(idxs[i]);
        }
    }

    public void renameSong(String playlist, int idx, String newPath) {
        ArrayList<Song> songs = this.getPlaylistByName(playlist).getSongs();
        songs.get(idx).setFilepath(newPath);
    }

    public void insertPlaylist(String playlist) {
        this.playlists.add(new Playlist(playlist));
    }

    public void removePlaylist(int idx) {
        this.playlists.remove(idx);
    }

    public void batchRemovePlaylist(int idxs[]) {
        for (int i = 0; i < idxs.length; i++) {
            this.playlists.remove(idxs[i]);
        }
    }

    public void renamePlaylist(int idx, String newName) {
        this.playlists.get(idx).setPlaylistName(newName);
    }

    public void updatePath(String oldpath, String newpath) {
        System.out.println("updatePath(): Not Implemented");
    }

    public String pathOf(String playlist, String song) {
        System.out.println("pathOf(): Not Implemented");
        return "Not Implemented";
    }

}
