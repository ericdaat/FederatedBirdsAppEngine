package fr.ecp.sio.twitterAppEngine.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.ecp.sio.twitterAppEngine.gson.GsonFactory;
import fr.ecp.sio.twitterAppEngine.model.User;
import fr.ecp.sio.twitterAppEngine.utils.TokenUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Created by Eric on 20/11/15.
 */
public class JsonServlet extends HttpServlet {

    @Override
    protected final void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Object response = null;
        try {
            response = doGet(req);
        } catch (ApiException e) {
            resp.setStatus(e.getError().status);
            sendResponse(e.getError(), resp);
        }
        sendResponse(response,resp);
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
        return TokenUtils.requestToUser(req);
    }

    protected static JsonObject getJsonRequestBody(HttpServletRequest req) throws IOException {
        return new JsonParser()
                .parse(req.getReader())
                .getAsJsonObject();
    }

    protected static <T> T getJsonRequestBody(HttpServletRequest req, Class<T> type) throws IOException {
        return GsonFactory.getGson().fromJson(req.getReader(), type);
    }
}
