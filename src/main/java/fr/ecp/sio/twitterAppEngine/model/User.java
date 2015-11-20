package fr.ecp.sio.twitterAppEngine.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;


/**
 * Created by Eric on 20/11/15.
 */
@Entity
public class User {

    @Id
    public long id;

    @Index
    public String login;

    public String avatar;
    public String coverPicture;

    @Index
    public String email;
    public String password;
}
