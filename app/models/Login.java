package models;

public class Login {
	public String username;
	public String password;
	public Producer user = new Producer();

	public String validate() {
		user = Producer.authenticate(username, password);
		if (user == null) {
			return "Invalid user or password";
		}
		return null;
	}

}
