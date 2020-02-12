package be.pxl.ja2.jdbc.dao;

public class Contact {
	private long id;
	private String name;
	private int phone;
	private String email;

	public Contact(String name, int phone, String email) {
		this.name = name;
		this.phone = phone;
		this.email = email;
	}

	public Contact() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPhone() {
		return phone;
	}

	public void setPhone(int phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "Contact{" +
				"id=" + id +
				", name='" + name + '\'' +
				", phone=" + phone +
				", email='" + email + '\'' +
				'}';
	}
}
