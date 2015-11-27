package fr.ecp.sio.twitterAppEngine.api;

import com.google.gson.JsonObject;
import fr.ecp.sio.twitterAppEngine.data.UsersRepository;
import fr.ecp.sio.twitterAppEngine.model.User;
import fr.ecp.sio.twitterAppEngine.utils.TokenUtils;
import fr.ecp.sio.twitterAppEngine.utils.ValidationUtils;
import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by Eric on 20/11/15.
 */
public class TokenServlet extends JsonServlet{

    @Override
    protected String doPost(HttpServletRequest req) throws IOException, ApiException {

        //get login and password from request
        JsonObject params = getJsonParameters(req);
        String login = params.get("login").getAsString();
        String password = params.get("password").getAsString();

        if (!ValidationUtils.validateLogin(login)){
            throw new ApiException(400,"invalidLogin","Login did not match the specs");
        }

        if (!ValidationUtils.validatePassword(password)){
            throw new ApiException(400,"invalidPassword","Password did not match the specs");
        }

        //get user from login
        User user = UsersRepository.getUserByLogin(login);
        if(user!=null){
            String hash = DigestUtils.sha256Hex(password + user.id);
            if (hash.equals(user.password)){
                return TokenUtils.generateToken(user.id);
            } else {
                throw new ApiException(403,"invalidPassword","Incorrect Password");
            }
        } else {
            throw new ApiException(403,"invalidLogin","User not found");
        }
    }
}
