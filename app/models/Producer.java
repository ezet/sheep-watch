package models;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import play.data.format.Formats;
import play.data.validation.Constraints;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
@Table(name = "Producer")
public class Producer extends Model {

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
	public List<Contact> contacts;
	
	@OneToMany(mappedBy="producer")
	List<Sheep> sheep = new ArrayList<>();

	public static Model.Finder<Long, Producer> find = new Model.Finder<>(Long.class, Producer.class);

	public static Producer create() {
		return new Producer();
	}

	public static List<Producer> findAll() {
		return find.all();
	}

	public static Producer findByUsername(String username) {
		return find.where().eq("username", username).findUnique();
	}

	public static Producer authenticate(String username, String password) {
		return find.where().eq("username", username).eq("password", password).findUnique();
	}

	public Producer() {

	}

	public Producer(long id) {
		this.id = id;
	}

}
