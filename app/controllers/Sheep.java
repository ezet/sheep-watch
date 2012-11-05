package controllers;

import play.mvc.Controller;
import play.mvc.Result;

public class Sheep extends Controller {
	
	  public static Result index() {
		  return TODO;
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
