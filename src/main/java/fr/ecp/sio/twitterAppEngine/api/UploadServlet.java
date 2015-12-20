package fr.ecp.sio.twitterAppEngine.api;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.ByteBuffer;

import com.google.api.client.util.IOUtils;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.appengine.tools.cloudstorage.*;
import fr.ecp.sio.twitterAppEngine.model.User;

/**
 * Created by Eric on 15/12/15.
 */
public class UploadServlet extends JsonServlet {

    private static String BUCKETNAME = "myBucket";



    @Override
    protected String doPost(HttpServletRequest req) throws ServletException, IOException, ApiException {
        User me = getLoggedInUser(req);
        String name = me.login + "_avatar.jpg";

        InputStream is = req.getInputStream();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        ImagesService imagesService = ImagesServiceFactory.getImagesService();

        int nRead;
        byte[] data = new byte[is.available()];

        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();

        GcsFilename fileName = new GcsFilename(BUCKETNAME,name);
        GcsFileOptions options = new GcsFileOptions.Builder()
                .mimeType("image/jpeg")
                .acl("public-read")
                .build();

        GcsService gcsService =
                GcsServiceFactory.createGcsService(RetryParams.getDefaultInstance());

        @SuppressWarnings("resource")
        GcsOutputChannel outputChannel =
                gcsService.createOrReplace(fileName, GcsFileOptions.getDefaultInstance());
        outputChannel.write(ByteBuffer.wrap(data));
        outputChannel.close();

        ServingUrlOptions urlOptions =
                ServingUrlOptions
                        .Builder.withGoogleStorageFileName("/gs/" + BUCKETNAME + "/" + name);

        me.avatar = imagesService.getServingUrl(urlOptions);

        return "image uploaded at " + me.avatar;
    }
}