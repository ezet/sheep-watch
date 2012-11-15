package models;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonManagedReference;

import play.data.validation.Constraints;
import play.db.ebean.Model;

@Entity
@Table(name = "Sheep")
public class Sheep extends Model {

	@Id
	@GeneratedValue
	public long id;
	
	@Constraints.Required
	@Column(unique=true, nullable=false)
	public long sheepId;
	
	@Transient
	public long producerId;
	
	@ManyToOne
	@JoinColumn(name="producer_id")
	public User producer;
	
	@Constraints.Required
	@Column(unique=true, nullable=false)
	public long rfid;
	
	
	@OneToMany(mappedBy="sheep")
	public List<Event> events = new ArrayList<>();

	
	public String name = "";
	public Double birthWeight;
	
	public Date dateOfBirth;
	public String notes = "";
	public boolean attacked = false;
	public Timestamp timeAdded;

	public static Model.Finder<Long, Sheep> find = new Model.Finder<Long, Sheep>(Long.class, Sheep.class);

	public static Sheep create(long id, long rfid, long producerId) {
		return new Sheep();
	}
	
	public static List<Sheep> findByProducerId(long id) {
		return find.where().eq("producer_id", id).findList();
	}

	public Sheep() {

	}

	public Sheep(long id) {
		this.id = id;
	}
	
	public String toString() {
		return "id:" + id + " sheepId:" + sheepId + " producerId:" + producerId + " rfid:" + rfid;
	}
	
	public static boolean isOwner(Long sheep, String producer) {
		return find.where().eq("id", sheep).eq("producer.producerId", producer).findRowCount() > 0;
	}

}
