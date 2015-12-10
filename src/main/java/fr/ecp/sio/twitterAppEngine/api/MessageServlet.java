package fr.ecp.sio.twitterAppEngine.api;

import fr.ecp.sio.twitterAppEngine.data.MessagesRepository;
import fr.ecp.sio.twitterAppEngine.model.Message;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by Eric on 20/11/15.
 */
public class MessageServlet extends JsonServlet {

    // A GET request should simply return the message
    @Override
    protected Message doGet(HttpServletRequest req) throws ServletException, IOException, ApiException {
        // TODO: Extract the id of the message from the last part of the path of the request
        // TODO: Check if this id is syntactically correct
        long id = RequestUtils.requestToId(req);
        // Lookup in repository
        Message message = MessagesRepository.getMessage(id);
        // TODO: Not found?
        return message;
    }

    // A POST request could be made to modify some properties of a message after it is created
    @Override
    protected Message doPost(HttpServletRequest req) throws ServletException, IOException, ApiException {
        // TODO: Get the message as below
        // TODO: Apply the changes
        // TODO: Return the modified message
        return null;
    }

    // A DELETE request should delete a message (if the user)
    @Override
    protected Void doDelete(HttpServletRequest req) throws ServletException, IOException, ApiException {
        // TODO: Get the message
        // TODO: Check that the calling user is the author of the message (security!)
        // TODO: Delete the message
        // A DELETE request shall not have a response body
        return null;
    }
}
