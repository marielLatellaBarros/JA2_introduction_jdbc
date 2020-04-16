package be.pxl.ja2.jdbc;

import be.pxl.ja2.jdbc.model.MusicDatasource;

/** Demo5:
 * Check if connection to database is open
 * Get the totals for albums, songs and artists (SELECT COUNT)
 * Close the connection
 */
public class CountingRows {
	public static void main(String[] args) {
		MusicDatasource datasource = new MusicDatasource();
		if(!datasource.open()) {
			System.out.println("Can't open datasource");
			return;
		}

		System.out.println("albums = " + datasource.getCount(MusicDatasource.TABLE_ALBUMS));
		System.out.println("songs = " + datasource.getCount(MusicDatasource.TABLE_SONGS));
		System.out.println("artists = " + datasource.getCount(MusicDatasource.TABLE_ARTISTS));
		datasource.close();
	}
}
