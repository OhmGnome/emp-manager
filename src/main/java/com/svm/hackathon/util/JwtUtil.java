package com.svm.hackathon.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.svm.hackathon.config.Roles;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtUtil {
		public static final String JWTKEY = "secret";

		public static boolean verifyRole(String token, String role){
			final Claims claims = Jwts.parser().setSigningKey(JWTKEY)
					.parseClaimsJws(token).getBody();
			return (((List<String>) claims.get("roles")).contains(role));
		}
		
		public static List<String> verifyRoles(String token, List<String> roles) {
			final Claims claims = Jwts.parser().setSigningKey(JWTKEY).parseClaimsJws(token).getBody();
			List<String> rolesVerified = new ArrayList<>();
			for (String role : roles) {
				if (((List<String>) claims.get("roles")).contains(role)) {
					rolesVerified.add(role);
				}
			}
			return rolesVerified;
		}
		
		public static String tokenGenerator(String username, String email){
			int count = 0;
			String token = "false";
			HashMap<String, List<String>> usersRoles = Roles.getInstance().getRoles();
			
			for (Entry<String, List<String>> userRoles : usersRoles.entrySet()) {
				if (((String) userRoles.getKey()).equals(email)) {
					token = Jwts.builder().setSubject(username)
				            .claim("roles", userRoles.getValue()).setIssuedAt(new Date())
				            .signWith(SignatureAlgorithm.HS256, JWTKEY).compact();
					break;
				}
				if (count == usersRoles.size() -1){
					token = Jwts.builder().setSubject(username).claim("roles", Arrays.asList(Roles.USER)).setIssuedAt(new Date()).signWith(SignatureAlgorithm.HS256, JwtUtil.JWTKEY).compact();		
				}
			}
			
			return token;
		}
}
