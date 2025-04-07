package com.company.neurolink.security;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import com.company.neurolink.model.User;
import com.company.neurolink.repository.UserRepository;

import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

   
    private static final String SECRET_KEY = "fFdkL7qgNlke3bJwR+nKoO8lR4iDQYqPB8ZQ5o5pqgM=";

    // Token expiration time in milliseconds (2 days)
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 48;
    private final UserRepository userRepository ;
    
    public JwtUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
//    public String generateToken(String username, Long userId, String name,Long orderId) {
//        @SuppressWarnings("deprecation")
//		String token = Jwts.builder()
//                .setSubject(username) 
//                .setIssuedAt(new Date()) 
//                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) 
//                .claim("userId", userId) 
//                .claim("name", name) 
//                .claim("orderId", orderId) // ✅ Include orderId in token
//                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) 
//                .compact(); 
//        System.out.println("Generated token: " + token);
//        return token;
//    }
    
    
    
    public String generateToken(String username, Long userId, String name, Long orderId) {
    	if (orderId == null) {
            System.out.println(" orderId is NULL while generating token!");
        }
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .claim("userId", userId)
                .claim("name", name)
                //.claim("orderId", orderId)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
        System.out.println("Generated token: " + token);
        return token;
    }


    
    public Long extractOrderId(String token) {
        try {
            Claims claims = extractAllClaims(token);
            Object orderIdClaim = claims.get("orderId");

            if (orderIdClaim == null) {
                System.out.println("❌ Order ID is missing in token payload!");
                return null;
            }

            return Long.valueOf(orderIdClaim.toString());
        } catch (Exception e) {
            System.out.println("❌ Error extracting orderId: " + e.getMessage());
            return null;
        }
    }





//    public Long extractUserId(String token) {
//        Claims claims = extractAllClaims(token);
//        String userIdClaim = claims.get("userId", String.class);
//        if (userIdClaim != null) {
//            return Long.parseLong(userIdClaim);
//        } else if (claims.getSubject() != null) {
//            return extractUserIdFromEmail(claims.getSubject());
//        }
//        throw new IllegalArgumentException("UserId not found in token");
//    }
    
    public Long extractUserId(String token) {
        try {
            Claims claims = extractAllClaims(token);
            Object userIdClaim = claims.get("userId");

            if (userIdClaim != null) {
                return Long.valueOf(userIdClaim.toString()); 
            }

            return extractUserIdFromEmail(claims.getSubject()); 
        } catch (Exception e) {
            System.out.println("Error extracting userId: " + e.getMessage());
            return null;
        }
    }

    public String extractName(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("name", String.class); // Extract the name claim
    }


    private Long extractUserIdFromEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found for email: " + email));
        return user.getId();
    }
    
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        System.out.println("Token claims: " + claims); 
        return claimsResolver.apply(claims);
    }

    
    private Claims extractAllClaims(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        System.out.println("Token claims: " + claims);
        return claims;
    }


    public boolean validateToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            System.out.println("Token is null or empty");
            return false;
        }
        try {
            Claims claims = extractAllClaims(token);
            System.out.println("Token claims: " + claims);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            System.out.println("Token validation failed: " + e.getMessage());
            return false;
        }
    }


	
}