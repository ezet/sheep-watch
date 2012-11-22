package controllers;

import play.mvc.Security;
import play.mvc.Http.Context;

/**
 * Utility class for handling authentication and authorization
 * 
 * @author Lars Kristian
 * 
 */
public class Auth extends Security.Authenticator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see play.mvc.Security.Authenticator#getUsername(play.mvc.Http.Context)
	 */
	@Override
	public String getUsername(Context ctx) {
		return ctx.session().get("userId");
	}

	/**
	 * Checks if current user is the owner of provided sheep id
	 * 
	 * @param sheepId The sheep ID to check
	 * @return true if current user is the owner, false otherwise
	 */
	public static boolean isOwnerOfSheep(Long sheepId) {
		return models.Sheep.isOwner(sheepId, Context.current().request().username());
	}

	/**
	 * Checks if the current user is the owner of provided contact id
	 * 
	 * @param contactId The contact ID to check
	 * @return true of current user is the owner, false otherwise
	 */
	public static boolean isOwnerOfContact(Long contactId) {
		return models.Contact.isOwner(contactId, Context.current().request().username());
	}

	/**
	 * Checks if the current user is the owner of provided event id
	 * 
	 * @param eventId The event ID to check
	 * @return true if the current user is the owner, false otherwise
	 */
	public static boolean isOwnerOfEvent(Long eventId) {
		return models.Event.isOwner(eventId, Context.current().request().username());
	}

}
