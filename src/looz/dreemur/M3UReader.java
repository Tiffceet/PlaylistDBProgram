package looz.dreemur;

import java.io.*;
import java.util.ArrayList;

/*
Facts about m3u8:
    - winamp user is going to like it
    - doesnt store playlist name in file content like wpl, so the filename is the playlist title
    - supports UTF-8 Encoding duhh
*/

public class M3UReader {

    String playlist;
    File file_input;
    ArrayList<String> songs;
    int status;
    
    public M3UReader(File in) {
        status = 0;
        this.file_input = in;
        
        // Get playlist name from title
        playlist = file_input.getAbsoluteFile().getName();
        if(playlist.toLowerCase().endsWith(".m3u"))
        {
            playlist = playlist.substring(0, playlist.length()-4);
            // System.out.println(playlist);
        } else if(playlist.toLowerCase().endsWith(".m3u8"))
        {
            playlist = playlist.substring(0, playlist.length()-5);
            // System.out.println(playlist);
        } else
        {
            status = 1;
            System.out.println("M3UReader: Invalid file format passed to constructor");
        }
        songs = new ArrayList<String>();
        if(status == 0)
        {
            read();
            printAllSongs();
        }
    }

    private void read()
    {
        if (!file_input.exists()) {
            status = 1;
            System.out.println("M3UReader: File doesn't exist in read()");
            return;
        }
        try {

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(file_input), "UTF8"));

            String str;

            while ((str = in.readLine()) != null) {
                str = str.trim();
                str = str.replace("\uFEFF", ""); // might be improved in future, code to replace UTF-8 BOM Marker
                if(str.charAt(0) == '#')
                {
                    continue;
                }
                // Leave only the file name
                // If it is a relative path / absolute path, substring it
                if(str.indexOf('\\') != -1){
                    str = str.substring(str.lastIndexOf('\\') + 1, str.length());
                }
                songs.add(str);
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
    
    // Debug function
    private void printAllSongs()
    {
        System.out.println("Playlist title: " + playlist);
        for(String ab : songs)
        {
            System.out.println(ab);
        }
    }
    
}
