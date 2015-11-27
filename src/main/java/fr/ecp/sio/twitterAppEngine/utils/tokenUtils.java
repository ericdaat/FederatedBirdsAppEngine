package fr.ecp.sio.twitterAppEngine.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;


/**
 * Created by Eric on 20/11/15.
 */
public class TokenUtils {

    private static final Key KEY;
    static {
        byte[] key = Base64.decodeBase64("BYWphxmLrblUgy6LzHdUfSActQw2y9SX");
        KEY = new SecretKeySpec(key,0,key.length,"AES");
    }


    public static String generateToken(long userId){
        return Jwts.builder()
                .setId(Long.toString(userId))
                .signWith(SignatureAlgorithm.HS512,KEY)
                .compact();
    }

    public static long parseToken(String token){
        return Long.parseLong(Jwts.parser()
                .setSigningKey(KEY)
                .parseClaimsJws(token)
                .getBody()
                .getId()
        );
    }

}
