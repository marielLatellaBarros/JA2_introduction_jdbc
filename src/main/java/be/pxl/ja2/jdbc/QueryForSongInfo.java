package be.pxl.ja2.jdbc;

import be.pxl.ja2.jdbc.model.MusicDatasource;
import be.pxl.ja2.jdbc.model.SongArtist;

import java.util.List;
import java.util.Scanner;

public class QueryForSongInfo {

	public static void main(String[] args) {
		MusicDatasource datasource = new MusicDatasource();
		if (!datasource.open()) {
			System.out.println("Can't open datasource");
			return;
		}

		Scanner input = new Scanner(System.in);
		System.out.println("Enter a title: ");
		String title = input.nextLine();
		// Go Your Own Way
		// Go Your Own Way' or 1=1 or '

		List<SongArtist> songArtists = datasource.querySongInfo(title);
		if (songArtists == null || songArtists.isEmpty()) {
			System.out.println("Couldn't find the artist for the song");
			return;
		}

		for (SongArtist artist : songArtists) {
			System.out.println("Artist name = " + artist.getArtistName() +
					" Album name = " + artist.getAlbumName() +
					" Track = " + artist.getTrack());
		}


	/*	int count = datasource.getCount(Datasource.TABLE_SONGS);
		System.out.println("Number of songs is: " + count); */



		datasource.close();

		// SELECT name, album, track FROM artist_list WHERE title = "Go Your Own Way" or 1=1 or ""

		// SELECT name, album, track FROM artist_list WHERE title = "Go Your Own Way or 1=1 or ""

	}
}


















