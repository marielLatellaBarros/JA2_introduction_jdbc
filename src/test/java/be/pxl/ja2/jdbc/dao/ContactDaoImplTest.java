package be.pxl.ja2.jdbc.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ContactDaoImplTest {

	private static final String URL = "jdbc:h2:~/test";
	private static final String USER = "sa";
	private static final String PASSWORD = "";

	private ContactDao contactDao = new ContactDaoImpl(URL, USER, PASSWORD);
	private String NAME = "Alf";
	private int PHONE = 666555;
	private String EMAIL = "alf@pxl.be";

	@BeforeEach
	public void init() throws SQLException {
		createDatabase();
	}

	@AfterEach
	public void tearDown() throws SQLException {
		destroyDatabase();
	}

	@Test
	public void testCreateContact() {
		Contact newContact = contactDao.createContact(new Contact(NAME, PHONE, EMAIL));
		assertNotNull(newContact);
		assertNotNull(newContact.getId());
		assertEquals(NAME, newContact.getName());
		assertEquals(PHONE, newContact.getPhone());
		assertEquals(EMAIL, newContact.getEmail());
	}

	@Test
	public void testUpdateContact() {
		Contact existingContact = contactDao.getContactByName("Fido");
		existingContact.setName(NAME);
		existingContact.setPhone(PHONE);
		existingContact.setEmail(EMAIL);
		contactDao.updateContact(existingContact);
		Contact updatedContact = contactDao.getContactById(existingContact.getId());
		assertNotNull(updatedContact);
		assertEquals(NAME, updatedContact.getName());
		assertEquals(PHONE, updatedContact.getPhone());
		assertEquals(EMAIL, updatedContact.getEmail());
		assertNull(contactDao.getContactByName("Fido"));
	}


	private void createDatabase() throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
		     Statement statement = connection.createStatement()) {
			statement.execute("CREATE TABLE IF NOT EXISTS contacts (id INTEGER NOT NULL AUTO_INCREMENT, name TEXT, phone INTEGER, email TEXT, PRIMARY KEY (id))");
			statement.execute("INSERT INTO contacts (name, phone, email) VALUES('Joe', 45632, 'joe@anywhere.com')");
			statement.execute("INSERT INTO contacts (name, phone, email) VALUES('Jane', 4829484, 'jane@somewhere.com')");
			statement.execute("INSERT INTO contacts (name, phone, email) VALUES('Fido', 9038, 'dog@email.com')");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void destroyDatabase() throws SQLException {
		try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
		     Statement statement = connection.createStatement()) {
			statement.execute("DROP ALL OBJECTS");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


}
