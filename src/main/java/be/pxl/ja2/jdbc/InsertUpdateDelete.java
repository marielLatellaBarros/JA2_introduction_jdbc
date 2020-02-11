package be.pxl.ja2.jdbc;

import java.sql.*;

public class InsertUpdateDelete {

	public static void main(String[] args) {

		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/musicdb?useSSL=false", "user", "password");
		     Statement statement = conn.createStatement()) {
			// conn.setAutoCommit(false);
			statement.execute("INSERT INTO contacts (name, phone, email) VALUES('Joe', 45632, 'joe@anywhere.com')");
			statement.execute("INSERT INTO contacts (name, phone, email) VALUES('Jane', 4829484, 'jane@somewhere.com')");
			statement.execute("INSERT INTO contacts (name, phone, email) VALUES('Fido', 9038, 'dog@email.com')");

			statement.execute("SELECT * FROM contacts");
			ResultSet results = statement.getResultSet();
			while (results.next()) {
				System.out.println(results.getString("name") + " " +
						results.getInt("phone") + " " +
						results.getString("email"));
			}

			statement.executeUpdate("UPDATE contacts set phone='486666' WHERE name = 'Jane'");

			statement.executeUpdate("DELETE FROM contacts WHERE email = 'dog@email.com'");

		} catch (SQLException e) {
			System.out.println("Something went wrong: " + e.getMessage());
		}
	}
}
