package com.shopping_list.controller;


import com.shopping_list.Repository.RoleRepository;
import com.shopping_list.Repository.UserRepository;
import com.shopping_list.entities.User;
import com.shopping_list.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
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
    private RoleRepository roleRepository;

    @Autowired
    private UserService userService;



    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/registration")
    public String form(Model model){
        model.addAttribute("user",  new User());
        return "user/form";
    }


    @PostMapping("/save")
    public String save(User utilisateur, BindingResult bindingResult, Model model){

        User userExists = userService.findByUsername(utilisateur.getUsername());
        if (userExists != null) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "there is already a user registered with a email provided");
        }

        if(bindingResult.hasErrors()) {
            return  "user/form";
        }else{
            System.out.println(utilisateur.getUsername());
            userService.createUser(utilisateur);

        }

        return "redirect:/login";
    }


}
