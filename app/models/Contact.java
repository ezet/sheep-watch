package models;

public class Contact {
	private String name;
	private String email;
	private String phoneSMS;
	private String phoneCall;

	public Contact(String name, String email, String phoneSMS, String phoneCall) {
		this.name = name;
		this.email = email;
		this.phoneSMS = phoneSMS;
		this.phoneCall = phoneCall;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneSMS() {
		return phoneSMS;
	}

	public void setPhoneSMS(String phoneSMS) {
		this.phoneSMS = phoneSMS;
	}

	public String getPhoneCall() {
		return phoneCall;
	}

	public void setPhoneCall(String phoneCall) {
		this.phoneCall = phoneCall;
	}
}
