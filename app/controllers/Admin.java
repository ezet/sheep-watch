package controllers;

import models.Login;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import views.html.application.index;
import views.html.application.login;
import views.html.application.settings;

public class Admin extends Controller {

	public static Result index() {
		if (session("username") != null) {
			return redirect(routes.Admin.cp());
		}
		return redirect(routes.Admin.login());
	}
	
	public static Result cp() {
		return TODO;
	}

	public static Result login() {
		if (session("username") != null) {
			return redirect(routes.Admin.cp());
		}
		return ok(login.render(form(Login.class)));
	}

	public static Result authenticate() {
		Form<Login> loginForm = form(Login.class).bindFromRequest();
		if (loginForm.hasErrors()) {
			return badRequest(login.render(loginForm));
		} else {
			session("username", loginForm.get().username);
			return redirect(routes.Admin.cp());
		}
	}

	public static Result logout() {
		session().clear();
		flash("succes", "You have been logged out.");
		return redirect(routes.Admin.login());
	}
	
	public static Result list() {
		return TODO;
	}
	
	public static Result show(long id) {
		return TODO;
	}
	
	public static Result add() {
		return TODO;
	}
	
	public static Result update(long id) {
		return TODO;
	}
	
	public static Result delete(long id) {
		return TODO;
	}
}
