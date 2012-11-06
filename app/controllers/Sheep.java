package controllers;

import java.util.List;

import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class Sheep extends Controller {
	
	  public static Result index() {
		  List<models.Sheep> list = models.Sheep.findByProducerId(Long.valueOf(session("producerId")));
		  
		  for (models.Sheep sheep : list) {
			  sheep.producer = null;
			  sheep.events = null;
		  }
		  return ok(Json.toJson(list));
	  }
	  
	  public static Result add() {
		  
		  return TODO;
	  }
	  
	  public static Result show(Long id) {
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
