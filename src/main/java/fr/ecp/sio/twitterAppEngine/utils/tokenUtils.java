package fr.ecp.sio.twitterAppEngine.utils;

import fr.ecp.sio.twitterAppEngine.api.ApiException;
import fr.ecp.sio.twitterAppEngine.data.UsersRepository;
import fr.ecp.sio.twitterAppEngine.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Eric on 20/11/15.
 */
public class TokenUtils {

    protected static final Pattern AUTHORIZATION_PATTERN = Pattern.compile("Bearer (.+)");

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

    public static User requestToUser(HttpServletRequest req) throws ApiException {
        String auth = req.getHeader("Authorization");
        if (auth != null){
            Matcher m = AUTHORIZATION_PATTERN.matcher(auth);
            if (!m.matches()){
                throw new ApiException(401,"invalidAuthorization","Invalid token");
            }
            long id = TokenUtils.parseToken(m.group(1));
            return UsersRepository.getUser(id);
        } else {
            return null;
        }
    }
}
