package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.data.validation.Constraints;
import play.db.ebean.Model;

@Entity
@Table(name = "Event")
public class Event extends Model {

	public enum MessageType {
		UPDATE, ALARM, EXCEPTION
	};

	@Id
	public long id;

	@Constraints.Required
	public long producerId;

	@ManyToOne
	public User producer;

	@Constraints.Required
	public long rfid;

	@ManyToOne
	public Sheep sheep;

	public MessageType type;
	public int timeSent;
	public int timeReceived;
	public GpsData gpsData;
	public int pulse;
	public double temperature;

	public static Model.Finder<Long, Event> find = new Model.Finder<Long, Event>(Long.class, Event.class);

	public static List<Event> findByProducerId(long id, int num) {
		return find.where().eq("producerId", id).setMaxRows(num).findList();
	}

	public static List<Event> findByRfid(long id) {
		return find.where().eq("rfid", id).findList();
	}

}
