package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.Logger;
import play.data.format.Formats;
import play.data.validation.Constraints;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
@Table(name = "User")
public class User extends Model {

	@Id
	public long id;

	@Required
//	@ManyToOne
	public  long producerId;

	@Constraints.Required
//	@Constraints.Email
	public String username;
	
//	public String name;
	
	@Required
//	@Formats.NonEmpty
	public String password;
	
	public int timeCreated;
	public boolean isAdmin;
	public Contact contactInfo;

	public static Model.Finder<String, User> find = new Model.Finder<String, User>(String.class, User.class);
	
	public static User create() {
		return new User();
	}

	public static List<User> findAll() {
		Logger.debug("test");
		System.out.println("test");
//		for (User user : find.all()) {
//		}
		return find.all();
	}
	
	public static User findByUsername(String username) {
		return find.where().eq("username", username).findUnique();
	}

	public static User authenticate(String username, String password) {
		return find.where().eq("username", username).eq("password", password).findUnique();
	}

}
