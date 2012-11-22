package models;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

import com.avaje.ebean.annotation.CreatedTimestamp;

/**
 * Contact model
 * 
 * @author Lars Kristian
 * 
 */
@Entity
@Table
public class Contact extends Model {

	private static final long serialVersionUID = 1L;

	@Id
	public long id;

	@Transient
	public long producerId;

	@Transient
	public long userId;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
	public User user;

	@CreatedTimestamp
	public Timestamp creTime;

	@Version
	public Timestamp updTime;
	@Required
	public String name;
	public String email;
	public String phone;
	public boolean emailAlert;
	public boolean callAlert;
	public boolean smsAlert;

	/**
	 * Contact finder
	 */
	public static Model.Finder<Long, Contact> find = new Model.Finder<Long, Contact>(Long.class, Contact.class);

	/**
	 * Fetches a list of all contacts owned by provided user ID
	 * 
	 * @param userId The user ID to find contacts for
	 * @return The list of contacts
	 */
	public static List<Contact> findByUserId(Long userId) {
		return find.where().eq("user.id", userId).findList();
	}

	/**
	 * Checks if provided user ID is the owner of provided contact ID
	 * 
	 * @param contactId
	 * @param userId
	 * @return True if the user is the owner, otherwise false
	 */
	public static boolean isOwner(Long contactId, String userId) {
		return find.where().eq("id", contactId).eq("user.id", userId).findRowCount() > 0;
	}

}
