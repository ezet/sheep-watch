package controllers;

import models.Login;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.application.*;

public class Application extends Controller {
	
	public static Result index() {
//		return ok(views.html.application.login.render("Sheep Watch"));
		return TODO;
	}
	
	public static Result app() {
		return ok(index.render(null));
	}
	
	public static Result settings() {
		return ok(settings.render("settings"));
	}
	

	public static Result login() {
		return ok(login.render(form(Login.class)));
	}
	
	public static Result authenticate() {
        Form<Login> loginForm = form(Login.class).bindFromRequest();
        if(loginForm.hasErrors()) {
            return badRequest(login.render(loginForm));
        } else {
            session("email", loginForm.get().username);
            return redirect(
                routes.Application.help()
            );
        }
	}

	public static Result logout() {
		return TODO;
	}
	
	public static Result saveContacts() {
		return TODO;
	}
	
	public static Result search(String term) {
		return TODO;
	}
	
	public static Result help() {
		return TODO;
	}
	
	public static Result about() {
		return TODO;
	}
	
	public static Result contact() {
		return TODO;
	}

}