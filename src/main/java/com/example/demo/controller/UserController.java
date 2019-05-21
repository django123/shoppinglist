package com.example.demo.controller;

import com.example.demo.entities.AddRoleToUser;
import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import com.example.demo.repositiory.RoleRepository;
import com.example.demo.repositiory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by EDOUGA on 21/05/2019.
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @GetMapping("/registration")
    public String form(Model model){
        model.addAttribute("user",  new User());
        return "user/form";
    }


    @PostMapping("/save")
    public String save(User user, Errors errors, Model model){


        User user1= userRepository.findByEmail(user.getEmail());

        if (user1 != null){
            errors.rejectValue("email", "utilisateur.error", "There is already a utilisateur registered with the email provided");
        }
        if (errors.hasErrors()){
            model.addAttribute("errors","Il existe un Utililsateur avec le meme adresse email.");
            return "user/form";
        }else {
            System.out.println(user.getPassword());
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            user.setActive(true);
            Role role= roleRepository.findByName("USER");
            if (user.getRoles() == null){
                if (role == null){
                    Role role1 = new Role("USER");
                    roleRepository.save(role1);
                    user.setRoles(new HashSet<Role>(Arrays.asList(role1)));
                }else{
                    user.setRoles(new HashSet<Role>(Arrays.asList(role)));
                }
            }
            userRepository.save(user);
        }
        return "redirect:/login";
    }

    @GetMapping("/update/{user_id}")
    public String update(@PathVariable Long userId, Model model){
        User utilisateur= userRepository.getOne(userId);
        model.addAttribute("user",utilisateur);
        return "user/update";
    }

    @PostMapping("/update/{user_id}")
    public String update(User user){
        userRepository.save(user);
        return "redirect:/logout";
    }

    @GetMapping("/delete/{user_id}")
    public String delete(@PathVariable Long user_id){
        userRepository.deleteById(user_id);
        return "redirect:/user/users";
    }

    @GetMapping("/detail/{user_id}")
    public String detail(@PathVariable Long user_id, Model model){
        User user = userRepository.getOne(user_id);
        AddRoleToUser form= new AddRoleToUser(roleRepository.findAll(),user);
        model.addAttribute("form", form);
        model.addAttribute("user",user);
        return "user/detail";
    }

    @PostMapping("/role/save")
    public String role(@PathVariable Long user_id, AddRoleToUser form){
        Role role= roleRepository.getOne(form.getRole_id());
        User user=userRepository.getOne(user_id);
        user.addRoles(role);
        userRepository.save(user);
        return "redirect:/user/detail/"+user.getUser_id();
    }
}
