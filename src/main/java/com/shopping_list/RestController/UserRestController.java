package com.shopping_list.RestController;
import com.shopping_list.Repository.RoleRepository;
import com.shopping_list.entities.Utilisateur;
import com.shopping_list.exception.HeaderUtil;
import com.shopping_list.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:8080","http://localhost:8100"})
@RestController
@RequestMapping("/api/user")
public class UserRestController {
    private final Logger log = LoggerFactory.getLogger(UserRestController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    /**
     * POST /User : Creer un nouveau user.
     *
     * @param user the compte to create
     * @return the ResponseEntity with status 201 (Created) and with body the
     * new compte, or with status 400 (Bad Request) if the compte has already an
     * ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */

    @RequestMapping(value = "/auth",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Utilisateur> createUser(@RequestBody Utilisateur user) throws URISyntaxException {
        log.debug("REST request to save user : {}", user);
        if (user.getUserId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("compte", "idexists", "A new compte cannot already have an ID")).body(null);
        }
        System.out.println(user.getUsername());
        Utilisateur result = userService.createUser(user);

        return ResponseEntity.created(new URI("/api/user/" + result.getUserId()))
                .headers(HeaderUtil.createEntityCreationAlert("user", result.getUserId().toString()))
                .body(result);
    }

    @RequestMapping(value = "/all",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Utilisateur>> findAllUtilisateur() throws URISyntaxException{
        log.debug("REST request to get user");
        List<Utilisateur> user = userService.findAllUtilisateur();
        return new ResponseEntity<>(user, null,HttpStatus.OK);
    }
}
