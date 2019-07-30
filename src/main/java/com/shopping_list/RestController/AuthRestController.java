package com.shopping_list.RestController;


import com.shopping_list.Repository.AppRoleRepository;
import com.shopping_list.entities.AppUser;
import com.shopping_list.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;




@RestController
public class AuthRestController {
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
        userService.saveUser(u);
        userService.addRoleToUser(data.getUsername(), "USER");
        return u;
    }
}
