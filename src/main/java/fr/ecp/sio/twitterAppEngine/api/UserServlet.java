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
        //starting with a null user
        User user = null;
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

    // A POST request could be used by a user to edit its own account
    // It could also handle relationship parameters like "followed=true"
    @Override
    protected User doPost(HttpServletRequest req) throws ServletException, IOException, ApiException {
        // TODO: Get the user as below
        // TODO: Apply some changes on the user (after checking for the connected user)
        // TODO: Handle special parameters like "followed=true" to create or destroy relationships
        // TODO: Return the modified user
        return null;
    }

    // A user can DELETE its own account
    @Override
    protected Void doDelete(HttpServletRequest req) throws ServletException, IOException, ApiException {
        // TODO: Security checks
        // TODO: Delete the user, the messages, the relationships
        // A DELETE request shall not have a response body
        return null;
    }
}
