package fr.ecp.sio.twitterAppEngine.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Eric on 27/11/15.
 */
public class GsonFactory {

    public static final String ISO_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    public static Gson getGson() {
        return new GsonBuilder()
                .disableHtmlEscaping()
                .registerTypeAdapterFactory(new RefAdapterFactory())
                .setDateFormat(ISO_DATE_FORMAT)
                .create();
    }

}
