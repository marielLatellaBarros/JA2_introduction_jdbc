package be.pxl.ja2.jdbc.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ContactDaoImpl implements ContactDao {

	private static final String SELECT_BY_ID = "SELECT * FROM contacts WHERE id = ?";
	private static final String SELECT_BY_NAME = "SELECT * FROM contacts WHERE name = ?";
	private static final String UPDATE = "UPDATE contacts SET name=?, phone=?, email=? WHERE id = ?";
	private static final String INSERT = "INSERT INTO contacts (name, phone, email) VALUES (?, ?, ?)";
	private static final String DELETE = "DELETE FROM contacts WHERE id = ?";
	private String url;
	private String user;
	private String password;

	public ContactDaoImpl(String url, String user, String password) {
		this.url = url;
		this.user = user;
		this.password = password;
	}

	public Contact getContactByName(String name) {
		try (Connection connection = getConnection(); PreparedStatement stmt = connection.prepareStatement(SELECT_BY_NAME)) {
			stmt.setString(1, name);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return mapContact(rs);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	private Contact mapContact(ResultSet rs) throws SQLException {
		Contact contact = new Contact();
		contact.setId(rs.getInt("id"));
		contact.setName(rs.getString("name"));
		contact.setPhone(rs.getInt("phone"));
		contact.setEmail(rs.getString("email"));
		return contact;
	}

	public Contact getContactById(long id) {
		try (Connection connection = getConnection(); PreparedStatement stmt = connection.prepareStatement(SELECT_BY_ID)) {
			stmt.setLong(1, id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return mapContact(rs);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return null;

	}

	public boolean updateContact(Contact contact) {
		try (Connection connection = getConnection(); PreparedStatement stmt = connection.prepareStatement(UPDATE)) {
			stmt.setString(1, contact.getName());
			stmt.setInt(2, contact.getPhone());
			stmt.setString(3, contact.getEmail());
			stmt.setLong(4, contact.getId());
			return stmt.execute();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return false;
	}

	public boolean deleteContact(long id) {
		try (Connection connection = getConnection(); PreparedStatement stmt = connection.prepareStatement(DELETE)) {
			stmt.setLong(4, id);
			return stmt.execute();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return false;
	}

	public Contact createContact(Contact contact) {
		try (Connection connection = getConnection(); PreparedStatement stmt = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, contact.getName());
			stmt.setInt(2, contact.getPhone());
			stmt.setString(3, contact.getEmail());
			if (stmt.executeUpdate() == 1) {
				try (ResultSet rs = stmt.getGeneratedKeys()) {
					if (rs.next()) {
						contact.setId(rs.getInt(1));
						return contact;
					}
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	private Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, user, password);

	}
}
