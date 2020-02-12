package be.pxl.ja2.jdbc.dao;

public interface ContactDao {

	Contact getContactByName(String name);

	Contact getContactById(long id);

	boolean updateContact(Contact contact);

	boolean deleteContact(long id);

	Contact createContact(Contact contact);

}
