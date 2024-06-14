package com.cognizant.user.utils;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtils {
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

	@Value("${blogsite.app.jwtSecret}")
	private String jwtSecret;

	@Value("${blogsite.app.jwtExpirationMs}")
	private int jwtExpirationMs;

	public String generateJwtToken(String ID) {
		logger.debug("inside generate method");
		long nowMillis = System.currentTimeMillis();
		Date exp = new Date(nowMillis + jwtExpirationMs);
        // Build the JWT token
        String token = Jwts.builder()
            .setId(ID)
            .setIssuedAt(new Date(nowMillis))
            .setExpiration(exp)
            .signWith(SignatureAlgorithm.HS256, jwtSecret)
            .compact();
        return token;
    }

	
}
