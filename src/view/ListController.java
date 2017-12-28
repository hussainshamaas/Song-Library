package view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import app.Song;
/**
 * @author Shamaas Hussain
 */
public class ListController {
	 /* Controller variables + data structures*/
	   @FXML         
	   ListView<Song> listView;                
	   @FXML 
	   private Button add, edit, delete;
	   @FXML
	   private TextField TitleText, ArtistText, AlbumText, YearText;
	   @FXML
	   private Label TitleLabel, artistLabel, albumLabel, yearLabel;
	   
	   private ObservableList<Song> songs = FXCollections.observableArrayList();             
	   private ArrayList<Song> songList;
	   private Scene scene;
	   int arrayIndex;
		
		public ListController(String fileName) throws IOException {
			FXMLLoader loader = new FXMLLoader( getClass().getResource( fileName ) );
			loader.setController( this );
			Parent root = ( Parent ) loader.load();
		      scene = new Scene(root);
		      initialize();
		      populateFromFile();
		}

		private void populateFromFile() {
			File file;
			try {
				file = new File("songs.ser");
				if (!file.exists()) {
					return;
				}
			} 
			catch(Exception e) {
				return;
			}
			try {
				List<Song> songList = (List<Song>) new ObjectInputStream(new FileInputStream("songs.ser")).readObject();
				songs = FXCollections.observableArrayList(songList);
				listView.setItems(songs);
				

				//listView.getItems().addAll(songs);

				
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				return;
			}
		}
		
		
		
	   /**
		 * Method for initializing data structures
		 */
	
	   public void initialize() {
		   //add listener here to copy inputs to the song details
			songList = new ArrayList<Song>();
			ArrayList<String> song = new ArrayList<>();
			
			listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Song>() {
			    @Override
			    public void changed(ObservableValue<? extends Song> observable, Song oldValue, Song newValue) {
			    	arrayIndex = listView.getSelectionModel().getSelectedIndex();
			    	
			        int index = listView.getSelectionModel().getSelectedIndex();
			        display(index);
			    }
			});
		
	   
			add.setOnAction(new EventHandler<ActionEvent>() {
				@Override
			    public void handle(ActionEvent e) {
			    	handleAddButton();
				   }
			});
		
			delete.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				    public void handle(ActionEvent e) {
				    	handleDeleteButton(listView.getSelectionModel().getSelectedItem());
				    }
			});
			edit.setOnAction(new EventHandler<ActionEvent>() {
				@Override
			    public void handle(ActionEvent e) {
					if (edit.getText().equals("Edit")) {
						edit.setText("Click to Confirm Edit");
						
					} else {
						edit.setText("Edit");
						handleEditButton(listView.getSelectionModel().getSelectedIndex());
					}
			    	
			    }
			});
	  
	   }
	   
		/**
		 * Method for handling song deletion
		 */
	   
	   private void handleDeleteButton(Song song ) {
			int c = songs.indexOf(song);
		    songs.remove(song);
		    listView.setItems(songs);
		    listView.scrollTo(c);	
			listView.getSelectionModel().select(c);
			listView.getFocusModel().focus(c);	
		    if(listView.getItems().isEmpty()) {
		    	TitleText.setText("");
				ArtistText.setText("");
				AlbumText.setText("");
				YearText.setText("");
		    }
	   }
	   
		/**
		 * Method for add song deletion
		 */
	   
	   private void handleAddButton() {
			
			String title = TitleText.getText() ;
			String artist = ArtistText.getText();
			String album = AlbumText.getText();
			String year = YearText.getText();
			//Song newSong = new Song(title, artist,album,year);
			String confirmMessage = "Song title: " + title + "\nArtist: " + artist + "\nAlbum: " + album + "\nYear: " + year;
	
			if (title == null || title.equals("") || artist == null || artist.equals("")) {
				//Error prompt
				emptyValueError();
				return;
			} else {
				Song song = new Song(title,artist);
				if (album != null && !album.equals("")) {
					song.setAlbum(album);
				}
				if (year != null && !year.equals("")) {
					
					song.setYear(year);
				}
				if (songCompareTo(song) ) {
					//Song and artist already exists. 
					errorMessage("Can't do. Song and artist already exist. Please enter a new song to add.");
					TitleText.setText("");
					ArtistText.setText("");
					AlbumText.setText("");
					YearText.setText("");
					return;
				} else {
					if (confirmChanges(confirmMessage)) {
						
						//insert alphabetically
						songs.add(song);
						FXCollections.sort(songs);
						this.listView.setItems(songs);
						int c = songs.indexOf(song);
						listView.scrollTo(c);	
						listView.getSelectionModel().select(c);
						listView.getFocusModel().focus(c);	
					}
				}
			}
		

			
	   }
	   
	   /**
		 * Method for edit song deletion
		 */
	   
	   private void handleEditButton(int i) {
		   
		    Song song = this.listView.getItems().get(i);
			String beforeTitle = song.getName();
			String beforeArtist = song.getArtist();
			String songTitle = TitleText.getText();
			String artist = ArtistText.getText();
			String album = AlbumText.getText();
			String year = YearText.getText();
			//error condition checking if album and year are empty.
			if (year == null) {
				year = "";
			}
			if (album == null) {
				album = "";
			}
			
			song.setYear(year);
			song.setAlbum(album);
			Song temp = new Song(songTitle, artist);
			
			String confirmMessage = "Song title: " + songTitle + "\nArtist: " + artist + "\nAlbum: " + album + "\nYear: " + year;
			if (songTitle.equals("") ||songTitle == null || artist.equals("")||artist == null ) {
				//prompt a Null value error 
				emptyValueError();
				return;
			} else if (!beforeTitle.equals(songTitle)) {
				//The song name is changed
				if (!beforeArtist.equals(artist) && songCompareTo(temp)) {
						if(songCompareTo(temp)) {
						//check if it exists, prompt error
						errorMessage("Song Already Exists");
						return;
					} else {
						//Song doesn't exist, add to list
						if (confirmChanges(confirmMessage)) {
							//insert alphabetically
							songs.add(song);
							FXCollections.sort(songs);
							listView.setItems(songs);
							int c = songs.indexOf(song);
							listView.scrollTo(c);	
							listView.getSelectionModel().select(c);
							listView.getFocusModel().focus(c);			
						}
					}
				} else {
					
					if (songCompareTo(temp)) {
						//prompt error
						errorMessage("Song Already Exists");
						return;
					} else {
						//Song doesn't exist, add to list
						if (confirmChanges(confirmMessage)) {
							song.setName(songTitle);
							listView.getItems().remove(i);
							//songList.remove(i);
							//sort alphabetically
							//insert alphabetically
							songs.add(song);
							FXCollections.sort(songs);
							listView.setItems(songs);
							int c = songs.indexOf(song);
							listView.scrollTo(c);	
							listView.getSelectionModel().select(c);
							listView.getFocusModel().focus(c);	
						}
					}
				}
	   } else if (!beforeArtist.equals(artist)) {
				//case where artist name changed only
				if (songCompareTo(temp)) {
					//prompt existence error
					errorMessage("Song Already Exists");
					return;
				} else {
					//Song doesn't exist, add to list
					if (confirmChanges(confirmMessage)) {
						//insert alphabetically
						song.setArtist(artist);
						//songs.add(song);
						FXCollections.sort(songs);
						listView.setItems(songs);
						int c = songs.indexOf(song);
						listView.scrollTo(c);	
						listView.getSelectionModel().select(c);
						listView.getFocusModel().focus(c);	
					}
				}
			} else {
				//Neither song title and artist did not change
				
				this.listView.getItems().set(i, song);
				
			}
			TitleText.setText("");
			ArtistText.setText("");
			AlbumText.setText("");
			YearText.setText("");
			display(arrayIndex);
	   }
	   /**
		 * Checking if a current song already exists
		 */
	   
		public boolean songCompareTo(Song currSong) {
			for (Song s1 : this.listView.getItems()) {
				if (s1.getName().equalsIgnoreCase(currSong.getName()) && s1.getArtist().equalsIgnoreCase(currSong.getArtist())) {
					return true;
				}
			}
			return false;
		}
		
		/**
		 * Method for handling error messages
		 */
		
	   private void errorMessage(String error) {
			Alert errormessage = new Alert(AlertType.ERROR);
			errormessage.setTitle("Error");
			errormessage.setContentText(error);
			errormessage.showAndWait();
			

	   }

	   
		/**
		 * fill in the display so user can see
		 */
	
		public void display(int index) {
			if (index < 0) {
				TitleLabel.setText("");
				artistLabel.setText("");
				albumLabel.setText("");
				yearLabel.setText("");
				return;
			}
			Song song = songs.get(index);
			TitleLabel.setText(song.getName());
			TitleText.setText(song.getName());
			artistLabel.setText(song.getArtist());
			ArtistText.setText(song.getArtist());
			if (song.getAlbum() != null) {
				albumLabel.setText(song.getAlbum());
				AlbumText.setText(song.getAlbum());
			} else {
				albumLabel.setText("");
				AlbumText.setText("");
			}
			if (song.getYear() != null) {
				yearLabel.setText(song.getYear());
				YearText.setText(song.getYear());
			} else {
				yearLabel.setText("");
				YearText.setText("");
			}
		}
		
		/**
		 * Confirm changes before applying
		 */
		private boolean confirmChanges(String confirmDetails) {
			Alert message = new Alert(AlertType.CONFIRMATION);
			message.setTitle("Confirm Changes");
			message.setHeaderText("Are you sure you want to apply these changes?");
			message.setContentText(confirmDetails);
			Optional<ButtonType> result = message.showAndWait();
			if (result.get() == ButtonType.OK) {
				return true;
			} else {
				//anything else, nothing happens
				return false;
			}
		}
		
		private void emptyValueError() {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setContentText("Need both title + artist name");
			alert.showAndWait();
		}
		
		
		public void saveItemsToFile() throws FileNotFoundException, IOException {
			ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(new File("songs.ser")));
			stream.writeObject(new ArrayList<>(this.listView.getItems()));
			//stream.writeObject(songList);
			//stream.writeObject(songList);
			stream.close();
		}
		
		public Scene getScene() {
			return scene;
		}


	}

