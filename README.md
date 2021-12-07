# PlaylistDBProgram
## Objective
To allow user to customise music playlist on their smart phones using computer

## Language Used
- Java 8
- IDE: Netbeans IDE 12.6

## Library Used
- JDBC 3.27.2.1 (Java Database Connectivity API)
- filedrop v1.1 by iHarder.net (http://iharder.sourceforge.net/current/java/filedrop/)
- FlatLAF 1.6.4 (https://search.maven.org/artifact/com.formdev/flatlaf/1.6.4/jar)

## How to build from Apache ant (If you don't have Netbeans IDE)
In the working directory, do `ant clean jar`

And the deployed JAR file will be in the `dist` folder.


## Program Layout: 
![Program Layout](https://i.imgur.com/mBU71IT.png)
1. Menus
2. Playlist listing
3. Songs listing (from selected playlist)
4. Progress Bar

## How does this work ?
This program currently only works with 1 offline music player

> Music Player made by Mobile_V5 (https://play.google.com/store/apps/details?id=media.music.musicplayer)


This program allows you to directly modify backup playlist located in `storage\MusicPlayer9Grid\backups\backup_xxxxxxxxxxxxx.db`
so that you can restore it from the app to update your existing playlist

## Main Features
- Add/Modify/Remove song names from/to the playlist
- Add/Modify/Remove file path (the song files in phone might have different location)
- Import song playlist from computer straight into phone

## How to use this program
For now, you need to have a backup ready to be opened to allow the program to function.

### In the music player app
To make a backup in Music Player app, goto playlist listing > more > backup list
The backup will be located in `storage\MusicPlayer9Grid\backups\backup_xxxxxxxxxxxxx.db` https://i.imgur.com/3dAnQe7.png

### In the program

`File > Open Database...` to open the backup playlist file

Basic Functionality: 
- Click on the playlist names to select them
- Click on the song names to select them
- Double-click on song/playlist name to modify them
- Ctrl + N to add playlist
- Ctrl + Shift + N to add song (In Progress)
- Add songs by dragging music files straight into Songs listing frame (not recommended)
- Import `.wpl` / `.m3u` / `.m3u8` playlist into the program by dragging the playlist file into the Playlist listing box
- `Edit > Song Paths...` to add songs path (the song files in phone might have different location)
- Ctrl + S to save the current backup file

Additional Feature:
- When opening a playlist backup, it will saves existing song filepath(on phone) temporary in the program, so user don't have to type the paths again

## Feature that might be added in future
- Create playlist from scratch without the need to export from the Music Player app first
- Batch remove

## Tested Music Player app version
- 3.6.8
