package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.ebean.Model;

@Entity
@Table(name = "sheep")
public class Sheep extends Model {

	@Id
	private long id;
	private String name;
	private Date birthDate;
	private double birthWeight;

	public static Model.Finder<Long, Sheep> find = new Model.Finder<Long, Sheep>(Long.class, Sheep.class);
	public static Model.Finder<String, Sheep> findByName = new Model.Finder<String, Sheep>(String.class, Sheep.class);

	public static List<Sheep> findAll() {
		return find.all();
	}

	public static List<Sheep> findByName(String name) {
		return findByName.where().eq("name", name).findList();
	}

	public Sheep(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public double getBirthWeight() {
		return birthWeight;
	}

	public void setBirthWeight(double birthWeight) {
		this.birthWeight = birthWeight;
	}

	public long getId() {
		return id;
	}

}
