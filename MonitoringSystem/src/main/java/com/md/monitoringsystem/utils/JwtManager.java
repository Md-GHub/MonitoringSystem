package com.md.monitoringsystem.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.md.monitoringsystem.model.User;

import java.util.Date;

public class JwtManager {
    public static void main(String[] args) {
//        DecodedJWT jwt = verifyToken(createJwt("mohamed"));
//        System.out.println(jwt.getClaims());
//        System.out.println(jwt.getSubject());
//        System.out.println(jwt.getIssuedAt());
//        System.out.println(jwt.getExpiresAt());
    }
    private static String SECRET_KEY = "secretKey";

    public static String createJwt(User user) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        return JWT.create()
                .withIssuer("auth")
                .withClaim("role",user.getRole().toString()) /// need to change
                .withSubject(user.getUserName())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600 * 1000))
                .sign(algorithm);
    }
    public static DecodedJWT verifyToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth")
                    .build();

            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT;
        } catch (JWTVerificationException exception) {
            System.out.println("Invalid token: " + exception.getMessage());
            return null;
        }
    }
}

