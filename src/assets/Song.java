package src.assets;

import com.mpatric.mp3agic.Mp3File;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import java.io.File;

public class Song {
    private String songTitle;
    private String artist;
    private String length;
    private String filepath;

    private Mp3File mp3File;
    private double framerate;


    public Song(String filepath){
        this.filepath= filepath;
        try {
            mp3File = new Mp3File(filepath);
            framerate=(double) mp3File.getFrameCount()/mp3File.getLengthInMilliseconds();
            length = convertSongLength();

            AudioFile audioFile = AudioFileIO.read(new File(filepath));

            Tag tag = audioFile.getTag();
            if(tag!= null){
               songTitle= tag.getFirst(FieldKey.TITLE);
                artist= tag.getFirst(FieldKey.ARTIST);
            }
            else{
                songTitle = "N/A";
                artist = "N/A";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private String convertSongLength(){
        long minutes = mp3File.getLengthInSeconds() / 60;
        long seconds = mp3File.getLengthInSeconds() % 60;
        String format=String.format("%02d:%02d",minutes,seconds) ;

        return format;

    }

    public String getSongTitle(){
        return songTitle;
    }
    public String getArtist(){
        return artist;
    }
    public String getLength(){
        return length;
    }
    public String getFilepath() {
        return filepath;
    }

    public Mp3File getMp3File(){
        return mp3File;
    }

    public double getFramerate() {
        return framerate;
    }
}

