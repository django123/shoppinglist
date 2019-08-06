package com.shopping_list.RestController;


import com.shopping_list.Repository.AppRoleRepository;
import com.shopping_list.entities.AppUser;
import com.shopping_list.service.AccountService;
import io.swagger.annotations.Api;
import javassist.tools.rmi.ObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.Collection;


@Api(description = "gestion des utilisateurs")
@RestController
public class AuthRestController {
    private final Logger log = LoggerFactory.getLogger(AuthRestController.class);
    @Autowired
    private AccountService userService;
    @Autowired
    private AppRoleRepository appRoleRepository;


    @PostMapping("/users")
    public AppUser signUp(@RequestBody RegistrationForm data){

        if (!data.getPassword().equals(data.getRepassword()))
            throw new RuntimeException("You must confirm your password");
        AppUser user = userService.findUserByUsername(data.getUsername());

        if (user != null)
            throw new RuntimeException("This user already exists");
        AppUser u = new AppUser();
        u.setUsername(data.getUsername());
        u.setPassword(data.getPassword());
        u.setRepassword(data.getRepassword());
        u.setActive(true);
        userService.saveUser(u);
        userService.addRoleToUser(data.getUsername(), "USER");
        return u;
    }

    @GetMapping("/users")
    public ResponseEntity<Collection<AppUser>> listUser()  throws URISyntaxException {
        log.debug("REST request to get a page of users");
        Collection<AppUser> users = userService.findAllUser();
        return  new ResponseEntity<>(users, null, HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<AppUser> getUser(@PathVariable Long id) throws ObjectNotFoundException {
        log.debug("REST request to get user : {}", id);
        AppUser result = userService.findById(id);
        return new ResponseEntity<>(result, null, HttpStatus.OK);
    }
}
