package rb00166_project_com1028;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

/**
 * This singleton class is in charge of creating, managing and manipulating Song objects for the system.
 * 
 * @author Ryan Burke
 */
public class SongController {

  /** An instance of itself to return. */
  private static SongController songController = null;
  /** A list of Song objects created from file paths. */
  private List<Song>            songList;

  /**
   * The private constructor for the SongController which stops it being created outside of this class.
   */
  private SongController() {
    this.songList = new ArrayList<Song>();
  }

  /**
   * This method will convert a list of file paths into Song objects. It uses the JAudioTagger external library in order to extract
   * the ID3 tags embedded within the audio files and add these into a Song object as attributes. These Songs are then added to a
   * songList which can be accessed through the getSongList method.
   * 
   * @param files
   *          The files to be processed and converted into Song objects.
   * 
   */
  public void convertFiles(Iterator<File> files) {
    int counter = 1;
    this.songList.clear();
    // Loop through the files.
    while (files.hasNext()) {
      // Create a temporary file using the next file.
      File tmpFile = files.next();
      AudioFile audioFile = null;
      try {
        audioFile = AudioFileIO.read(tmpFile);
      }
      catch (Exception e) {
        // There are going to be a files it cant read, dont want to crash the program every time.
        System.out.println("Not a valid file.");
      }
      // If audioFile is not null then it is a valid file.
      if (audioFile != null) {
        // Get the tag from the audioFile.
        Tag tag = audioFile.getTag();
        // If the tag exists.
        if (tag != null) {
          // Get the audioheader for the track length.
          AudioHeader header = audioFile.getAudioHeader();
          // Create a new song using the ID3 data from the audioFile.
          Song tmpSong = new Song(tag.getFirst(FieldKey.TITLE), tag.getFirst(FieldKey.ARTIST), tag.getFirst(FieldKey.ALBUM),
              tag.getFirst(FieldKey.YEAR), tag.getFirst(FieldKey.GENRE), header.getTrackLength(), tmpFile.getPath(), counter);
          // Add this song to the songList.
          this.songList.add(tmpSong);
          counter++;
        }
        else {
          System.out.println("No tags found");
        }
      }
    }
  }

  /**
   * This method returns the songList as an iterator as to protect its data.
   * 
   * @return The songList as an iterator.
   */
  public Iterator<Song> create_iterator() {
    return this.songList.iterator();
  }

  /**
   * This method will get the file path for a song with the given ID. This is needed by the table as the filepath is not stored in
   * the table but sometimes it is needed by it.
   * 
   * @param id
   *          The id of the song to find the file path with.
   * @return
   */
  public String getPath(String id) {
    // Loop through each song.
    for (Song song : this.songList) {
      // If the songs id matches the one given
      if (song.getId() == Integer.parseInt(id)) {
        // Return that songs filepath.
        return song.getFilePath();
      }
    }
    return null;
  }

  /**
   * This method gets the top statistics within the songList.
   * 
   * @return Top artist, album, genre and year. In that order.
   */
  public String[] getTopStats() {

    // Create the string array to return.
    String[] results = new String[4];

    // Create 4 hashmaps.
    HashMap<String, Integer> artistMap = new HashMap<String, Integer>();
    HashMap<String, Integer> albumMap = new HashMap<String, Integer>();
    HashMap<String, Integer> genreMap = new HashMap<String, Integer>();
    HashMap<String, Integer> yearMap = new HashMap<String, Integer>();

    // For every song in the songList.
    for (Song song : this.songList) {
      // If the artistMap doesn't already contain this artist.
      if (!artistMap.containsKey(song.getArtist())) {
        // Then add it to the map with a value of 1.
        artistMap.put(song.getArtist(), 1);
      }
      // If the artistMap does contain this artist.
      else {
        // Then increment the number it is linked to.
        artistMap.put(song.getArtist(), artistMap.get(song.getArtist()) + 1);
      }
      // If the albumMap doesn't already contain this album.
      if (!albumMap.containsKey(song.getAlbum())) {
        // Then add it to the map with a value of 1.
        albumMap.put(song.getAlbum(), 1);
      }
      // If the albumMap does contain this artist.
      else {
        // Then increment the number it is linked to.
        albumMap.put(song.getAlbum(), albumMap.get(song.getAlbum()) + 1);
      }
      // If the genreMap doesn't already contain this album.
      if (!genreMap.containsKey(song.getGenre())) {
        // Then add it to the map with a value of 1.
        genreMap.put(song.getGenre(), 1);
      }
      // If the genreMap does contain this artist.
      else {
        // Then increment the number it is linked to.
        genreMap.put(song.getGenre(), genreMap.get(song.getGenre()) + 1);
      }
      // If the yearMap doesn't already contain this album.
      if (!yearMap.containsKey(song.getYear())) {
        // Then add it to the map with a value of 1.
        yearMap.put(song.getYear(), 1);
      }
      // If the yearMap does contain this artist.
      else {
        // Then increment the number it is linked to.
        yearMap.put(song.getYear(), yearMap.get(song.getYear()) + 1);
      }
    }

    // Now we should have 4 hashmaps with a list of all unique values and the number of their occurences.

    // Create an entry.
    Entry<String, Integer> maxArtist = null;
    // Get the entry set and for every key in the entry set.
    for (Map.Entry<String, Integer> entry : artistMap.entrySet()) {
      // If the max artist is null or the value is greater.
      if (maxArtist == null || entry.getValue().compareTo(maxArtist.getValue()) > 0) {
        // Change the maxArtist to this entry.
        maxArtist = entry;
      }
    }
    // Create an entry.
    Entry<String, Integer> maxAlbum = null;
    // Get the entry set and for every key in the entry set.
    for (Map.Entry<String, Integer> entry : albumMap.entrySet()) {
      // If the max album is null or the value is greater.
      if (maxAlbum == null || entry.getValue().compareTo(maxAlbum.getValue()) > 0) {
        // Change the maxAlbum to this entry.
        maxAlbum = entry;
      }
    }
    // Create an entry.
    Entry<String, Integer> maxGenre = null;
    // Get the entry set and for every key in the entry set.
    for (Map.Entry<String, Integer> entry : genreMap.entrySet()) {
      // If the max genre is null or the value is greater.
      if (maxGenre == null || entry.getValue().compareTo(maxGenre.getValue()) > 0) {
        // Change the maxGenre to this entry.
        maxGenre = entry;
      }
    }
    // Create an entry.
    Entry<String, Integer> maxYear = null;
    // Get the entry set and for every key in the entry set.
    for (Map.Entry<String, Integer> entry : yearMap.entrySet()) {
      // If the max year is null or the value is greater.
      if (maxYear == null || entry.getValue().compareTo(maxYear.getValue()) > 0) {
        // Change the maxYear to this entry.
        maxYear = entry;
      }
    }

    // Fill the results with the max values by getting their key values.
    results[0] = maxArtist.getKey();
    results[1] = maxAlbum.getKey();
    results[2] = maxGenre.getKey();
    results[3] = maxYear.getKey();

    // Return the string array.
    return results;
  }

  /**
   * Get the instance of the SongController singleton.
   * 
   * @return The songController object.
   */
  public static SongController getInstance() {
    if (songController == null) {
      songController = new SongController();
    }
    return songController;
  }
}
