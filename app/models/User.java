package models;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import play.data.format.Formats;
import play.data.validation.Constraints;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

import com.avaje.ebean.annotation.CreatedTimestamp;

/**
 * User model
 * 
 * @author Lars Kristian
 * 
 */
@Entity
@Table
public class User extends Model {
	private static final long serialVersionUID = 1L;

	@Id
	public Long id;

	@Required
	@Column(nullable = false, unique = true)
	public Long producerId;

	@Constraints.Required
	@Constraints.Email
	@Column(nullable = false, unique = true)
	public String username;

	public String name;

	@Required
	@Formats.NonEmpty
	@Column(nullable = false)
	public String password;

	@CreatedTimestamp
	public Timestamp creTime;

	@Version
	public Timestamp updTime;

	@Column(nullable = false)
	public Integer accessLevel;
	
	@OneToMany(mappedBy="user", cascade=CascadeType.ALL)
	public List<Contact> contacts = new ArrayList<Contact>();
	
	@OneToMany(mappedBy="user", cascade=CascadeType.ALL)
	public List<Sheep> sheep = new ArrayList<Sheep>();

	/**
	 * User finder
	 */
	public static Model.Finder<Long, User> find = new Model.Finder<Long, User>(Long.class, User.class);

	/**
	 * Fetches a list of all users
	 * 
	 * @return A list of all users
	 */
	public static List<User> findAll() {
		return find.all();
	}

	/**
	 * Fetches a user by username
	 * 
	 * @param username Username to fetch
	 * @return User object if username was found, null otherwise
	 */
	public static User findByUsername(String username) {
		return find.where().eq("username", username).findUnique();
	}

	/**
	 * Fetches a User with the given username and password
	 * 
	 * @param username Username to find
	 * @param password Password to find
	 * @return A User object if the user was found, null otherwise
	 */
	public static User authenticate(String username, String password) {
		return find.where().eq("username", username).eq("password", password).findUnique();
	}
}
