package controllers;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

/**
 * Handles all REST requests for Sheep. All requests require a valid session and user ID.
 * 
 * @author Lars Kristian
 * 
 */
@Security.Authenticated(Auth.class)
public class Sheep extends Controller {

	private static ObjectMapper mapper = new ObjectMapper();

	static {
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss"));
	}

	/**
	 * Prepared a Sheep for JSON to avoid infinite cyles
	 * 
	 * @param sheep The sheep to prepare
	 */
	public static void jsonPrepare(models.Sheep sheep) {
		sheep.events = null;
		sheep.userId = sheep.user.id;
		sheep.user = null;
	}

	/**
	 * Handles requests for a Sheep list
	 * 
	 * @return A json list of Sheep
	 */
	public static Result list() {
		List<models.Sheep> list = models.Sheep.findByUserId(Long.valueOf(session("userId")));
		for (models.Sheep sheep : list) {
			jsonPrepare(sheep);
		}
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		node.put("data", Json.toJson(list));
		return ok(node);
	}

	/**
	 * Handles requests for a single sheep
	 * 
	 * @param id The sheep ID
	 * @return The sheep data as json
	 */
	public static Result show(Long id) {
		if (!Auth.isOwnerOfSheep(id)) {
			return unauthorized();
		}
		models.Sheep sheep = models.Sheep.find.byId(id);
		if (sheep == null) {
			return notFound();
		}
		jsonPrepare(sheep);
		Logger.debug(sheep.toString());
		return ok(Json.toJson(sheep));
	}

	/**
	 * Handles requests for adding a new Sheep
	 * 
	 * @return The newly added sheep
	 */
	public static Result add() {
		Form<models.Sheep> sheepForm = form(models.Sheep.class);
		sheepForm = sheepForm.bindFromRequest();
		if (sheepForm.hasErrors()) {
			Logger.debug(sheepForm.errorsAsJson().asText());
			return badRequest(sheepForm.errorsAsJson());
		} else {
			models.Sheep sheep = sheepForm.get();
			sheep.user = User.find.ref(Long.valueOf(session("userId")));
			sheep.save();
			sheep.refresh();
			jsonPrepare(sheep);
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

	/**
	 * Handles requests for updating a Sheep
	 * 
	 * @param id The sheep ID to update
	 * @return The ID that was updated
	 */
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

	/**
	 * Handles requests for deleting a Sheep
	 * 
	 * @param id The sheep ID to delete
	 * @return The ID that was deleted
	 */
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

	/**
	 * Handles requests for a list of all current positions for all sheep
	 * 
	 * @return A list of the most recent event for every sheep
	 */
	public static Result positions() {
		List<models.Sheep> sheep = models.Sheep.findByUserId(Long.valueOf(session("userId")));
		List<models.Event> events = new ArrayList<models.Event>();
		for (models.Sheep s : sheep) {
			List<models.Event> sheepEvents = models.Event.findBySheepIdLimit(s.id, 1);
			if (!sheepEvents.isEmpty()) {
				models.Event event = sheepEvents.get(0);
				Event.jsonPrepare(event);
				events.add(event);
			}
		}
		return ok(Json.toJson(events));
	}
}