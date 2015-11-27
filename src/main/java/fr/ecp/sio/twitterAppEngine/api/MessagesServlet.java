package fr.ecp.sio.twitterAppEngine.api;

import com.googlecode.objectify.Ref;
import fr.ecp.sio.twitterAppEngine.data.MessagesRepository;
import fr.ecp.sio.twitterAppEngine.model.Message;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by Eric on 20/11/15.
 */
public class MessagesServlet extends JsonServlet {

    @Override
    protected Object doGet(HttpServletRequest req) {
        return MessagesRepository.getMessages();
    }

    @Override
    protected Object doPost(HttpServletRequest req) throws IOException, ApiException {
        Message message = getJsonParameters(req,Message.class);
        if (message == null){
            throw new ApiException(400,"invalidRequest","Invalid JsonBody");
        }

        message.user = Ref.create(getAuthenticatedUser(req));
        message.id = null;
        message.id = MessagesRepository.insertMessage(message);

        return message;
    }
}
