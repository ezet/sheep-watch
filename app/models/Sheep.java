package models;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import com.avaje.ebean.annotation.CreatedTimestamp;

/**
 * Sheep model
 * 
 * @author Lars Kristian
 * 
 */
@Entity
@Table
public class Sheep extends Model {

	private static final long serialVersionUID = 1L;

	@Id
	public Long id;

	@Constraints.Required
	@Column(nullable = false)
	public Long sheepPid;

	@Transient
	public Long producerId;

	@Transient
	public Long userId;

	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
	public User user;

	@Constraints.Required
	@Column(unique = true, nullable = false)
	public Long rfid;

	@OneToMany(mappedBy = "sheep", cascade = CascadeType.ALL)
	public List<Event> events = new ArrayList<>();

	public String name;

	public Double birthWeight;

	public Date dateOfBirth;

	public String notes;

	@CreatedTimestamp
	public Timestamp creTime;

	@Version
	public Timestamp updTime;

	/**
	 * Sheep finder
	 */
	public static Model.Finder<Long, Sheep> find = new Model.Finder<Long, Sheep>(Long.class, Sheep.class);

	/**
	 * Fetches a list of sheep for a given user ID
	 * 
	 * @param id The user ID
	 * @return A list of sheep
	 */
	public static List<Sheep> findByUserId(Long id) {
		return find.where().eq("user.id", id).findList();
	}

	/**
	 * Checks if the given user ID is the owner of the given sheep ID
	 * 
	 * @param sheep
	 * @param userId
	 * @return True if the user ID is the owner, false otherwise
	 */
	public static boolean isOwner(Long sheep, String userId) {
		return find.where().eq("id", sheep).eq("user.id", userId).findRowCount() > 0;
	}

	public String toString() {
		return "id:" + id + " sheepId:" + sheepPid + " producerId:" + producerId + " rfid:" + rfid;
	}

}
