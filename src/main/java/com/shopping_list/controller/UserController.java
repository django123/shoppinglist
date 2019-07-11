package com.shopping_list.controller;

import com.shopping_list.Repository.RoleRepository;
import com.shopping_list.Repository.UserAndRoleRepository;
import com.shopping_list.Repository.UserRepository;
import com.shopping_list.entities.Role;
import com.shopping_list.entities.UserAndRole;
import com.shopping_list.entities.Utilisateur;
import com.shopping_list.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;


@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserAndRoleRepository userAndRoleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/registration")
    public String form(Model model){
        model.addAttribute("user",  new Utilisateur());
        return "user/form";
    }


    @PostMapping("/save")
    public String save(Utilisateur utilisateur, BindingResult bindingResult, Model model){

        Utilisateur userExists = userService.findUserByEmail(utilisateur.getEmail());
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

    @GetMapping("/update/{userId}")
    public String update(@PathVariable Long userId, Model model){
        Utilisateur utilisateur= userRepository.getOne(userId);
        model.addAttribute("user",utilisateur);
        return "user/update";
    }

    @PostMapping("/update/{userId}")
    public String update(Utilisateur utilisateur){
        userRepository.save(utilisateur);
        return "redirect:/logout";
    }

    @GetMapping("/delete/{userId}")
    public String delete(@PathVariable Long userId){
        userRepository.deleteById(userId);
        return "redirect:/user/users";
    }

    @GetMapping("/detail/{userId}")
    public String detail(@PathVariable Long userId, Model model,HttpSession session){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Utilisateur utilisateur = userRepository.findByEmail(auth.getName());
        model.addAttribute("user",utilisateur);
        UserAndRole form = new UserAndRole();
        session.setAttribute("userId", utilisateur.getUserId());
        UserAndRole editForm = userAndRoleRepository.findByUserId(utilisateur.getUserId());
        List<Role> role1 = roleRepository.findAll();
        model.addAttribute("form", form);
        model.addAttribute("update",editForm);
        model.addAttribute("role1", role1);
        return "user/detail";
    }

    @PostMapping("/role/save")
    public String role(@PathVariable Long userId, UserAndRole form, HttpSession session){
        Role role= roleRepository.getOne(form.getRoleId());
        Utilisateur utilisateur= userRepository.getOne(userId);
        utilisateur.addRoles(role);
        form.setUserId(utilisateur.getUserId());
        userAndRoleRepository.save(form);
        userRepository.save(utilisateur);
        return "redirect:/user/detail/"+utilisateur.getUserId();
    }



    @PostMapping("/role/update/{userId}")
    public String updateRole(UserAndRole roleUser, @PathVariable Long userId){
        Role role = roleRepository.getOne(roleUser.getRoleId());
        Utilisateur user = userRepository.getOne(userId);
        for (Role role1 : user.getRoles()){
            user.removeRelation(role1);
        }
        user.setRoles(role);
        roleUser.setUserId(user.getUserId());
        userAndRoleRepository.save(roleUser);
        userRepository.save(user);
        return "redirect:/user/detail"+user.getUserId();
    }



}
