package models;

public class User {
	private long userId;
	private final long producerId;
	private String username;
	private String password;
	private int timeCreated;
	private boolean isAdmin;
	private Contact contactInfo;
	
	public User() {
		producerId = 0;
	}

	public User(long producerId, Contact contactInfo) {
		this.contactInfo = contactInfo;
		this.producerId = producerId;
	}

	public User(long userId, long producerId, String username, String password, int timeCreated, boolean isAdmin) {
		this.userId = userId;
		this.producerId = producerId;
		this.username = username;
		this.password = password;
		this.timeCreated = timeCreated;
		this.isAdmin = isAdmin;
	}

	public Contact getContactInfo() {
		return contactInfo;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public long getUserId() {
		return userId;
	}

	public long getProducerId() {
		return producerId;
	}

	public int getTimeCreated() {
		return timeCreated;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

}
