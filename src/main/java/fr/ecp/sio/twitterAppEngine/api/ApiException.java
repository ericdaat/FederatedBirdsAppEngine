package fr.ecp.sio.twitterAppEngine.api;

import fr.ecp.sio.twitterAppEngine.model.Error;

/**
 * Created by Eric on 20/11/15.
 */
public class ApiException extends Exception {
    private Error error;

    public ApiException(int status, String code, String message){
        super(message);
        Error error = new Error();
        this.error.status = status;
        this.error.code = code;
        this.error.message = message;
    }

    public Error getError () {
        return this.error;
    }

}
