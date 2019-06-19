package com.shopping_list.service;

import com.shopping_list.Repository.RoleRepository;
import com.shopping_list.Repository.UtilisateurRepository;
import com.shopping_list.entities.Role;
import com.shopping_list.entities.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Created by EDOUGA on 19/06/2019.
 */
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UtilisateurRepository utilisateurRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Override
    public void createUser(Utilisateur user) {
        BCryptPasswordEncoder encoder = new  BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        user.setActive(true);
        Role userRole = roleRepository.findByName("USER");
        if (userRole != null) {
            user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        }else{
            Role role=new Role("USER");
            roleRepository.save(role);
            user.setRoles(new HashSet<Role>(Arrays.asList(role)));
        }
        utilisateurRepository.save(user);

    }



    @Override
    public List<Utilisateur> findByName(String name) {
        return  utilisateurRepository.findByName(name);
    }

    @Override
    public Utilisateur findUserByEmail(String email) {
        return utilisateurRepository.findByEmail(email);
    }


}
