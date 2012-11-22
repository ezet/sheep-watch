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

@Security.Authenticated(Auth.class)
public class Event extends Controller {
	
	private static ObjectMapper mapper = new ObjectMapper();

	static {
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss"));
	}
	public static Result list() {
		List<models.Event> events = models.Event.findByProducerId(Long.valueOf(session("producerId")), Integer.MAX_VALUE - 1);
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
	
	public static Result listLimit(Integer num) {
		List<models.Event> events = models.Event.findByProducerId(Long.valueOf(session("producerId")), num);
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

	public static void jsonPrepare(models.Event event) {
		event.rfid = event.sheep.rfid;
		event.sheepPid = event.sheep.sheepPid;
		event.sheepId = event.sheep.id;
		event.sheep = null;
	}

}
