package controllers;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import models.Event;

import org.codehaus.jackson.map.ObjectMapper;

import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.application.eventList;

public class Events extends Controller {
	public static Result show(long id) {
		return TODO;
	}

	public static Result index() {
		return TODO;
	}
	
	public static Result alarms() {
		return TODO;
	}
	
	@BodyParser.Of(BodyParser.Json.class)
	public static Result recentAlarms(int num) {
		List<Event> events = Event.findByProducerId(Long.valueOf(session("producerId")), num);
		ObjectMapper mapper = new ObjectMapper();
		StringWriter writer = new StringWriter();
		try {
			mapper.writeValue(writer, events);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ok(Json.toJson(events));
	}
	
	public static Result recentExceptions(Long num) {
		return TODO;
	}

}
