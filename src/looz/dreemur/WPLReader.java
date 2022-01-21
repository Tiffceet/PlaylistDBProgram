package looz.dreemur;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

class WPLReader {

    String playlist;
    ArrayList<String> songs;
    File FILE_input;
    int status;

    public WPLReader(File in) {
        this.FILE_input = in;
        songs = new ArrayList<String>();
        status = 0;
        read();
    }

    private void read() {
        if (!FILE_input.exists() || !FILE_input.getAbsoluteFile().getName().toLowerCase().endsWith(".wpl")) {
            status = 1;
            return;
        }
        try {

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(FILE_input), "UTF8"));

            String str;

            while ((str = in.readLine()) != null) {
                str = str.trim();
                // if its title
                if (str.startsWith("<title>")) {
                    if (str.length() == 15) {
                        playlist = "Empty Playlist";
                    } else {
                        playlist = find_TITLE(str);
                    }
                }
                // if its a media
                if (str.startsWith("<media")) {
                    // System.out.println("strval: " + str);
                    String src = find_SRC(str);
                    songs.add(getSongName_fromSRC(src));
                }

            }
            in.close();
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
            status = 1;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            status = 1;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            status = 1;
        }
    }
    
    // New bug: source dont always ends with mp3
    // Suggested fix: let find_SRC() function add additional " symbol to the end
    //                and use that as ending indicator in this function
    private String getSongName_fromSRC(String src) {
        String songName = "";
        songName = src.substring(src.lastIndexOf("\\") + 1, src.indexOf("\""));
        return songName;
    }

    // Known Bug: if tid doesn't exist, it cause wpl import fail (unreported exception)
    // Bug status: fixed
    // Solution: find next " symbol after the beginning of found src
    private String find_SRC(String in) {
        String src = "";
        // System.out.println("idxofSRC: " + in.indexOf("src=\"") + "\nidxOftid: " + in.indexOf("tid"));
        // src = in.substring(in.indexOf("src=\"") + 5, in.indexOf("tid") - 2);
        src = in.substring(in.indexOf("src=\"") + 5, in.length());
        src = src.substring(0, src.indexOf("\"") + 1); // + 1 because getSongName_fromSRC() needs indicator for the end of song name
        // System.out.println("srcval: " + src);
        
        return src;
    }

    private String find_TITLE(String in) {
        String title = "";
        title = in.substring(in.indexOf("<title>") + 7, in.indexOf("</title>"));
        return title;
    }
}
