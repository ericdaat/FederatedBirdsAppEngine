package fr.ecp.sio.twitterAppEngine.model;

import com.google.appengine.api.datastore.Blob;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import fr.ecp.sio.twitterAppEngine.data.UsersRepository.UsersList;

import java.util.List;


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
    public String keyAvatar;
    public String coverPicture;

    @Index
    public String email;
    public String password;

}


