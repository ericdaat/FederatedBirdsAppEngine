package fr.ecp.sio.twitterAppEngine.api;

import com.google.appengine.repackaged.com.google.gson.Gson;
import com.google.appengine.repackaged.com.google.gson.JsonObject;
import com.google.appengine.repackaged.com.google.gson.JsonParser;
import fr.ecp.sio.twitterAppEngine.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Eric on 20/11/15.
 */
public class JsonServlet extends HttpServlet {

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
        Object response = doPost(req);
        sendResponse(response,resp);
    }

    protected Object doPost(HttpServletRequest req) {
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
        new Gson().toJson(response,resp.getWriter());
    }

    protected User getAuthenticatedUser(HttpServletRequest req){
        String auth = req.getHeader("Authorization");
        if (auth != null){
            long id = 2;
            return null;
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
        return new Gson().fromJson(
                new InputStreamReader(req.getInputStream()),
                type);
    }
}
