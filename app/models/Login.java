package models;

/**
 * Model for login forms
 * 
 * @author Lars Kristian
 * 
 */
public class Login {
	public String username;
	public String password;
	public User user = new User();

	/**
	 * Performs login form validation
	 * 
	 * @return
	 */
	public String validate() {
		user = User.authenticate(username, password);
		if (user == null) {
			return "Invalid user or password";
		}
		return null;
	}

}
