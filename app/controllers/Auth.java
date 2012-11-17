package controllers;

import play.mvc.Security;
import play.mvc.Http.Context;

public class Auth extends Security.Authenticator {
	
    @Override
    public String getUsername(Context ctx) {
        return ctx.session().get("userId");
    }
    
    public static boolean isOwnerOfSheep(Long sheepId) {
    	return models.Sheep.isOwner(sheepId, Context.current().request().username());
    }
    
    public static boolean isOwnerOfContact(Long contactId) {
    	return models.Contact.isOwner(contactId, Context.current().request().username());
    }
    
    public static boolean isOwnerOfEvent(Long eventId) {
    	return models.Event.isOwner(eventId, Context.current().request().username());
    }
    
    
	
	
}
