package com.shopping_list.RestController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Map;

@CrossOrigin(origins = {"http://localhost:8080","http://localhost:8100"})
@RestController
public class LoginRestController {

    @GetMapping("/token")
    public Map<String, String> token(HttpSession session, HttpServletRequest request){
        System.out.println(request.getRemoteHost());

        String remoteHost = request.getRemoteHost();
        int portNumber = request.getRemotePort();

        System.out.println(remoteHost+" : "+ portNumber);
        System.out.println(request.getRemoteAddr());

        return Collections.singletonMap("token", session.getId());
    }

    @GetMapping("/checkSession")
    public ResponseEntity checkSession() {
        return new ResponseEntity("Session Active !", HttpStatus.OK);
    }

    @PostMapping(value="/user/logout")
    public ResponseEntity logout() {
        SecurityContextHolder.clearContext();
        return new ResponseEntity("Logout Sucessfully!", HttpStatus.OK);
    }
}
