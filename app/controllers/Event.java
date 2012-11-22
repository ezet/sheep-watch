package controllers;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

import models.Event.MessageType;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

/**
 * Handles all REST requests for Events. All requests require a valid session and user ID.
 * 
 * @author Lars Kristian
 * 
 */
@Security.Authenticated(Auth.class)
public class Event extends Controller {

	/**
	 * JSON mapper
	 */
	private static ObjectMapper mapper = new ObjectMapper();

	static {
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss"));
	}

	/**
	 * Handles requests for a list of all events
	 * 
	 * @return A json list of all events
	 */
	public static Result list() {
		List<models.Event> events = models.Event.findByUserId(Long.valueOf(session("userId")), Integer.MAX_VALUE - 1);
		for (models.Event event : events) {
			jsonPrepare(event);
		}
		ObjectNode node = mapper.createObjectNode();
		StringWriter writer = new StringWriter();

		try {
			mapper.writeValue(writer, events);
			node.put("data", mapper.readTree(writer.toString()));
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ok(node);
	}

	/**
	 * Handles requests for a list of events with a maximum length
	 * 
	 * @param num The list length
	 * @return A json list of events
	 */
	public static Result listLimit(Integer num) {
		List<models.Event> events = models.Event.findByUserId(Long.valueOf(session("userId")), num);
		for (models.Event event : events) {
			jsonPrepare(event);
		}
		ObjectNode node = mapper.createObjectNode();
		StringWriter writer = new StringWriter();
		try {
			mapper.writeValue(writer, events);
			node.put("data", mapper.readTree(writer.toString()));
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ok(node);
	}

	/**
	 * Handles request for a single event
	 * 
	 * @param id The event id
	 * @return A json event
	 */
	public static Result show(Long id) {
		if (!Auth.isOwnerOfEvent(id)) {
			return unauthorized();
		}
		models.Event event = models.Event.find.byId(id);
		if (event == null) {
			return notFound();
		}
		jsonPrepare(event);
		StringWriter writer = new StringWriter();
		try {
			mapper.writeValue(writer, event);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ok(writer.toString());
	}

	/**
	 * Handles requests for a list of alar,m events, with a max length
	 * 
	 * @param num The max length
	 * @return A json list of alarm events
	 */
	public static Result alarmList(Integer num) {
		List<models.Event> events = models.Event.findTypeByUserId(Long.valueOf(session("userId")), MessageType.ALARM, num);
		Collections.reverse(events);
		for (models.Event event : events) {
			jsonPrepare(event);
		}
		StringWriter writer = new StringWriter();
		try {
			mapper.writeValue(writer, events);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ok(writer.toString());
	}

	/**
	 * Handles request for a list of all events belonging to a specified sheep id
	 * 
	 * @param sheepId The sheep ID to find events for
	 * @return A json list of events
	 */
	public static Result listBySheep(Long sheepId) {
		if (!Auth.isOwnerOfSheep(sheepId)) {
			return unauthorized();
		}
		List<models.Event> events = models.Event.findBySheepId(sheepId);
		for (models.Event event : events) {
			jsonPrepare(event);
		}

		ObjectNode node = mapper.createObjectNode();
		StringWriter writer = new StringWriter();

		try {
			mapper.writeValue(writer, events);
			node.put("data", mapper.readTree(writer.toString()));
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ok(node);
	}

	/**
	 * Prepares an event for JSON to avoid infinite cycles
	 * 
	 * @param event The Event to prepare
	 */
	public static void jsonPrepare(models.Event event) {
		event.rfid = event.sheep.rfid;
		event.sheepPid = event.sheep.sheepPid;
		event.sheepId = event.sheep.id;
		event.sheep = null;
	}

}
