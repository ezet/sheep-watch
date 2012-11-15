package controllers;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
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
import play.mvc.Security;

@Security.Authenticated(Auth.class)
public class Sheep extends Controller {
	
	private static ObjectMapper mapper = new ObjectMapper();

	static {
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss"));
	}

	public static Result list() {
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

	public static Result show(Long id) {
		if (!Auth.isOwnerOfSheep(id)) {
			return unauthorized();
		}
		models.Sheep sheep = models.Sheep.find.byId(id);
		if (sheep == null) {
			return notFound();
		}
		sheep.producer = null;
		sheep.events = null;
		Logger.debug(sheep.toString());
		return ok(Json.toJson(sheep));
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
			return ok(writer.toString());
		}
	}


	public static Result update(Long id) {
		if (!Auth.isOwnerOfSheep(id)) {
			return unauthorized();
		}
		Form<models.Sheep> form = form(models.Sheep.class).bindFromRequest();
		if (form.hasErrors()) {
			return badRequest(form.errorsAsJson());
		}
		models.Sheep sheep = form.get();
		try {
			sheep.update(id);
		} catch (Exception e) {
			return notFound();
		}
		return ok(String.valueOf(id));
	}

	public static Result delete(Long id) {
		if (!Auth.isOwnerOfSheep(id)) {
			return unauthorized();
		}
		try {
			models.Sheep.find.ref(id).delete();
		} catch (Exception e) {
			return notFound();
		}
		return ok(String.valueOf(id));
	}
	
	public static Result position(Long sheep) {
		if (!Auth.isOwnerOfSheep(sheep)) {
			return unauthorized();
		}
		models.Event event = models.Event.findLatestEvent(sheep);
		if (event == null) {
			return notFound();
		}
		event.sheepId = event.sheep.sheepId;
		event.sheep = null;
		return ok(Json.toJson(event));
	}

}