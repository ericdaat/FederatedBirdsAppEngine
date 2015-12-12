package fr.ecp.sio.twitterAppEngine.api;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.*;
import com.google.appengine.api.images.*;
import fr.ecp.sio.twitterAppEngine.model.User;

import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * Created by Eric on 10/12/15.
 */
public class UploadServlet extends JsonServlet {

    @Override
    protected String doPost(HttpServletRequest req)
            throws ServletException, IOException, ApiException {

        User me = getAuthenticatedUser(req);

        BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
        ImagesService imagesService = ImagesServiceFactory.getImagesService();

        Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
        List<BlobKey> blobKeys = blobs.get("uploadedFile");

        me.keyAvatar = blobKeys.get(0).getKeyString();

        String imageURL = imagesService.getServingUrl(
                ServingUrlOptions.Builder.withBlobKey(blobKeys.get(0))
        );

        me.avatar = imageURL;

        return imageURL;
    }

}
