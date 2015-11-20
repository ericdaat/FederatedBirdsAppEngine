package fr.ecp.sio.twitterAppEngine.data;

import com.googlecode.objectify.ObjectifyService;
import fr.ecp.sio.twitterAppEngine.model.User;

/**
 * Created by Eric on 20/11/15.
 */
public class UsersRepository {
    static{
        ObjectifyService.register(User.class);
    }

    public static User getUserByLogin(final String login){
        return ObjectifyService.ofy()
                .load()
                .type(User.class)
                .filter("login",login)
                .first()
                .now();
    }

    public static User getUserByEmail(final String email){
        return ObjectifyService.ofy()
                .load()
                .type(User.class)
                .filter("email",email)
                .first()
                .now();
    }

    public static User getUser(final long id){
        return ObjectifyService.ofy()
                .load()
                .type(User.class)
                .id(id)
                .now();
    }

    public static long insertUser(User user){
        return ObjectifyService.ofy()
                .save()
                .entity(user)
                .now()
                .getId();
    }
}
