package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import play.data.format.Formats;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
@Table(name = "Event")
public class Event extends Model {

	public enum MessageType {
		UPDATE, ALARM, EXCEPTION
	};

	@Id
	@GeneratedValue
	public long id;
	
	@Transient
	public long sheepId;
	
	@Transient
	public long rfid;
	
	@ManyToOne
	@JoinColumn(name="rfid")
	@Required
	public Sheep sheep;

	public MessageType messageType;
	
	@Formats.DateTime(pattern="yyyy-MM-dd hh:mm:ss")
	public Date timeSent;
	
	@Formats.DateTime(pattern="yyyy-MM-dd hh:mm:ss")
	public Date timeReceived;
	
	public double latitude;
	public double longitude;
	
	public int pulse;
	public double temperature;

	public static Model.Finder<Long, Event> find = new Model.Finder<Long, Event>(Long.class, Event.class);

	public static List<Event> findByProducerId(long id, int num) {
		return find.where().eq("sheep.producer.producerId", id).orderBy("timeSent").setMaxRows(num).findList();
	}
	
	public static List<Event> findTypeByProducerId(long producerId, MessageType type, int num) {
		return find.where().eq("sheep.producer.producerId", producerId).eq("messageType", type).orderBy("timeSent").setMaxRows(num).findList();
	}
	
	public static List<Event> findLatestEvents(long producerId) {
		return find.where().eq("sheep.producer.producerId", producerId).orderBy("timeSent").findList();
	}
	
	public static Event findLatestEvent(long sheepId) {
		return find.where().eq("sheep.sheepId", sheepId).orderBy("timeSent").setMaxRows(1).findUnique();
	}
	
	public static List<Event> findBySheepId(long id) {
		return find.where().eq("sheep.sheepId", id).orderBy("timeSent").findList();
	}

	public static List<Event> findByRfid(long id) {
		return find.where().eq("rfid", id).findList();
	}

}
