package controllers;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import models.User;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;

import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class Sheep extends Controller {
	
	public static Result index() {
		return ok(views.html.application.sheep.render());
	}

	public static Result list() {
		// TODO require login
		List<models.Sheep> list = models.Sheep.findByProducerId(Long.valueOf(session("producerId")));

		for (models.Sheep sheep : list) {
			sheep.events = null;
			sheep.producerId = sheep.producer.id;
			sheep.producer = null;
		}
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		node.put("data", Json.toJson(list));
		return ok(node);
	}
	
	public static Result positions() {
		return TODO;
	}
	
	public static Result position(Long id) {
		models.Event event = models.Event.findLatestEvent(id);
		event.sheepId = event.sheep.sheepId;
		event.sheep = null;
		return ok(Json.toJson(event));
	}

	public static Result add() {
		Form<models.Sheep> sheepForm = form(models.Sheep.class);
		sheepForm = sheepForm.bindFromRequest();
		if (sheepForm.hasErrors()) {
			Logger.debug(sheepForm.errorsAsJson().asText());
			return badRequest(sheepForm.errorsAsJson());
		} else {
			
		models.Sheep sheep = sheepForm.get();
		sheep.producer = User.find.ref(Long.valueOf(session("producerId")));
		sheep.save();
		sheep.refresh();
		sheep.producerId = sheep.producer.producerId;
		sheep.producer = null;
		sheep.events = null;
		StringWriter writer = new StringWriter();
		try {
			new ObjectMapper().writeValue(writer, sheep);
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
		Logger.debug(writer.toString());
		return ok(writer.toString());
		}
	}

	public static Result show(Long id) {
		// TODO require owner of sheep
		Logger.debug(String.valueOf(id));
		models.Sheep sheep = models.Sheep.find.byId(id);
		sheep.producer = null;
		sheep.events = null;
		Logger.debug(sheep.toString());
		return ok(Json.toJson(sheep));
	}

	public static Result update(Long id) {
		return TODO;
	}

	public static Result delete(Long id) {
		models.Sheep.find.ref(id).delete();
		return ok();
	}

}
