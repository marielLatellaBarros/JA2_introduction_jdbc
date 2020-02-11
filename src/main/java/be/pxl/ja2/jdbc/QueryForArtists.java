package be.pxl.ja2.jdbc;

import be.pxl.ja2.jdbc.model.Artist;
import be.pxl.ja2.jdbc.model.MusicDatasource;

import java.util.List;

public class QueryForArtists {

	public static void main(String[] args) {
		MusicDatasource datasource = new MusicDatasource();
		if (!datasource.open()) {
			System.out.println("Can't open datasource");
			return;
		}

		List<Artist> artists = datasource.queryArtists();
		if (artists == null || artists.isEmpty()) {
			System.out.println("No artists!");
			return;
		}

		for (Artist artist : artists) {
			System.out.println("ID = " + artist.getId() + ", Name = " + artist.getName());
		}

		datasource.close();
	}
}
