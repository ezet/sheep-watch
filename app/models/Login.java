package models;

public class Login {
	public String username;
	public String password;
	public User user = new User();

	public String validate() {
		user = User.authenticate(username, password);
		if (user == null) {
			return "Invalid user or password";
		}
		return null;
	}

}
