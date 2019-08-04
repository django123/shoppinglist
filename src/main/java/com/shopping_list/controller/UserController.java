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
    private UserService userService;


    @Autowired
    private AppUserRepository userRepository;



    @GetMapping("/registration")
    public String form(Model model){
        model.addAttribute("user",  new AppUser());
        return "user/form";
    }


    @PostMapping("/save")
    public String save(AppUser user, BindingResult bindingResult, Model model){

        AppUser userExists = userService.findByUsername(user.getUsername());
        if (userExists != null) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "there is already a user registered with a email provided");
        }

        if(bindingResult.hasErrors()) {
            return  "user/form";
        }else{
            System.out.println(user.getPassword());
            userService.createUser(user);

        }

        return "redirect:/login";
    }

}
