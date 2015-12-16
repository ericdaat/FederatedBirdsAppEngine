package fr.ecp.sio.twitterAppEngine.api;

import com.google.common.io.ByteStreams;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


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
        
        if (!body.has(PATH)){
            throw new ApiException(400,"bodyError","Must specify a path");
        }

        String path = body.get(PATH).getAsString();

        InputStream inputStream = UploadServlet.class.getResourceAsStream(path);
        byte[] bytes = ByteStreams.toByteArray(inputStream);

        GcsFilename fileName = new GcsFilename(BUCKETNAME,me.login + "_avatar.jpg");

        GcsFileOptions options = new GcsFileOptions.Builder()
                .mimeType("image/jpeg")
                .acl("public-read")
                .build();

        @SuppressWarnings("resource")
        GcsOutputChannel outputChannel =
                gcsService.createOrReplace(fileName,options);
        outputChannel.write(ByteBuffer.wrap(bytes));
        outputChannel.close();


        return "done";
    }
}
