package controllers;

import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

@Security.Authenticated(Auth.class)
public class Contact extends Controller {

	public static Result list() {
		return TODO;
	}

	public static Result show(Long id) {
		return TODO;
	}

	public static Result add() {
		Form<models.Contact> form = form(models.Contact.class).bindFromRequest();
		if (form.hasErrors()) {
			Logger.debug("errors");
			return badRequest(form.errorsAsJson());
		} else {
			models.Contact contact = form.get();
			contact.user = models.User.find.ref(Long.valueOf(session("producerId")));
			try {
				contact.save();
				contact.refresh();
			} catch (Exception e) {
				return badRequest();
			}
			return ok(String.valueOf(contact.id));
		}
	}

	public static Result delete(Long id) {
		if (!Auth.isOwnerOfContact(id)) {
			return unauthorized();
		}
		models.Contact contact = models.Contact.find.ref(id);
		try {
			contact.delete();
		} catch (Exception e) {
			return badRequest();
		}
		return ok();
	}

	public static Result update(Long id) {
		if (!Auth.isOwnerOfContact(id)) {
			return unauthorized();
		}
		Form<models.Contact> form = form(models.Contact.class).bindFromRequest();
		if (form.hasErrors()) {
			return badRequest(form.errorsAsJson());
		} else {
			models.Contact contact = form.get();
			try {
				contact.update(id);
			} catch (Exception e) {
				return badRequest();
			}
			return ok(String.valueOf(id));
		}

	}

}
