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

    /**
     * Get all the users from the datastore (usage???)
     * @param limit The maximum number of items to retrieve, optional
     * @param cursor Optional cursor to get the next items
     * @return All users
     */
    public static UsersList getUsers(Integer limit, String cursor) {
        return new UsersList(
                ObjectifyService.ofy()
                        .load()
                        .type(User.class)
                        .list(),
                "dummyCursor"
        );
    }

    public static long allocateNewId() {
        // Sometime we need to allocate an id before persisting, the library allows it
        return new ObjectifyFactory().allocateId(User.class).getId();
    }

    /**
     * Persist a user into the datastore
     * @param user The user to save
     */
    public static void saveUser(User user) {
        user.id = ObjectifyService.ofy()
                .save()
                .entity(user)
                .now()
                .getId();
    }

    /**
     * @param id The id of the user to remove
     */
    public static void deleteUser(long id) {
        ObjectifyService.ofy()
                .delete()
                .type(User.class)
                .id(id)
                .now();
    }

    /**
     * @param id The id of the user
     * @param limit The maximum number of items to retrieve, optional
     * @param cursor Optional cursor to get the next items
     * @return A list of users with optionally a cursor
     */
    public static UsersList getUserFollowing(long id, Integer limit, String cursor) {
        return getUsers(limit, cursor);
    }

    /**
     * @param id The id of the user
     * @param limit The maximum number of items to retrieve, optional
     * @param cursor Optional cursor to get the next items
     * @return A list of users with optionally a cursor
     */
    public static UsersList getUserFollowers(long id, Integer limit, String cursor) {
        return getUsers(limit, cursor);
    }

    /**
     * @param followerId The follower id
     * @param followedId The followed id
     * @param follow true to follow, false to unfollow
     */
    public static void setUserFollowed(long followerId, long followedId, boolean follow) {
        User follower = getUser(followerId);
        User followed = getUser(followedId);
        /*
        if (follow){
            followed.followers.add(follower);
        } else {
            followed.followers.remove(follower);
        }
        */
    }

    /**
     * A list of users, with optionally a cursor to get the next items
     */
    public static class UsersList {

        public final List<User> users;
        public final String cursor;

        private UsersList(List<User> users, String cursor) {
            this.users = users;
            this.cursor = cursor;
        }
    }
}
