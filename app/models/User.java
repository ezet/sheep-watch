package models;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonManagedReference;

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
	public long producerId;

	@Constraints.Required
	@Constraints.Email
	public String username;

	public String name;

	@Required
	@Formats.NonEmpty
	public String password;

	public Timestamp timeCreated;
	public int accessLevel;
	
	@OneToMany(mappedBy="producer")
	public List<Contact> contacts = new ArrayList<>();
	
	@OneToMany(mappedBy="producer")
	public List<Sheep> sheep = new ArrayList<>();

	public static Model.Finder<Long, User> find = new Model.Finder<>(Long.class, User.class);

	public static User create() {
		return new User();
	}

	public static List<User> findAll() {
		return find.all();
	}

	public static User findByUsername(String username) {
		return find.where().eq("username", username).findUnique();
	}

	public static User authenticate(String username, String password) {
		return find.where().eq("username", username).eq("password", password).findUnique();
	}

	public User() {

	}

	public User(long id) {
		this.id = id;
	}

}
