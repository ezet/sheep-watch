package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.Model;

@Entity
@Table(name = "User")
public class User extends Model {

	@Id
	@Constraints.Required
	public long id;

	@Constraints.Required
	@ManyToOne
	public  long producerId;

	@Constraints.Required
	@Constraints.Email
	public String username;

	@Constraints.Required
	@Formats.NonEmpty
	public String password;

	public int timeCreated;
	public boolean isAdmin;
	public Contact contactInfo;

	public static Model.Finder<String, User> find = new Model.Finder<String, User>(String.class, User.class);

	public static List<User> findAll() {
		return find.all();
	}

	public static User findByUsername(String username) {
		return find.where().eq("username", username).findUnique();
	}

	public static User authenticate(String username, String password) {
		return find.where().eq("username", username).eq("password", password).findUnique();
	}

}
