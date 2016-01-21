package fr.ecp.sio.twitterAppEngine.api;

import fr.ecp.sio.twitterAppEngine.data.UsersRepository;
import fr.ecp.sio.twitterAppEngine.model.User;
import fr.ecp.sio.twitterAppEngine.utils.ValidationUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * Created by Eric on 20/11/15.
 */
public class UserServlet extends JsonServlet{

    // A GET request should simply return the user
    @Override
    protected User doGet(HttpServletRequest req) throws ServletException, IOException, ApiException {
        return getUserFromRequest(req);
    }

    // A POST request could be used by a user to edit its own account
    // It could also handle relationship parameters like "followed=true"
    @Override
    protected User doPost(HttpServletRequest req) throws ServletException, IOException, ApiException {

        //get the logged in user
        User me = getLoggedInUser(req);

        //get parameters from request. Here, it would only contain follow = true or false
        Map<String, String> paramsMap = getRequestParams(req);

        if (paramsMap != null && paramsMap.containsKey("follow")) {
            /**
             * Handle following/unfollowing a user here
             * get follow parameter, parse it as boolean, and follow or unfollow user accordingly
             */
            String param = paramsMap.get("follow");
            User target = getUserFromRequest(req);

            if (me.id == target.id) {
                //a user can't follow/unfollow himself
                throw new ApiException(400,"followError","You can't follow/unfollow yourself");
            }

            UsersRepository.setUserFollowed(
                    me,
                    target,
                    Boolean.parseBoolean(param)
            );

        } else if (verifyUserPermission(req)){
            /**
             * Handle any other account modification here
             * if the user is trying to modify his own account, let him do it
             */
        } else {
            //the user can't modify someone else's account
            throw new ApiException(400,"unauthorized","Can't modify someone else's account");
        }
        //return the modified user
        return me;
    }

    // A user can DELETE its own account
    @Override
    protected Void doDelete(HttpServletRequest req) throws ServletException, IOException, ApiException {

        if (verifyUserPermission(req)){
            // TODO: Delete the user, the messages, the relationships
        }

        // A DELETE request shall not have a response body
        return null;
    }
}
