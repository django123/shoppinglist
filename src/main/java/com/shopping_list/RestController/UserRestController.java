package com.shopping_list.RestController;

import com.shopping_list.entities.Utilisateur;
import com.shopping_list.security.JwtProvider;
import com.shopping_list.service.UserService;
import com.shopping_list.vo.JwtResponse;
import com.shopping_list.vo.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class UserRestController {

    @Autowired
    private UserService userService;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    AuthenticationManager authenticationManager;


    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginForm loginForm) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginForm.getUsername(), loginForm.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtProvider.generate(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Utilisateur user = userService.findByUsername(userDetails.getUsername());
            return ResponseEntity.ok(new JwtResponse(jwt, user.getUsername()));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Utilisateur> createUser(@RequestBody Utilisateur user) {
        try {
            return ResponseEntity.ok(userService.createUser(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
