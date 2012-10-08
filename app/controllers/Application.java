package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

public class Application extends Controller {
  
  public static Result index() {
    return ok(index.render("index"));
  }
  
  public static Result settings() {
	  return TODO;
  }
  
  public static Result sheep() {
	  return TODO;
  }
  
  public static Result newSheep() {
	  return TODO;
  }
  
  public static Result viewSheep(long id) {
	  return TODO;
  }
  
  public static Result editSheep(long id) {
	  return TODO;
  }
  
  public static Result deleteSheep(long id) {
	  return TODO;
  }
  
  public static Result login() {
	  return TODO;
  }
  
  public static Result logout() {
	  return TODO;
  }
  
  public static Result viewEvent(long id) {
	  return TODO;
  }
  
  public static Result events() {
	  return TODO;
  }
  
}