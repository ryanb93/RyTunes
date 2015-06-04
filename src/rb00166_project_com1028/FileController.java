package rb00166_project_com1028;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

/**
 * This singleton class is in charge of reading, sorting, editing files on the users computer. 
 * 
 * @author Ryan Burke
 */
public class FileController {

  /** An instance of itself to return. */
  private static FileController fileController = null;
  /** A list of files found in a folder. */
  private List<File>            fileList;

  /**
   * The private constructor for the FileController which stops it being created outside of this class.
   */
  private FileController() {
    this.fileList = new ArrayList<File>();
  }

  /**
   * This method returns the fileList as an iterator as to protect its data.
   * 
   * @return The fileList as an iterator.
   */
  public Iterator<File> create_iterator() {
    return this.fileList.iterator();
  }

  /**
   * A private recursive method which loops though a folder. If it finds a folder then it calls itself again.
   * It it finds a file then it checks if it is an audio file, and if so it adds it to fileList.
   * 
   * @param path The folder to search.
   */
  private void looper(String path) {
    File folder = new File(path);
    if (folder.isDirectory()) {
      File[] files = folder.listFiles();
      for (int i = 0; i < files.length; i++) {
        // If the file is a directory.
        if (files[i].isDirectory()) {
          this.looper(files[i].getAbsolutePath());
        }
        // If the file is a file.
        else {

          try {
            AudioFile tmp = AudioFileIO.read(files[i]);
            if (tmp != null) {
              this.fileList.add(files[i]);
            }
          }
          // Catch non-audio files.
          catch (CannotReadException e1) {
            // Dont do anything.
          }
          // Catch any other exceptions thrown.
          catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    }
  }

  /**
   * This method modifies the ID3 data contained within an audio file. 
   * 
   * @param path - The path to the audio file.
   * @param columnName - The name of the tag to change.
   * @param data - The data to change the tag to.
   */
  public void modifyID3(String path, String columnName, String data) {

    try {
      //Get the tags from the audio file.
      File file = new File(path);
      AudioFile f = null;
      f = AudioFileIO.read(file);
      Tag tag = f.getTag();

      //Depending on the column selected change that value in the tag.
      if (columnName.equalsIgnoreCase("title")) {
        tag.setField(FieldKey.TITLE, data);
      }
      else if (columnName.equalsIgnoreCase("artist")) {
        tag.setField(FieldKey.ARTIST, data);
      }
      else if (columnName.equalsIgnoreCase("album")) {
        tag.setField(FieldKey.ALBUM, data);
      }
      else if (columnName.equalsIgnoreCase("year")) {
        tag.setField(FieldKey.YEAR, data);
      }
      else if (columnName.equalsIgnoreCase("genre")) {
        tag.setField(FieldKey.GENRE, data);
      }
      //Write the tag back.
      AudioFileIO.write(f);
    }
    catch (Exception e) {
      e.printStackTrace();
    }

  }

  /**
   * This method reads a folder using the recursive method looper.
   * 
   * @param folderPath The root folder to read from.
   */
  public void readFolder(String folderPath) {

    this.fileList.clear();
    this.looper(folderPath);

  }

  /**
   * This method sorts the folders into subfolders based on the order selected by the user.
   * 
   * @param folderPath The folder to sort.
   * @param order The way the subfolders are sorted.
   */
  public void sortFolders(String folderPath, String order) {

    // Get the list of songs as Song objects.
    Iterator<Song> songs = SongController.getInstance().create_iterator();

    // Create an empty list.
    List<String> choices = new ArrayList<String>();

    // Go through the songs.
    while (songs.hasNext()) {
      Song song = songs.next();
      String choice = null;
      //Depending on the selected method then get that tag.
      if (order.equalsIgnoreCase("artist")) {
        choice = song.getArtist();
      }
      else if (order.equalsIgnoreCase("album")) {
        choice = song.getAlbum();
      }
      else if (order.equalsIgnoreCase("genre")) {
        choice = song.getGenre();
      }
      else if (order.equalsIgnoreCase("year")) {
        choice = song.getYear();
      }
      //Create a new folder.
      String newFolder = (folderPath + File.separator + choice);
      //Create a new file using the path of the song.
      File musicFile = new File(song.getFilePath());
      // If it doesn't exist in the list then add it and create a folder.
      if (!choices.contains(choice)) {
        choices.add(choice);
        new File(newFolder).mkdirs();
      }
      
      File newFile = new File(newFolder, musicFile.getName());
      // Move file to new directory
      musicFile.renameTo(newFile);
    }
    // Refresh file list.
    this.readFolder(folderPath);
  }

  /**
   * Get the instance of the FileController singleton.
   * 
   * @return The fileController object.
   */
  public static FileController getInstance() {
    if (fileController == null) {
      fileController = new FileController();
    }
    return fileController;
  }
}
