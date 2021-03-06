package fr.ecp.sio.twitterAppEngine.api;

import fr.ecp.sio.twitterAppEngine.data.UsersRepository;
import fr.ecp.sio.twitterAppEngine.data.UsersRepository.UsersList;
import fr.ecp.sio.twitterAppEngine.model.User;
import fr.ecp.sio.twitterAppEngine.utils.MD5Utils;
import fr.ecp.sio.twitterAppEngine.utils.TokenUtils;
import fr.ecp.sio.twitterAppEngine.utils.ValidationUtils;
import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Eric on 20/11/15.
 */

public class UsersServlet extends JsonServlet {

    private static String FOLLOWEROF = "followerOf";
    private static String FOLLOWEDBY = "followedBy";

    // A GET request should return a list of users
    @Override
    protected UsersList doGet(HttpServletRequest req)
            throws ServletException, IOException, ApiException {

        Map <String, String> paramsMap = getRequestParams(req);

        UsersList users = null;

        if (paramsMap != null){
            long id;
            int limit = 20;
            //int limit = Integer.parseInt(req.getHeader("limit"));
            //String continuationToken = req.getHeader("continuationToken");

            if (paramsMap.containsKey(FOLLOWEROF)){
                String value = paramsMap.get(FOLLOWEROF);
                if (value.equals("me")) {
                    id = getLoggedInUser(req).id;
                } else if (ValidationUtils.validateId(value)){
                        id = Long.parseLong(value);
                } else {
                    throw new ApiException(400,"wrongID","ID doesn't match the specs");
                }
                users = UsersRepository.getUserFollowing(id,limit,"dummyCursor");

            } else if (paramsMap.containsKey(FOLLOWEDBY)) {
                String value = paramsMap.get(FOLLOWEDBY);
                if (value.equals("me") ) {
                    id = getLoggedInUser(req).id;
                } else if (ValidationUtils.validateId(value)){
                    id = Long.parseLong(value);
                } else {
                    throw new ApiException(400,"wrongID","ID doesn't match the specs");
                }
                users = UsersRepository.getUserFollowers(id,20,"dummyCursor");
            }
        } else {
            users = UsersRepository.getUsers(20,"dummyCursor");
        }

        return users;
    }

    // A POST request can be used to create a user
    // We can use it as a "register" endpoint; in this case we return a token to the client.
    @Override
    protected String doPost(HttpServletRequest req) throws ServletException, IOException, ApiException {

        // The request should be a JSON object describing a new user
        User user = getJsonRequestBody(req, User.class);
        if (user == null) {
            throw new ApiException(400, "invalidRequest", "Invalid JSON body");
        }
        // Perform all the usual checks
        if (!ValidationUtils.validateLogin(user.login)) {
            throw new ApiException(400, "invalidLogin", "Login did not match the specs");
        }
        if (!ValidationUtils.validatePassword(user.password)) {
            throw new ApiException(400, "invalidPassword", "Password did not match the specs");
        }
        if (!ValidationUtils.validateEmail(user.email)) {
            throw new ApiException(400, "invalidEmail", "Invalid email");
        }
        if (UsersRepository.getUserByLogin(user.login) != null) {
            throw new ApiException(400, "duplicateLogin", "Duplicate login");
        }
        if (UsersRepository.getUserByEmail(user.email) != null) {
            throw new ApiException(400, "duplicateEmail", "Duplicate email");
        }

        user.avatar = "http://www.gravatar.com/avatar/" + MD5Utils.md5Hex(user.email) + "?d=wavatar";

        // Explicitly give a fresh id to the user (we need it for next step)
        user.id = UsersRepository.allocateNewId();

        // Hash the user password with the id a a salt
        user.password = DigestUtils.sha256Hex(user.password + user.id);

        // Persist the user into the repository
        UsersRepository.saveUser(user);

        // Create and return a token for the new user
        return TokenUtils.generateToken(user.id);

    }

}
