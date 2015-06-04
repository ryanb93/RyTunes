package rb00166_project_com1028;

/**
 * A class which stores data about a song.
 * 
 * @author Ryan Burke
 */
public class Song {

  /** The title of the song. */
  private String title    = null;
  /** The artist of the song. */
  private String artist   = null;
  /** The album of the song. */
  private String album    = null;
  /** The year of the song. */
  private String year     = null;
  /** The genre of the song. */
  private String genre    = null;
  /** The file path of the song. */
  private String filePath = null;
  /** The length of the song. */
  private int    length   = 0;
  /** The id of the song. */
  private int    id       = 0;

  
  /**
   * Parametrised constructor of the Song object which creates a new song.
   * 
   * 
   * @param title The title of the song.
   * @param artist The artist of the song.
   * @param album The album of the song.
   * @param year The year of the song.
   * @param genre The genre of the song.
   * @param length The length of the song in seconds.
   * @param filePath The file name of the song.
   * @param id The unique ID of the song.
   */
  public Song(String title, String artist, String album, String year, String genre, int length, String filePath, int id) {

    //If the title is not null then set the title.
    if (title != null) {
      this.title = title;
    }
    //Else throw an error.
    else {
      throw new IllegalArgumentException("Invalid title");
    }
    //If the artist is not null then set the artist.
    if (artist != null) {
      this.artist = artist;
    }
    else {
      throw new IllegalArgumentException("Invalid artist");
    }
    //If the album is not null then set the album.
    if (album != null) {
      this.album = album;
    }
    else {
      throw new IllegalArgumentException("Invalid album");
    }
    //If the year is not null then set the year.
    if (year != null) {
      this.year = year;
    }
    else {
      throw new IllegalArgumentException("Invalid year");
    }
    //If the genre is not null then set the genre.
    if (genre != null) {
      this.genre = genre;
    }
    else {
      throw new IllegalArgumentException("Invalid genre");
    }
    //If the filePath is not null then set the filePath.
    if (filePath != null) {
      this.filePath = filePath;
    }
    else {
      throw new IllegalArgumentException("Invalid fileName");
    }
    //If the length is not negative or 0 then set the length.
    if (length > 0) {
      this.length = length;
    }
    else {
      throw new IllegalArgumentException("Invalid length");
    }
    //If the id is not negative or 0 then set the id.
    if (id > 0) {
      this.id = id;
    }
    else {
      throw new IllegalArgumentException("Invalid id");
    }
  }

  /**
   * @return the album
   */
  public String getAlbum() {
    return this.album;
  }

  /**
   * @return the artist
   */
  public String getArtist() {
    return this.artist;
  }

  /**
   * @return the filePath
   */
  public String getFilePath() {
    return this.filePath;
  }

  /**
   * @return the genre
   */
  public String getGenre() {
    return this.genre;
  }

  /**
   * @return the id
   */
  public int getId() {
    return this.id;
  }

  /**
   * @return the length
   */
  public int getLength() {
    return this.length;
  }

  /**
   * @return the title
   */
  public String getTitle() {
    return this.title;
  }

  /**
   * @return the year
   */
  public String getYear() {
    return this.year;
  }

}
