package app;

import java.io.Serializable;

/**
 * @author Shamaas Hussain
 *
 */
public class Song implements Comparable<Song>, Serializable {
	String name;
	String artist;
	String album;
	String year;
	public Song(String name, String artist){
		this.name=name;
		this.artist=artist;
		this.album="";
		this.year="";
	}
	
	public Song(String name, String artist, String album, String year){ //constructor
		this.name = name;
		this.artist = artist;
		this.album = album;
		this.year = year;
	}
	@Override
	public String toString()
	{
		return name + ", " + artist;
	}
	
	@Override
	public int compareTo(Song song)
	{	
		if (name.toLowerCase().compareTo(song.getName().toLowerCase())==0 ) {
			return artist.toLowerCase().compareTo(song.getArtist().toLowerCase());
		}
		return name.toLowerCase().compareTo(song.getName().toLowerCase());		
	}

	
	
	public String getName(){
		return name;
	}
	
	public String getArtist(){
		return artist;
	}
	
	public String getYear(){
		return year;
	}
	
	public String getAlbum(){
		return album;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setArtist(String artist) {
		this.artist = artist;
	}
	
	public void setAlbum(String album) {
		this.album = album;
	}
	
	public void setYear(String year) {
		this.year = year;
	}
}
