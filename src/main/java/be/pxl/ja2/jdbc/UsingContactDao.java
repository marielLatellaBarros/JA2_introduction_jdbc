package be.pxl.ja2.jdbc;

import be.pxl.ja2.jdbc.dao.Contact;
import be.pxl.ja2.jdbc.dao.ContactDao;
import be.pxl.ja2.jdbc.dao.ContactDaoImpl;

public class UsingContactDao {

	public static void main(String[] args) {
		ContactDao contactDao = new ContactDaoImpl("jdbc:mysql://localhost:3306/musicdb?useSSL=false", "user", "password");
		Contact contact = contactDao.getContactById(2);
		System.out.println(contact);
		Contact emma = contactDao.createContact(new Contact("Emma", 124353, "emma@gmail.com" ));
		System.out.println(emma);
		Contact john = contactDao.getContactByName("Joe");
		System.out.println(john);

	}

}
