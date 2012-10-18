package models;

public class Date {
	
	private int weeknumber;
	private int year;
	
	public Date(int week, int year){
		this.weeknumber = week;
		this.year = year;
	}

	public int getWeeknumber() {
		return weeknumber;
	}

	public int getYear() {
		return year;
	}
	
	

}
