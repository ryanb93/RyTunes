package rb00166_project_com1028;

import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

/**
 * This is the class containing the main method. It contains the code for the GUI using swing to generate the interface. It
 * essentially handles user input and control, using the other classes to perform the main part of logic behind the program.
 * 
 * @author Ryan Burke
 */
public class RyTunes {

  /** The instance of FileController. */
  private FileController    fileController        = FileController.getInstance();
  /** The instance of SongController. */
  private SongController    songController        = SongController.getInstance();

  /** The frame in which the user will see the application. */
  private JFrame            frame;
  /** The table which the user will see the ID3 data. */
  private JTable            table;
  /** The data model used by the table. */
  private DefaultTableModel model;
  /** The button which will sort the physical folder. */
  private JButton           btnSortFolder;
  /** The button which will calculate the top data. */
  private JButton           btnCalculateTopStats;
  /** The button which will share the top data. */
  private JButton           btnShare;
  /** The FileChooser which lets the user select a folder to load. */
  private JFileChooser      folderChooser;
  /** The progress bar which will indicate processing is occuring. */
  private JProgressBar      progressBar;

  /** The label which shows the importing message */
  private JLabel            lblImporting;
  /** The label which shows the importing message */
  private JLabel            lblFolderPath;
  /** The label which the result for the top artist will be loaded into. */
  private JLabel            artistResult;
  /** The label which the result for the top album will be loaded into. */
  private JLabel            albumResult;
  /** The label which the result for the top genre will be loaded into. */
  private JLabel            genreResult;
  /** The label which the result for the top year will be loaded into. */
  private JLabel            yearResult;

  /** The path selected by the user. */
  private String            folderPath;
  /** A boolean value which holds the state of the load button being pressed. */
  private boolean           loadButtonPressed     = false;
  /** A boolean value which holds the state of the get stats button being pressed. */
  private boolean           getStatsButtonPressed = false;

  /**
   * The RyTunes object is created. Calls an internal method to set up the GUI.
   */
  public RyTunes() {
    this.initialize();
  }

  /**
   * Initialise the contents of the frame.
   */
  @SuppressWarnings("serial")
  private void initialize() {

    // Create a new frame.
    this.frame = new JFrame();
    this.frame.setBounds(100, 100, 996, 588);
    this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.frame.getContentPane().setLayout(null);
    this.frame.setTitle("ryTunes 1.0");

    // The file chooser where the user selects a folder to use.
    this.folderChooser = new JFileChooser();
    // Make sure the user can only select folders.
    this.folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

    // Top Artist Label
    JLabel lblTopArtist = new JLabel("Top Artist");
    lblTopArtist.setFont(new Font("Lucida Grande", Font.BOLD, 13));
    lblTopArtist.setBounds(6, 137, 170, 16);
    this.frame.getContentPane().add(lblTopArtist);
    // Top Album Label
    JLabel lblTopAlbum = new JLabel("Top Album");
    lblTopAlbum.setFont(new Font("Lucida Grande", Font.BOLD, 13));
    lblTopAlbum.setBounds(6, 198, 170, 16);
    this.frame.getContentPane().add(lblTopAlbum);
    // Top Genre Label
    JLabel lblTopGenre = new JLabel("Top Genre");
    lblTopGenre.setFont(new Font("Lucida Grande", Font.BOLD, 13));
    lblTopGenre.setBounds(6, 263, 170, 16);
    this.frame.getContentPane().add(lblTopGenre);
    // Top Year Label
    JLabel lblTopYear = new JLabel("Top Year");
    lblTopYear.setFont(new Font("Lucida Grande", Font.BOLD, 13));
    lblTopYear.setBounds(6, 331, 170, 16);
    this.frame.getContentPane().add(lblTopYear);
    // The label which the result for the top artist will be loaded into.
    this.artistResult = new JLabel();
    this.artistResult.setBounds(6, 165, 170, 16);
    this.frame.getContentPane().add(this.artistResult);
    // The label which the result for the top album will be loaded into.
    this.albumResult = new JLabel();
    this.albumResult.setBounds(6, 235, 170, 16);
    this.frame.getContentPane().add(this.albumResult);
    // The label which the result for the top genre will be loaded into.
    this.genreResult = new JLabel();
    this.genreResult.setBounds(6, 291, 170, 16);
    this.frame.getContentPane().add(this.genreResult);
    // The label which the result for the top year will be loaded into.
    this.yearResult = new JLabel();
    this.yearResult.setBounds(6, 359, 170, 16);
    this.frame.getContentPane().add(this.yearResult);
    // The label which provides feedback about the folderPath selected.
    this.lblFolderPath = new JLabel("No Folder Loaded.");
    this.lblFolderPath.setBounds(188, 23, 786, 23);
    this.frame.getContentPane().add(this.lblFolderPath);

    /* Play button */
    final JButton btnPlay = new JButton("Play");
    btnPlay.setEnabled(false);
    btnPlay.setBounds(10, 527, 166, 29);
    btnPlay.addActionListener(new ActionListener() {

      /**
       * ActionListener for when the btnPlay is pressed by the user.
       * 
       * @param arg0
       *          The type of action that was performed.
       * 
       * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
       */
      @Override
      public void actionPerformed(ActionEvent arg0) {
        RyTunes.this.playSong();
      }
    });
    this.frame.getContentPane().add(btnPlay);

    /* Table setup */
    // The names of the columns.
    final String[] columnNames = { "ID", "Title", "Artist", "Album", "Year", "Genre", "Length" };
    // The data model used by the table. Using null data and the columnNames just set.
    this.model = new DefaultTableModel(null, columnNames);
    // Create a new JTable using the model just made.
    this.table = new JTable(RyTunes.this.model) {

      /**
       * Method called when a user double clicks on a cell in the table. It checks to see if the cell is editable and returns this
       * value.
       * 
       * @param rowIndex
       *          The index of the row selected.
       * @param colIndex
       *          The index of the column selected.
       * @return If that cell is editable or not.
       * 
       * @see javax.swing.JTable#isCellEditable(int, int)
       */
      @Override
      public boolean isCellEditable(int rowIndex, int colIndex) {
        // If the column pressed is the first or last then shouldnt be edited.
        // These are properties used by the system (ID) and the time of the song which is static.
        if (colIndex == 0 || colIndex == 6) {
          return false;
        }
        else {
          return true;
        }
      }
    };
    // Let the table handle sorting by column so I dont have to.
    this.table.setAutoCreateRowSorter(true);
    // Only allow a single selection of a song.
    this.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    // Add a new listener for when the table changes.
    this.table.getModel().addTableModelListener(new TableModelListener() {

      /**
       * When there has been a change in the table. This could be an update, deletion or insert. This override deals with just
       * updating the table as I don't want the user to be able to delete or insert extra items to the table.
       * 
       * @param e
       *          - The event which has occurred.
       * 
       * @see javax.swing.event.TableModelListener#tableChanged(javax.swing.event.TableModelEvent)
       */
      @Override
      public void tableChanged(TableModelEvent e) {
        // If there was an update.
        if (e.getType() == TableModelEvent.UPDATE) {
          // Get the row and column that was pressed which gives us a cell.
          int row = e.getFirstRow();
          int column = e.getColumn();
          // Get the name of the column pressed so we know which ID3 tag to change.
          String columnName = RyTunes.this.model.getColumnName(column);
          // Get the new value entered.
          String data = (String) RyTunes.this.model.getValueAt(row, column);
          // Get the ID of the changed song.
          String id = (String) RyTunes.this.model.getValueAt(row, 0);
          // Get the filePath of this song.
          String path = RyTunes.this.songController.getPath(id);
          // Tell the fileController to save the edit to the audio file.
          RyTunes.this.fileController.modifyID3(path, columnName, data);
        }
      }
    });
    // Add another listener to the table which listens for a selection of a row.
    this.table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

      /**
       * When a row has been selected enable the play button.
       * 
       * @param arg0
       *          The type of action.
       * 
       * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
       */
      @Override
      public void valueChanged(ListSelectionEvent arg0) {
        btnPlay.setEnabled(true);
      }
    });

    // Set the last and first columns have a max width as by default they are too big.
    this.table.getColumnModel().getColumn(0).setMaxWidth(40);
    this.table.getColumnModel().getColumn(6).setMaxWidth(40);
    // Disable reordering.
    this.table.getTableHeader().setReorderingAllowed(false);

    // Create a scroll pane to put the table inside.
    JScrollPane pane = new JScrollPane(this.table);
    pane.setBounds(188, 58, 786, 498);
    this.frame.getContentPane().add(pane);

    // Create the tweet button.
    this.btnShare = new JButton("Tweet Statistics");
    this.btnShare.setEnabled(false);
    this.btnShare.setBounds(10, 503, 166, 23);
    // Listener for when the button is pressed.
    this.btnShare.addActionListener(new ActionListener() {

      /**
       * When the button has been pressed. This method calls another method to send a tweet.
       * 
       * @param arg0
       *          The action performed.
       * 
       * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
       */
      @Override
      public void actionPerformed(ActionEvent arg0) {
        RyTunes.this.shareButtonPressed();
      }
    });
    this.frame.getContentPane().add(this.btnShare);

    /* Progress Bar */
    this.progressBar = new JProgressBar(0, 100);
    this.progressBar.setBounds(188, 23, 786, 20);
    this.progressBar.setValue(0);
    this.frame.getContentPane().add(this.progressBar);
    this.progressBar.setVisible(false);

    /* Importing music label */
    this.lblImporting = new JLabel("Importing Music Files...");
    this.lblImporting.setHorizontalAlignment(SwingConstants.CENTER);
    this.lblImporting.setBounds(188, 6, 786, 16);
    this.lblImporting.setVisible(false);
    this.frame.getContentPane().add(this.lblImporting);

    /* Sort button */
    this.btnSortFolder = new JButton("Sort Folder");
    this.btnSortFolder.setEnabled(false);
    this.btnSortFolder.setBounds(10, 54, 166, 29);
    // Listener for when the sort button is pressed.
    this.btnSortFolder.addActionListener(new ActionListener() {

      /**
       * When the sort button is pressed.
       * 
       * @param arg0
       *          The action performed.
       * 
       * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
       */
      @Override
      public void actionPerformed(ActionEvent arg0) {
        RyTunes.this.sortButtonPressed();
      }
    });
    this.frame.getContentPane().add(this.btnSortFolder);

    /* Calculate Top Stats button */
    this.btnCalculateTopStats = new JButton("Calculate Top Stats");
    this.btnCalculateTopStats.setEnabled(false);
    this.btnCalculateTopStats.setBounds(10, 84, 166, 29);
    this.frame.getContentPane().add(this.btnCalculateTopStats);
    this.btnCalculateTopStats.addActionListener(new ActionListener() {

      /**
       * When the calculate top stats button has been pressed. This gets the stats from the songcontroller and then sets the text of
       * the labels in the GUI.
       * 
       * @param arg0
       *          The action performed.
       * 
       * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
       */
      @Override
      public void actionPerformed(ActionEvent arg0) {
        RyTunes.this.topStatsButtonPressed();
      }
    });

    /* Load button */
    JButton btnLoadFolder = new JButton("Load Folder");
    btnLoadFolder.addActionListener(new ActionListener() {

      /**
       * When the load button is pressed. This method loads a folder selected by the user into memory and is the the main processing
       * button on the GUI.
       * 
       * @param arg0
       *          The action performed.
       * 
       * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
       */
      @Override
      public void actionPerformed(ActionEvent arg0) {
        RyTunes.this.loadButtonPressed();
      }
    });
    btnLoadFolder.setBounds(10, 23, 166, 23);
    this.frame.getContentPane().add(btnLoadFolder);
  }

  /**
   * When the load button is pressed. This method loads a folder selected by the user into memory and is the the main processing
   * button on the GUI.
   * 
   * @param arg0
   *          The action performed.
   * 
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
  public void loadButtonPressed() {
    // Show the dialog and get its value.
    int returnVal = this.folderChooser.showOpenDialog(this.folderChooser);
    // If the user pressed okay.
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      // Get the folder path.
      RyTunes.this.folderPath = this.folderChooser.getSelectedFile().toString();
      // If the folder path does not equal null.
      if (RyTunes.this.folderPath != null) {
        // Set various GUI components to visible.
        this.lblImporting.setVisible(true);
        this.lblFolderPath.setVisible(false);
        // Enable the progress bar.
        this.progressBar.setVisible(true);
        // We cant tell how many files are in the folder selected without processing them first
        // so the progress bar cant give exact progress, but it can show that processing is happening.
        this.progressBar.setIndeterminate(true);
        // A runnable to do some processing in seperate from the UI thread.
        Runnable run = new Runnable() {

          /**
           * Inner Inner method which is a Runnable. This enables me to perform operations in a separate thread without freezing the
           * thread containing the GUI. This lets the progress bar work. This method makes the fileController read the folder
           * selected by the user and reloads the table. Innerception.
           * 
           * @author Ryan Burke
           */
          @Override
          public void run() {
            try {
              // Get the fileController to read the folder.
              RyTunes.this.fileController.readFolder(RyTunes.this.folderPath);
              // Reload the table.
              RyTunes.this.reloadTable();
            }
            catch (Exception e) {
              e.printStackTrace();
            }
            finally {
              // Make changes to the GUI when complete, have to do in here or they will change before this thread has finished and
              // use invalid data.
              RyTunes.this.progressBar.setIndeterminate(false);
              RyTunes.this.progressBar.setVisible(false);
              RyTunes.this.btnSortFolder.setEnabled(true);
              RyTunes.this.btnCalculateTopStats.setEnabled(true);
              RyTunes.this.lblImporting.setVisible(false);
              RyTunes.this.lblFolderPath.setText("Loaded folder: " + RyTunes.this.folderPath + " into system memory.");
              RyTunes.this.lblFolderPath.setVisible(true);
            }
          }
        };
        // Create a seperate thread so the progress bar doesnt lag.
        Thread newThread = new Thread(run);
        newThread.start();
        this.loadButtonPressed = true;
      }
    }
  }

  /**
   * 
   * Launches the currently selected song in the JTable in the users default media player.
   * 
   */
  public void playSong() {
    if (this.loadButtonPressed) {
      // Get the ID of the row selected.
      String id = (String) RyTunes.this.model.getValueAt(RyTunes.this.table.getSelectedRow(), 0);
      // Get the path of that song as it is not stored in the table.
      String path = RyTunes.this.songController.getPath(id);
      try {
        // Check to see if Desktop is supported on this version of Java.
        if (Desktop.isDesktopSupported()) {
          // Open the file using the default program.
          if (path != null) {
            Desktop.getDesktop().open(new File(path));
          }
        }
        else {
          JOptionPane.showMessageDialog(RyTunes.this.frame, "Sorry your Java does not support opening files.");
        }
      }
      catch (Exception e) {
        JOptionPane.showMessageDialog(RyTunes.this.frame, "This file has moved, please load the folder again.");
      }
    }
  }

  /**
   * This method reloads the table shown to the user. It removes all rows in the table and then adds each song a row at a time.
   */
  private void reloadTable() {
    // Get the number of rows in the data model.
    final int rows = this.model.getRowCount();
    // For each of those rows.
    for (int i = 0; i < rows; i++) {
      // Remove the first row. When it is removed the row above it moves down to fill its place.
      this.model.removeRow(0);
    }
    try {
      // Convert the files read.
      RyTunes.this.songController.convertFiles(this.fileController.create_iterator());
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    // Get the song list.
    Iterator<Song> data = this.songController.create_iterator();
    // Loop through the data.
    while (data.hasNext()) {
      // Get the song.
      Song song = data.next();
      // Create a string array using the data from this song.
      String[] newRow = { "" + song.getId(), song.getTitle(), song.getArtist(), song.getAlbum(), song.getYear(), song.getGenre(),
          "" + song.getLength() };
      // Add the data to the data model.
      this.model.addRow(newRow);
    }
  }

  /**
   * When the button has been pressed. This method creates a customised link that will create a tweet. This method working is
   * dependent on Twitter.com keeping this link working.
   * 
   */
  public void shareButtonPressed() {
    if (this.loadButtonPressed && this.getStatsButtonPressed) {
      // Create a new stringbuilder so that it is mutable.
      StringBuilder htmlFilePath = new StringBuilder();
      // Always need this at the start.
      String url = "https://twitter.com/intent/tweet?text=";
      // If the top artist is not empty.
      if (!this.artistResult.getText().equals("")) {
        // Add the top artist to the string.
        htmlFilePath.append("Top Artist:" + this.artistResult.getText());
      }
      // If the top album is not empty.
      if (!this.albumResult.getText().equals("")) {
        // Add the top album to the string.
        htmlFilePath.append(" Top Album:" + this.albumResult.getText());
      }
      // If the top genre is not empty.
      if (!this.genreResult.getText().equals("")) {
        // Add the top genre to the string.
        htmlFilePath.append(" Top Genre:" + this.genreResult.getText());
      }
      // If the top year is not empty.
      if (!this.yearResult.getText().equals("")) {
        // Add the top year to the string.
        htmlFilePath.append(" Top Year:" + this.yearResult.getText());
      }
      // At the end of the string add the hashtag.
      htmlFilePath.append("&hashtags=ryTunes");
      // Convert the stringbuilder to a string.
      String tmp = htmlFilePath.toString();
      // Going to be parsed as a URL so replace any odd values as encoded.
      try {
        // URLEncoder replaces spaces with "+" which mess up the link, replace them with hex value for space.
        tmp = java.net.URLEncoder.encode(url, "UTF-8").replace("+", "%20");
        // If the desktop is supported.
        if (Desktop.isDesktopSupported()) {
          // Open the link in the default browser.
          java.awt.Desktop.getDesktop().browse(java.net.URI.create(url + tmp));
        }
        else {
          JOptionPane.showMessageDialog(RyTunes.this.frame, "Sorry your Java does not support opening URL's.");
        }
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * When the sort button is pressed. This will sort the original unordered folder into a folder containing sub-folders named from
   * the ID3 tags and it will move the audio files into these new folders.
   */
  public void sortButtonPressed() {
    if (this.loadButtonPressed) {
      String chosenValue = null;
      // Present the user with a pop up box asking them how they would like it sorted.
      String[] options = new String[] { "Artist", "Album", "Genre", "Year" };
      chosenValue = (String) JOptionPane.showInputDialog(RyTunes.this.frame, // Parent
          "Please select how you would like your new folders to be sorted.", // Message text
          "Sort Folders", // Message title
          JOptionPane.PLAIN_MESSAGE, // Message type
          null, options, // Options
          options[0]); // Default selection
      // If the user didnt press cancel.
      if (chosenValue != null) {
        // Get the file controller to sort the folders.
        this.fileController.sortFolders(RyTunes.this.folderPath, chosenValue);
        try {
          // Refresh the table.
          RyTunes.this.reloadTable();
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * This method gets the top stats from the SongController and displays it to the user by setting the labels. This must be called
   * after the loadFolder button has been pressed.
   */
  public void topStatsButtonPressed() {
    if (this.loadButtonPressed) {
      // Fill a string array with the results.
      String[] topStats = this.songController.getTopStats();
      // Set the text of the labels to show the data.
      this.artistResult.setText(topStats[0]);
      this.albumResult.setText(topStats[1]);
      this.genreResult.setText(topStats[2]);
      this.yearResult.setText(topStats[3]);
      // Now the user can share these stats.
      this.btnShare.setEnabled(true);
      this.getStatsButtonPressed = true;
    }
  }

  /**
   * The main method for the application. It launches the GUI in a separate thread.
   * 
   * @param args
   *          The arguments to the application.
   */
  public static void main(String[] args) {

    // When there is a space in the eventQueue.
    EventQueue.invokeLater(new Runnable() {

      // Run this method.
      @Override
      public void run() {
        try {
          // Create a new instance of RyTunes which is the window.
          RyTunes window = new RyTunes();
          // Set the frame to be visible.
          window.frame.setVisible(true);
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

}
