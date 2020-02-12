package be.pxl.ja2.jdbc.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @Copyright timbuchalka on 9/12/16
 */
public class MusicDatasource {

	private static final String DB_NAME = "musicdb";

	private static final String CONNECTION_STRING = "jdbc:mysql://localhost:3306/" + DB_NAME + "?useSSL=false";
	private static final String USER_NAME = "user";
	private static final String USER_PASSWORD = "password";

	public static final String TABLE_ALBUMS = "albums";
	private static final String COLUMN_ALBUM_ID = "_id";
	private static final String COLUMN_ALBUM_NAME = "name";
	private static final String COLUMN_ALBUM_ARTIST = "artist";

	public static final String TABLE_ARTISTS = "artists";
	private static final String COLUMN_ARTIST_ID = "_id";
	private static final String COLUMN_ARTIST_NAME = "name";

	public static final String TABLE_SONGS = "songs";
	private static final String COLUMN_SONG_TRACK = "track";
	private static final String COLUMN_SONG_TITLE = "title";
	private static final String COLUMN_SONG_ALBUM = "album";

	public static final int ORDER_BY_NONE = 1;
	public static final int ORDER_BY_ASC = 2;
	public static final int ORDER_BY_DESC = 3;

	public static final String QUERY_ALBUMS_BY_ARTIST_START =
			"SELECT a.name FROM albums a INNER JOIN artists ar ON a.artist = ar._id WHERE ar.name = '";

	public static final String QUERY_ALBUMS_BY_ARTIST_SORT =
			" ORDER BY a.name ";

	public static final String QUERY_SONG_INFO = "SELECT ar.name, al.name, s.track, s.title FROM albums al\n" +
			"  INNER JOIN artists ar ON ar._id = al.artist\n" +
			"  INNER JOIN songs s ON al._id = s.album\n" +
			"WHERE s.title = '";

	public static final String INSERT_ARTIST = "INSERT INTO " + TABLE_ARTISTS +
			'(' + COLUMN_ARTIST_NAME + ") VALUES(?)";
	public static final String INSERT_ALBUMS = "INSERT INTO " + TABLE_ALBUMS +
			'(' + COLUMN_ALBUM_NAME + ", " + COLUMN_ALBUM_ARTIST + ") VALUES(?, ?)";

	public static final String INSERT_SONGS = "INSERT INTO " + TABLE_SONGS +
			'(' + COLUMN_SONG_TRACK + ", " + COLUMN_SONG_TITLE + ", " + COLUMN_SONG_ALBUM +
			") VALUES(?, ?, ?)";

	public static final String QUERY_ARTIST = "SELECT " + COLUMN_ARTIST_ID + " FROM " +
			TABLE_ARTISTS + " WHERE " + COLUMN_ARTIST_NAME + " = ?";

	public static final String QUERY_ALBUM = "SELECT " + COLUMN_ALBUM_ID + " FROM " +
			TABLE_ALBUMS + " WHERE " + COLUMN_ALBUM_NAME + " = ?";

	private Connection conn;
	//private PreparedStatement querySongInfo;
	private PreparedStatement insertIntoArtists;
	private PreparedStatement insertIntoAlbums;
	private PreparedStatement insertIntoSongs;

	private PreparedStatement queryArtist;
	private PreparedStatement queryAlbum;

	public boolean open() {
		try {
			conn = DriverManager.getConnection(CONNECTION_STRING, USER_NAME, USER_PASSWORD);
			//querySongInfo = conn.prepareStatement(QUERY_SONG_INFO);
			insertIntoArtists = conn.prepareStatement(INSERT_ARTIST, Statement.RETURN_GENERATED_KEYS);
			insertIntoAlbums = conn.prepareStatement(INSERT_ALBUMS, Statement.RETURN_GENERATED_KEYS);
			insertIntoSongs = conn.prepareStatement(INSERT_SONGS);
			queryArtist = conn.prepareStatement(QUERY_ARTIST);
			queryAlbum = conn.prepareStatement(QUERY_ALBUM);
			return true;
		} catch (SQLException e) {
			System.out.println("Couldn't connect to database: " + e.getMessage());
			return false;
		}
	}

	public void close() {
		try {
			//	if (querySongInfo != null) {
			//		querySongInfo.close();
			//	}
			if (insertIntoArtists != null) {
				insertIntoArtists.close();
			}

			if (insertIntoAlbums != null) {
				insertIntoAlbums.close();
			}

			if (insertIntoSongs != null) {
				insertIntoSongs.close();
			}

			if (queryArtist != null) {
				queryArtist.close();
			}

			if (queryAlbum != null) {
				queryAlbum.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			System.out.println("Couldn't close connection: " + e.getMessage());
		}
	}

	public List<Artist> queryArtists() {

		try (Statement statement = conn.createStatement();
		     ResultSet results = statement.executeQuery("SELECT * FROM " + TABLE_ARTISTS)) {

			List<Artist> artists = new ArrayList<>();
			while (results.next()) {
				Artist artist = new Artist();
				artist.setId(results.getInt(COLUMN_ARTIST_ID));
				artist.setName(results.getString(COLUMN_ARTIST_NAME));
				artists.add(artist);
			}

			return artists;

		} catch (SQLException e) {
			System.out.println("Query failed: " + e.getMessage());
			return null;
		}
	}

	public int getCount(String table) {
		String sql = "SELECT COUNT(*) AS count FROM " + table;
		try (Statement statement = conn.createStatement();
		     ResultSet results = statement.executeQuery(sql)) {
			if (results.next()) {
				int count = results.getInt("count");
				return count;
			}
			return -1;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Query failed: " + e.getMessage());
			return -1;
		}
	}

	public List<String> queryAlbumsForArtist(String artistName, int sortOrder) {

		StringBuilder sb = new StringBuilder(QUERY_ALBUMS_BY_ARTIST_START);
		sb.append(artistName);
		sb.append("'");

		if (sortOrder != ORDER_BY_NONE) {
			sb.append(QUERY_ALBUMS_BY_ARTIST_SORT);
			if (sortOrder == ORDER_BY_DESC) {
				sb.append("DESC");
			} else {
				sb.append("ASC");
			}
		}

		System.out.println("SQL statement = " + sb.toString());

		try (Statement statement = conn.createStatement();
		     ResultSet results = statement.executeQuery(sb.toString())) {

			List<String> albums = new ArrayList<>();
			while (results.next()) {
				albums.add(results.getString(1));
			}

			return albums;

		} catch (SQLException e) {
			System.out.println("Query failed: " + e.getMessage());
			return null;
		}
	}

	public List<SongArtist> querySongInfo(String title) {

		StringBuilder sb = new StringBuilder(QUERY_SONG_INFO);
		sb.append(title);
		sb.append("'");
		try (Statement statement = conn.createStatement();
		     ResultSet results = statement.executeQuery(sb.toString())) {

			List<SongArtist> songArtists = new ArrayList<>();
			while (results.next()) {
				SongArtist songArtist = new SongArtist();
				songArtist.setArtistName(results.getString(1));
				songArtist.setAlbumName(results.getString(2));
				songArtist.setTrack(results.getInt(3));
				songArtists.add(songArtist);
			}

			return songArtists;

		} catch (SQLException e) {
			System.out.println("Query failed: " + e.getMessage());
			return null;
		}
	}

	private int insertArtist(String name) throws SQLException {

		queryArtist.setString(1, name);
		ResultSet results = queryArtist.executeQuery();
		if (results.next()) {
			return results.getInt(1);
		} else {
			// Insert the artist
			insertIntoArtists.setString(1, name);
			int affectedRows = insertIntoArtists.executeUpdate();

			if (affectedRows != 1) {
				throw new SQLException("Couldn't insert artist!");
			}

			ResultSet generatedKeys = insertIntoArtists.getGeneratedKeys();
			if (generatedKeys.next()) {
				return generatedKeys.getInt(1);
			} else {
				throw new SQLException("Couldn't get _id for artist");
			}
		}
	}

	private int insertAlbum(String name, int artistId) throws SQLException {

		queryAlbum.setString(1, name);
		ResultSet results = queryAlbum.executeQuery();
		if (results.next()) {
			return results.getInt(1);
		} else {
			// Insert the album
			insertIntoAlbums.setString(1, name);
			insertIntoAlbums.setInt(2, artistId);
			int affectedRows = insertIntoAlbums.executeUpdate();

			if (affectedRows != 1) {
				throw new SQLException("Couldn't insert album!");
			}

			ResultSet generatedKeys = insertIntoAlbums.getGeneratedKeys();
			if (generatedKeys.next()) {
				return generatedKeys.getInt(1);
			} else {
				throw new SQLException("Couldn't get _id for album");
			}
		}
	}

	public void insertSong(String title, String artist, String album, int track) {

		try {
			conn.setAutoCommit(false);

			int artistId = insertArtist(artist);
			int albumId = insertAlbum(album, artistId);
			insertIntoSongs.setInt(1, track);
			insertIntoSongs.setString(2, title);
			insertIntoSongs.setInt(3, albumId);
			int affectedRows = insertIntoSongs.executeUpdate();
			if (affectedRows == 1) {
				conn.commit();
			} else {
				throw new SQLException("The song insert failed");
			}

		} catch (Exception e) {
			System.out.println("Insert song exception: " + e.getMessage());
			try {
				System.out.println("Performing rollback");
				conn.rollback();
			} catch (SQLException e2) {
				System.out.println("Oh boy! Things are really bad! " + e2.getMessage());
			}
		} finally {
			try {
				System.out.println("Resetting default commit behavior");
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				System.out.println("Couldn't reset auto-commit! " + e.getMessage());
			}

		}
	}

}















