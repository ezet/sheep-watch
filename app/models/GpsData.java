package models;

public class GpsData {
	private double latitude;//positive north for equator, negative south
	private double longitude; //positive east for Prime Meridian, negative west
	
	public GpsData(){
		this.latitude=0;
		this.longitude=0;
	}
	public GpsData(double latitude, double longitude){
		this.latitude=latitude;
		this.longitude=longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public String toString(){
		return longitude+","+latitude;
	}
	
}
