package com.example.Chess.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    @Value("${security.jwt.security.key}")
    private String SECRET_KEY;
    
    /**
     * Extracts the username (subject) from the JWT token.
     * 
     * @param token the JWT token from which the username is extracted
     * @return String representing the username stored in the JWT token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts a specific claim from the JWT token.
     * 
     * @param token the JWT token from which to extract the claim
     * @param claimsResolver a function to extract the specific claim from the Claims object
     * @return the extracted claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Generates a JWT token for the given user details with no additional claims.
     * 
     * @param userDetails the user details to include in the JWT token
     * @return String representing the generated JWT token
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Generates a JWT token with the specified extra claims and user details.
     * 
     * @param extraClaims a map of additional claims to include in the JWT token
     * @param userDetails the user details to include in the JWT token
     * @return String representing the generated JWT token
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 180))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Validates the JWT token by checking if the username matches the username found 
     * in the JWT token and if the token is not expired.
     * 
     * @param token the JWT token to validate
     * @param userDetails the user details to compare against
     * @return boolean value which is true if the token is valid, otherwise false
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Checks if the JWT token is expired.
     * 
     * @param token the JWT token to check for expiration
     * @return boolean value which is true if the token is expired, otherwise false
     */
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts the expiration date from the JWT token.
     * 
     * @param token the JWT token from which to extract the expiration date
     * @return Date representing the expiration date of the token
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts all claims from the JWT token.
     * 
     * @param token the JWT token to extract claims from
     * @return the Claims object containing all claims from the token
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Retrieves the signing key from the secret key.
     * 
     * @return Key representing the signing key used to sign and verify JWT tokens
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}