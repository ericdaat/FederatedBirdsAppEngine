package fr.ecp.sio.twitterAppEngine.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

import java.security.Key;


/**
 * Created by Eric on 20/11/15.
 */
public class TokenUtils {

    private static final Key KEY = MacProvider.generateKey();

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
