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
        // TODO: Extract the id of the user from the last part of the path of the request
        // TODO: Check if this id is syntactically correct
        long id = 0;
        // Lookup in repository
        User user = UsersRepository.getUser(id);
        // TODO: Not found?
        // TODO: Add some mechanism to hide private info about a user (email) except if he is the caller
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
