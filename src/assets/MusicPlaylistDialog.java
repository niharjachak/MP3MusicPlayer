package src.assets;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class MusicPlaylistDialog extends JDialog {
    private MusicPlayerGUI musicPlayerGUI;

    private ArrayList<String>songPath;

    public MusicPlaylistDialog( MusicPlayerGUI musicPlayerGUI){
        this.musicPlayerGUI = musicPlayerGUI;
        songPath = new ArrayList<>();

        setTitle("Create Playlist");
        setSize(400,400);
        setResizable(true);
        getContentPane().setBackground(MusicPlayerGUI.FRAME_COLOR);
        setLayout(null);
        setModal(true);
        setLocationRelativeTo(musicPlayerGUI);

        addDialogComponents();
    }

    private void addDialogComponents(){
        JPanel songContainer = new JPanel();
        songContainer.setLayout(new BoxLayout(songContainer ,BoxLayout.Y_AXIS));
        songContainer.setBounds((int) (getWidth()*0.025),10, (int)(getWidth()*0.90),(int)(getHeight()*0.75));
        add(songContainer);


        JButton addsong = new JButton("Add");
        addsong.setBounds(60,(int)(getHeight()*0.80),100,24);
        addsong.setFont(new Font("Dialog",Font.BOLD,14));
        addsong.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setFileFilter(new FileNameExtensionFilter("MP3","mp3"));
                jFileChooser.setCurrentDirectory(new File("src/assets"));

                int result = jFileChooser.showOpenDialog(MusicPlaylistDialog.this);
                File selectedFile = jFileChooser.getSelectedFile();

                if(result == JFileChooser.APPROVE_OPTION && selectedFile !=null){
                    JLabel filepathlabel = new JLabel(selectedFile.getPath());
                    filepathlabel.setFont(new Font("Dialog",Font.BOLD,12));
                    filepathlabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                    songPath.add(filepathlabel.getText());
                    songContainer.add(filepathlabel);
                    songContainer.revalidate();
                }

            }
        });
        add(addsong);

        JButton saveplaylist = new JButton("Save");
        saveplaylist.setBounds(215,(int)(getHeight()*0.80),100,24);
        saveplaylist.setFont(new Font("Dialog",Font.BOLD,14));
        saveplaylist.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{JFileChooser jFileChooser = new JFileChooser();
                    jFileChooser.setCurrentDirectory(new File("src/assets"));

                    int result = jFileChooser.showSaveDialog(MusicPlaylistDialog.this);

                    if(result ==jFileChooser.APPROVE_OPTION){
                        File selectedFile= jFileChooser.getSelectedFile();
                        if(!selectedFile.getName().substring(selectedFile.getName().length()-4).equalsIgnoreCase("txt")){
                            selectedFile = new File(selectedFile.getAbsoluteFile()+"txt");
                        }
                        selectedFile.createNewFile();

                        FileWriter fileWriter = new FileWriter(selectedFile);
                        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

                        for(String songaPath:songPath){
                            bufferedWriter.write(songaPath+ "\n");
                        }
                        bufferedWriter.close();

                        JOptionPane.showMessageDialog(MusicPlaylistDialog.this,"Playlist Created Succefully!");

                        MusicPlaylistDialog.this.dispose();
                    }

                }catch(Exception m){
                    m.printStackTrace();
                }
            }
        });
        add(saveplaylist);
    }
}
