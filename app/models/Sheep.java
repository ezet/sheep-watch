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

@Entity
@Table
public class Sheep extends Model {
	
	private static final long serialVersionUID = 1L;

	@Id
	public Long id;
	
	@Constraints.Required
	@Column(nullable=false)
	public Long sheepPid;
	
	@Transient
	public Long producerId;
	
	@Transient
	public Long userId;
	
	@ManyToOne
	@JoinColumn(name="user_id", referencedColumnName="id", nullable=false)
	public User user;
	
	@Constraints.Required
	@Column(unique=true, nullable=false)
	public Long rfid;
	
	
	@OneToMany(mappedBy="sheep", cascade=CascadeType.ALL)
	public List<Event> events = new ArrayList<Event>();

	public String name;
	
	public Double birthWeight;
	
	public Date dateOfBirth;
	
	public String notes;
	
	@CreatedTimestamp
	public Timestamp creTime;
	
	@Version
	public Timestamp updTime;

	public static Model.Finder<Long, Sheep> find = new Model.Finder<Long, Sheep>(Long.class, Sheep.class);

	public static List<Sheep> findByUserId(Long id) {
		return find.where().eq("user.id", id).findList();
	}

	public String toString() {
		return "id:" + id + " sheepId:" + sheepPid + " producerId:" + producerId + " rfid:" + rfid;
	}
	
	public static boolean isOwner(Long sheep, String userId) {
		return find.where().eq("id", sheep).eq("user.id", userId).findRowCount() > 0;
	}

}
