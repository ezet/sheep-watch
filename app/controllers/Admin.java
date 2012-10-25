package controllers;

import models.Login;
import models.User;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.admin.cp;
import views.html.admin.login;
import views.html.admin.userlist;

public class Admin extends Controller {

	public static Result index() {
		if (session("admin") != null) {
			return redirect(routes.Admin.cp());
		}
		return redirect(routes.Admin.login());
	}
	
	public static Result cp() {
		return ok(cp.render(form(User.class), User.findAll()));
	}

	public static Result login() {
		if (session("admin") != null) {
			return redirect(routes.Admin.cp());
		}
		return ok(login.render(form(Login.class)));
	}

	public static Result authenticate() {
		Form<Login> loginForm = form(Login.class).bindFromRequest();
		if (loginForm.hasErrors()) {
			return badRequest(login.render(loginForm));
		} else {
			session("admin", loginForm.get().username);
			return redirect(routes.Admin.cp());
		}
	}

	public static Result logout() {
		session().clear();
		flash("succes", "You have been logged out.");
		return redirect(routes.Admin.login());
	}
	
	public static Result list() {
		return ok(userlist.render(User.findAll())); 
	}
	
	public static Result show(long id) {
		return TODO;
	}
	
	public static Result add() {
		User user = form(User.class).bindFromRequest().get();
		user.save();
		return redirect(routes.Admin.cp());
		
	}
	
	public static Result update(long id) {
		return TODO;
	}
	
	public static Result delete(long id) {
		return TODO;
	}
}
