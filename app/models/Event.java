package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import play.data.format.Formats;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

import com.avaje.ebean.annotation.CreatedTimestamp;

@Entity
@Table
public class Event extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum MessageType {
		UPDATE, ALARM, EXCEPTION
	};

	@Id
	public long id;
	
	@Transient
	public Long sheepId;
	
	@Transient
	public Long sheepPid;
	
	@Column(nullable=false)
	public long rfid;
	
	@Required
	@ManyToOne(optional=false)
	@JoinColumn(name="sheep_id", referencedColumnName="id", nullable=false)
	public Sheep sheep;

	public MessageType messageType;
	
	@Formats.DateTime(pattern="yyyy-MM-dd hh:mm:ss")
	public Date timeSent;
	
	@CreatedTimestamp
	@Formats.DateTime(pattern="yyyy-MM-dd hh:mm:ss")
	public Date timeReceived;
	
	public double latitude;
	public double longitude;
	
	public int pulse;
	public double temperature;

	public static Model.Finder<Long, Event> find = new Model.Finder<Long, Event>(Long.class, Event.class);

	public static List<Event> findByProducerId(Long producerId, int num) {
		return find.where().eq("sheep.user.producerId", producerId).order().desc("timeSent").setMaxRows(num).findList();
	}
	
	public static List<Event> findTypeByProducerId(long producerId, MessageType type, int num) {
		return find.where().eq("sheep.user.producerId", producerId).eq("messageType", type).order().desc("timeSent").setMaxRows(num).findList();
	}
	
	public static List<Event> findLatestEvents(long producerId) {
		return find.where().eq("sheep.user.producerId", producerId).order().desc("timeSent").findList();
	}
	
	public static Event findLatestEvent(long sheepId) {
		return find.where().eq("sheep.sheepPid", sheepId).order().desc("timeSent").setMaxRows(1).findUnique();
	}
	
	public static List<Event> findBySheepId(Long id) {
		return find.where().eq("sheep.id", id).order().desc("timeSent").findList();
	}

	public static List<Event> findByRfid(long id) {
		return find.where().eq("rfid", id).findList();
	}
	
	public static boolean isOwner(Long eventId, String userId) {
		return find.where().eq("id", eventId).eq("sheep.user.id", userId).findRowCount() > 0;
	}

}
