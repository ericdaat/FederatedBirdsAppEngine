package fr.ecp.sio.twitterAppEngine.model;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Load;

import java.util.Date;

/**
 * Created by Eric on 20/11/15.
 */
@Entity
public class Message {
    @Id
    public Long id;
    public String text;
    public Date date;

    @Load
    public Ref<User>user;
}
