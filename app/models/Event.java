package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.data.validation.Constraints;
import play.db.ebean.Model;

@Entity
@Table(name = "event")
public class Event extends Model {

	public enum MessageType {
		UPDATE, ALARM, EXCEPTION
	};

	@Id
	public final long id;

	@Constraints.Required
	public final long producerId;

	@ManyToOne
	public Producer producer;

	@Constraints.Required
	public final long rfid;

	@ManyToOne
	public Sheep sheep;

	public final MessageType type;
	public final int timeSent;
	public final int timeReceived;
	public final GpsData gpsData;
	public final int pulse;
	public final double temperature;

	public static Model.Finder<Long, Event> find = new Model.Finder<Long, Event>(Long.class, Event.class);

	public static List<Event> findByProducerId(long id) {
		return find.where().eq("producerId", id).findList();
	}

	public static List<Event> findByRfid(long id) {
		return find.where().eq("rfid", id).findList();
	}

	public Event(long id, long producerId, long rfid, MessageType type, int timeSent, int timeReceived,
			GpsData gpsData, int pulse, double temperature) {
		super();
		this.id = id;
		this.producerId = producerId;
		this.rfid = rfid;
		this.type = type;
		this.timeSent = timeSent;
		this.timeReceived = timeReceived;
		this.gpsData = gpsData;
		this.pulse = pulse;
		this.temperature = temperature;
	}

	public long getRfid() {
		return rfid;
	}

	public MessageType getType() {
		return type;
	}

	public int getTimeSent() {
		return timeSent;
	}

	public int getTimeReceived() {
		return timeReceived;
	}

	public GpsData getGpsData() {
		return gpsData;
	}

	public int getPulse() {
		return pulse;
	}

	public double getTemperature() {
		return temperature;
	}
}
