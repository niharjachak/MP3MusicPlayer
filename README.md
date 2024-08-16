# MP3MusicPlayer
The MP3 Music Player is a desktop application built using Java Swing that allows users to play and manage their MP3 music files. The application features a clean and intuitive graphical user interface (GUI) where users can load, play, pause, and navigate through songs. Additionally, users can create and manage playlists, adding multiple songs and saving them for later use.

## Features
-Play MP3 Files: Load and play MP3 files with support for basic playback controls (play, pause, next, previous).

-Playlist Management: Create and save playlists in .txt format. Load playlists to automatically play a sequence of songs.

-Song Details Display: The GUI displays the song title, artist, and duration.

-Customizable GUI: The application features a sleek, black-themed user interface.

-Seek Functionality: Users can navigate to different parts of a song using the seek bar.

-Support for ID3 Tags: The player reads and displays ID3 tag information, including song title and artist.


## Libraries Used
1.Java Swing: For building the graphical user interface.

2.JLayer (javazoom.jl): For MP3 playback.

3.mp3agic: For reading and parsing MP3 files.

4.JAudiotagger: For handling and displaying metadata (ID3 tags) from MP3 files.

## How to Use
Load a Song: Click on the Song > Load Song menu option to select an MP3 file from your file system.

Play/Pause a Song: Use the play and pause buttons to control playback.

Next/Previous Song: Navigate through songs in the loaded playlist using the next and previous buttons.

Create a Playlist: Add songs and save your playlist using the Playlist > Create Playlist option.

Load a Playlist: Load an existing playlist using the Playlist > Load Playlist option.
