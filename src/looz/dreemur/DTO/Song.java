package looz.dreemur.DTO;

public class Song {

    private String filepath;

    /**
     * CTOR for creating song object
     *
     * @param filepath filepath of the song
     */
    public Song(String filepath) {
        this.filepath = filepath;
    }

    public String getFilepath() {
        return this.filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    @Override
    public boolean equals(Object song) {
        if (!(song instanceof Song)) {
            return false;
        }

        Song s = (Song) song;
        return s.filepath.equals(this.filepath);
    }
}
