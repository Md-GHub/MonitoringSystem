package com.md.monitoringsystem.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.md.monitoringsystem.constant.Role;
import com.md.monitoringsystem.model.User;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;

import java.util.Date;

public class JwtManager {
    private static final String SECRET = "mySecretKey123!";
    private static final long EXPIRATION_TIME = 1000 * 60 * 60;

    private final Algorithm algorithm;
    private final JWTVerifier verifier;

    public JwtManager() {
        this.algorithm = Algorithm.HMAC256(SECRET);
        this.verifier = JWT.require(algorithm).build();
    }

    // Create token
    public String generateToken(User user) {
        return JWT.create()
                .withSubject(user.getUserName())
                .withClaim("userid", user.getUserId())
                .withClaim("email", user.getEmail())
                .withClaim("password", user.getPassword())
                .withClaim("role", user.getRole().toString())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(algorithm);
    }

    // Validate token
    public boolean validateToken(String token) {
        try {
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    // extract user id
    public int getUserId(String token) {
        DecodedJWT decoded = verifier.verify(token);
        int id  = decoded.getClaim("userid").asInt();
        return id;
    }
    // Extract username
    public String getUsername(String token) {
        DecodedJWT decoded = verifier.verify(token);
        return decoded.getSubject();
    }

    // extract Role
    public Role getUserRole(String token) {
        DecodedJWT decoded = verifier.verify(token);
        return Role.valueOf(decoded.getClaim("role").asString());
    }

    //extract email
    public Role getUserEmail(String token) {
        DecodedJWT decoded = verifier.verify(token);
        return Role.valueOf(decoded.getClaim("email").asString());
    }
}
