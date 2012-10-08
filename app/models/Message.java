package models;


public class Message {

	public enum MessageType {
		UPDATE, ALARM
	};

	private final long messageId;
	private final long rfid;
	private final MessageType type;
	private final int timeSent;
	private final int timeReceived;
	private final GpsData gpsData;
	private final int pulse;
	private final double temperature;

	public Message(long messageId, long sheepId, MessageType type, int timeSent, int timeReceived, GpsData gpsData,
			int pulse, double temperature) {
		super();
		this.messageId = messageId;
		this.rfid = sheepId;
		this.type = type;
		this.timeSent = timeSent;
		this.timeReceived = timeReceived;
		this.gpsData = gpsData;
		this.pulse = pulse;
		this.temperature = temperature;
	}

	public long getMessageId() {
		return messageId;
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
