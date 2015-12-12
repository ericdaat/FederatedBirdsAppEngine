package fr.ecp.sio.twitterAppEngine.api;

import fr.ecp.sio.twitterAppEngine.model.Error;

/**
 * Created by Eric on 20/11/15.
 */
public class ApiException extends Exception {
    private Error mError;

    public ApiException(int status, String code, String message){
        super(message);
        mError = new Error();
        mError.status = status;
        mError.code = code;
        mError.message = message;
    }

    public Error getError () {
        return mError;
    }

}
