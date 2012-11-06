package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
	public long id;
	
	public long rfid;
	
	@ManyToOne
	@JoinColumn(name="rfid")
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
		return find.where().eq("sheep.producer.producerId", id).findList();
	}

	public static List<Event> findByRfid(long id) {
		return find.where().eq("rfid", id).findList();
	}

}
