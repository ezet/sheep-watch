package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import play.db.ebean.Model;

@Entity
@Table(name = "Contact")
public class Contact extends Model {

	@Id
	public long id;

	@Transient
	public long producerId;
	@ManyToOne
	@JoinColumn(name = "producer_id")
	public User producer;
	public String name;
	public String email;
	public String phone;
	public boolean emailAlert;
	public boolean callAlert;
	public boolean smsAlert;

	public Contact(String name, String email, String phone) {
		this.name = name;
		this.email = email;
		this.phone = phone;
	}

	public static Model.Finder<Long, Contact> find = new Model.Finder<>(Long.class, Contact.class);

	public static List<Contact> findByProducerId(Long producerId) {
		return find.where().eq("producerId", producerId).findList();
	}

}
