package com.shopping_list.controller;

import com.shopping_list.Repository.RoleRepository;
import com.shopping_list.Repository.UtilisateurRepository;
import com.shopping_list.entities.AddRoleToUser;
import com.shopping_list.entities.Role;
import com.shopping_list.entities.Utilisateur;
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


@Controller
@RequestMapping("/utilisateur")
public class UtilisateurController {

    @Autowired
    private RoleRepository roleRepository;

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
    public String detail(@PathVariable Long userId, Model model){
        Utilisateur utilisateur = utilisateurRepository.getOne(userId);
        AddRoleToUser form= new AddRoleToUser(roleRepository.findAll(),utilisateur);
        model.addAttribute("form", form);
        model.addAttribute("utilisateur",utilisateur);
        return "utilisateur/detail";
    }

    @PostMapping("/role/save")
    public String role(@PathVariable Long userId, AddRoleToUser form){
        Role role= roleRepository.getOne(form.getRoleId());
        Utilisateur utilisateur=utilisateurRepository.getOne(userId);
        utilisateur.addRoles(role);
        utilisateurRepository.save(utilisateur);
        return "redirect:/utilisateur/detail/"+utilisateur.getUserId();
    }
}
