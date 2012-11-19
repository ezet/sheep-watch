package controllers;

import java.util.ArrayList;
import java.util.List;

import models.Login;
import play.Routes;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.application.dashboard;
import views.html.application.login;

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
		return ok(dashboard.render());
	}
	
	public static Result manage() {
		return ok(views.html.application.manage.render());
	}
	
	public static Result statistics() {
		return TODO;
	}

	public static Result contacts() {
		List<Form<models.Contact>> contactForms = new ArrayList<Form<models.Contact>>();
		List<models.Contact> contacts = models.Contact.findByUserId(Long.valueOf(session("userId")));
		for (models.Contact contact : contacts) {
			Form<models.Contact> contactForm = form("form", models.Contact.class);
			contactForm = contactForm.fill(contact);
			contact = contactForm.get();
			contactForms.add(contactForm);
		}
		return ok(views.html.application.contacts.render(contactForms));
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
			session("userId", String.valueOf(loginForm.get().user.id));
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
				controllers.routes.javascript.Sheep.add(),
				controllers.routes.javascript.Sheep.update(),
				controllers.routes.javascript.Sheep.list(),
				controllers.routes.javascript.Sheep.delete(),
				controllers.routes.javascript.Sheep.show(),

				// Events
				controllers.routes.javascript.Event.list(),
				controllers.routes.javascript.Event.listLimit(),
				controllers.routes.javascript.Event.listBySheep(),
				controllers.routes.javascript.Event.alarmList(),
				controllers.routes.javascript.Event.show(),
				
				// Contacts
				controllers.routes.javascript.Contact.add(),
				controllers.routes.javascript.Contact.delete(),
				controllers.routes.javascript.Contact.update(),
				
				// Misc
				controllers.routes.javascript.Assets.at()
				
		));
	}

}