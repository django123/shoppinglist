package com.shopping_list.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopping_list.entities.AppUser;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import io.jsonwebtoken.Jwts;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager){
        super();
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        AppUser user = null;
        try {
            user = new ObjectMapper().readValue(request.getInputStream(), AppUser.class);
        }catch (Exception e) {
            throw  new RuntimeException(e);
        }
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()
                ));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult)
            throws IOException, ServletException {

        User springUser = (User)authResult.getPrincipal();
        String jwtToken = Jwts.builder()
                .setSubject(springUser.getUsername())
                .setExpiration(new Date(
                        System.currentTimeMillis()+SecurityConstants.EXPIRATION_TIME
                )).signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET)
                .claim("roles", springUser.getAuthorities())
                .compact();
        response.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX+jwtToken);
    }
}
