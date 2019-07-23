package shopping_list.RestController;

import com.shopping_list.entities.Utilisateur;

import com.shopping_list.security.jwt.JwtProvider;
import com.shopping_list.service.UserService;
import com.shopping_list.vo.request.LoginForm;
import com.shopping_list.vo.response.JwtResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class UserRestController {
    @Autowired
    UserService userService;


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
            Utilisateur user = userService.findOne(userDetails.getUsername());
            return ResponseEntity.ok(new JwtResponse(jwt, user.getEmail(), user.getUsername(), user.getRole()));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    @PostMapping("/register")
    public ResponseEntity<Utilisateur> save(@RequestBody Utilisateur user) {
        try {
            return ResponseEntity.ok(userService.createUser(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


}
