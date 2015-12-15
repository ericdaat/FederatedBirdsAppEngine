package fr.ecp.sio.twitterAppEngine.api;

import com.google.gson.JsonObject;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.channels.Channels;

import com.google.appengine.tools.cloudstorage.*;
import fr.ecp.sio.twitterAppEngine.model.User;

/**
 * Created by Eric on 15/12/15.
 */
public class UploadServlet extends JsonServlet {

    private static String PATH = "path";
    private static String BUCKETNAME = "federatedbirds";


    private final GcsService gcsService =
            GcsServiceFactory.createGcsService(RetryParams.getDefaultInstance());

    @Override
    protected String doPost(HttpServletRequest req) throws ServletException, IOException, ApiException {
        User me = getLoggedInUser(req);
        JsonObject body = getJsonRequestBody(req);
        String path;
        
        if (body.has(PATH)){
            path = body.get(PATH).getAsString();
        } else {
            throw new ApiException(400,"bodyError","Must specify a path");
        }

        BufferedImage avatar = ImageIO.read(new File(path));

        GcsFilename fileName = new GcsFilename(BUCKETNAME,me.login + "_avatar");
        GcsOutputChannel outputChannel =
                gcsService.createOrReplace(fileName, GcsFileOptions.getDefaultInstance());

        @SuppressWarnings("resource")
        ObjectOutputStream oout =
                new ObjectOutputStream(Channels.newOutputStream(outputChannel));
        oout.writeObject(avatar);
        oout.close();

        return null;
    }
}
