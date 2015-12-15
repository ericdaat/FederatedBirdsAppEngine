package fr.ecp.sio.twitterAppEngine.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.ecp.sio.twitterAppEngine.data.UsersRepository;
import fr.ecp.sio.twitterAppEngine.gson.GsonFactory;
import fr.ecp.sio.twitterAppEngine.model.User;
import fr.ecp.sio.twitterAppEngine.utils.TokenUtils;
import fr.ecp.sio.twitterAppEngine.utils.ValidationUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Eric on 20/11/15.
 */
public class JsonServlet extends HttpServlet {

    protected static final Pattern AUTHORIZATION_PATTERN = Pattern.compile("Bearer (.+)");
    protected static Logger LOG = Logger.getLogger(JsonServlet.class.getSimpleName());

    @Override
    protected final void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Object response = null;
        try {
            response = doGet(req);
            sendResponse(response, resp);
        } catch (ApiException e) {
            resp.setStatus(e.getError().status);
            sendResponse(e.getError(), resp);
        }
    }

    protected Object doGet (HttpServletRequest req) throws ServletException, IOException, ApiException{
        return null;
    }

    @Override
    protected final void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            Object response = doPost(req);
            sendResponse(response,resp);
        } catch (ApiException e) {
            resp.setStatus(e.getError().status);
            sendResponse(e.getError(), resp);
        }
    }

    protected Object doPost(HttpServletRequest req)
            throws ServletException, IOException, ApiException {
        return null;
    }

    @Override
    protected final void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            Object response = doDelete(req);
            sendResponse(response,resp);
        } catch (ApiException e) {
            resp.setStatus(e.getError().status);
            sendResponse(e.getError(), resp);
        }
    }

    protected Object doDelete(HttpServletRequest req)
            throws ServletException, IOException, ApiException {
        return null;
    }

    private void sendResponse(Object response, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        GsonFactory.getGson().toJson(response,resp.getWriter());
    }

    protected User getAuthenticatedUser(HttpServletRequest req) throws ApiException {
        String auth = req.getHeader("Authorization");
        if (auth != null){
            Matcher m = AUTHORIZATION_PATTERN.matcher(auth);
            if (!m.matches()){
                throw new ApiException(401,"invalidAuthorization","Invalid token");
            }
            long id = TokenUtils.parseToken(m.group(1));
            return UsersRepository.getUser(id);
        } else {
            return null;
        }
    }

    protected static JsonObject getJsonRequestBody(HttpServletRequest req) throws IOException {
        return new JsonParser()
                .parse(req.getReader())
                .getAsJsonObject();
    }

    protected static <T> T getJsonRequestBody(HttpServletRequest req, Class<T> type) throws IOException {
        return GsonFactory.getGson().fromJson(req.getReader(), type);
    }

    protected User getUserFromRequest(HttpServletRequest req) throws ApiException {
        /**
         * this function will return the targeted user from request url. eg :
         * from /users/1 : we return getUser(1)
         * from /users/me : we return the logged in user
         */

        String pathInfo = req.getPathInfo().substring(1);

        if (pathInfo.equals("me")){
            User me = getLoggedInUser(req);
            /**
             * if user doesn't know his id, he can use me keyword
             * we look at his token to get his id and then return him as user
             */

            return me;

        } else if (ValidationUtils.validateId(pathInfo)){
            /**
             * if request is on an user id, simply find the corresponding user
             * and return it
             */
            long id = Long.parseLong(pathInfo);
            return UsersRepository.getUser(id);
        } else {
            throw new ApiException(400,"wrongId","id is not valid");
        }
    }

    protected User getLoggedInUser (HttpServletRequest req) throws ApiException {
        User me = getAuthenticatedUser(req);
        if (me == null){
            throw new ApiException(400,"loggedInError","You are not logged in");
        } else {
            return me;
        }
    }

    protected boolean verifyUserPermission (HttpServletRequest req) throws ApiException {

        User me = getLoggedInUser(req);
        User target = getUserFromRequest(req);

        return (me.equals(target));
    }

    protected Map<String, String> getRequestParams(HttpServletRequest req){

        String query = req.getQueryString();

        if (query != null){
            String[] params = query.split("&");

            Map<String, String> paramsMap = new HashMap<String, String>();
            for (String param : params)
            {
                String name = param.split("=")[0];
                String value = param.split("=")[1];
                paramsMap.put(name, value);
            }

            return paramsMap;
        } else {
            return null;
        }
    }
}