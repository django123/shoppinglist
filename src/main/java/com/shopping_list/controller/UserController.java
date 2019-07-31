package com.shopping_list.controller;


import com.shopping_list.Repository.AppRoleRepository;
import com.shopping_list.Repository.AppUserRepository;
import com.shopping_list.entities.AppUser;
import com.shopping_list.service.AccountService;
import com.shopping_list.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private AppRoleRepository roleRepository;

    @Autowired
    private AccountService userService;

    @Autowired
     private UserService userService1;
    @Autowired
    private AppUserRepository userRepository;



    @GetMapping("/registration")
    public String form(Model model){
        model.addAttribute("user",  new AppUser());
        return "user/form";
    }


    @PostMapping("/save")
    public String save(AppUser user,  BindingResult bindingResult, Model model){
        String username = user.getUsername();
        String password = user.getPassword();
        String repassword = user.getRepassword();
        AppUser userExists = userService.findUserByUsername(user.getUsername());
        if (userExists != null) {
            bindingResult
                    .rejectValue("username", "error.user",
                            "there is already a user registered with a username provided");
        }
        if (!password.equals(repassword))
            throw new RuntimeException("You must confirm your password");

        if(bindingResult.hasErrors()) {
            return  "user/form";
        }else{

            user.setUsername(username);
            user.setPassword(password);
            user.setActive(true);
            userService.saveUser(user);
            userService.addRoleToUser(user.getUsername(), "USER");
            System.out.println(user.getUsername());
        }

        return "redirect:/login";
    }


}
