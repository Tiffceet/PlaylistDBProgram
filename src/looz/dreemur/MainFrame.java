package looz.dreemur;

import java.awt.*;
import java.awt.event.KeyEvent;
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.filechooser.*;
import net.iharder.dnd.FileDrop;

public class MainFrame extends javax.swing.JFrame {

    private final Color BACKGROUND = new Color(60, 63, 65);
    private final Color TEXTFOREGROUND = new Color(255, 255, 255);
    private final Font DEFAULTFONT = new java.awt.Font("Segoe UI", 0, 12);
    private final Font UTF8_FONT = new java.awt.Font("MS Gothic", 0, 12);
    public DefaultListModel playlist, songFileName;
    public DBManager db;
    public PlaylistManager pm;
    private boolean ProgramActivated;
    private boolean changesMade;

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        playlist = new DefaultListModel();
        songFileName = new DefaultListModel();

        initComponents();
        this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(BACKGROUND);

        // triggers for some event
        ProgramActivated = false;
        changesMade = false;

        // overwrite onclose listener
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (!changesMade) {
                    System.exit(0);
                    return;
                }

                int some_boi_trying_to_close_window = JOptionPane.showConfirmDialog(null,
                        "Do you wish to save before closing?", "Close Window?",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

                if (some_boi_trying_to_close_window == JOptionPane.YES_OPTION) {
                    try {
                        db.overwriteSourceDatabase();
                        // JOptionPane.showMessageDialog(null, "Saved.", "Save", JOptionPane.PLAIN_MESSAGE);
                        System.exit(0);
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                        if (JOptionPane.showOptionDialog(null,
                                "Save Failed.\nClose anyway?", "Error",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE,
                                null,
                                new String[]{"Do not close", "Close Anyway"},
                                "Do not close") == 1) {
                            System.exit(0);
                        } else {
                            //donothing
                        }

                    }

                } else if (some_boi_trying_to_close_window == JOptionPane.NO_OPTION) {
                    System.exit(0);
                }
            }
        });

        // add filedrop listner to Playlist Panel (ScrollPane inherits Panel obviously :3)
        new FileDrop(this.jScrollPane1, new FileDrop.Listener() {
            public void filesDropped(java.io.File[] files) {
                // System.out.println("PLS DO NOT CRASH THANKS");

                // When theres no database loaded into program, consider giving user a error msg?
                if (!ProgramActivated) {
                    return;
                }
                if (files[0].getAbsolutePath().toLowerCase().endsWith(".wpl")) {
                    WPLReader wpl = new WPLReader(files[0]);
                    if (wpl.status == 0) {
                        pm.insertPlaylist(wpl.playlist);

                        // convert ArrayList to array as always
                        String[] arr = new String[wpl.songs.size()];
                        for (int a = 0; a < arr.length; a++) {
                            arr[a] = wpl.songs.get(a);
                        }

                        promptAddSongs(wpl.playlist, arr);
                        changesMade = true;
                    }
                    return;
                }
                if (files[0].getAbsolutePath().toLowerCase().endsWith(".m3u") || files[0].getAbsolutePath().toLowerCase().endsWith(".m3u8")) {
                    M3UReader m3ureader = new M3UReader(files[0]);
                    // System.out.println("Sup");
                    if (m3ureader.status == 0) {
                        
                        pm.insertPlaylist(m3ureader.playlist);
                        
                        // convert ArrayList to array
                        String[] arr = new String[m3ureader.songs.size()];
                        for (int a = 0; a < arr.length; a++) {
                            arr[a] = m3ureader.songs.get(a);
                        }
                        
                        promptAddSongs(m3ureader.playlist, arr);
                        changesMade = true;
                    } else {
                        System.out.println("MainFrame: m3ureader returns non-zero code.");
                    }
                }
            }
        });

        // add filedrop listner to Song'sFileName Panel (ScrollPane inherits Panel obviously :3)
        new FileDrop(this.jScrollPane2, new FileDrop.Listener() {
            public void filesDropped(java.io.File[] files) {
                
                System.out.println("PLS DO NOT CRASH THANKS");

                // playlist need to be selected first
                int idxs[] = LIST_Playlist.getSelectedIndices();
                if (idxs.length <= 0) {
                    JOptionPane.showMessageDialog(null, "No playlist is selected.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String[] songNames = new String[files.length];
                for (int a = 0; a < files.length; a++) {
                    songNames[a] = files[a].getName();
                }
                changesMade = true;
                promptAddSongs(playlist.get(idxs[0]).toString(), songNames);
            }
        });

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        LABEL_Playlist = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        LIST_Playlist = new javax.swing.JList<>();
        LABEL_SongFilename = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        LIST_SongFilename = new javax.swing.JList<>();
        LABEL_SongFilePath = new javax.swing.JLabel();
        TEXT_SongFilePath = new javax.swing.JTextField();
        BTN_ChangeSongFilePath = new javax.swing.JButton();
        bar = new javax.swing.JProgressBar();
        MenuBar = new javax.swing.JMenuBar();
        Menu_File = new javax.swing.JMenu();
        MItem_Open = new javax.swing.JMenuItem();
        MItem_Close = new javax.swing.JMenuItem();
        MItem_NewPlaylist = new javax.swing.JMenuItem();
        MItem_AddSongs = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        MItem_Save = new javax.swing.JMenuItem();
        MItem_SaveAs = new javax.swing.JMenuItem();
        Menu_Edit = new javax.swing.JMenu();
        MItem_DefaultSongfilepath = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        MItem_RenamePlaylist = new javax.swing.JMenuItem();
        MItem_RenameSong = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        MItem_RemovePlaylist = new javax.swing.JMenuItem();
        MItem_RemoveSongFromPlaylist = new javax.swing.JMenuItem();
        Menu_Help = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Playlist Converter");

        LABEL_Playlist.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        LABEL_Playlist.setForeground(TEXTFOREGROUND);
        LABEL_Playlist.setText("Playlist");

        LIST_Playlist.setFont(new java.awt.Font("MS Gothic", 0, 12)); // NOI18N
        LIST_Playlist.setModel(playlist);
        LIST_Playlist.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LIST_PlaylistMouseClicked(evt);
            }
        });
        LIST_Playlist.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                LIST_PlaylistKeyPressed(evt);
            }
        });
        LIST_Playlist.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                LIST_PlaylistValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(LIST_Playlist);

        LABEL_SongFilename.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        LABEL_SongFilename.setForeground(TEXTFOREGROUND);
        LABEL_SongFilename.setText("Song's Filename");

        LIST_SongFilename.setFont(new java.awt.Font("MS Gothic", 0, 12)); // NOI18N
        LIST_SongFilename.setModel(songFileName);
        LIST_SongFilename.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LIST_SongFilenameMouseClicked(evt);
            }
        });
        LIST_SongFilename.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                LIST_SongFilenameKeyPressed(evt);
            }
        });
        LIST_SongFilename.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                LIST_SongFilenameValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(LIST_SongFilename);

        LABEL_SongFilePath.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        LABEL_SongFilePath.setForeground(TEXTFOREGROUND);
        LABEL_SongFilePath.setText("Selected song's filepath: ");

        TEXT_SongFilePath.setEditable(false);
        TEXT_SongFilePath.setFont(new java.awt.Font("MS Gothic", 0, 12)); // NOI18N

        BTN_ChangeSongFilePath.setText("Change");
        BTN_ChangeSongFilePath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTN_ChangeSongFilePathActionPerformed(evt);
            }
        });

        MenuBar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        Menu_File.setText("File");
        Menu_File.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N

        MItem_Open.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        MItem_Open.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        MItem_Open.setText("Open Database...");
        MItem_Open.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MItem_OpenActionPerformed(evt);
            }
        });
        Menu_File.add(MItem_Open);

        MItem_Close.setFont(DEFAULTFONT);
        MItem_Close.setText("Close Database");
        MItem_Close.setEnabled(false);
        MItem_Close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MItem_CloseActionPerformed(evt);
            }
        });
        Menu_File.add(MItem_Close);

        MItem_NewPlaylist.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        MItem_NewPlaylist.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        MItem_NewPlaylist.setText("New Playlist");
        MItem_NewPlaylist.setEnabled(false);
        MItem_NewPlaylist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MItem_NewPlaylistActionPerformed(evt);
            }
        });
        Menu_File.add(MItem_NewPlaylist);

        MItem_AddSongs.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        MItem_AddSongs.setFont(DEFAULTFONT);
        MItem_AddSongs.setText("Add song(s)");
        MItem_AddSongs.setBorderPainted(false);
        MItem_AddSongs.setEnabled(false);
        Menu_File.add(MItem_AddSongs);
        Menu_File.add(jSeparator1);

        MItem_Save.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        MItem_Save.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        MItem_Save.setText("Save");
        MItem_Save.setEnabled(false);
        MItem_Save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MItem_SaveActionPerformed(evt);
            }
        });
        Menu_File.add(MItem_Save);

        MItem_SaveAs.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        MItem_SaveAs.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        MItem_SaveAs.setText("Save As...");
        MItem_SaveAs.setEnabled(false);
        MItem_SaveAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MItem_SaveAsActionPerformed(evt);
            }
        });
        Menu_File.add(MItem_SaveAs);

        MenuBar.add(Menu_File);

        Menu_Edit.setText("Edit");
        Menu_Edit.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N

        MItem_DefaultSongfilepath.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        MItem_DefaultSongfilepath.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        MItem_DefaultSongfilepath.setText("Song Paths...");
        MItem_DefaultSongfilepath.setEnabled(false);
        MItem_DefaultSongfilepath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MItem_DefaultSongfilepathActionPerformed(evt);
            }
        });
        Menu_Edit.add(MItem_DefaultSongfilepath);
        Menu_Edit.add(jSeparator2);

        MItem_RenamePlaylist.setFont(DEFAULTFONT);
        MItem_RenamePlaylist.setText("Rename Playlist");
        MItem_RenamePlaylist.setEnabled(false);
        MItem_RenamePlaylist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MItem_RenamePlaylistActionPerformed(evt);
            }
        });
        Menu_Edit.add(MItem_RenamePlaylist);

        MItem_RenameSong.setFont(DEFAULTFONT);
        MItem_RenameSong.setText("Rename Song");
        MItem_RenameSong.setEnabled(false);
        MItem_RenameSong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MItem_RenameSongActionPerformed(evt);
            }
        });
        Menu_Edit.add(MItem_RenameSong);
        Menu_Edit.add(jSeparator3);

        MItem_RemovePlaylist.setFont(DEFAULTFONT);
        MItem_RemovePlaylist.setText("Remove Playlist");
        MItem_RemovePlaylist.setEnabled(false);
        MItem_RemovePlaylist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MItem_RemovePlaylistActionPerformed(evt);
            }
        });
        Menu_Edit.add(MItem_RemovePlaylist);

        MItem_RemoveSongFromPlaylist.setFont(DEFAULTFONT);
        MItem_RemoveSongFromPlaylist.setText("Remove Song(s) from Playlist");
        MItem_RemoveSongFromPlaylist.setEnabled(false);
        MItem_RemoveSongFromPlaylist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MItem_RemoveSongFromPlaylistActionPerformed(evt);
            }
        });
        Menu_Edit.add(MItem_RemoveSongFromPlaylist);

        MenuBar.add(Menu_Edit);

        Menu_Help.setText("?");
        Menu_Help.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        MenuBar.add(Menu_Help);

        setJMenuBar(MenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LABEL_Playlist)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(LABEL_SongFilename)
                                .addGap(0, 540, Short.MAX_VALUE))
                            .addComponent(jScrollPane2)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(LABEL_SongFilePath)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(TEXT_SongFilePath)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(BTN_ChangeSongFilePath)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LABEL_Playlist)
                    .addComponent(LABEL_SongFilename))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 309, Short.MAX_VALUE)
                    .addComponent(jScrollPane2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BTN_ChangeSongFilePath)
                    .addComponent(TEXT_SongFilePath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LABEL_SongFilePath))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void MItem_NewPlaylistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MItem_NewPlaylistActionPerformed
        this.promptNewPlaylist();
        System.out.println("New Playlist Operated");
    }//GEN-LAST:event_MItem_NewPlaylistActionPerformed

    private void MItem_OpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MItem_OpenActionPerformed
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

        int returnValue = jfc.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION && jfc.getSelectedFile().exists()) {
            hardReset();
            File selectedFile = jfc.getSelectedFile();

            // if the extension is wrong, do not proceed
            if (!selectedFile.getAbsolutePath().toLowerCase().endsWith(".db")) {
                JOptionPane.showMessageDialog(this, "Given file is not in correct format (.db)");
                return;
            }

            // reads DB for further queries
            db = new DBManager(this, selectedFile);

            // proceed only if database structure is correct
            if (db.status == 0) {

                ////// more code
                pm = new PlaylistManager(this, db);

                // proceed only if database's data is not corrupted
                if (pm.status == 0) {
                    toggleMenuItem(true);
                    for (String pl : pm.playlist) {
                        this.playlist.addElement(pl);
                    }
                    ProgramActivated = true;
                    changesMade = true;
                }

            }
        } else {
            System.out.println("No File Selected");
            JOptionPane.showMessageDialog(this, "No file is selected");

        }
        System.out.println("Open Operated");
    }//GEN-LAST:event_MItem_OpenActionPerformed

    private void MItem_SaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MItem_SaveActionPerformed
        try {
            db.overwriteSourceDatabase();
            JOptionPane.showMessageDialog(null, "Saved.", "Save", JOptionPane.PLAIN_MESSAGE);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Save Failed", "Error", JOptionPane.ERROR_MESSAGE);
        }
        System.out.println("Save Operated");
        changesMade = false;
    }//GEN-LAST:event_MItem_SaveActionPerformed

    private void MItem_SaveAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MItem_SaveAsActionPerformed

        System.out.println("Save As Operated");
    }//GEN-LAST:event_MItem_SaveAsActionPerformed

    private void MItem_DefaultSongfilepathActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MItem_DefaultSongfilepathActionPerformed
        this.promptSongPaths();
        System.out.println("Song Path prompt operated");
    }//GEN-LAST:event_MItem_DefaultSongfilepathActionPerformed

    private void LIST_PlaylistValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_LIST_PlaylistValueChanged

        // prevent multiple valueChanged() event being fired as my listModel is always changing
        if (evt.getValueIsAdjusting()) {
            return;
        }

        int[] idxs = LIST_Playlist.getSelectedIndices();

        // if nothing is selected clear songFileName and return -> usually happens when user deletes a playlist
        if (idxs.length < 1) {
            this.songFileName.removeAllElements();
            return;
        }
        // ensure visibility when scrolling or other stuff
        LIST_Playlist.ensureIndexIsVisible(idxs[0]);

        this.songFileName.removeAllElements();
        int idx = this.LIST_Playlist.getSelectedIndex();

        // ensure selected idx always in view
        this.LIST_Playlist.ensureIndexIsVisible(idx);

        String playlist_name = playlist.get(idx).toString();
        ArrayList<String> songs = pm.getSongs(playlist_name);

        // if any playlist failed to obtain songs from database properly, close database
        if (pm.status == 1) {
            hardReset();
            return;
        }
        for (String str : songs) {
            this.songFileName.addElement(str);
        }


    }//GEN-LAST:event_LIST_PlaylistValueChanged

    private void LIST_PlaylistMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LIST_PlaylistMouseClicked

        // checks for doubleclick
        if (evt.getClickCount() == 2) {
            int idx = LIST_Playlist.getSelectedIndex();
            if (idx == -1) {
                return;
            }

            this.promptRenamePlaylist(idx);

        }
    }//GEN-LAST:event_LIST_PlaylistMouseClicked

    private void LIST_PlaylistKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_LIST_PlaylistKeyPressed

        int[] idxs = this.LIST_Playlist.getSelectedIndices();
        if (evt.getKeyCode() == KeyEvent.VK_F2 && this.LIST_Playlist.isFocusOwner() && idxs.length > 0) {
            changesMade = true;
            this.promptRenamePlaylist(idxs[0]);
        }

        if (evt.getKeyCode() == KeyEvent.VK_DELETE && this.LIST_Playlist.isFocusOwner() && idxs.length > 0) {
            changesMade = true;
            if (idxs.length > 1) {
                promptBatchDeletePlaylist(idxs);
            } else {
                promptDeletePlaylist(idxs[0]);
            }

            // make sure when something is deleted, it re-selects the nearest index
            LIST_Playlist.setSelectedIndex((idxs[0] <= playlist.getSize() - 1) ? idxs[0] : -1);
        }
    }//GEN-LAST:event_LIST_PlaylistKeyPressed

    private void MItem_RenamePlaylistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MItem_RenamePlaylistActionPerformed
        changesMade = true;
        int[] idxs = this.LIST_Playlist.getSelectedIndices();
        if (idxs.length == 0) {
            JOptionPane.showMessageDialog(null, "No playlist is being selected", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        this.promptRenamePlaylist(idxs[0]);
    }//GEN-LAST:event_MItem_RenamePlaylistActionPerformed

    private void MItem_RemovePlaylistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MItem_RemovePlaylistActionPerformed
        changesMade = true;
        int[] idxs = this.LIST_Playlist.getSelectedIndices();
        if (idxs.length == 0) {
            JOptionPane.showMessageDialog(null, "No playlist is being selected", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (idxs.length == 1) {
            promptDeletePlaylist(idxs[0]);
            return;
        }
        if (idxs.length > 1) {
            promptBatchDeletePlaylist(idxs);
            return;
        }
    }//GEN-LAST:event_MItem_RemovePlaylistActionPerformed

    private void LIST_SongFilenameValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_LIST_SongFilenameValueChanged
        // prevent multiple valueChanged() event being fired as my listModel is always changing
        if (evt.getValueIsAdjusting()) {
            return;
        }

        int[] idxs = this.LIST_SongFilename.getSelectedIndices();
        if (idxs.length <= 0) {
            return;
        }

        // Code where if length is not zero then should be done
        // ensure visibility when scrolling or other stuff
        LIST_SongFilename.ensureIndexIsVisible(idxs[0]);

        // get filepath to be shown
        TEXT_SongFilePath.setText(pm.pathOf(LIST_Playlist.getSelectedValue(), LIST_SongFilename.getSelectedValue()));

        // Code where specifically need to know whether idxs is "more than 1" or "1-only"
        if (idxs.length == 1) {
            // if only one idx is selected
            return;
        }

        if (idxs.length > 1) {
            // if multiple stuff is selected
            return;
        }


    }//GEN-LAST:event_LIST_SongFilenameValueChanged

    private void LIST_SongFilenameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_LIST_SongFilenameKeyPressed
        int[] idxs = this.LIST_SongFilename.getSelectedIndices();
        changesMade = true;
        if (evt.getKeyCode() == KeyEvent.VK_F2 && this.LIST_SongFilename.isFocusOwner() && idxs.length > 0) {
            
            // if user request rename song(1 at a time only)
            promptRenameSong(String.valueOf(LIST_Playlist.getSelectedValue()), idxs[0]);
        }

        if (evt.getKeyCode() == KeyEvent.VK_DELETE && this.LIST_SongFilename.isFocusOwner() && idxs.length > 0) {
            // user requests delete song from playlist
            if (idxs.length > 1) {
                // user requests batch delete
                pm.batchRemoveSong(LIST_Playlist.getSelectedValue(), idxs);

            } else {
                pm.removeSong(LIST_Playlist.getSelectedValue(), idxs[0]);
            }

            // make sure when something is deleted, it re-selects the nearest index
            LIST_SongFilename.setSelectedIndex((idxs[0] <= songFileName.getSize() - 1) ? idxs[0] : -1);
        }
    }//GEN-LAST:event_LIST_SongFilenameKeyPressed

    private void LIST_SongFilenameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LIST_SongFilenameMouseClicked
        // checks for doubleclick
        if (evt.getClickCount() == 2) {
            changesMade = true;
            int idx = LIST_SongFilename.getSelectedIndex();
            if (idx == -1) {
                return;
            }

            this.promptRenameSong(LIST_Playlist.getSelectedValue(), idx);

        }
    }//GEN-LAST:event_LIST_SongFilenameMouseClicked

    private void MItem_CloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MItem_CloseActionPerformed
        try {
            db.overwriteSourceDatabase();
            hardReset();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }//GEN-LAST:event_MItem_CloseActionPerformed

    private void BTN_ChangeSongFilePathActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTN_ChangeSongFilePathActionPerformed
        changesMade = true;
        int _PList_idx = LIST_Playlist.getSelectedIndex();
        int _Song_idx = LIST_SongFilename.getSelectedIndex();
        if (_PList_idx == -1 || _Song_idx == -1) {
            return;
        }

        promptRenameSong(LIST_Playlist.getSelectedValue(), LIST_SongFilename.getSelectedIndex());
    }//GEN-LAST:event_BTN_ChangeSongFilePathActionPerformed

    private void MItem_RenameSongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MItem_RenameSongActionPerformed
        BTN_ChangeSongFilePathActionPerformed(evt);
    }//GEN-LAST:event_MItem_RenameSongActionPerformed

    private void MItem_RemoveSongFromPlaylistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MItem_RemoveSongFromPlaylistActionPerformed
        changesMade = true;
        int[] idxs = this.LIST_SongFilename.getSelectedIndices();

        // user requests delete song from playlist
        if (idxs.length > 1) {
            // user requests batch delete
            pm.batchRemoveSong(LIST_Playlist.getSelectedValue(), idxs);

        } else {
            pm.removeSong(LIST_Playlist.getSelectedValue(), idxs[0]);
        }

        // make sure when something is deleted, it re-selects the nearest index
        LIST_SongFilename.setSelectedIndex((idxs[0] <= songFileName.getSize() - 1) ? idxs[0] : -1);

    }//GEN-LAST:event_MItem_RemoveSongFromPlaylistActionPerformed

    // =========================================================================
    // My Functions
    // =========================================================================
    void toggleMenuItem(boolean mode) {
        MItem_Close.setEnabled(mode);

        MItem_NewPlaylist.setEnabled(mode);
        MItem_Save.setEnabled(mode);
        MItem_SaveAs.setEnabled(mode);
        MItem_DefaultSongfilepath.setEnabled(mode);
        MItem_AddSongs.setEnabled(mode);
        MItem_RenamePlaylist.setEnabled(mode);
        MItem_RenameSong.setEnabled(mode);
        MItem_RemovePlaylist.setEnabled(mode);
        MItem_RemoveSongFromPlaylist.setEnabled(mode);
        BTN_ChangeSongFilePath.setEnabled(mode);
    }

    void hardReset() {
        toggleMenuItem(false);
        this.playlist.removeAllElements();
        this.songFileName.removeAllElements();
        if (db != null && db.conn != null) {
            try {
                db.conn.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        db = null;
        pm = null;
        ProgramActivated = false;
    }

    void promptNewPlaylist() {
        changesMade = true;
        // Custom Components to be added to OptionPane
        JLabel label1 = new JLabel("New Playlist's Name?");
        label1.setFont(DEFAULTFONT);
        JTextField jtf = new JTextField();
        jtf.setFont(UTF8_FONT);

        JComponent[] jc = new JComponent[]{
            label1,
            jtf
        };

        // get selected choice
        String[] yes_no = {"Add", "Cancel"};
        int result = JOptionPane.showOptionDialog(null, jc, "New Playlist...", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, yes_no, yes_no[0]);

        String newPlaylistName = jtf.getText().trim();
        if (newPlaylistName.equals("") && result == 0) {
            JOptionPane.showMessageDialog(null, "Empty name is not allowed !", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (result == 0) {
            pm.insertPlaylist(newPlaylistName);
        }

    }

    void promptRenamePlaylist(int idx) {
        changesMade = true;
        // idx refers to which index of shown playlist on screen to be renamed
        // Prompt renaming dialog
        JTextField jtf = new JTextField();
        jtf.setFont(UTF8_FONT);
        jtf.setText(playlist.get(idx).toString());
        String[] options = {"Rename", "Cancel"};

        int result = JOptionPane.showOptionDialog(null, jtf, "Rename Playlist?", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        if (result == 0) {
            String newName = jtf.getText().trim();
            pm.renamePlaylist(idx, newName);
        }
    }

    void promptDeletePlaylist(int idx) {
        changesMade = true;
        String playlistName = playlist.get(idx).toString();
        int dialogResult = JOptionPane.showConfirmDialog(null, "Do you wish to erase this playlist ?\n\n(" + playlistName + ")", "Warning", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            pm.removePlaylist(idx);
        }
    }

    void promptBatchDeletePlaylist(int[] idxs) {
        changesMade = true;
        String playlistNames = "";
        for (int idx : idxs) {
            playlistNames = playlistNames + "(" + playlist.get(idx).toString() + ")\n";
        }
        int dialogResult = JOptionPane.showConfirmDialog(null, "Do you wish to erase these playlist ?\n\n" + playlistNames, "Warning", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            pm.batchRemovePlaylist(idxs);
        }
    }

    void promptRenameSong(String playlist, int idx) {
        changesMade = true;
        // Convert pathlist(ArrayList) -> String Array
        // Reason: JComboBox dont accept ArrayList >:(
        Object[] path_list = pm.filepaths.toArray();
        String[] _str_path_list = new String[path_list.length];
        for (int a = 0; a < path_list.length; a++) {
            _str_path_list[a] = path_list[a].toString();
        }

        // setting up dialog
        JLabel _dialog_playlist = new JLabel("Playlist: " + playlist);
        JLabel _lab_path = new JLabel("Path: ");
        JComboBox combo = new JComboBox(_str_path_list);

        // make sure combobox finds the path before proceed as im passing the value of combobox directly into pm.renameSong();
        boolean pathFound = false;
        String songPathFromInterface = TEXT_SongFilePath.getText();
        for (int a = 0; a < path_list.length; a++) {
            if (path_list[a].equals(songPathFromInterface)) {
                combo.setSelectedIndex(a);
                pathFound = true;
            }
        }

        if (!pathFound) {
            /*
            System.out.println("songPathFromInterface: " + songPathFromInterface);
            for (int a = 0; a < path_list.length; a++) {
                System.out.println("comboBox[" + a + "]: " + combo.getItemAt(a).toString());
            }
             */

        }

        JLabel _lab_song = new JLabel("Song Name: ");
        JTextField _txt_song = new JTextField(songFileName.get(idx).toString());

        _dialog_playlist.setFont(UTF8_FONT);
        _lab_path.setFont(DEFAULTFONT);
        combo.setFont(UTF8_FONT);
        _lab_song.setFont(DEFAULTFONT);
        _txt_song.setFont(UTF8_FONT);

        JComponent[] jc = new JComponent[]{
            _dialog_playlist,
            _lab_path,
            combo,
            _lab_song,
            _txt_song
        };

        String[] choice = {"Update", "Cancel"};
        int result = JOptionPane.showOptionDialog(null, jc, "Edit song", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, choice, choice[0]);
        if (result == 0) {
            // validate inputs
            String _str_songname = _txt_song.getText().trim();
            if (_str_songname.equals("")) {
                JOptionPane.showMessageDialog(null, "Empty song name is not allowed.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String _str_fullsong_path = String.valueOf(combo.getSelectedItem()) + _str_songname;
            pm.renameSong(playlist, idx, _str_fullsong_path);
        }
    }

    void promptBatchDeleteSongs() {
        changesMade = true;
    }

    void promptAddSongs(String playlist, String[] songName) {
        changesMade = true;
        if (pm.playlist.size() == 0) {
            JOptionPane.showMessageDialog(null, "No playlist is selected,\nPlease use CTRL + N to add playlist", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (pm.filepaths.size() == 0) {
            JOptionPane.showMessageDialog(null, "No path is defined,\nPlease use CTRL + SHIFT + P to add path", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int amt = songName.length;

        // Convert pathlist(ArrayList) -> String Array
        // Reason: JComboBox dont accept ArrayList >:(
        Object[] path_list = pm.filepaths.toArray();
        String[] _str_path_list = new String[path_list.length];
        for (int a = 0; a < path_list.length; a++) {
            _str_path_list[a] = path_list[a].toString();
        }

        // ==========================
        // checks for irregular files
        // ==========================
        ArrayList<Integer> irregular_file_idx = new ArrayList<Integer>();
        int irregular_file_name_count = 0;
        for (int a = 0; a < songName.length; a++) {
            if (!songName[a].toLowerCase().endsWith(".mp3") && !songName[a].toLowerCase().endsWith(".flac")) {
                irregular_file_idx.add(a);
                irregular_file_name_count++;
            }
        }
        String optionalMSG = irregular_file_name_count == 0 ? "" : "!! --> " + irregular_file_name_count + " of them is not .mp3 <-- !!";
        JLabel _opt_LAB_opt = new JLabel(optionalMSG);
        _opt_LAB_opt.setFont(DEFAULTFONT);
        // ==========================
        // ==========================

        // setting up dialog
        JLabel _LAB_1 = new JLabel(amt + " of songs will be added into " + playlist + "\n");

        JLabel _LAB_2 = new JLabel("Please select path of these songs: ");
        JComboBox _COM_BOX_paths = new JComboBox(_str_path_list);
        _LAB_1.setFont(UTF8_FONT);
        _LAB_2.setFont(DEFAULTFONT);

        JComponent[] jc = new JComponent[]{
            _LAB_1,
            _opt_LAB_opt,
            _LAB_2,
            _COM_BOX_paths
        };

        String[] selection = irregular_file_name_count == 0 ? new String[]{"Add", "Add Path...", "Cancel"} : new String[]{"Add", "Add Path...", "See what files are not .mp3", "Cancel"};
        int result = JOptionPane.showOptionDialog(null, jc, "Add Songs?", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, selection, selection[0]);

        if (result == 1) {
            // users decide to add path first
            promptAddPath();
            promptAddSongs(playlist, songName);
            // adding return to prevent recursive stuff to happen
            return;
        }
        // ==========================
        // checks for irregular files
        // ==========================

        if (irregular_file_name_count != 0 && result == 2) {
            // User wants to see what files he/she added wrongly
            DefaultListModel _opt_model = new DefaultListModel();
            JList _opt_list = new JList();
            _opt_list.setModel(_opt_model);
            JScrollPane _opt_scrollPane = new JScrollPane();
            _opt_scrollPane.setViewportView(_opt_list);
            for (int idx : irregular_file_idx) {
                _opt_model.addElement(songName[idx]);
            }
            JComponent[] _opt_component = new JComponent[]{
                _opt_scrollPane,
                new JLabel("Add anyway ?")
            };

            String[] _opt_selection = {"Add anyway", "Don't add these", "Cancel"};
            int _opt_result = JOptionPane.showOptionDialog(null, _opt_component, "Hmm?", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, _opt_selection, _opt_selection[0]);
            if (_opt_result == 0) {
                // user checks and still want to add anyway
                result = 0;
            } else if (_opt_result == 1) {
                // this is complicated AHHHHH
                // user dont want to add some of the files he chose now FML
                String[] _new_songName = new String[songName.length - irregular_file_name_count];
                int _new_arr_counter = 0;
                for (int a = 0; a < songName.length; a++) {
                    if (irregular_file_idx.indexOf(a) == -1) {
                        // current idx is still wanted
                        _new_songName[_new_arr_counter] = songName[a];
                        _new_arr_counter++;
                    }
                }

                songName = _new_songName;
                result = 0;
            } else {
                // go back to previous window instead of doing shit
                promptAddSongs(playlist, songName);
                return;
            }
        }

        // ==========================
        // ==========================
        if (result == 0) {
            // user decided to add songs
            String path = String.valueOf(_COM_BOX_paths.getSelectedItem());

            bar.setValue(0);
            bar.setMaximum(songName.length);

            int a = 0;
            while (a < songName.length) {
                pm.insertSong(playlist, path + songName[a]);
                updateProgressBar(a);
                a++;
            }
            updateProgressBar(a);
            a = 0;

            JOptionPane.showMessageDialog(null, "Songs added.", "", JOptionPane.INFORMATION_MESSAGE);
            updateProgressBar(a);
        }

    }

    void promptSongPaths() {
        changesMade = true;
        // Convert pathlist(ArrayList) -> String Array
        // Reason: JComboBox dont accept ArrayList >:(
        Object[] path_list = pm.filepaths.toArray();
        String[] _str_path_list = new String[path_list.length];
        for (int a = 0; a < path_list.length; a++) {
            _str_path_list[a] = path_list[a].toString();
        }

        // Setting up dialog component
        JLabel _lab_1 = new JLabel("Detected Paths: ");
        JComboBox pathList_combo = new JComboBox(_str_path_list);
        pathList_combo.setFont(UTF8_FONT);

        _lab_1.setFont(DEFAULTFONT);

        JComponent[] _comp = new JComponent[]{
            _lab_1,
            pathList_combo

        };

        String[] selection = {"Add Path...", "Edit Path...", "Cancel"};
        int result = JOptionPane.showOptionDialog(null, _comp, "Song Paths", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, selection, selection[2]);
        if (result == 0) {
            // Add Path selected
            promptAddPath();
            promptSongPaths();
            return;
        } else if (result == 1) {
            // Edit Path selected
            if (pm.filepaths.size() == 0) {
                JOptionPane.showMessageDialog(null, "Sorry but you cannot edit empty path. \nPlease use \"Add Path...\"", "Error", JOptionPane.ERROR_MESSAGE);
                promptSongPaths();
                return;
            }
            promptEditPath(String.valueOf(pathList_combo.getSelectedItem()));
            promptSongPaths();
            return;
        } else {
            // otherwise
        }
    }

    void promptAddPath() {
        changesMade = true;
        String[] s2 = {"Add", "Cancel"};
        JTextField _TXT_newPathName = new JTextField();
        _TXT_newPathName.setFont(UTF8_FONT);
        int r2 = JOptionPane.showOptionDialog(null, _TXT_newPathName, "New Path ?", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, s2, s2[0]);
        if (r2 == 0) {
            // User had given new path
            String newPath = _TXT_newPathName.getText();

            // path validation -> compaitable with android only probably
            if (newPath.length() == 0 || newPath.charAt(0) != '/' || newPath.charAt(newPath.length() - 1) != '/') {
                JOptionPane.showMessageDialog(null, "New Path given is not valid.\n\nPlease make sure your path ends and start with '/'\nEmpty path is not allowed", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Add path officially
            pm.filepaths.add(newPath);

            JOptionPane.showMessageDialog(null, "Path Added successfully (session)", "Success !", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    void promptEditPath(String _oldPath) {
        changesMade = true;
        // setting up dialog
        String[] s3 = {"Update", "Cancel"};
        JLabel _LAB_oldPath = new JLabel("Old Path: " + _oldPath + "\n");
        JLabel _LAB_newPath_TXT = new JLabel("New Path: ");
        JTextField _TXT_newPath_toBeReplaced = new JTextField(_oldPath);
        _LAB_oldPath.setFont(UTF8_FONT);
        _TXT_newPath_toBeReplaced.setFont(UTF8_FONT);
        JLabel _fair_warning = new JLabel("\nWARNING: UPDATING PATH WILL OVERWRITE ALL OLD PATH INTO THE NEW ONES");

        JComponent[] jc = new JComponent[]{
            _LAB_oldPath,
            _LAB_newPath_TXT,
            _TXT_newPath_toBeReplaced,
            _fair_warning
        };

        int result3 = JOptionPane.showOptionDialog(null, jc, "Update Path", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, s3, s3[0]);
        if (result3 == 0) {
            // User decided to Update path
            String newPath = _TXT_newPath_toBeReplaced.getText();
            String oldPath = _oldPath;

            // path validation -> compaitable with android only probably
            if (newPath.charAt(0) != '/' || newPath.charAt(newPath.length() - 1) != '/') {
                JOptionPane.showMessageDialog(null, "New Path given is not valid.\n\nPlease make sure your path ends and start with '/'", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Update Path <-- DANGEROUS SHIT RIGHT HERE PLEASE STABLISE UPDATE FUNCTION THANKS
            pm.updatePath(oldPath, newPath);
            JOptionPane.showMessageDialog(null, "Path Updated successfully.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    void updateProgressBar(int val) {
        bar.setValue(val);
        bar.update(bar.getGraphics());
        jScrollPane2.update(jScrollPane2.getGraphics());
        LIST_SongFilename.update(LIST_SongFilename.getGraphics());
    }

    public String getPath(String absolutePath) {
        return absolutePath.substring(0, absolutePath.lastIndexOf("/") + 1);
    }

    public String getSong(String absolutePath) {
        return absolutePath.substring(absolutePath.lastIndexOf("/") + 1);
    }

    // =========================================================================
    // =========================================================================
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BTN_ChangeSongFilePath;
    private javax.swing.JLabel LABEL_Playlist;
    private javax.swing.JLabel LABEL_SongFilePath;
    private javax.swing.JLabel LABEL_SongFilename;
    private javax.swing.JList<String> LIST_Playlist;
    private javax.swing.JList<String> LIST_SongFilename;
    private javax.swing.JMenuItem MItem_AddSongs;
    private javax.swing.JMenuItem MItem_Close;
    private javax.swing.JMenuItem MItem_DefaultSongfilepath;
    private javax.swing.JMenuItem MItem_NewPlaylist;
    private javax.swing.JMenuItem MItem_Open;
    private javax.swing.JMenuItem MItem_RemovePlaylist;
    private javax.swing.JMenuItem MItem_RemoveSongFromPlaylist;
    private javax.swing.JMenuItem MItem_RenamePlaylist;
    private javax.swing.JMenuItem MItem_RenameSong;
    private javax.swing.JMenuItem MItem_Save;
    private javax.swing.JMenuItem MItem_SaveAs;
    private javax.swing.JMenuBar MenuBar;
    private javax.swing.JMenu Menu_Edit;
    private javax.swing.JMenu Menu_File;
    private javax.swing.JMenu Menu_Help;
    public javax.swing.JTextField TEXT_SongFilePath;
    private javax.swing.JProgressBar bar;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    // End of variables declaration//GEN-END:variables
}
