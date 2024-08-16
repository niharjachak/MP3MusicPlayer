package src.assets;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Hashtable;

public class MusicPlayerGUI extends JFrame {

    public static final Color FRAME_COLOR = Color.darkGray;
    public static final Color TEXT_COLOR = Color.white;

    private MusicPlayer musicPlayer;

    private JFileChooser jFileChooser;

    private  JLabel songTitle,songartist;
    private JPanel playbackbutton;
    private JSlider playback;

    public  MusicPlayerGUI(){
        super("Music Player");

        setSize(400,600);


        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        setResizable(false);

        setLayout(null);


        getContentPane().setBackground(FRAME_COLOR);

        musicPlayer= new MusicPlayer(this);

        jFileChooser = new JFileChooser();
        jFileChooser.setCurrentDirectory(new File("src/assets"));

        jFileChooser.setFileFilter(new FileNameExtensionFilter("MP3","mp3"));

        addGuiComponents();
    }
    private void addGuiComponents(){
        addToolbar();

        JLabel songImage = new JLabel(loadImage("src/assets/music.png"));
        songImage.setBounds(0,50,getWidth()-20,225);
        add(songImage);

        songTitle = new JLabel("Song Title");
        songTitle.setBounds(0,285,getWidth()-10,30);
        songTitle.setFont(new Font("Dialog",Font.BOLD,24));
        songTitle.setForeground(TEXT_COLOR);
        songTitle.setHorizontalAlignment(SwingConstants.CENTER);
        add(songTitle);

        songartist = new JLabel("Artist");
        songartist.setBounds(0,315,getWidth()-10,30);
        songartist.setFont(new Font("Dialog",Font.PLAIN,22));
        songartist.setForeground(TEXT_COLOR);
        songartist.setHorizontalAlignment(SwingConstants.CENTER);
        add(songartist);

        playback = new JSlider(JSlider.HORIZONTAL,0,100,0);
        playback.setBounds(getWidth()/2-300/2,365,300,40);
        playback.setBackground(null);
        playback.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                musicPlayer.pauseSong();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                JSlider source = (JSlider) e.getSource();

                int frame = source.getValue();

                musicPlayer.setCurrentFrame(frame);

                musicPlayer.setCurrentTime((int) (frame / (1.3 * musicPlayer.getCurrentSong().getFramerate())));

                musicPlayer.playcurrentSong();

                enablePausedisablePLay();

            }
        });
        add(playback);

        addPlaybackButtons();

    }

    private void addToolbar(){

        JToolBar toolbar = new JToolBar();
        toolbar.setBounds(0,0,getWidth(),25);
        toolbar.setFloatable(false);

        JMenuBar menubar = new JMenuBar();
        toolbar.add(menubar);

        JMenu song = new JMenu("Song");
        menubar.add(song);

        JMenuItem loadsong = new JMenuItem("Load Song");
        loadsong.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result =  jFileChooser.showOpenDialog(MusicPlayerGUI.this);
                File selectedFile =jFileChooser.getSelectedFile();

                if (result== JFileChooser.APPROVE_OPTION && selectedFile!=null){
                    Song song = new Song(selectedFile.getPath());

                    musicPlayer.loadSong(song);

                    updateSongDetails(song);

                    updatePlaybackSlider(song);

                    enablePausedisablePLay();
                }
            }
        });
        song.add(loadsong);

        JMenu playlist = new JMenu("PLaylist");
        menubar.add(playlist);

        JMenuItem createplaylist = new JMenuItem("Create Playlist");

        createplaylist.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MusicPlaylistDialog(MusicPlayerGUI.this).setVisible(true);
            }
        });
        playlist.add(createplaylist);

        JMenuItem load = new JMenuItem("Load Playlist");
        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setFileFilter(new FileNameExtensionFilter("Playlist","txt"));
                jFileChooser.setCurrentDirectory(new File("src/assets"));
                int result = jFileChooser.showOpenDialog(MusicPlayerGUI.this);

                File selectedFile = jFileChooser.getSelectedFile();

                if(result == JFileChooser.APPROVE_OPTION && selectedFile !=null){
                    musicPlayer.stopSong();

                    musicPlayer.loadPlaylist(selectedFile);

                }
            }
        });
        playlist.add(load);

        add(toolbar);


    }

    private void addPlaybackButtons(){
        playbackbutton= new JPanel();
        playbackbutton.setBounds(0,435,getWidth()-10,80);
        playbackbutton.setBackground(null);

        JButton prev = new JButton(loadImage("src/assets/previous.png"));
        prev.setBorderPainted(false);
        prev.setBackground(null);
        prev.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                musicPlayer.previousSong();
            }
        });
        playbackbutton.add(prev);

        JButton play = new JButton(loadImage("src/assets/play.png"));
        play.setBorderPainted(false);
        play.setBackground(null);
        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enablePausedisablePLay();

                musicPlayer.playcurrentSong();
            }
        });
        playbackbutton.add(play);

        JButton pause = new JButton(loadImage("src/assets/pause.png"));
        pause.setBorderPainted(false);
        pause.setBackground(null);
        pause.setVisible(false);
        pause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enablePLaydisablePause();

                musicPlayer.pauseSong();
            }
        });
        playbackbutton.add(pause);

        JButton next = new JButton(loadImage("src/assets/next.png"));
        next.setBorderPainted(false);
        next.setBackground(null);
        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                musicPlayer.nextSong();
            }
        });
        playbackbutton.add(next);

        add(playbackbutton);
    }

    public void setPLayBackSliderValue(int frame){
        playback.setValue(frame);
    }


    public void updateSongDetails(Song song){
        songTitle.setText(song.getSongTitle());
        songartist.setText(song.getArtist());

    }

    public void updatePlaybackSlider(Song song){
        playback.setMaximum(song.getMp3File().getFrameCount());

        Hashtable<Integer,JLabel> labelTable = new Hashtable<>();

        JLabel begining = new JLabel("00:00");
        begining.setFont(new Font("Dialog",Font.BOLD,18));
        begining.setForeground(TEXT_COLOR);

        JLabel end = new JLabel(song.getLength());
        end.setFont(new Font("Dialog",Font.BOLD,18));
        end.setForeground(TEXT_COLOR);

        labelTable.put(0,begining);
        labelTable.put(song.getMp3File().getFrameCount(),end);

        playback.setLabelTable(labelTable);
        playback.setPaintLabels(true);



    }

    public void enablePausedisablePLay(){

        JButton playbutton= (JButton) playbackbutton.getComponent(1);
        JButton pausebutton= (JButton) playbackbutton.getComponent(2);

        playbutton.setVisible(false);
        playbutton.setEnabled(false);

        pausebutton.setVisible(true);
        pausebutton.setEnabled(true);

    }

    public void enablePLaydisablePause(){

        JButton playbutton= (JButton) playbackbutton.getComponent(1);
        JButton pausebutton= (JButton) playbackbutton.getComponent(2);

        playbutton.setVisible(true);
        playbutton.setEnabled(true);

        pausebutton.setVisible(false);
        pausebutton.setEnabled(false);

    }

    private ImageIcon loadImage(String  imagePath){
        try {
            BufferedImage image = ImageIO.read(new File(imagePath));

            return  new ImageIcon(image);

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
        
    }
}

