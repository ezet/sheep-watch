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
