package com.shopping_list.controller;

import com.shopping_list.Repository.RoleRepository;
import com.shopping_list.Repository.UserAndRoleRepository;
import com.shopping_list.Repository.UtilisateurRepository;
import com.shopping_list.entities.AddRoleToUser;
import com.shopping_list.entities.Role;
import com.shopping_list.entities.UserAndRole;
import com.shopping_list.entities.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


@Controller
@RequestMapping("/utilisateur")
public class UtilisateurController {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserAndRoleRepository userAndRoleRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/registration")
    public String form(Model model){
        model.addAttribute("utilisateur",  new Utilisateur());
        return "utilisateur/form";
    }


    @PostMapping("/save")
    public String save(Utilisateur utilisateur, Errors errors, Model model){


        Utilisateur utilisateur1= utilisateurRepository.findByEmail(utilisateur.getEmail());

        if (utilisateur1 != null){
            errors.rejectValue("email", "utilisateur.error", "There is already a utilisateur registered with the email provided");
        }
        if (errors.hasErrors()){
            model.addAttribute("errors","Il existe un Utililsateur avec le meme adresse email.");
            return "utilisateur/form";
        }else {
            System.out.println(utilisateur.getPassword());
            utilisateur.setPassword(bCryptPasswordEncoder.encode(utilisateur.getPassword()));
            utilisateur.setActive(true);
            Role role= roleRepository.findByName("USER");
            if (utilisateur.getRoles() == null){
                if (role == null){
                    Role role1 = new Role("USER");
                    roleRepository.save(role1);
                    utilisateur.setRoles(new HashSet<Role>(Arrays.asList(role1)));
                }else{
                       utilisateur.setRoles(new HashSet<Role>(Arrays.asList(role)));
                    }
            }
            utilisateurRepository.save(utilisateur);
        }
        return "redirect:/login";
    }

    @GetMapping("/update/{userId}")
    public String update(@PathVariable Long userId, Model model){
        Utilisateur utilisateur= utilisateurRepository.getOne(userId);
        model.addAttribute("utilisateur",utilisateur);
        return "utilisateur/update";
    }

    @PostMapping("/update/{userId}")
    public String update(Utilisateur utilisateur){
        utilisateurRepository.save(utilisateur);
        return "redirect:/logout";
    }

    @GetMapping("/delete/{userId}")
    public String delete(@PathVariable Long userId){
        utilisateurRepository.deleteById(userId);
        return "redirect:/utilisateur/utilisateurs";
    }

    @GetMapping("/detail/{userId}")
    public String detail(@PathVariable Long userId, Model model,HttpSession session){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Utilisateur utilisateur = utilisateurRepository.findByEmail(auth.getName());
        model.addAttribute("utilisateur",utilisateur);
        UserAndRole form = new UserAndRole();
        session.setAttribute("userId", utilisateur.getUserId());
        UserAndRole editForm = userAndRoleRepository.findByUserId(utilisateur.getUserId());
        List<Role> role1 = roleRepository.findAll();
        model.addAttribute("form", form);
        model.addAttribute("update",editForm);
        model.addAttribute("role1", role1);
        return "utilisateur/detail";
    }

    @PostMapping("/role/save")
    public String role(@PathVariable Long userId, UserAndRole form, HttpSession session){
        Role role= roleRepository.getOne(form.getRoleId());
        Utilisateur utilisateur=utilisateurRepository.getOne(userId);
        utilisateur.addRoles(role);
        form.setUserId(utilisateur.getUserId());
        userAndRoleRepository.save(form);
        utilisateurRepository.save(utilisateur);
        return "redirect:/utilisateur/detail/"+utilisateur.getUserId();
    }



    @PostMapping("/role/update/{userId}")
    public String updateRole(UserAndRole roleUser, @PathVariable Long userId){
        Role role = roleRepository.getOne(roleUser.getRoleId());
        Utilisateur user = utilisateurRepository.getOne(userId);
        for (Role role1 : user.getRoles()){
            user.removeRelation(role1);
        }
        user.setRoles(role);
        roleUser.setUserId(user.getUserId());
        userAndRoleRepository.save(roleUser);
        utilisateurRepository.save(user);
        return "redirect:/utilisateur/detail"+user.getUserId();
    }



}
