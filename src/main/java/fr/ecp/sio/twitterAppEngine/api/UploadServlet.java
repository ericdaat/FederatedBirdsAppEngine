package fr.ecp.sio.twitterAppEngine.api;

import fr.ecp.sio.twitterAppEngine.model.User;
import fr.ecp.sio.twitterAppEngine.utils.TokenUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;



/**
 * Created by Eric on 10/12/15.
 */
public class UploadServlet extends JsonServlet {

    @Override
    protected User doGet(HttpServletRequest req)
            throws ServletException, IOException, ApiException {
        return null;
    }

    @Override
    protected Object doPost(HttpServletRequest req) throws ServletException, IOException, ApiException {
        User me = getAuthenticatedUser(req);



        return null;
    }
}
