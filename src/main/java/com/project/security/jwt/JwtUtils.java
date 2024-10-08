package com.project.security.jwt;

import com.project.security.service.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${backendapi.app.jwtExpirationMs}")
    private Long jwtExpirationMs;

    @Value("${backendapi.app.jwtSecret}")
    private String jwtSecret;

    //Note: Generate JWT Token ********************
    public String generateJwtToken(Authentication authentication){

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return generateJwtTokenFromUsername(userDetails.getUsername());
    }

    public String generateJwtTokenFromUsername(String username){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime()+jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512,jwtSecret)
                .compact();
    }

    //Note: Validate JWT Token ********************

    public boolean validateJwtToken(String jwtToken){
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwtToken);
            return true;
        } catch (ExpiredJwtException e) {
           LOGGER.error("Jwt token is expired : {}",e.getMessage());
        } catch (UnsupportedJwtException e) {
            LOGGER.error("Jwt token is unsupported : {}",e.getMessage());
        } catch (MalformedJwtException e) {
            LOGGER.error("Jwt token is invalid : {}",e.getMessage());
        } catch (SignatureException e) {
            LOGGER.error("Jwt token is invalid : {}",e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.error("Jwt  is empty : {}",e.getMessage());
        }
        return false;
    }

    //Note: getUsernameFromJWT ********************
    public String getUsernameFromJwtToken(String jwtToken){
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(jwtToken)
                .getBody()
                .getSubject();
    }


}
