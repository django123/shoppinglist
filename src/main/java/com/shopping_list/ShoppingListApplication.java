package com.shopping_list;

import com.shopping_list.Repository.RoleRepository;
import com.shopping_list.entities.Role;
import com.shopping_list.entities.Utilisateur;
import com.shopping_list.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.HashSet;

@SpringBootApplication
public class ShoppingListApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingListApplication.class, args);
    }

    @Autowired
    private UserService userService;
    @Autowired
    private RoleRepository roleRepository;
    @Override
    public void run(String... args) throws Exception {
      BCryptPasswordEncoder encoder = new  BCryptPasswordEncoder();
        Utilisateur user = new Utilisateur();
        user.setUsername("kenza");
        user.setPassword("bonjour");
        user.setPassword(encoder.encode(user.getPassword()));
        user.setEmail("enzo@gmail.com");
        user.setActive(true);
        Role userRole = roleRepository.findByName("USER");
        if (userRole != null) {
            user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        }else{
            Role role=new Role("USER");
            roleRepository.save(role);
            user.setRoles(new HashSet<Role>(Arrays.asList(role)));
        }
        userService.createUser(user);
    }

}
