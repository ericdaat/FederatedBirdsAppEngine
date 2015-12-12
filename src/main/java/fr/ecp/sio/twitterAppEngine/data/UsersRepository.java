package fr.ecp.sio.twitterAppEngine.data;

import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import fr.ecp.sio.twitterAppEngine.model.User;

import java.util.List;

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

    public static UsersList getUsers() {
        return new UsersList(
                ObjectifyService.ofy()
                        .load()
                        .type(User.class)
                        .list(),
                "dummyCursor"
        );
    }

    public static UsersList getUserFollowed(long id, int limit) {
        return getUsers();
    }

    public static UsersList getUserFollowed(String cursor, int limit) {
        return getUsers();
    }

    public static UsersList getUserFollowers(long id) {
        return getUsers();
    }

    public static UsersList getUserFollowers(String cursor, long id) {
        return getUsers();
    }

    public static void setUsersFollowed (long followerId, long followedId, boolean isFollowed){}

    public static long allocateNewId() {
        // Sometimes we need to allocate an id before persisting, the library allows it
        return new ObjectifyFactory().allocateId(User.class).getId();
    }

    public static long insertUser(User user){
        return ObjectifyService.ofy()
                .save()
                .entity(user)
                .now()
                .getId();
    }

    public static class UsersList {

        public final List<User> users;
        public final String cursor;

        private UsersList(List<User> users, String cursor) {
            this.users = users;
            this.cursor = cursor;
        }
    }
}
