package src.assets;

import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

import java.io.*;
import java.nio.Buffer;
import java.util.ArrayList;

public class MusicPlayer extends PlaybackListener {

    private static final Object playSignal = new Object();
    private MusicPlayerGUI musicPlayerGUI;

    private Song currentSong;

    public Song getCurrentSong(){
        return currentSong;
    }

    private ArrayList<Song>playlist;

    private int currentIndex;

    private AdvancedPlayer advancedPlayer;
    private boolean isPaused;

    private boolean songFinished;
    private boolean pressednext,pressedprev;
    private int currentFrame;
    public void setCurrentFrame(int frame){
        currentFrame = frame;
    }

    private int currentTime;
    public void setCurrentTime(int timeinMili){
        currentTime= timeinMili;
    }



    public MusicPlayer(MusicPlayerGUI musicPlayerGUI){
        this.musicPlayerGUI = musicPlayerGUI;

    }

    public void loadSong (Song song){
        currentSong = song;
        playlist = null;

        isPaused = false;
        songFinished= false;
        pressedprev= false;
        pressednext= false;

        if(!songFinished)
            stopSong();

        if(currentSong !=null){
            currentFrame =0;
            currentTime=0;
            musicPlayerGUI.setPLayBackSliderValue(0);
            playcurrentSong();
        }
    }


    public void loadPlaylist(File playlistFile ){
        playlist = new ArrayList<>();

        try {
            FileReader fileReader = new FileReader(playlistFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String songPath;
            while ((songPath= bufferedReader.readLine())!=null){
                Song song = new Song(songPath);
                playlist.add(song);
            }
        }catch (Exception e ){
            e.printStackTrace();
        }

        if(playlist.size()>0){
            musicPlayerGUI.setPLayBackSliderValue(0);
            currentTime= 0;

            currentSong= playlist.get(0);

            currentFrame = 0;

            musicPlayerGUI.enablePausedisablePLay();
            musicPlayerGUI.updateSongDetails(currentSong);
            musicPlayerGUI.updatePlaybackSlider(currentSong);

            playcurrentSong();
        }

    }
    public void pauseSong(){
        if(advancedPlayer !=null){
            isPaused = true;
            stopSong();
        }
    }

    public void stopSong(){
        if(advancedPlayer!=null){
            advancedPlayer.stop();
            advancedPlayer.close();
            advancedPlayer= null;
        }
    }

    public void nextSong(){
        if(playlist== null)
            return;

        if(currentIndex + 1 >playlist.size()-1)
            return;

        pressednext = true;

        if(!songFinished)
            stopSong();



        currentIndex++;
        currentSong = playlist.get(currentIndex);

        currentFrame=0;
        currentTime=0;

        musicPlayerGUI.enablePausedisablePLay();
        musicPlayerGUI.updateSongDetails(currentSong);
        musicPlayerGUI.updatePlaybackSlider(currentSong);

        playcurrentSong();
    }

    public void previousSong(){
        if(playlist== null)
            return;

        if(currentIndex - 1 < 0)
            return;

        pressedprev= true;

        if(!songFinished)
            stopSong();

        currentIndex--;
        currentSong = playlist.get(currentIndex);

        currentFrame=0;
        currentTime=0;

        musicPlayerGUI.enablePausedisablePLay();
        musicPlayerGUI.updateSongDetails(currentSong);
        musicPlayerGUI.updatePlaybackSlider(currentSong);

        playcurrentSong();


    }
    public void playcurrentSong(){
        if(currentSong== null)
            return;
        try {
            FileInputStream fileInputStream= new FileInputStream(currentSong.getFilepath());
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

            advancedPlayer = new AdvancedPlayer(bufferedInputStream);
            advancedPlayer.setPlayBackListener(this);

            startMusicThread();

            startPlayBackSliderThread();

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void startMusicThread(){
      new Thread(new Runnable() {
          @Override
          public void run() {
              try{
                  if(isPaused){
                      synchronized (playSignal){
                          isPaused=false;
                          playSignal.notify();
                      }
                      advancedPlayer.play(currentFrame,Integer.MAX_VALUE);
                  }
                  else{
                      advancedPlayer.play();
                  }
              }catch(Exception e){
                  e.printStackTrace();
              }

          }
      }).start();
    }


    private void startPlayBackSliderThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
            if(isPaused){
                try{
                    synchronized(playSignal){
                        playSignal.wait();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
                while(!isPaused && !songFinished && !pressednext && !pressedprev){
                    try {
                        currentTime++;

                        int calculatedFrame = (int)((double) currentTime* 1.3 * currentSong.getFramerate());
                        musicPlayerGUI.setPLayBackSliderValue(calculatedFrame);
                        Thread.sleep(1);
                    }catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

//    private void setPLaybackSliderThread(){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while(!isPaused){
//                    currentTime ++;
//                    int calculatedFrame =(int)((double) currentTime *1.3*  currentSong.getFramerate());
//                }
//            }
//        }).start();
//    }
//    @Override
    public void playbackStarted(PlaybackEvent evt) {
        System.out.println("Playback Started");
        songFinished= false;
        pressednext= false;
        pressedprev= false;


    }

    @Override
    public void playbackFinished(PlaybackEvent evt) {
        System.out.println("Playback Stopped ");
        if (isPaused){
            currentFrame +=(int)((double)evt.getFrame()*currentSong.getFramerate());
        }
        else{
            if(pressednext|| pressedprev)
                return;

            songFinished= true;

            if(playlist== null)
            {
                musicPlayerGUI.enablePLaydisablePause();
            }
            else {
                if(currentIndex== playlist.size()-1){
                    musicPlayerGUI.enablePLaydisablePause();
                }
                else {
                    nextSong();
                }
            }
        }
    }
}
