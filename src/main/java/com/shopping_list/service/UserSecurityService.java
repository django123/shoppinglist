package com.shopping_list.service;


import com.shopping_list.Repository.UserRepository;
import com.shopping_list.entities.Utilisateur;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserSecurityService implements UserDetailsService {

    private static final Logger LOG = LoggerFactory.getLogger(UserDetailsService.class);

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utilisateur user = userRepository.findByUsername(username);

        if(null == user) {
            LOG.warn("Username {} not found", username);
            throw new UsernameNotFoundException("Username "+username+" not found");
        }
        return user;
    }

}
