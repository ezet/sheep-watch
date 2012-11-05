package models;

public class Login {
	public User user;
	public String username;
	public String password;

	public String validate() {
		user = User.authenticate(username, password);
		if (user == null) {
			return "Invalid user or password";
		}
		return null;
	}

}
