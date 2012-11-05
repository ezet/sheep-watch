package controllers;

import models.Login;
import play.Routes;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.application.*;

public class Application extends Controller {

	public static Result index() {
		if (session("username") != null) {
			return redirect(routes.Application.app());
		}
		return redirect(routes.Application.login());
	}

	public static Result app() {
		if (session("username") == null) {
			return redirect(routes.Application.index());
		}
		return ok(index.render(null));
	}

	public static Result settings() {
		return ok(settings.render("settings"));
	}

	public static Result login() {
		if (session("username") != null) {
			return redirect(routes.Application.app());
		}
		return ok(login.render(form(Login.class)));
	}

	public static Result authenticate() {
		Form<Login> loginForm = form(Login.class).bindFromRequest();
		if (loginForm.hasErrors()) {
			return badRequest(login.render(loginForm));
		} else {
			session("username", loginForm.get().username);
			session("producerId", String.valueOf(loginForm.get().user.producerId));
			return redirect(routes.Application.app());
		}
	}

	public static Result logout() {
		session().clear();
		flash("succes", "You have been logged out.");
		return redirect(routes.Application.login());
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

	public static Result javascriptRoutes() {
		response().setContentType("text/javascript");
		return ok(Routes.javascriptRouter("jsRoutes",

				// Routes for Sheep
				controllers.routes.javascript.Sheep.index(),
				controllers.routes.javascript.Sheep.add(),
				controllers.routes.javascript.Sheep.delete()
				
		));
	}

}