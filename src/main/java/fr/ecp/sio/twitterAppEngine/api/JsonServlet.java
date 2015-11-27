package fr.ecp.sio.twitterAppEngine.api;



import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.ecp.sio.twitterAppEngine.data.UsersRepository;
import fr.ecp.sio.twitterAppEngine.gson.GsonFactory;
import fr.ecp.sio.twitterAppEngine.model.User;
import fr.ecp.sio.twitterAppEngine.utils.TokenUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.SignatureException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Eric on 20/11/15.
 */
public class JsonServlet extends HttpServlet {

    protected static final Pattern AUTHORIZATION_PATTERN = Pattern.compile("Bearer (.+)");

    @Override
    protected final void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object response = doGet(req);
        sendResponse(response,resp);
    }

    protected Object doGet (HttpServletRequest req) {
        return null;
    }

    @Override
    protected final void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            Object response = doPost(req);
            sendResponse(response,resp);
        } catch (ApiException e) {
            resp.setStatus(e.getError().status);
            sendResponse(e.getError(), resp);
        }
    }

    protected Object doPost(HttpServletRequest req) throws IOException, ApiException {
        return null;
    }

    @Override
    protected final void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object response = doDelete(req);
        sendResponse(response,resp);
    }

    protected Object doDelete(HttpServletRequest req){
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

    protected static JsonObject getJsonParameters(HttpServletRequest req) throws IOException {
        return new JsonParser()
                .parse(
                        new InputStreamReader(req.getInputStream())
                ).getAsJsonObject();
    }

    protected static <T> T getJsonParameters (HttpServletRequest req, Class<T> type) throws IOException {
        return GsonFactory.getGson().fromJson(
                new InputStreamReader(req.getInputStream()),
                type);
    }
}
