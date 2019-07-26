package com.shopping_list.RestController;


import com.shopping_list.entities.User;
import com.shopping_list.service.UserService;
import com.shopping_list.util.CustomError;
import javassist.tools.rmi.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserRestController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserById(@PathVariable("userId") Long userId) {

        try {
            User user = userService.findById(userId);
            return new ResponseEntity<User>(user, HttpStatus.OK);

        } catch (ObjectNotFoundException onfe) {
            onfe.printStackTrace();
            CustomError error = new CustomError("User with id = " + userId + " is not found");
            return new ResponseEntity<CustomError>(error, HttpStatus.UNPROCESSABLE_ENTITY);

        } catch (Exception e) {
            e.printStackTrace();
            CustomError error = new CustomError("An error has occured");
            return new ResponseEntity<CustomError>(error, HttpStatus.EXPECTATION_FAILED);
        }

    }

    @RequestMapping(value = "/signup", method = { RequestMethod.POST })
    public ResponseEntity<User> save(@RequestBody User user) {
        try {
            return ResponseEntity.ok(userService.createUser(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
