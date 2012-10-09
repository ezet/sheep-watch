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
@Table(name = "producer")
public class Producer extends Model {

	@Id
	@Constraints.Required
	public long id;

	@Constraints.Required
	@ManyToOne
	public final long producerId;

	@Constraints.Required
	@Constraints.Email
	public String username;

	@Constraints.Required
	@Formats.NonEmpty
	public String password;

	public int timeCreated;
	public boolean isAdmin;
	public Contact contactInfo;

	public static Model.Finder<String, Producer> find = new Model.Finder<String, Producer>(String.class, Producer.class);

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
		producerId = 0;
	}

	public Producer(long producerId, Contact contactInfo) {
		this.contactInfo = contactInfo;
		this.producerId = producerId;
	}

	public Producer(long id, long producerId, String username, String password, int timeCreated, boolean isAdmin) {
		this.id = id;
		this.producerId = producerId;
		this.username = username;
		this.password = password;
		this.timeCreated = timeCreated;
		this.isAdmin = isAdmin;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getTimeCreated() {
		return timeCreated;
	}

	public void setTimeCreated(int timeCreated) {
		this.timeCreated = timeCreated;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public Contact getContactInfo() {
		return contactInfo;
	}

	public void setContactInfo(Contact contactInfo) {
		this.contactInfo = contactInfo;
	}

	public static Model.Finder<String, Producer> getFind() {
		return find;
	}

	public static void setFind(Model.Finder<String, Producer> find) {
		Producer.find = find;
	}

	public long getProducerId() {
		return producerId;
	}

	public String toString() {
		return "User(" + username + ")";
	}

}
