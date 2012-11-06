package controllers;

import java.util.List;

import org.codehaus.jackson.JsonNode;

import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class Sheep extends Controller {
	
	  public static Result index() {
		  List<models.Sheep> list = models.Sheep.findByProducerId(Long.valueOf(session("producerId")));
		  
		  for (models.Sheep sheep : list) {
			  for (models.Event event: sheep.events) {
				  Logger.debug(String.valueOf(event.id));
			  }
		  }
		  return ok(list.get(0).name);
	  }
	  
	  public static Result add() {
		  
		  return TODO;
	  }
	  
	  public static Result show(long id) {
		  return TODO;
	  }
	  
	  public static Result update(long id) {
		  return TODO;
	  }
	  
	  public static Result delete(long id) {
		  new models.Sheep(id).delete();
		  return ok();
	  }

}
