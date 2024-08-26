package com.banking.springboot_bank.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

//generate token
@Component
public class JwtTokenProvider {
    @Value("${app.jwt-secret}")
    private String jwtSecret;

    @Value("${app.jwt-expiration}")
    private long jwtExpirationDate;

    public String generateToken(Authentication authentication){
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);
// Builds the JWT token with the subject (username), issued date, expiration date, and signs it with a key.
        return Jwts.builder().
                //Extracts the subject (which is the username in this case) from the claims.
                setSubject(username).
                setIssuedAt(currentDate).
                setExpiration(expireDate).
                signWith(key())
                .compact();
    }

    //decode jwtSecret key
    private Key key(){
        byte[] bytes = Decoders.BASE64.decode(jwtSecret);
        //Generates a signing key using the HMAC SHA algorithm, which is used to sign the JWT
        return Keys.hmacShaKeyFor(bytes);
    }
    //get username extracted from the token
    public String getUsername(String token){
        Claims claims = Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token){
        //Parses the JWT token, verifies it using the signing key, and retrieves the claims from the token.
        try {
            Jwts.parserBuilder().
                    setSigningKey(key()).
                    build().
                    parse(token);
            return true;
        }
        //If the token is expired or malformed, appropriate exceptions
        catch (ExpiredJwtException | IllegalArgumentException | MalformedJwtException | SignatureException e) {
            throw new RuntimeException(e);
        }
    }
}