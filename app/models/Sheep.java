package models;




public class Sheep {
	
	private long id;
	private String name;
	private Date birthWeek;
	private double birthWeight;
	
	// Det bør vurderes om vi trenger flere konstruktører
	public Sheep(long id){
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getBirthWeek() {
		return birthWeek;
	}

	public void setBirthWeek(Date birthWeek) {
		this.birthWeek = birthWeek;
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
