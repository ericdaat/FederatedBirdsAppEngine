package fr.ecp.sio.twitterAppEngine.api;

import com.googlecode.objectify.ObjectifyFactory;
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
public class UsersServlet extends JsonServlet {
    @Override
    protected Object doGet(HttpServletRequest req) {
        return UsersRepository.getUsers();
    }

    @Override
    protected Object doPost(HttpServletRequest req) throws IOException, ApiException {
        User user = getJsonParameters(req,User.class);

        //check validity before creating user
        if(!ValidationUtils.validateLogin(user.login)){
            throw new ApiException(400,"invalidLogin","Login did not match the specs");
        }

        if(!ValidationUtils.validatePassword(user.password)){
            throw new ApiException(400,"invalidPassword","Password did not match the specs");
        }

        if(!ValidationUtils.validateEmail(user.email)){
            throw new ApiException(400,"invalidEmail","Invalid Email");
        }

        if(UsersRepository.getUserByLogin(user.login) != null){
            throw new ApiException(400,"duplicateLogin","Login is already existing");
        }

        if(UsersRepository.getUserByEmail(user.email) != null){
            throw new ApiException(400,"duplicateEmail","Email is already existing");
        }

        //get unique id and password hash
        user.id = new ObjectifyFactory().allocateId(User.class).getId();
        user.password = DigestUtils.sha256Hex(user.password + user.id);

        //save user
        long id = UsersRepository.insertUser(user);

        //return user's token based on its id
        return TokenUtils.generateToken(id);

    }
}
