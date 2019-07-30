package com.shopping_list.service.serviceImpl;

import com.shopping_list.Repository.AppRoleRepository;
import com.shopping_list.Repository.AppUserRepository;
import com.shopping_list.entities.AppRole;
import com.shopping_list.entities.AppUser;
import com.shopping_list.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private AppRoleRepository appRoleRepository;
    @Override
    public AppUser findByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    @Override
    public AppUser createUser(AppUser user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        AppRole userRole = appRoleRepository.findByRole("USER");
        if (userRole != null){
            user.setRoles(new HashSet<AppRole>(Arrays.asList(userRole)));
        }else{
            AppRole role=new AppRole(null,"USER");
            appRoleRepository.save(role);
            user.setRoles(new HashSet<AppRole>(Arrays.asList(role)));
        }
        return appUserRepository.save(user);
    }
}
