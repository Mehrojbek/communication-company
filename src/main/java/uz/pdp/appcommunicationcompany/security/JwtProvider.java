package uz.pdp.appcommunicationcompany.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import uz.pdp.appcommunicationcompany.entity.Role;


import java.util.Collection;
import java.util.Date;
import java.util.Set;


@Component
public class JwtProvider {
    private final String SECRET_KEY="secret";
    private final Long EXPIRE_TIME=1000 * 60 * 60 *24l;

    public String generateToken(String username, Collection<? extends GrantedAuthority> roles){
        Date expireDate=new Date(System.currentTimeMillis()+EXPIRE_TIME);
        String token = Jwts
                .builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .claim("roles",roles)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
        return token;
    }



    public String getUsername(String  token){
       try {
           String username = Jwts
                   .parser()
                   .setSigningKey(SECRET_KEY)
                   .parseClaimsJws(token)
                   .getBody()
                   .getSubject();
           return username;
       }catch (Exception e){
           return null;
       }
    }
}
