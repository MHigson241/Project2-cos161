package searchengine;

public class Song {
	public String name;
	public String tag;
	public String artist;
	public String year;
	public String views;
	public String lyrics;
	
	public Song(String name, String tag, String artist, String year, String views, String lyrics) {
		this.name = name;
		this.tag = tag;
		this.artist = artist;
		this.year = year;
		this.views = views;
		this.lyrics = lyrics;
	}
	//getters
	public String getName() {
		return name;
	}
	public String getTag() {
		return tag;
	}
	public String getArtist() {
		return artist;
	}
	public String getYear() {
		return year;
	}
	public String getViews() {
		return views;
	}
	public String getLyrics() {
		return lyrics;
	}


}
