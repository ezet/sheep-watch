package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import play.db.ebean.Model;

@Entity
@Table (name="Contact")
public class Contact extends Model {
	
	@Id
	public long id;

	@Transient
	public long producerId;
	@ManyToOne
	@JoinColumn(name="producer_id")
	public User producer;
	public String name;
	public String email;
	public String phoneSMS;
	public String phoneCall;
	public boolean emailAlert;
	public boolean callAlert;
	public boolean smsAlert;

	public Contact(String name, String email, String phoneSMS, String phoneCall) {
		this.name = name;
		this.email = email;
		this.phoneSMS = phoneSMS;
		this.phoneCall = phoneCall;
	}

}
