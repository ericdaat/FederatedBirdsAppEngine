package fr.ecp.sio.twitterAppEngine.api;

import fr.ecp.sio.twitterAppEngine.data.UsersRepository;
import fr.ecp.sio.twitterAppEngine.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by Eric on 20/11/15.
 */
public class UserServlet extends JsonServlet{

    // A GET request should simply return the user
    @Override
    protected User doGet(HttpServletRequest req) throws ServletException, IOException, ApiException {
        return getUser(req);
    }

    // A POST request could be used by a user to edit its own account
    // It could also handle relationship parameters like "followed=true"
    @Override
    protected User doPost(HttpServletRequest req) throws ServletException, IOException, ApiException {

        if (verifyUserPermission(req)){
            // TODO: Apply some changes on the user (after checking for the connected user)
            // TODO: Handle special parameters like "followed=true" to create or destroy relationships
            // TODO: Return the modified user
        }

        return null;
    }

    // A user can DELETE its own account
    @Override
    protected Void doDelete(HttpServletRequest req) throws ServletException, IOException, ApiException {
        // TODO: Security checks

        if (verifyUserPermission(req)){
            //delete the user
        }

        // TODO: Delete the user, the messages, the relationships
        // A DELETE request shall not have a response body
        return null;
    }

    private User getUser(HttpServletRequest req) throws ApiException {
        /**
         * this function will return the targeted user from request url. eg :
         * from /users/1 : we return getUser(1)
         * from /users/me : we return getAuthenticatedUser(req)
         */
        User user;
        //get the path and remove the "/" from it
        String pathInfo = req.getPathInfo().substring(1);

        if (pathInfo.equals("me")){
            /**
             * if user doesn't know his id, he can use me keyword
             * we look at his token to get his id and then return him as user
             */
            user = getAuthenticatedUser(req);
        } else {
            /**
             * if request is on an user id, simply find the corresponding user
             * and return it
             */
            long id = Long.parseLong(pathInfo);
            user = UsersRepository.getUser(id);
        }
        return user;
    }

    private boolean verifyUserPermission (HttpServletRequest req) throws ApiException {
        User me = getAuthenticatedUser(req);
        if (me != null){
            /**
             * If me isn't null, user is logged in
             * Now check if the user is indeed
             * trying to modify his own account
             */
            User target = getUser(req);
            return me.id == target.id;
        } else {
            //If me is null, then user is not logged in, return false
            return false;
        }
    }
}
