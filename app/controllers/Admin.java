package controllers;

import java.util.List;

import models.Login;
import models.User;

import org.codehaus.jackson.JsonNode;

import play.Routes;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.admin.cp;
import views.html.admin.login;
import views.html.admin.userlist;

/**
 * Handles all admin requests
 * 
 * @author Lars Kristian
 * 
 */
public class Admin extends Controller {

	/**
	 * Handles requests for /admin. Redirect to main page if already logged in, or login page otherwise
	 * 
	 * @return Result
	 */
	public static Result index() {
		if (session("admin") != null) {
			return redirect(routes.Admin.cp());
		}
		return redirect(routes.Admin.login());
	}

	/**
	 * The main page
	 * 
	 * @return Result
	 */
	public static Result cp() {
		return ok(cp.render(form(User.class), User.findAll()));
	}

	/**
	 * Handles login requests, redirect to admin page if already logged in
	 * 
	 * @return Result
	 */
	public static Result login() {
		if (session("admin") != null) {
			return redirect(routes.Admin.cp());
		}
		return ok(login.render(form(Login.class)));
	}

	/**
	 * Handles authentication. Redirects to main page if successful, or back to login if not.
	 * 
	 * @return Result
	 */
	public static Result authenticate() {
		Form<Login> loginForm = form(Login.class).bindFromRequest();
		if (loginForm.hasErrors()) {
			return ok(login.render(loginForm));
		} else {
			session("admin", loginForm.get().username);
			return redirect(routes.Admin.cp());
		}
	}

	/**
	 * Handles logout requests
	 * 
	 * @return Login page
	 */
	public static Result logout() {
		session().clear();
		flash("succes", "You have been logged out.");
		return redirect(routes.Admin.login());
	}

	/**
	 * Finds a list of all users
	 * 
	 * @return
	 */
	public static Result list() {
		return ok(userlist.render(User.findAll()));
	}

	/**
	 * @param id
	 * @return
	 */
	public static Result show(long id) {
		return TODO;
	}

	/**
	 * Handles requests to add a new user
	 * 
	 * @return Result
	 */
	public static Result add() {
		Form<User> form = form(User.class).bindFromRequest();
		if (form.hasErrors()) {
			return badRequest(form.errorsAsJson());
		}
		User user = form.get();
		user.save();
		return redirect(routes.Admin.cp());
	}

	/**
	 * Handles requests to update user information
	 * 
	 * @param id The user ID to update
	 * @return
	 */
	public static Result update(long id) {
		return TODO;
	}

	/**
	 * Finds a list of all users
	 * 
	 * @return
	 */
	public static Result listUsers() {
		List<models.User> users = models.User.findAll();
		return ok();
	}

	/**
	 * Handles requests for deleting a specific user
	 * 
	 * @param id User ID to delete
	 * @return
	 */
	public static Result delete(long id) {
		User user = new User();
		user.id = id;
		user.delete();
		return ok();
	}

	/**
	 * Returns a javascript with all allowed JS routes.
	 * 
	 * @return
	 */
	public static Result javascriptRoutes() {
		response().setContentType("text/javascript");
		return ok(Routes.javascriptRouter("jsAdminRoutes",

		// Admin routes
				controllers.routes.javascript.Admin.add(), controllers.routes.javascript.Admin.listUsers()

		));
	}
}
