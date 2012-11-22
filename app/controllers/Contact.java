package controllers;

import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

/**
 * Handles all REST requests for Contacts. All requests require a valid session and user ID.
 * 
 * @author Lars Kristian
 * 
 */
@Security.Authenticated(Auth.class)
public class Contact extends Controller {

	/**
	 * Prepares a Contact for JSON to avoid infinite cycles
	 * 
	 * @param contact The contact to prepare
	 */
	public static void jsonPrepare(models.Contact contact) {
		contact.userId = contact.user.id;
		contact.user = null;
	}

	/**
	 * Handles requests for a list of all contacts
	 * 
	 * @return The contact list as json
	 */
	public static Result list() {
		return TODO;
	}

	/**
	 * Handles requests a specific contact
	 * 
	 * @param id The contact ID to find
	 * @return The contact as json
	 */
	public static Result show(Long id) {
		return TODO;
	}

	/**
	 * Handles requests for adding a new contact
	 * 
	 * @return The added contact as json if successful, else errors as json
	 */
	public static Result add() {
		Form<models.Contact> form = form(models.Contact.class).bindFromRequest();
		if (form.hasErrors()) {
			Logger.debug("errors");
			return badRequest(form.errorsAsJson());
		} else {
			models.Contact contact = form.get();
			contact.user = models.User.find.ref(Long.valueOf(session("userId")));
			try {
				contact.save();
				contact.refresh();
			} catch (Exception e) {
				return badRequest();
			}
			jsonPrepare(contact);
			return ok(Json.toJson(contact));
		}
	}

	/**
	 * Handles requests for deleting a user
	 * 
	 * @param id The user ID to delete
	 * @return The id deleted if successful, or a bad request otherwise
	 */
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
		return ok(Json.toJson(id));
	}

	/**
	 * Handles requests for updating a user
	 * 
	 * @param id The ID to update
	 * @return The ID that was updated if successful, otherwise a bad request
	 */
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
